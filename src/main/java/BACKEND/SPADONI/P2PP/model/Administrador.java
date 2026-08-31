package BACKEND.SPADONI.P2PP.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ADMINISTRADOR")
public class Administrador extends Usuario {

    public Administrador() {
        super();
    }

    public Administrador(String nombreUsuario, String contrasena, Rol rol) {
        super(nombreUsuario, contrasena, rol);
    }
    /*
    @Override
    public List<Proyecto> filtrarProyectos(ProyectoRepository repo) {   // igual voy a interceptar esto para q no llegue
        return null;
    }
    
    @Override
    public List<Usuario> obtenerUsuariosVisibles(UsuarioRepository repo) {
        // El Admin ve absolutamente a todos los usuarios del sistema
        return repo.findAll();
    }
    */
}