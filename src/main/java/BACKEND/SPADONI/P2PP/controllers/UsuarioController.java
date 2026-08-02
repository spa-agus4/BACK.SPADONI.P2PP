package BACKEND.SPADONI.P2PP.controllers;

import BACKEND.SPADONI.P2PP.DTO.CredencialesDTO;
import BACKEND.SPADONI.P2PP.DTO.RespuestaActualizacion;
import BACKEND.SPADONI.P2PP.DTO.UsuarioAltaDTO;
import BACKEND.SPADONI.P2PP.model.Usuario;
import BACKEND.SPADONI.P2PP.repository.UsuarioRepository;
import BACKEND.SPADONI.P2PP.security.JwtUtil;
import BACKEND.SPADONI.P2PP.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "${app.frontend.url}") // React
public class UsuarioController {

    @Autowired
    private UsuarioService service;
    
    @Autowired
    private UsuarioRepository repo;
    
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/{id}")
    public Usuario obtener(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }
    
    // Auxiliar para obtener el objeto Usuario a partir del token JWT actual    
    private Usuario getUsuarioAutenticado(Authentication auth) {
    System.out.println("AUTH = " + auth);
    System.out.println("AUTH NAME = " + auth.getName());

    return repo.findByNombreUsuario(auth.getName())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado"));
    }
    
    @GetMapping
    public List<Usuario> listar(Authentication auth) {
        Usuario usuarioLogueado = getUsuarioAutenticado(auth);
        return service.listar(usuarioLogueado.getId());
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario crear(@Valid @RequestBody UsuarioAltaDTO datos) {
        return service.crear(datos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Usuario> eliminar(@PathVariable Long id) {
        Usuario usuarioResultado = service.eliminar(id);

        // Si retornó el usuario (significa que solo se inhabilitó) -> HTTP 200 con el usuario en el body
        if (usuarioResultado != null) {
            return ResponseEntity.ok(usuarioResultado);
        }

        // Si retornó null (se eliminó de la BD) -> HTTP 204 No Content
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}/habilitar")
    public ResponseEntity<Void> habilitar(@PathVariable Long id) {
        service.habilitar(id);
        return ResponseEntity.noContent().build(); // Devuelve HTTP 204 No Content
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<RespuestaActualizacion> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CredencialesDTO datos,
            Authentication auth) {

        // Obtener el usuario autenticado ANTES de modificar la BD
        Usuario usuarioLogueado = getUsuarioAutenticado(auth);

        // 1. Modificamos las credenciales mediante el servicio
        Usuario usuarioActualizado = service.modificarCredenciales(id, datos);
        String nuevoToken = null;

        // 2. Si editó su propia cuenta, regeneramos el JWT
        if (usuarioLogueado.getId().equals(usuarioActualizado.getId())) {
            nuevoToken = jwtUtil.generarToken(
                    usuarioActualizado.getNombreUsuario(),
                    usuarioActualizado.getRol().toString(),
                    usuarioActualizado.getId()
            );
        }
        // 3. Si editó a OTRO usuario, 'nuevoToken' va como null
        return ResponseEntity.ok(new RespuestaActualizacion(usuarioActualizado, nuevoToken));
    }
}