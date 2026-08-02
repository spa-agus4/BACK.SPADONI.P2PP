package BACKEND.SPADONI.P2PP.services;

import BACKEND.SPADONI.P2PP.DTO.DesarrolladorAltaDTO;
import BACKEND.SPADONI.P2PP.DTO.DesarrolladorEdicionDTO;
import BACKEND.SPADONI.P2PP.model.Desarrollador;
import BACKEND.SPADONI.P2PP.model.Proyecto;
import BACKEND.SPADONI.P2PP.repository.DesarrolladorRepository;
import BACKEND.SPADONI.P2PP.repository.ProyectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DesarrolladorService {

    @Autowired
    private DesarrolladorRepository repo;

    @Autowired
    private ProyectoRepository proyectoRepo;

    public Desarrollador obtenerPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Desarrollador no encontrado con ID: " + id));
    }

    public List<Desarrollador> listar() {
        return repo.findAll();
    }

    public List<Desarrollador> listarPorProyectoId(Long proyectoId) {
        return repo.findByProyectoId(proyectoId);
    }

    public void eliminar(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Desarrollador no encontrado con ID: " + id);
        }
        repo.deleteById(id);
    }

    @Transactional
    public void asignarDesarrolladoresAProyecto(Long proyectoId, List<Long> desarrolladoresIds) {
        // Validamos que el proyecto realmente exista en la base de datos
        Proyecto proyecto = proyectoRepo.findById(proyectoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proyecto no encontrado con ID: " + proyectoId));

        // 1. Desvincular a los desarrolladores que antes estaban en este proyecto pero ya no vienen en la lista
        List<Desarrollador> devsActuales = repo.findByProyectoId(proyectoId);
        for (Desarrollador dev : devsActuales) {
            if (!desarrolladoresIds.contains(dev.getId())) {
                dev.setProyecto(null);
                dev.setDisponible(true); // Vuelven a estar libres
            }
        }

        // 2. Vincular a los nuevos desarrolladores seleccionados
        for (Long devId : desarrolladoresIds) {
            Desarrollador dev = repo.findById(devId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Desarrollador no encontrado con ID: " + devId));

            dev.setProyecto(proyecto);
            dev.setDisponible(false); // Ahora están ocupados
        }
    }

    public Desarrollador crear(DesarrolladorAltaDTO datos) {
        Desarrollador d = new Desarrollador();
        d.setNombre(datos.nombre());
        d.setHabilidades(datos.habilidades());
        d.setDisponible(true);
        try {
            return repo.save(d);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre del desarrollador ya está en uso");
        }
    }

    @Transactional
    public Desarrollador actualizar(Long id, DesarrolladorEdicionDTO datos) {
        Desarrollador d = obtenerPorId(id);
        if (datos.nombre() != null) d.setNombre(datos.nombre());
        if (datos.habilidades() != null) d.setHabilidades(datos.habilidades());
        if (datos.disponible() != null) {
            if (!d.getDisponible() && datos.disponible()) {
                d.setProyecto(null);
            }
            d.setDisponible(datos.disponible());
        }
        try {
            return repo.saveAndFlush(d);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre del desarrollador ya está en uso");
        }
    }
}