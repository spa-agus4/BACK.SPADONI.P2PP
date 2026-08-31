package BACKEND.SPADONI.P2PP.services.usuario;

import BACKEND.SPADONI.P2PP.model.Rol;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class UsuarioViewerFactory {
    
    private final Map<Rol, UsuarioViewer> viewers;
    
    public UsuarioViewerFactory(AdministradorUsuarioViewer adminViewer, GerenteUsuarioViewer gerenteViewer){
        
        viewers = new HashMap<>();
        
        viewers.put(Rol.ADMINISTRADOR, adminViewer);
        viewers.put(Rol.GERENTE, gerenteViewer);
    }
    
    public UsuarioViewer obtenerViewer(Rol rol){
        
        UsuarioViewer viewer = viewers.get(rol);
        
        if(viewer == null)
            throw new IllegalArgumentException("No existe un viewer par el rol: " + rol);
        
        return viewer;
    }
}