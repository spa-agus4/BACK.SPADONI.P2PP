package BACKEND.SPADONI.P2PP.DTO;

import BACKEND.SPADONI.P2PP.model.CanalSolicitud;
import BACKEND.SPADONI.P2PP.model.EstadoProceso;
import BACKEND.SPADONI.P2PP.model.EstadoProyecto;
import java.time.LocalDate;


public class ProyectoDTO {

    private Long id;
    private String nombre;
    private ClienteDTO cliente;
    private CanalSolicitud canalSolicitud;
    private LocalDate fechaInicio;
    private LocalDate fechaFinalizacion;
    private Double presupuesto;
    private EstadoProyecto estadoProyecto;
    private EstadoProceso estadoProceso;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    public CanalSolicitud getCanalSolicitud() {
        return canalSolicitud;
    }

    public void setCanalSolicitud(CanalSolicitud canalSolicitud) {
        this.canalSolicitud = canalSolicitud;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(LocalDate fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public Double getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(Double presupuesto) {
        this.presupuesto = presupuesto;
    }

    public EstadoProyecto getEstadoProyecto() {
        return estadoProyecto;
    }

    public void setEstadoProyecto(EstadoProyecto estadoProyecto) {
        this.estadoProyecto = estadoProyecto;
    }

    public EstadoProceso getEstadoProceso() {
        return estadoProceso;
    }

    public void setEstadoProceso(EstadoProceso estadoProceso) {
        this.estadoProceso = estadoProceso;
    }
}