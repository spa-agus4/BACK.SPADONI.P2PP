package BACKEND.SPADONI.P2PP.repository;

import BACKEND.SPADONI.P2PP.model.Desarrollador;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DesarrolladorRepository extends JpaRepository<Desarrollador, Long> {
    
    // Busca desarrolladores cuyo atributo 'proyecto' tenga el 'id' provisto
    List<Desarrollador> findByProyectoId(Long proyectoId);
}