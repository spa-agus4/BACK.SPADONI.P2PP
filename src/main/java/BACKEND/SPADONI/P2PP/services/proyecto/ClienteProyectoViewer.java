package BACKEND.SPADONI.P2PP.services.proyecto;

import BACKEND.SPADONI.P2PP.model.Proyecto;
import BACKEND.SPADONI.P2PP.model.Usuario;
import BACKEND.SPADONI.P2PP.repository.ProyectoRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ClienteProyectoViewer implements ProyectoViewer {

    private final ProyectoRepository repo;

    public ClienteProyectoViewer(ProyectoRepository repo) {
        this.repo = repo;
    }

    @Override
    public boolean puedeVerProyecto(Usuario u, Proyecto p) {
        return (p.getCliente().getId().equals(u.getId()));
    }

    @Override
    public List<Proyecto> mostrarProyectos(Usuario usuario) {
        return repo.findByClienteId(usuario.getId());
    }
}