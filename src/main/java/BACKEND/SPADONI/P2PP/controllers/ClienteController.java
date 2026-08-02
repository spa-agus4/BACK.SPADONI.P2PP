package BACKEND.SPADONI.P2PP.controllers;

import BACKEND.SPADONI.P2PP.DTO.ClienteDatosDTO;
import BACKEND.SPADONI.P2PP.DTO.RespuestaActualizacion;
import BACKEND.SPADONI.P2PP.model.Cliente;
import BACKEND.SPADONI.P2PP.repository.ClienteRepository;
import BACKEND.SPADONI.P2PP.security.JwtUtil;
import BACKEND.SPADONI.P2PP.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "${app.frontend.url}") // React
public class ClienteController {

    @Autowired
    private ClienteService service;

    @Autowired
    private ClienteRepository repo;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/{id}")
    public Cliente obtener(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @GetMapping
    public List<Cliente> listar() {
        return service.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente crear(@Valid @RequestBody ClienteDatosDTO datos) {
        return service.crear(datos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Cliente> eliminar(@PathVariable Long id) {
        Cliente resultado = service.eliminar(id);
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
    public Cliente actualizar(@PathVariable Long id, @Valid @RequestBody ClienteDatosDTO datos) {
        return service.actualizarDatosPersonales(id, datos);
    }
}