package BACKEND.SPADONI.P2PP.repository;

import BACKEND.SPADONI.P2PP.model.Desarrollador;
import BACKEND.SPADONI.P2PP.model.ProyectoDesarrollador;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProyectoDesarrolladorRepository extends JpaRepository<ProyectoDesarrollador, Long>{
    
    // Historial completo de un proyecto
    List<ProyectoDesarrollador> findByProyecto_Id(Long proyectoId);
    
    // Historial completo, pero solo devolvemos desarrolladores
    @Query("SELECT DISTINCT pd.desarrollador FROM ProyectoDesarrollador pd WHERE pd.proyecto.id = :proyectoId")
    List<Desarrollador> findHistorialDesarrolladoresPorProyectoId(@Param("proyectoId") Long proyectoId);
    
    // Desarrolladores actualmente asignados a un proyecto
    List<ProyectoDesarrollador> findByProyecto_IdAndFechaDesasignacionIsNull(Long proyectoId);
    
    @Query("SELECT pd.desarrollador FROM ProyectoDesarrollador pd WHERE pd.proyecto.id = :proyectoId AND pd.fechaDesasignacion IS NULL")
    List<Desarrollador> findDesarrolladoresActivosPorProyectoId(@Param("proyectoId") Long proyectoId);
    
    // Devuelve true si el desarrollador tiene un proyecto
    boolean existsByDesarrollador_IdAndFechaDesasignacionIsNull(Long desarrolladorId);
    
    // Proyecto actual del desarrollador
    Optional<ProyectoDesarrollador> findByDesarrollador_IdAndFechaDesasignacionIsNull(Long desarrolladorId);
    
    long deleteByProyecto_Id(Long proyectoId);
    
    void deleteByDesarrollador_Id(Long desarrolladorId);
}