package BACKEND.SPADONI.P2PP.controllers;

import BACKEND.SPADONI.P2PP.DTO.ClienteDTO;
import BACKEND.SPADONI.P2PP.DTO.ClienteDatosDTO;
import BACKEND.SPADONI.P2PP.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "${app.frontend.url}")
public class ClienteController {

    private final ClienteService service;
    
    public ClienteController(ClienteService service){ this.service = service; }

    @GetMapping
    public List<ClienteDTO> listar() {
        return service.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteDTO crear(@Valid @RequestBody ClienteDatosDTO datos) {
        return service.crear(datos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ClienteDTO> eliminar(@PathVariable Long id) {
        ClienteDTO resultado = service.eliminar(id);
        if (resultado != null) {
            return ResponseEntity.ok(resultado);
        }
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}/habilitar")
    public ResponseEntity<Void> habilitar(@PathVariable Long id) {
        service.habilitar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ClienteDTO actualizar(@PathVariable Long id, @Valid @RequestBody ClienteDatosDTO datos) {
        return service.actualizarDatosPersonales(id, datos);
    }
}