package BACKEND.SPADONI.P2PP.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "desarrollador")
public class Desarrollador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nombre", unique = true, nullable = false)
    private String nombre;
    @ManyToOne
    @JoinColumn(name = "proyecto_id")
    private Proyecto proyecto;
    @Column(name = "habilidades")
    private String habilidades;
    @Column(name = "disponible")
    private Boolean disponible;

    public Desarrollador() {
    }
    
    public Desarrollador(String nombre, Proyecto proyecto, String habilidades, Boolean disponible) {
        this.nombre = nombre;
        this.proyecto = proyecto;
        this.habilidades = habilidades;
        this.disponible = disponible;
    }

    public Desarrollador(String nombre, String habilidades, Boolean disponible) {
        this.nombre = nombre;
        this.habilidades = habilidades;
        this.disponible = disponible;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public String getHabilidades() {
        return habilidades;
    }

    public void setHabilidades(String habilidades) {
        this.habilidades = habilidades;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
