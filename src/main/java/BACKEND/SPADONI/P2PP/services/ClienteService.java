package BACKEND.SPADONI.P2PP.services;

import BACKEND.SPADONI.P2PP.DTO.ClienteDTO;
import BACKEND.SPADONI.P2PP.DTO.ClienteDatosDTO;
import BACKEND.SPADONI.P2PP.exceptions.ClienteInhabilitadoException;
import BACKEND.SPADONI.P2PP.exceptions.ClienteNoEncontradoException;
import BACKEND.SPADONI.P2PP.exceptions.ClienteSinCredenciales;
import BACKEND.SPADONI.P2PP.mapper.ClienteMapper;
import BACKEND.SPADONI.P2PP.model.Cliente;
import BACKEND.SPADONI.P2PP.repository.ClienteRepository;
import BACKEND.SPADONI.P2PP.repository.ProyectoRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepo;
    private final ProyectoRepository proyectoRepo;
    
    private final ClienteMapper cMapper;
 
    public ClienteService(
            ClienteRepository clienteRepo,
            ClienteMapper cMapper,
            ProyectoRepository proyectoRepo){
        
        this.clienteRepo = clienteRepo;
        this.cMapper = cMapper;
        this.proyectoRepo = proyectoRepo;
    }
  
    private Cliente obtenerPorId(Long id) {
        return clienteRepo.findById(id)
                .orElseThrow(() -> new ClienteNoEncontradoException("Cliente no encontrado con ID: " + id));
    }

    public List<ClienteDTO> listar() {
        return cMapper.toDTO(clienteRepo.findAll());
    }

    public ClienteDTO crear(ClienteDatosDTO datos) {
        Cliente cliente = new Cliente();
        cliente.setNombre(datos.nombre());
        cliente.setDireccion(datos.direccion());
        cliente.setEmail(datos.email());
        cliente.setTelefono(datos.telefono());
        cliente.setActivo(false); // sin credenciales todavía
        return cMapper.toDTO(clienteRepo.save(cliente));
    }

    public ClienteDTO eliminar(Long id) {
        
        Cliente cliente = obtenerPorId(id);

        if(!puedeEliminarse(cliente)){
            cliente.setActivo(false);
            return cMapper.toDTO(clienteRepo.save(cliente)); // Retorna el cliente inhabilitado (activo = false)
            //El cliente tiene proyectos asociados, no se puede eliminar
        }

        clienteRepo.delete(cliente);
        return null; // Retorna null indicando que fue borrado físicamente
    }
    
    private boolean puedeEliminarse(Cliente c){   
        return !proyectoRepo.existsByClienteId(c.getId());    // El cliente tiene proyectos asociados, por lo que no se elimina. Se lo inhabilita.
    }
    
    public Cliente obtenerActivo(Long clienteId) {

        Cliente cliente = obtenerPorId(clienteId);
        
        /*  NO PASA NADA SI EL CLIENTE ESTÁ INACTIVO, SE LE ASIGA EL PROYECTO PERO EL CLIENTE IGUAL NO PUEDE ENTRAR A SU CUENTA
        if (Boolean.FALSE.equals(cliente.getActivo())) {
            throw new ClienteInhabilitadoException("No se pueden asignar proyectos a un cliente inhabilitado");
        }*/

        return cliente;
    }
    
    public void habilitar(Long id) {
        Cliente cliente = obtenerPorId(id); // Trae el cliente o tira excepción si no existe

        // Validamos primero que no sea null, y luego que no esté vacío.
        if(cliente.getNombreUsuario() == null || cliente.getNombreUsuario().trim().isEmpty()) {
            throw new ClienteSinCredenciales("El cliente no puede ser habilitado porque no tiene credenciales");
        }

        cliente.setActivo(true);
        clienteRepo.save(cliente);
    }

    public ClienteDTO actualizarDatosPersonales(Long id, ClienteDatosDTO datos) {
        Cliente cliente = obtenerPorId(id); // tira exception si no lo encuentra
        cliente.setNombre(datos.nombre());
        cliente.setDireccion(datos.direccion());
        cliente.setEmail(datos.email());
        cliente.setTelefono(datos.telefono());
        return cMapper.toDTO(clienteRepo.save(cliente));
    }
}