package BACKEND.SPADONI.P2PP.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.util.List;
import BACKEND.SPADONI.P2PP.repository.ProyectoRepository;

@Entity
@DiscriminatorValue("CLIENTE")
public class Cliente extends Usuario {

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "email")
    private String email;

    @Column(name = "telefono")
    private String telefono;

    // Constructor vacío obligatorio para JPA
    public Cliente() {
        super();
    }

    // Constructor completo para cuando registrás o creás un cliente nuevo
    public Cliente(String nombreUsuario, String contrasena, String nombre, String direccion, String email, String telefono) {
        super(nombreUsuario, contrasena, Rol.CLIENTE);
        this.nombre = nombre;
        this.direccion = direccion;
        this.email = email;
        this.telefono = telefono;
    }

    // --- IMPLEMENTACIÓN POLIMÓRFICA ---
    @Override
    public List<Proyecto> filtrarProyectos(ProyectoRepository repo) {
        // El cliente solo puede consultar los proyectos asociados a su ID
        return repo.findByClienteId(this.getId());
    }
    
    @Override
    public boolean puedeEliminarse(ProyectoRepository proyectoRepo) {
        return filtrarProyectos(proyectoRepo).isEmpty();
    }
    

    // --- GETTERS Y SETTERS ---

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}