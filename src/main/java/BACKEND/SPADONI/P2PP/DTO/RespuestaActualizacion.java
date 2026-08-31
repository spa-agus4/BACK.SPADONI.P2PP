package BACKEND.SPADONI.P2PP.DTO;

public class RespuestaActualizacion {
    private UsuarioDTO uDTO;
    private String token;

    public RespuestaActualizacion(UsuarioDTO uDTO, String token) {
        this.uDTO = uDTO;
        this.token = token;
    }
    // Getters y Setters
    public UsuarioDTO getUsuarioDTO() { return uDTO; }
    public String getToken() { return token; }
}