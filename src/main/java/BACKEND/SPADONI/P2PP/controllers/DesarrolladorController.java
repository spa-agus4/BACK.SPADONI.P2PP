package BACKEND.SPADONI.P2PP.controllers;

import BACKEND.SPADONI.P2PP.DTO.DesarrolladorAltaDTO;
import BACKEND.SPADONI.P2PP.DTO.DesarrolladorEdicionDTO;
import BACKEND.SPADONI.P2PP.model.Desarrollador;
import BACKEND.SPADONI.P2PP.model.Proyecto;
import BACKEND.SPADONI.P2PP.model.Usuario;
import BACKEND.SPADONI.P2PP.repository.UsuarioRepository;
import BACKEND.SPADONI.P2PP.services.DesarrolladorService;
import BACKEND.SPADONI.P2PP.services.ProyectoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/desarrolladores")
@CrossOrigin(origins = "${app.frontend.url}")
public class DesarrolladorController {

    @Autowired
    private DesarrolladorService service;

    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario getUsuarioAutenticado(Authentication auth) {
        return usuarioRepository.findByNombreUsuario(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado"));
    }

    @GetMapping("/{id}")
    public Desarrollador obtener(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @GetMapping
    public List<Desarrollador> listar() {
        return service.listar();
    }

    /**
     * Devuelve los desarrolladores asignados a un proyecto específico.
     * Valida que el usuario autenticado tenga permisos para acceder a dicho proyecto.
     */
    @GetMapping("/proyecto/{proyectoId}")
    public List<Desarrollador> listarPorProyecto(@PathVariable Long proyectoId, Authentication auth) {
        Usuario usuario = getUsuarioAutenticado(auth);
        
        // Verificamos si el proyecto con ID proyectoId es uno de los que el usuario puede ver
        List<Proyecto> accesibles = proyectoService.listar(usuario.getId());
        boolean tieneAcceso = accesibles.stream().anyMatch(p -> p.getId().equals(proyectoId));

        if (!tieneAcceso) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tenés permiso para ver los desarrolladores de este proyecto");
        }

        return service.listarPorProyectoId(proyectoId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Desarrollador crear(@Valid @RequestBody DesarrolladorAltaDTO datos) {
        return service.crear(datos);
    }

    @PutMapping("/{id}")
    public Desarrollador actualizar(@PathVariable Long id, @Valid @RequestBody DesarrolladorEdicionDTO datos) {
        return service.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    // Asigna un listado de IDs de desarrolladores a un proyecto.
    @PostMapping("/proyecto/{proyectoId}/asignar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void asignarDevs(@PathVariable Long proyectoId, @RequestBody List<Long> devsIds) {
        service.asignarDesarrolladoresAProyecto(proyectoId, devsIds);
    }
}