package BACKEND.SPADONI.P2PP.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("GERENTE")
public class Gerente extends Usuario {

    public Gerente() {
        super();
    }

    public Gerente(String nombreUsuario, String contrasena, Rol rol) {
        super(nombreUsuario, contrasena, rol);
    }
/*
   
    @Override
    public List<Usuario> obtenerUsuariosVisibles(UsuarioRepository repo) {
        // El Gerente solo debe ver a los Clientes
        // Asumiendo que tenés un método en UsuarioRepository o filtrás por el discriminador
        return repo.findAllClientes(); 
    }
*/
}