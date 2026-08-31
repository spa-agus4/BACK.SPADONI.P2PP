package BACKEND.SPADONI.P2PP.services.proyecto;

import BACKEND.SPADONI.P2PP.model.Proyecto;
import BACKEND.SPADONI.P2PP.model.Rol;
import BACKEND.SPADONI.P2PP.model.Usuario;
import BACKEND.SPADONI.P2PP.repository.ProyectoRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GerenteProyectoViewer implements ProyectoViewer {

    private final ProyectoRepository repo;

    public GerenteProyectoViewer(ProyectoRepository repo) {
        this.repo = repo;
    }

    @Override
    public boolean puedeVerProyecto(Usuario u, Proyecto p) {
        return (u.getRol() == Rol.GERENTE);
    }

    @Override
    public List<Proyecto> mostrarProyectos(Usuario usuario) {
        return repo.findAll();
    }
}
