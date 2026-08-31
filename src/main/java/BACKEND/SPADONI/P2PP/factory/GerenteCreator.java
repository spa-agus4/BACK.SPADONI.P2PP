package BACKEND.SPADONI.P2PP.factory;

import BACKEND.SPADONI.P2PP.DTO.UsuarioAltaDTO;
import BACKEND.SPADONI.P2PP.model.Gerente;
import BACKEND.SPADONI.P2PP.model.Rol;
import BACKEND.SPADONI.P2PP.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class GerenteCreator implements UsuarioCreator{

    @Override
    public boolean puedeCrear(Rol rol){ return rol == Rol.GERENTE; }
    
    @Override
    public Usuario crear(UsuarioAltaDTO datos) {
        
        return new Gerente(
                datos.nombreUsuario(),
                datos.contrasena(),
                Rol.GERENTE
        );
    }
}