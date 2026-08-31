package BACKEND.SPADONI.P2PP.services.usuario;

import BACKEND.SPADONI.P2PP.model.Usuario;
import java.util.List;

public interface UsuarioViewer {
    
    List<Usuario> mostrarUsuarios(Usuario u);
}