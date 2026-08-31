package BACKEND.SPADONI.P2PP.services.proyecto;

import BACKEND.SPADONI.P2PP.model.Proyecto;
import BACKEND.SPADONI.P2PP.model.Usuario;
import java.util.List;

public interface ProyectoViewer {
    
    boolean puedeVerProyecto(Usuario usuario, Proyecto proyecto);
    
    List<Proyecto> mostrarProyectos(Usuario usuario);
}