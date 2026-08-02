package BACKEND.SPADONI.P2PP.DTO;

// Lo que recibe el login
public class LoginDTO {
    private String nombreUsuario;
    private String contrasena;
    // getters y setters

    
    public LoginDTO() {}
    
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}