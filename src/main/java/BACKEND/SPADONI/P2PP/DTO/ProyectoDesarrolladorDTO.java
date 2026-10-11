package BACKEND.SPADONI.P2PP.DTO;

import java.time.LocalDate;

public class ProyectoDesarrolladorDTO {
    
    private Long id;
    private ProyectoDTO proyecto;
    private DesarrolladorDTO desarrollador;
    private LocalDate fechaAsignacion;
    private LocalDate fechaDesasignacion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProyectoDTO getProyecto() {
        return proyecto;
    }

    public void setProyecto(ProyectoDTO proyecto) {
        this.proyecto = proyecto;
    }

    public DesarrolladorDTO getDesarrollador() {
        return desarrollador;
    }

    public void setDesarrollador(DesarrolladorDTO desarrollador) {
        this.desarrollador = desarrollador;
    }

    public LocalDate getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDate fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public LocalDate getFechaDesasignacion() {
        return fechaDesasignacion;
    }

    public void setFechaDesasignacion(LocalDate fechaDesasignacion) {
        this.fechaDesasignacion = fechaDesasignacion;
    }
    
    
}