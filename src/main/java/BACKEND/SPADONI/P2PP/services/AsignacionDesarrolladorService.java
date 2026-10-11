package BACKEND.SPADONI.P2PP.services;

import BACKEND.SPADONI.P2PP.exceptions.DesarrolladorNoDisponibleException;
import BACKEND.SPADONI.P2PP.exceptions.DesarrolladorNoEncontradoException;
import BACKEND.SPADONI.P2PP.exceptions.ProyectoNoEncontradoException;
import BACKEND.SPADONI.P2PP.model.Desarrollador;
import BACKEND.SPADONI.P2PP.model.Proyecto;
import BACKEND.SPADONI.P2PP.model.ProyectoDesarrollador;
import BACKEND.SPADONI.P2PP.repository.DesarrolladorRepository;
import BACKEND.SPADONI.P2PP.repository.ProyectoDesarrolladorRepository;
import BACKEND.SPADONI.P2PP.repository.ProyectoRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AsignacionDesarrolladorService {

    private final DesarrolladorRepository devRepo;
    private final ProyectoRepository proyectoRepo;
    private final ProyectoDesarrolladorRepository proyectoDesarrolladorRepo;

    public AsignacionDesarrolladorService(
            DesarrolladorRepository devRepo,
            ProyectoRepository proyectoRepo,
            ProyectoDesarrolladorRepository proyectoDesarrolladorRepo) {
        this.devRepo = devRepo;
        this.proyectoRepo = proyectoRepo;
        this.proyectoDesarrolladorRepo = proyectoDesarrolladorRepo;
    }

    @Transactional
    public void asignarDesarrolladoresAProyecto(Long proyectoId, List<Long> desarrolladoresIds) {

        Proyecto proyecto = obtenerProyectoPorId(proyectoId);

        sincronizarDesarrolladores(proyecto, desarrolladoresIds);
    }

    private void sincronizarDesarrolladores(Proyecto proyecto, List<Long> desarrolladoresIds) {

        //List<Desarrollador> devsActuales = devRepo.findByProyectoId(proyecto.getId()); // cambiar metodo y usar el repo nuevo
        //List<Desarrollador> devsActuales = proyectoDesarrolladorRepo.findDesarrolladoresActivosPorProyectoId(proyecto.getId());
        List<ProyectoDesarrollador> proyectoDevActuales = proyectoDesarrolladorRepo.findByProyecto_IdAndFechaDesasignacionIsNull(proyecto.getId());

        removerDesarrolladoresQueYaNoEstan(proyectoDevActuales, desarrolladoresIds);

        asignarDesarrolladores(proyecto, desarrolladoresIds);
    }

    private void removerDesarrolladoresQueYaNoEstan(List<ProyectoDesarrollador> proyectoDevActuales, List<Long> desarrolladoresIds) {

        for (ProyectoDesarrollador pd : proyectoDevActuales) {

            if (!desarrolladoresIds.contains(pd.getDesarrollador().getId())) {
                //removerDevDeProyecto(dev);
                desasignarDev(pd);
            }
        }
    }

    private void asignarDesarrolladores(Proyecto proyecto, List<Long> desarrolladoresIds) {
        for (Long devId : desarrolladoresIds) {
            Desarrollador dev = obtenerDesarrolladorPorId(devId);
            asignarDev(dev, proyecto);
        }
    }

    private void validarDisponibilidad(Desarrollador dev, Proyecto proyecto) {

        /*if (dev.getProyecto() != null && !dev.getProyecto().getId().equals(proyecto.getId())) {
            throw new DesarrolladorNoDisponibleException("El desarrollador con ID: " + dev.getId() + " ya está asignado a otro proyecto");
        }*/
        Optional<ProyectoDesarrollador> pDev = proyectoDesarrolladorRepo.findByDesarrollador_IdAndFechaDesasignacionIsNull(dev.getId());

        // Verificamos si hay una asignación activa (si el Optional tiene contenido)
        if (pDev.isPresent()) {
            // Sacamos el objeto ProyectoDesarrollador de adentro del Optional
            ProyectoDesarrollador asignacionActiva = pDev.get();

            // Comparamos los IDs de los proyectos
            if (!asignacionActiva.getProyecto().getId().equals(proyecto.getId())) {
                throw new DesarrolladorNoDisponibleException("El desarrollador ya está asignado a un proyecto distinto (ID: " + asignacionActiva.getProyecto().getId() + ")");
            }
        }
    }

    private void asignarDev(Desarrollador dev, Proyecto proyecto) {

        Optional<ProyectoDesarrollador> asignacionOpt = proyectoDesarrolladorRepo.findByDesarrollador_IdAndFechaDesasignacionIsNull(dev.getId());

        if (asignacionOpt.isPresent()) {
            ProyectoDesarrollador asignacion = asignacionOpt.get();

            if (!asignacion.getProyecto().getId().equals(proyecto.getId())) {
                throw new DesarrolladorNoDisponibleException("El desarrollador con ID: " + dev.getId()+ " ya está asignado a otro proyecto");
            }
            return; // Ya está asignado a este proyecto; no duplicamos el registro.
        }

        if (!Boolean.TRUE.equals(dev.getDisponible())) {
            throw new DesarrolladorNoDisponibleException("El desarrollador con ID: " + dev.getId()+ " no está disponible");
        }

        ProyectoDesarrollador nuevaAsignacion = new ProyectoDesarrollador();
        nuevaAsignacion.setProyecto(proyecto);
        nuevaAsignacion.setDesarrollador(dev);
        nuevaAsignacion.setFechaAsignacion(LocalDate.now());

        proyectoDesarrolladorRepo.save(nuevaAsignacion);

        // Mantener sincronizado el modelo actual mientras conserve estos campos.
        dev.setProyecto(proyecto);
        //dev.setDisponible(false);
    }

    private void removerDevDeProyecto(Desarrollador dev) {  // metodo viejo
        dev.setProyecto(null);
        //dev.setDisponible(true);
    }

    private void desasignarDev(ProyectoDesarrollador pd) {
        pd.setFechaDesasignacion(LocalDate.now());

        Desarrollador dev = pd.getDesarrollador();
        dev.setProyecto(null);
        //dev.setDisponible(true);
    }

    private Proyecto obtenerProyectoPorId(Long id) {
        return proyectoRepo.findById(id)
                .orElseThrow(() -> new ProyectoNoEncontradoException("Proyecto no encontrado con ID: " + id));
    }

    private Desarrollador obtenerDesarrolladorPorId(Long id) {
        return devRepo.findById(id)
                .orElseThrow(() -> new DesarrolladorNoEncontradoException("Desarrollador no encontrado con ID: " + id));
    }

    /*@Transactional
    public void removerDevsDeProyecto(Long proyectoId) {

        List<Desarrollador> devs = devRepo.findByProyectoId(proyectoId);

        for (Desarrollador dev : devs) {
            removerDevDeProyecto(dev);
        }
    }*/
    @Transactional
    public void cerrarAsignacionesVigentesDeProyecto(Long proyectoId) {

        List<ProyectoDesarrollador> asignaciones = proyectoDesarrolladorRepo.findByProyecto_IdAndFechaDesasignacionIsNull(proyectoId);

        for (ProyectoDesarrollador asignacion : asignaciones) {
            desasignarDev(asignacion);
        }
    }
    
    @Transactional
    public void eliminarHistorialDeProyecto(Long proyectoId) {

        List<ProyectoDesarrollador> asignaciones = proyectoDesarrolladorRepo.findByProyecto_IdAndFechaDesasignacionIsNull(proyectoId);

        for (ProyectoDesarrollador asignacion : asignaciones) {
            Desarrollador dev = asignacion.getDesarrollador();

            dev.setProyecto(null);
            //dev.setDisponible(true);
        }

        proyectoDesarrolladorRepo.deleteByProyecto_Id(proyectoId);
    }
    
    @Transactional
    public void eliminarHistorialDeDesarrollador(Long desarrolladorId) {
        proyectoDesarrolladorRepo.deleteByDesarrollador_Id(desarrolladorId);
    }
    
    public List<Desarrollador> listarAsignadosActuales(Long proyectoId){
        return proyectoDesarrolladorRepo.findDesarrolladoresActivosPorProyectoId(proyectoId);
    }
    
    public List<Desarrollador> listarParticipantesHistoricos(Long proyectoId){
        return proyectoDesarrolladorRepo.findHistorialDesarrolladoresPorProyectoId(proyectoId);
    }
}
