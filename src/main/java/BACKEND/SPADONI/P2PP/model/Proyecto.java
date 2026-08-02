package BACKEND.SPADONI.P2PP.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "proyecto")
public class Proyecto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nombre")
    private String nombre;
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    @Column(name = "canalSolicitud")
    @Enumerated(EnumType.STRING)
    private CanalSolicitud canalSolicitud;
    @Column(name = "fechaInicio")
    private LocalDate fechaInicio;
    @Column(name = "fechaFinalizacion")
    private LocalDate fechaFinalizacion;
    @Column(name = "presupuesto")
    private Double presupuesto;
    @Column(name = "estadoProyecto")
    @Enumerated(EnumType.STRING)
    private EstadoProyecto estadoProyecto;
    @Column(name = "estadoProceso")
    @Enumerated(EnumType.STRING)
    private EstadoProceso estadoProceso;


    
    public Proyecto() { }
    
    public Proyecto(String nombre, Cliente cliente, CanalSolicitud canalSolicitud, LocalDate fechaInicio, LocalDate fechaFinalizacion, Double presupuesto, EstadoProyecto estadoProyecto, EstadoProceso estadoProceso) {
        this.nombre = nombre;
        this.cliente = cliente;
        this.canalSolicitud = canalSolicitud;
        this.fechaInicio = fechaInicio;
        this.fechaFinalizacion = fechaFinalizacion;
        this.presupuesto = presupuesto;
        this.estadoProyecto = estadoProyecto;
        this.estadoProceso = estadoProceso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
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

    public EstadoProyecto getEstadoProyecto() { return estadoProyecto; }
    public void setEstadoProyecto(EstadoProyecto estadoProyecto) { this.estadoProyecto = estadoProyecto; }
    public EstadoProceso getEstadoProceso() { return estadoProceso; }
    public void setEstadoProceso(EstadoProceso estadoProceso) { this.estadoProceso = estadoProceso; }
    
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}