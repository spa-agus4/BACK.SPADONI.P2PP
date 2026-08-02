package BACKEND.SPADONI.P2PP.repository;

import BACKEND.SPADONI.P2PP.model.Cliente;
import BACKEND.SPADONI.P2PP.model.Proyecto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long> { 

    public List<Proyecto> findByClienteId(Long clienteId);
    
    boolean existsByClienteId(Long clienteId);
}