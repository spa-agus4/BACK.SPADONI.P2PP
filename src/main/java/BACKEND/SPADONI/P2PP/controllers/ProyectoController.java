package BACKEND.SPADONI.P2PP.controllers;

import BACKEND.SPADONI.P2PP.DTO.ProyectoDatosDTO;
import BACKEND.SPADONI.P2PP.model.Proyecto;
import BACKEND.SPADONI.P2PP.model.Usuario;
import BACKEND.SPADONI.P2PP.repository.UsuarioRepository;
import BACKEND.SPADONI.P2PP.services.ProyectoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/proyectos")
@CrossOrigin(origins = "${app.frontend.url}") // React
public class ProyectoController {

    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Auxiliar para obtener el objeto Usuario a partir del token JWT actual
    private Usuario getUsuarioAutenticado(Authentication auth) {
        return usuarioRepository.findByNombreUsuario(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado"));
    }

    // Devuelve solo los proyectos accesibles para el usuario autenticado. Gracias a usuario.filtrarProyectos(repo) en la capa de modelo/servicio.
    @GetMapping
    public List<Proyecto> listar(Authentication auth) {
        Usuario usuario = getUsuarioAutenticado(auth);
        return proyectoService.listar(usuario.getId());
    }   // Auth es el contexto que arma el JwtFilter, de ahi obtenemos el nombreUsuario para evitar que todos accedan a todos los proyectos. Cada accede a los que tiene acceso.

    // Obtiene un proyecto por ID asegurando que el usuario tenga acceso a él.
    @GetMapping("/{id}")
    public Proyecto obtener(@PathVariable Long id, Authentication auth) {
        return listar(auth).stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "No tenés permiso para ver este proyecto"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Proyecto crear(@Valid @RequestBody ProyectoDatosDTO datos) {
        return proyectoService.crear(datos);
    }

    @PutMapping("/{id}")
    public Proyecto actualizar(@PathVariable Long id, @Valid @RequestBody ProyectoDatosDTO datos) {
        return proyectoService.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        proyectoService.eliminar(id);
    }
}