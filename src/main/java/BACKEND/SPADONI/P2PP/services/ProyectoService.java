package BACKEND.SPADONI.P2PP.services;

import BACKEND.SPADONI.P2PP.DTO.ProyectoDTO;
import BACKEND.SPADONI.P2PP.DTO.ProyectoDatosDTO;
import BACKEND.SPADONI.P2PP.exceptions.AccesoProyectoDenegadoException;
import BACKEND.SPADONI.P2PP.exceptions.ProyectoNoEncontradoException;
import BACKEND.SPADONI.P2PP.mapper.ProyectoMapper;
import BACKEND.SPADONI.P2PP.model.EstadoProceso;
import BACKEND.SPADONI.P2PP.model.Proyecto;
import BACKEND.SPADONI.P2PP.model.Usuario;
import BACKEND.SPADONI.P2PP.repository.ProyectoRepository;
import BACKEND.SPADONI.P2PP.services.proyecto.ProyectoViewer;
import BACKEND.SPADONI.P2PP.services.proyecto.ProyectoViewerFactory;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProyectoService {

    private final ProyectoRepository proyectoRepo;
    
    private final UsuarioService usuarioService; // Necesario para buscar al usuario que solicita los proyectos
    private final ClienteService clienteService;
    private final AsignacionDesarrolladorService asignacionDevService;
    
    private final ProyectoViewerFactory proyectoViewerFactory;
    
    private final ProyectoMapper pMapper;

    public ProyectoService(
            ProyectoRepository proyectoRepo,
            UsuarioService usuarioService,
            ProyectoViewerFactory proyectoViewerFactory,
            ProyectoMapper pMapper,
            ClienteService clienteService,
            AsignacionDesarrolladorService asignacionDevService) {

        this.proyectoRepo = proyectoRepo;
        this.usuarioService = usuarioService;
        this.proyectoViewerFactory = proyectoViewerFactory;
        this.pMapper = pMapper;
        this.clienteService = clienteService;
        this.asignacionDevService = asignacionDevService;
    }

    public Proyecto obtenerPorId(Long id) {
        return proyectoRepo.findById(id)
                .orElseThrow(() -> new ProyectoNoEncontradoException("Proyecto no encontrado con ID: " + id));
    }
    
    public ProyectoDTO obtenerProyecto(Long idProyecto, String nombreUsuario){
        Proyecto p = obtenerPorId(idProyecto);
        if(puedeVerProyecto(p, usuarioService.obtenerPorNombreUsuario(nombreUsuario)))
            return pMapper.toDTO(p);
        else
            throw new AccesoProyectoDenegadoException("Acceso al proyecto denegado");
    }
    
    public List<ProyectoDTO> listar(String nombreUsuario) {

        Usuario usuario = usuarioService.obtenerPorNombreUsuario(nombreUsuario);

        ProyectoViewer viewer = proyectoViewerFactory.obtenerViewer(usuario.getRol());
        
        return pMapper.listToDTO(viewer.mostrarProyectos(usuario));
    }

    public ProyectoDTO crear(ProyectoDatosDTO datos) {
        Proyecto p = new Proyecto();
        p.setCliente(clienteService.obtenerActivo(datos.clienteId()));
        p = aplicarDatos(p, datos);
        return pMapper.toDTO(proyectoRepo.save(p));
    }
    
    private boolean puedeVerProyecto(Proyecto p, Usuario u){
        ProyectoViewer viewer = proyectoViewerFactory.obtenerViewer(u.getRol());
        return viewer.puedeVerProyecto(u, p); 
    }
    
    public boolean puedeVerProyecto(Long idProyecto, Usuario u) {
        Proyecto p = obtenerPorId(idProyecto);
        return puedeVerProyecto(p, u);
    }
    
    @Transactional
    public ProyectoDTO actualizar(Long id, ProyectoDatosDTO datos) {
        Proyecto p = obtenerPorId(id);
        if (!datos.clienteId().equals(p.getCliente().getId())) {
            p.setCliente(clienteService.obtenerActivo(datos.clienteId()));  // Si el proyecto cambió de dueño, verifico q el actual no esté inhabilitado y le asigno el proyecto
        }

        boolean saleDeEnCurso = p.getEstadoProceso() == EstadoProceso.EN_CURSO
                && datos.estadoProceso() != EstadoProceso.EN_CURSO;

        Proyecto guardado = proyectoRepo.save(aplicarDatos(p, datos));

        if (saleDeEnCurso) {
            liberarDevs(id);    // liberamos a los devs porque el proyecto ya no está en desarrollo
            // fecha final (hoy)
        }

        return pMapper.toDTO(guardado);
    }

    private void liberarDevs(Long proyectoId) {
        asignacionDevService.removerDevsDeProyecto(proyectoId);
    }

    private Proyecto aplicarDatos(Proyecto p, ProyectoDatosDTO datos) {
        p.setNombre(datos.nombre());
        p.setCanalSolicitud(datos.canalSolicitud());
        p.setFechaInicio(datos.fechaInicio());
        p.setFechaFinalizacion(datos.fechaFinalizacion());
        p.setPresupuesto(datos.presupuesto());
        p.setEstadoProyecto(datos.estadoProyecto());
        p.setEstadoProceso(datos.estadoProceso());
        return p;
    }

    @Transactional
    public void eliminar(Long proyectoId) {
        Proyecto proyecto = obtenerPorId(proyectoId);
        liberarDevs(proyectoId);
        proyectoRepo.delete(proyecto);
    }
}