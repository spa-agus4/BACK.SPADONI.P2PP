package BACKEND.SPADONI.P2PP.services.usuario;

import BACKEND.SPADONI.P2PP.model.Usuario;
import BACKEND.SPADONI.P2PP.repository.UsuarioRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AdministradorUsuarioViewer implements UsuarioViewer{

    private final UsuarioRepository repo;
    
    public AdministradorUsuarioViewer(UsuarioRepository repo){
        this.repo = repo;
    }
    
    @Override
    public List<Usuario> mostrarUsuarios(Usuario u) {
        return repo.findAll();
    }
}