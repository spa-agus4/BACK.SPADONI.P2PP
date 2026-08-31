package BACKEND.SPADONI.P2PP.factory;

import BACKEND.SPADONI.P2PP.DTO.UsuarioAltaDTO;
import BACKEND.SPADONI.P2PP.exceptions.InvalidRolException;
import BACKEND.SPADONI.P2PP.model.Usuario;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class UsuarioFactory {
    
    private final List<UsuarioCreator> creators;
    
    public UsuarioFactory(List<UsuarioCreator> creators){
        this.creators = new ArrayList<>(creators);
    }
    
    public Usuario crear(UsuarioAltaDTO datos) {

        for (UsuarioCreator creator : creators) {
            if (creator.puedeCrear(datos.rol())) {
                return creator.crear(datos);
            }
        }

        throw new InvalidRolException("Rol inválido");
    }
}