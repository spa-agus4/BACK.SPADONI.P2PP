package BACKEND.SPADONI.P2PP.services.proyecto;

import BACKEND.SPADONI.P2PP.model.Rol;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ProyectoViewerFactory {
    
    private final Map<Rol, ProyectoViewer> viewers;
    
    public ProyectoViewerFactory(ClienteProyectoViewer clienteViewer, GerenteProyectoViewer gerenteViewer){
        
        viewers = new HashMap<>();
        
        viewers.put(Rol.CLIENTE, clienteViewer);
        viewers.put(Rol.GERENTE, gerenteViewer);
    }
    
    public ProyectoViewer obtenerViewer(Rol rol){
        
        ProyectoViewer viewer = viewers.get(rol);
        
        if(viewer == null)
            throw new IllegalArgumentException("No existe un viewer par el rol: " + rol);
        
        return viewer;
    }
}