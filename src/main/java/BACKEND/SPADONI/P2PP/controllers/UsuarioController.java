package BACKEND.SPADONI.P2PP.controllers;

import BACKEND.SPADONI.P2PP.DTO.CredencialesDTO;
import BACKEND.SPADONI.P2PP.DTO.RespuestaActualizacion;
import BACKEND.SPADONI.P2PP.DTO.UsuarioAltaDTO;
import BACKEND.SPADONI.P2PP.DTO.UsuarioDTO;
import BACKEND.SPADONI.P2PP.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "${app.frontend.url}")
public class UsuarioController {

    private UsuarioService service;
    
    public UsuarioController(UsuarioService service){ this.service = service; }
    
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioDTO crear(@Valid @RequestBody UsuarioAltaDTO datos) {
        return service.crear(datos);
    }
    
    @PutMapping("/{id}/habilitar")
    public ResponseEntity<Void> habilitar(@PathVariable Long id) {
        service.habilitar(id);
        return ResponseEntity.noContent().build(); // Devuelve HTTP 204 No Content
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<UsuarioDTO> eliminar(Authentication auth, @PathVariable Long id) {
        
        UsuarioDTO usuarioAEliminar = service.eliminar(id, auth.getName());

        // Si retornó el usuario (significa que solo se inhabilitó) -> HTTP 200 con el usuario en el body
        if (usuarioAEliminar != null) {
            return ResponseEntity.ok(usuarioAEliminar);
        }

        // Si retornó null (se eliminó de la BD) -> HTTP 204 No Content
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<RespuestaActualizacion> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CredencialesDTO datos,
            Authentication auth) {
        
        RespuestaActualizacion respuesta = service.actualizar(id, datos, auth.getName());

        return ResponseEntity.ok(respuesta);
    }
    
    @GetMapping
    public List<UsuarioDTO> listar(Authentication auth) {
        return service.listar(auth.getName());
    }
}