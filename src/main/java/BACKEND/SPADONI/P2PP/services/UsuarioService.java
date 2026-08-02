package BACKEND.SPADONI.P2PP.services;

import BACKEND.SPADONI.P2PP.DTO.CredencialesDTO;
import BACKEND.SPADONI.P2PP.DTO.UsuarioAltaDTO;
import BACKEND.SPADONI.P2PP.model.Administrador;
import BACKEND.SPADONI.P2PP.model.Cliente;
import BACKEND.SPADONI.P2PP.model.Gerente;
import BACKEND.SPADONI.P2PP.model.Rol;
import BACKEND.SPADONI.P2PP.model.Usuario;
import BACKEND.SPADONI.P2PP.repository.ProyectoRepository;
import BACKEND.SPADONI.P2PP.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private ProyectoRepository proyectoRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Usuario obtenerPorId(Long id) {
        return usuarioRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id));
    }

    public Optional<Usuario> buscarParaLogin(String nombreUsuario) {
        return usuarioRepo.findByNombreUsuario(nombreUsuario);
    }

    public Usuario obtenerPorNombreUsuario(String nombreUsuario) {
        return usuarioRepo.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado: " + nombreUsuario));
    }

    public List<Usuario> listar(Long usuarioIdLogueado) {
        Usuario solicitante = obtenerPorId(usuarioIdLogueado);

        // Delegación polimórfica: según si es Admin, Gerente o Cliente, ejecutará su propio método
        return solicitante.obtenerUsuariosVisibles(usuarioRepo);
    }

    public Usuario guardar(Usuario u) {
        try {
            u.setContrasena(passwordEncoder.encode(u.getContrasena())); // codificamos la password antes de guardar
            return usuarioRepo.save(u);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre de usuario ya está en uso");
        }
    }
    
    public Usuario crear(UsuarioAltaDTO datos) {
        if (datos.rol() == Rol.CLIENTE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los clientes se dan de alta desde la sección de Clientes");
        }

        Usuario nuevo = switch (datos.rol()) {
            case GERENTE -> new Gerente(datos.nombreUsuario(), datos.contrasena(), datos.rol());
            case ADMINISTRADOR -> new Administrador(datos.nombreUsuario(), datos.contrasena(), datos.rol());
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rol inválido");
        };
        nuevo.setContrasena(passwordEncoder.encode(nuevo.getContrasena()));

        try {
            return usuarioRepo.save(nuevo);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre de usuario ya está en uso");
        }
    }

    @Transactional
    public void habilitar(Long id) {
        Usuario usuario = obtenerPorId(id); // Trae el usuario o lanza la excepción si no existe
        usuario.setActivo(true);
        usuarioRepo.save(usuario);
    }
    
    public Usuario eliminar(Long id) {
        Usuario usuario = obtenerPorId(id);
        if (!usuario.puedeEliminarse(proyectoRepo)) {
            usuario.setActivo(false);
            return usuarioRepo.save(usuario); // Retorna el cliente inhabilitado (activo = false)
            //El usuario tiene proyectos asociados, no se puede eliminar
        }
        usuarioRepo.delete(usuario);
        return null; // Retorna null indicando que fue borrado físicamente
    }

    @Transactional
    public Usuario modificarCredenciales(Long id, CredencialesDTO datos) {
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
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre de usuario ya está en uso");
        }
    }
}
