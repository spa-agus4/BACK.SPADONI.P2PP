package BACKEND.SPADONI.P2PP.controllers;

import BACKEND.SPADONI.P2PP.DTO.DesarrolladorAltaDTO;
import BACKEND.SPADONI.P2PP.DTO.DesarrolladorDTO;
import BACKEND.SPADONI.P2PP.DTO.DesarrolladorEdicionDTO;
import BACKEND.SPADONI.P2PP.services.AsignacionDesarrolladorService;
import BACKEND.SPADONI.P2PP.services.DesarrolladorService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/desarrolladores")
@CrossOrigin(origins = "${app.frontend.url}")
public class DesarrolladorController {

    private final DesarrolladorService devService;
    private final AsignacionDesarrolladorService asignacionDevService;

    public DesarrolladorController(DesarrolladorService devService, AsignacionDesarrolladorService asignacionDevService) {
        this.devService = devService;
        this.asignacionDevService = asignacionDevService;
    }

    @GetMapping
    public List<DesarrolladorDTO> listar() {
        return devService.listar();
    }

    /**
     * Devuelve los desarrolladores asignados a un proyecto específico. Valida
     * que el usuario autenticado tenga permisos para acceder a dicho proyecto.
     */
    @GetMapping("/proyecto/{proyectoId}")
    public List<DesarrolladorDTO> listarPorProyecto(@PathVariable Long proyectoId, Authentication auth) {
        return devService.listarAsignadosActualesPorProyecto(proyectoId, auth.getName());
    }
    
    // Lo mismo que arriba, pero es para ser llamada desde la lista histórica
    @GetMapping("/proyecto/historial/{proyectoId}")
    public List<DesarrolladorDTO> listarHistorialPorProyecto(@PathVariable Long proyectoId, Authentication auth) {
        return devService.listarHistorialPorProyecto(proyectoId, auth.getName());
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DesarrolladorDTO crear(@Valid @RequestBody DesarrolladorAltaDTO datos) {
        return devService.crear(datos);
    }

    @PutMapping("/{id}")
    public DesarrolladorDTO actualizar(@PathVariable Long id, @Valid @RequestBody DesarrolladorEdicionDTO datos) {
        return devService.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        devService.eliminar(id);
    }

    // Asigna un listado de IDs de desarrolladores a un proyecto.
    @PostMapping("/proyecto/{proyectoId}/asignar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void asignarDevs(@PathVariable Long proyectoId, @RequestBody List<Long> devsIds) {
        asignacionDevService.asignarDesarrolladoresAProyecto(proyectoId, devsIds);
    }
}