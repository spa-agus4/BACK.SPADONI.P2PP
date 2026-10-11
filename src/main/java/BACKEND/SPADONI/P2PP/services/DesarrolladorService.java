package BACKEND.SPADONI.P2PP.services;

import BACKEND.SPADONI.P2PP.DTO.DesarrolladorAltaDTO;
import BACKEND.SPADONI.P2PP.DTO.DesarrolladorDTO;
import BACKEND.SPADONI.P2PP.DTO.DesarrolladorEdicionDTO;
import BACKEND.SPADONI.P2PP.exceptions.AccesoProyectoDenegadoException;
import BACKEND.SPADONI.P2PP.exceptions.DesarrolladorNoEncontradoException;
import BACKEND.SPADONI.P2PP.exceptions.NombreDesarrolladorExistenteException;
import BACKEND.SPADONI.P2PP.mapper.DesarrolladorMapper;
import BACKEND.SPADONI.P2PP.model.Desarrollador;
import BACKEND.SPADONI.P2PP.model.EstadoProceso;
import BACKEND.SPADONI.P2PP.model.Proyecto;
import BACKEND.SPADONI.P2PP.model.Usuario;
import BACKEND.SPADONI.P2PP.repository.DesarrolladorRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DesarrolladorService {

    private final DesarrolladorRepository devRepo;

    private final UsuarioService usuarioService;
    private final ProyectoService proyectoService;
    private final AsignacionDesarrolladorService asignacionDevService;
    
    private final DesarrolladorMapper dMapper;

    public DesarrolladorService(
            DesarrolladorRepository devRepo,
            DesarrolladorMapper dMapper,
            UsuarioService usuarioService,
            ProyectoService proyectoService,
            AsignacionDesarrolladorService asignacionDevService) {

        this.devRepo = devRepo;
        this.dMapper = dMapper;
        this.usuarioService = usuarioService;
        this.proyectoService = proyectoService;
        this.asignacionDevService = asignacionDevService;
    }

    private Desarrollador obtenerPorId(Long id) {
        return devRepo.findById(id)
                .orElseThrow(() -> new DesarrolladorNoEncontradoException("Desarrollador no encontrado con ID: " + id));
    }

    public List<DesarrolladorDTO> listar() {
        return dMapper.toDTO(devRepo.findAll());
    }

    public List<DesarrolladorDTO> listarAsignadosActualesPorProyecto(Long proyectoId, String nombreUsuario) {
        validarAccesoAlProyectoSegunUsuario(proyectoId, nombreUsuario);
        return dMapper.toDTO(asignacionDevService.listarAsignadosActuales(proyectoId));
    }
    
    public List<DesarrolladorDTO> listarHistorialPorProyecto(Long proyectoId, String nombreUsuario) {
        validarAccesoAlProyectoSegunUsuario(proyectoId, nombreUsuario);
        return dMapper.toDTO(asignacionDevService.listarParticipantesHistoricos(proyectoId));
    }
    
    private void validarAccesoAlProyectoSegunUsuario(Long proyectoId, String nombreUsuario){
        Usuario usuario = usuarioService.obtenerPorNombreUsuario(nombreUsuario);
        
        // Verificamos si el proyecto con ID proyectoId es uno de los que el usuario puede ver
        if (!proyectoService.puedeVerProyecto(proyectoId, usuario)) {
            throw new AccesoProyectoDenegadoException("No tenés permiso para ver los desarrolladores de este proyecto");
        }
    }
            
    public DesarrolladorDTO crear(DesarrolladorAltaDTO datos) {

        Desarrollador d = new Desarrollador(datos.nombre(), datos.habilidades(), true);

        try {
            return dMapper.toDTO(devRepo.save(d));
        } catch (DataIntegrityViolationException e) {
            throw new NombreDesarrolladorExistenteException("El nombre del desarrollador ya está en uso");
        }
    }

    @Transactional
    public void eliminar(Long id) {

        Desarrollador desarrollador = obtenerPorId(id);

        asignacionDevService.eliminarHistorialDeDesarrollador(id);

        devRepo.delete(desarrollador);
    }
    
    @Transactional
    public DesarrolladorDTO actualizar(Long id, DesarrolladorEdicionDTO datos) {
        Desarrollador d = obtenerPorId(id);
        if (datos.nombre() != null) {
            d.setNombre(datos.nombre());
        }
        if (datos.habilidades() != null) {
            d.setHabilidades(datos.habilidades());
        }
        if (datos.disponible() != null) {
            if (!d.getDisponible() && datos.disponible()) {
                d.setProyecto(null);    // # quitarr
            }
            d.setDisponible(datos.disponible());
        }
        try {
            return dMapper.toDTO(devRepo.saveAndFlush(d));
        } catch (DataIntegrityViolationException e) {
            throw new NombreDesarrolladorExistenteException("El nombre del desarrollador ya está en uso");
        }
    }
}