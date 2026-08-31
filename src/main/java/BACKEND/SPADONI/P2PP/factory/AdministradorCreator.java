package BACKEND.SPADONI.P2PP.factory;

import BACKEND.SPADONI.P2PP.DTO.UsuarioAltaDTO;
import BACKEND.SPADONI.P2PP.model.Administrador;
import BACKEND.SPADONI.P2PP.model.Rol;
import BACKEND.SPADONI.P2PP.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class AdministradorCreator implements UsuarioCreator{

    @Override
    public boolean puedeCrear(Rol rol){ return rol == Rol.ADMINISTRADOR; }
    
    @Override
    public Usuario crear(UsuarioAltaDTO datos) {
        
        return new Administrador(
                datos.nombreUsuario(),
                datos.contrasena(),
                Rol.ADMINISTRADOR
        );
    }
}