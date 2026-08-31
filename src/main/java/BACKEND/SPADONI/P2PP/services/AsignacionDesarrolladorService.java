package BACKEND.SPADONI.P2PP.services;

import BACKEND.SPADONI.P2PP.exceptions.DesarrolladorNoDisponibleException;
import BACKEND.SPADONI.P2PP.exceptions.DesarrolladorNoEncontradoException;
import BACKEND.SPADONI.P2PP.exceptions.ProyectoNoEncontradoException;
import BACKEND.SPADONI.P2PP.model.Desarrollador;
import BACKEND.SPADONI.P2PP.model.Proyecto;
import BACKEND.SPADONI.P2PP.repository.DesarrolladorRepository;
import BACKEND.SPADONI.P2PP.repository.ProyectoRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AsignacionDesarrolladorService {

    private final DesarrolladorRepository devRepo;
    private final ProyectoRepository proyectoRepo;

    public AsignacionDesarrolladorService(DesarrolladorRepository devRepo, ProyectoRepository proyectoRepo) {
        this.devRepo = devRepo;
        this.proyectoRepo = proyectoRepo;
    }

    @Transactional
    public void asignarDesarrolladoresAProyecto(Long proyectoId, List<Long> desarrolladoresIds) {

        Proyecto proyecto = obtenerProyectoPorId(proyectoId);

        sincronizarDesarrolladores(proyecto, desarrolladoresIds);
    }

    private void sincronizarDesarrolladores(Proyecto proyecto, List<Long> desarrolladoresIds) {

        List<Desarrollador> devsActuales = devRepo.findByProyectoId(proyecto.getId());

        removerDesarrolladoresQueYaNoEstan(devsActuales, desarrolladoresIds);

        asignarDesarrolladores(proyecto, desarrolladoresIds);
    }

    private void removerDesarrolladoresQueYaNoEstan(List<Desarrollador> devsActuales, List<Long> desarrolladoresIds) {

        for (Desarrollador dev : devsActuales) {

            if (!desarrolladoresIds.contains(dev.getId())) {
                removerDevDeProyecto(dev);
            }
        }
    }

    private void asignarDesarrolladores(Proyecto proyecto, List<Long> desarrolladoresIds) {

        for (Long devId : desarrolladoresIds) {
            Desarrollador dev = obtenerDesarrolladorPorId(devId);

            validarDisponibilidad(dev, proyecto);

            dev.setProyecto(proyecto);
            dev.setDisponible(false);
        }
    }

    private void validarDisponibilidad(Desarrollador dev, Proyecto proyecto) {
        if (dev.getProyecto() != null && !dev.getProyecto().getId().equals(proyecto.getId())) {
            throw new DesarrolladorNoDisponibleException("El desarrollador con ID: " + dev.getId() + " ya está asignado a otro proyecto");
        }
    }

    private void removerDevDeProyecto(Desarrollador dev) {
        dev.setProyecto(null);
        dev.setDisponible(true);
    }

    private Proyecto obtenerProyectoPorId(Long id) {
        return proyectoRepo.findById(id)
                .orElseThrow(() -> new ProyectoNoEncontradoException("Proyecto no encontrado con ID: " + id));
    }

    private Desarrollador obtenerDesarrolladorPorId(Long id) {
        return devRepo.findById(id)
                .orElseThrow(() -> new DesarrolladorNoEncontradoException("Desarrollador no encontrado con ID: " + id));
    }

    @Transactional
    public void removerDevsDeProyecto(Long proyectoId) {

        List<Desarrollador> devs = devRepo.findByProyectoId(proyectoId);

        for (Desarrollador dev : devs) {
            removerDevDeProyecto(dev);
        }
    }
}