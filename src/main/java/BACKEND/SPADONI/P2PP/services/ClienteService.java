package BACKEND.SPADONI.P2PP.services;

import BACKEND.SPADONI.P2PP.DTO.ClienteDatosDTO;
import BACKEND.SPADONI.P2PP.model.Cliente;
import BACKEND.SPADONI.P2PP.model.Proyecto;
import BACKEND.SPADONI.P2PP.model.Usuario;
import BACKEND.SPADONI.P2PP.repository.ClienteRepository;
import BACKEND.SPADONI.P2PP.repository.ProyectoRepository;
import BACKEND.SPADONI.P2PP.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepo;
    private final ProyectoRepository proyectoRepo;

    public ClienteService(ClienteRepository clienteRepo, ProyectoRepository proyectoRepo) {
        this.clienteRepo = clienteRepo;
        this.proyectoRepo = proyectoRepo;
    }

    public Cliente obtenerPorId(Long id) {
        return clienteRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado con ID: " + id));
    }

    public List<Cliente> listar() {
        return clienteRepo.findAll(); // Devuelve directamente List<Cliente>
    }

    public Cliente crear(ClienteDatosDTO datos) {
        Cliente cliente = new Cliente();
        cliente.setNombre(datos.nombre());
        cliente.setDireccion(datos.direccion());
        cliente.setEmail(datos.email());
        cliente.setTelefono(datos.telefono());
        cliente.setActivo(false); // sin credenciales todavía
        return clienteRepo.save(cliente);
    }

    public Cliente eliminar(Long id) {
        Cliente cliente = clienteRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));

        if (cliente.puedeEliminarse(proyectoRepo)) {
            clienteRepo.delete(cliente);
            return null; // se eliminó físicamente
        }

        cliente.setActivo(false);
        return clienteRepo.save(cliente); // se inhabilitó
    }
    
    public void habilitar(Long id) {
        Cliente cliente = clienteRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
        cliente.setActivo(true);
        clienteRepo.save(cliente);
    }

    public Cliente actualizarDatosPersonales(Long id, ClienteDatosDTO datos) {
        Cliente cliente = clienteRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
        cliente.setNombre(datos.nombre());
        cliente.setDireccion(datos.direccion());
        cliente.setEmail(datos.email());
        cliente.setTelefono(datos.telefono());
        return clienteRepo.save(cliente);
    }
}