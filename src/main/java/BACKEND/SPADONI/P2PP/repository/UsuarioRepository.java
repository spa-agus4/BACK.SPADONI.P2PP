package BACKEND.SPADONI.P2PP.repository;

import BACKEND.SPADONI.P2PP.model.Rol;
import BACKEND.SPADONI.P2PP.model.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> { 

    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
    
    List<Usuario> findByRol(Rol rol);
    
    // Consulta JPQL usando TYPE para filtrar solo la subclase Cliente
    @Query("SELECT u FROM Usuario u WHERE TYPE(u) = Cliente")
    List<Usuario> findAllClientes();

}