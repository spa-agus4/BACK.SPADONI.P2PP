package BACKEND.SPADONI.P2PP.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.util.List;
import BACKEND.SPADONI.P2PP.repository.ProyectoRepository;
import BACKEND.SPADONI.P2PP.repository.UsuarioRepository;

@Entity
@DiscriminatorValue("GERENTE")
public class Gerente extends Usuario {

    public Gerente() {
        super();
    }

    public Gerente(String nombreUsuario, String contrasena, Rol rol) {
        super(nombreUsuario, contrasena, rol);
    }

    @Override
    public List<Proyecto> filtrarProyectos(ProyectoRepository repo) {
        // El gerente ve la totalidad de los proyectos
        return repo.findAll();
    }
    
    @Override
    public List<Usuario> obtenerUsuariosVisibles(UsuarioRepository repo) {
        // El Gerente solo debe ver a los Clientes
        // Asumiendo que tenés un método en UsuarioRepository o filtrás por el discriminador
        return repo.findAllClientes(); 
    }
}