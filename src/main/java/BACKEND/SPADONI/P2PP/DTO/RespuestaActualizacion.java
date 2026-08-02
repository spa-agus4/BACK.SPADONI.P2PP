package BACKEND.SPADONI.P2PP.DTO;

// RespuestaActualizacion.java

import BACKEND.SPADONI.P2PP.model.Usuario;

public class RespuestaActualizacion {
    private Usuario usuario;
    private String token;

    public RespuestaActualizacion(Usuario usuario, String token) {
        this.usuario = usuario;
        this.token = token;
    }
    // Getters y Setters
    public Usuario getUsuario() { return usuario; }
    public String getToken() { return token; }
}