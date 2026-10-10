package BACKEND.SPADONI.P2PP.services;

import BACKEND.SPADONI.P2PP.security.TokenService;
import BACKEND.SPADONI.P2PP.DTO.CredencialesDTO;
import BACKEND.SPADONI.P2PP.DTO.RespuestaActualizacion;
import BACKEND.SPADONI.P2PP.DTO.UsuarioAltaDTO;
import BACKEND.SPADONI.P2PP.DTO.UsuarioDTO;
import BACKEND.SPADONI.P2PP.DTO.UsuarioListadoDTO;
import BACKEND.SPADONI.P2PP.exceptions.AltaClienteNoPermitidaException;
import BACKEND.SPADONI.P2PP.exceptions.AutoEliminacionNoPermitidaException;
import BACKEND.SPADONI.P2PP.exceptions.NombreUsuarioExistenteException;
import BACKEND.SPADONI.P2PP.exceptions.UsuarioNoEncontradoException;
import BACKEND.SPADONI.P2PP.factory.UsuarioFactory;
import BACKEND.SPADONI.P2PP.mapper.UsuarioMapper;
import BACKEND.SPADONI.P2PP.model.Rol;
import BACKEND.SPADONI.P2PP.model.Usuario;
import BACKEND.SPADONI.P2PP.repository.ProyectoRepository;
import BACKEND.SPADONI.P2PP.repository.UsuarioRepository;
import BACKEND.SPADONI.P2PP.services.usuario.UsuarioViewer;
import BACKEND.SPADONI.P2PP.services.usuario.UsuarioViewerFactory;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepo;
    private final ProyectoRepository proyectoRepo;

    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenService tokenService;
       
    private final UsuarioViewerFactory usuarioViewerFactory;
    private final UsuarioFactory usuarioFactory;
    
    private final UsuarioMapper uMapper;
    
    public UsuarioService(
            UsuarioRepository usuarioRepo,
            ProyectoRepository proyectoRepo,
            BCryptPasswordEncoder passwordEncoder,
            UsuarioViewerFactory usuarioViewerFactory,
            TokenService tokenService,
            UsuarioMapper uMapper,
            UsuarioFactory usuarioFactory){
        
        this. usuarioRepo = usuarioRepo;
        this. proyectoRepo = proyectoRepo;
        this.passwordEncoder = passwordEncoder;
        this.usuarioViewerFactory = usuarioViewerFactory;
        this.tokenService = tokenService;
        this.uMapper = uMapper;
        this.usuarioFactory = usuarioFactory;
    }
    
    
    public List<UsuarioListadoDTO> listar(String usuarioLogueadoNombre) {   // Al final no era necesario polimorfismo, pero podria servir si se agrega otro ROL
        Usuario solicitante = obtenerPorNombreUsuario(usuarioLogueadoNombre);
        UsuarioViewer viewer = usuarioViewerFactory.obtenerViewer(solicitante.getRol());
        return uMapper.toListadoDTO(viewer.mostrarUsuarios(solicitante));
    }
    
    public UsuarioListadoDTO crear(UsuarioAltaDTO datos) {
        if (datos.rol() == Rol.CLIENTE) {
            throw new AltaClienteNoPermitidaException("Los clientes se dan de alta desde la sección de Clientes");
        }

        Usuario nuevo = usuarioFactory.crear(datos);
        
        nuevo.setContrasena(passwordEncoder.encode(nuevo.getContrasena()));

        try {
            return uMapper.toListadoDTO(usuarioRepo.save(nuevo));
        } catch (DataIntegrityViolationException  e) {
            throw new NombreUsuarioExistenteException("El nombre de usuario ya está en uso");  // #esto va en el controller creo
        }
    }
    
    public UsuarioListadoDTO eliminar(Long id, String nombreUsuarioAuth) {
        
        Usuario usuarioAEliminar = obtenerPorId(id);
        Usuario usuarioAutenticado = obtenerPorNombreUsuario(nombreUsuarioAuth);
        
        validarAutoEliminacion(usuarioAEliminar, usuarioAutenticado);
        
        if(!puedeEliminarse(usuarioAEliminar)){
            usuarioAEliminar.setActivo(false);
            return uMapper.toListadoDTO(usuarioRepo.save(usuarioAEliminar)); // Retorna el cliente inhabilitado (activo = false)
            //El usuario tiene proyectos asociados, no se puede eliminar
        }
        
        usuarioRepo.delete(usuarioAEliminar);
        return null; // Retorna null indicando que fue borrado físicamente
    }
    
    @Transactional
    public void habilitar(Long id) {
        Usuario usuario = obtenerPorId(id); // Trae el usuario o lanza la excepción si no existe
        usuario.setActivo(true);
        usuarioRepo.save(usuario);
    }
    
    private void validarAutoEliminacion(Usuario usuarioAEliminar, Usuario usuarioAutenticado){
        if (usuarioAEliminar.getId().equals(usuarioAutenticado.getId()) && usuarioAEliminar.getRol() == Rol.ADMINISTRADOR)
            throw new AutoEliminacionNoPermitidaException("Un administrador no puede eliminarse a sí mismo");
    } 
    
    private boolean puedeEliminarse(Usuario u){   
        if(u.getRol() != Rol.CLIENTE) return true;    // Guarda, si este Usuario no es CLIENTE, no me importa
        return !proyectoRepo.existsByClienteId(u.getId());    // El cliente tiene proyectos asociados, por lo que no se elimina. Se lo inhabilita.
    }
    
    @Transactional
    public RespuestaActualizacion actualizar(Long id, CredencialesDTO datos, String nombreUsuarioAuth){
        
        // Obtener el usuario autenticado ANTES de modificar la BD
        Usuario usuarioLogueado = obtenerPorNombreUsuario(nombreUsuarioAuth);

        // 1. Modificamos las credenciales mediante el servicio
        Usuario usuarioActualizado = modificarCredenciales(id, datos);
        String nuevoToken = null;

        // 2. Si editó su propia cuenta, regeneramos el JWT
        if (usuarioLogueado.getId().equals(usuarioActualizado.getId())) {
            nuevoToken = tokenService.generarToken(usuarioActualizado);
        }
        // 3. Si editó a OTRO usuario, 'nuevoToken' va como null
        return new RespuestaActualizacion(uMapper.toListadoDTO(usuarioActualizado), nuevoToken);
    }
    
    public Usuario buscarParaLogin(String nombreUsuario) {
        return usuarioRepo.findByNombreUsuario(nombreUsuario)
            .orElse(null);
    }
    
    private Usuario obtenerPorId(Long id) {
        return usuarioRepo.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado con ID: " + id));
    }

    public Usuario obtenerPorNombreUsuario(String nombreUsuario) {
        return usuarioRepo.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado: " + nombreUsuario));
    }

    private Usuario modificarCredenciales(Long id, CredencialesDTO datos) {
        Usuario u = obtenerPorId(id);
        if (datos.nombreUsuario() != null && !datos.nombreUsuario().isBlank()) {
            u.setNombreUsuario(datos.nombreUsuario());
        }
        if (datos.contrasena() != null && !datos.contrasena().isBlank()) {
            u.setContrasena(passwordEncoder.encode(datos.contrasena()));
        }
        if (u.getNombreUsuario() != null && !u.getNombreUsuario().isBlank()
                && u.getContrasena() != null && !u.getContrasena().isBlank()) {
            u.setActivo(true);
        }
        try {
            return usuarioRepo.saveAndFlush(u);
        } catch (DataIntegrityViolationException e) {
            throw new NombreUsuarioExistenteException("El nombre de usuario ya está en uso");  // # Reemplazar por excepciones propias
        }
    }
}