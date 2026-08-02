package BACKEND.SPADONI.P2PP.services;

import BACKEND.SPADONI.P2PP.DTO.ProyectoDatosDTO;
import BACKEND.SPADONI.P2PP.model.Cliente;
import BACKEND.SPADONI.P2PP.model.Desarrollador;
import BACKEND.SPADONI.P2PP.model.EstadoProceso;
import BACKEND.SPADONI.P2PP.model.Proyecto;
import BACKEND.SPADONI.P2PP.model.Usuario;
import BACKEND.SPADONI.P2PP.repository.ClienteRepository;
import BACKEND.SPADONI.P2PP.repository.DesarrolladorRepository;
import BACKEND.SPADONI.P2PP.repository.ProyectoRepository;
import BACKEND.SPADONI.P2PP.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProyectoService {

    @Autowired
    private ProyectoRepository proyectoRepo;

    @Autowired
    private UsuarioRepository usuarioRepo; // Necesario para buscar al usuario que solicita los proyectos

    @Autowired
    private DesarrolladorRepository desarrolladorRepo;

    @Autowired
    private ClienteRepository clienteRepo;

    public Proyecto obtenerPorId(Long id) {
        return proyectoRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proyecto no encontrado con ID: " + id));
    }

    /**
     * Método polimórfico principal para listar proyectos. Le pide al modelo
     * Usuario (Cliente, Gerente, Admin) que ejecute su propia lógica.
     */
    public List<Proyecto> listar(Long usuarioId) {
        Usuario usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + usuarioId));

        // El usuario ejecuta su propia versión de filtrarProyectos(repo)
        return usuario.filtrarProyectos(proyectoRepo);
    }

    public Proyecto crear(ProyectoDatosDTO datos) {
        Proyecto p = new Proyecto();
        p.setCliente(obtenerClienteActivo(datos.clienteId()));
        p = aplicarDatos(p, datos);
        p.setEstadoProceso(datos.estadoProceso());
        return proyectoRepo.save(p);
    }

    private Cliente obtenerClienteActivo(Long clienteId) {  // veificamos si el cliente està inhabilitado o no
        Cliente cliente = clienteRepo.findById(clienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
        if (Boolean.FALSE.equals(cliente.getActivo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se pueden asignar proyectos a un cliente inhabilitado");
        }
        return cliente;
    }

    @Transactional
    public Proyecto actualizar(Long id, ProyectoDatosDTO datos) {
        Proyecto p = obtenerPorId(id);
        if (!datos.clienteId().equals(p.getCliente().getId())) {
            p.setCliente(obtenerClienteActivo(datos.clienteId()));
        }

        boolean saleDeEnCurso = p.getEstadoProceso() == EstadoProceso.EN_CURSO
                && datos.estadoProceso() != EstadoProceso.EN_CURSO;

        Proyecto guardado = proyectoRepo.save(aplicarDatos(p, datos));

        if (saleDeEnCurso) {
            liberarDevs(id);
            // fecha final (hoy)
        }

        return guardado;
    }

    private void liberarDevs(Long proyectoId) {
        List<Desarrollador> devs = desarrolladorRepo.findByProyectoId(proyectoId);
        for (Desarrollador dev : devs) {
            dev.setProyecto(null);
            dev.setDisponible(true);
        }
        desarrolladorRepo.saveAll(devs);
    }

    private Proyecto aplicarDatos(Proyecto p, ProyectoDatosDTO datos) {
        p.setNombre(datos.nombre());
        p.setCanalSolicitud(datos.canalSolicitud());
        p.setFechaInicio(datos.fechaInicio());
        p.setFechaFinalizacion(datos.fechaFinalizacion());
        p.setPresupuesto(datos.presupuesto());
        p.setEstadoProyecto(datos.estadoProyecto());
        p.setEstadoProceso(datos.estadoProceso());
        return p;
    }

    @Transactional
    public void eliminar(Long proyectoId) {
        Proyecto proyecto = proyectoRepo.findById(proyectoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proyecto no encontrado"));
        liberarDevs(proyectoId);
        proyectoRepo.delete(proyecto);
    }
}
