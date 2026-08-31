package BACKEND.SPADONI.P2PP.factory;

import BACKEND.SPADONI.P2PP.DTO.UsuarioAltaDTO;
import BACKEND.SPADONI.P2PP.model.Rol;
import BACKEND.SPADONI.P2PP.model.Usuario;

public interface UsuarioCreator {
    
    boolean puedeCrear(Rol rol);
    
    Usuario crear(UsuarioAltaDTO datos);
}