package BACKEND.SPADONI.P2PP.controllers;

import BACKEND.SPADONI.P2PP.DTO.ProyectoDTO;
import BACKEND.SPADONI.P2PP.DTO.ProyectoDatosDTO;
import BACKEND.SPADONI.P2PP.services.ProyectoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/proyectos")
@CrossOrigin(origins = "${app.frontend.url}")
public class ProyectoController {

    private final ProyectoService proyectoService;

    public ProyectoController(ProyectoService proyectoService) { this.proyectoService = proyectoService; }

    // Devuelve solo los proyectos accesibles para el usuario autenticado. Gracias al factory
    @GetMapping
    public List<ProyectoDTO> listar(Authentication auth) {
        return proyectoService.listar(auth.getName());
    }   // Auth es el contexto que arma el JwtFilter, de ahi obtenemos el nombreUsuario para evitar que todos accedan a todos los proyectos. Cada accede a los que tiene acceso.

    // Obtiene un proyecto por ID asegurando que el usuario tenga acceso a él.
    @GetMapping("/{id}")
    public ProyectoDTO obtener(@PathVariable Long id, Authentication auth) {
        return proyectoService.obtenerProyecto(id, auth.getName());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProyectoDTO crear(@Valid @RequestBody ProyectoDatosDTO datos) {
        return proyectoService.crear(datos);
    }

    @PutMapping("/{id}")
    public ProyectoDTO actualizar(@PathVariable Long id, @Valid @RequestBody ProyectoDatosDTO datos) {
        return proyectoService.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        proyectoService.eliminar(id);
    }
}