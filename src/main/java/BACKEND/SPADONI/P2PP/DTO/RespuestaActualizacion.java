package BACKEND.SPADONI.P2PP.DTO;

public class RespuestaActualizacion {
    private UsuarioListadoDTO uDTO;
    private String token;

    public RespuestaActualizacion(UsuarioListadoDTO uDTO, String token) {
        this.uDTO = uDTO;
        this.token = token;
    }
    // Getters y Setters
    public UsuarioListadoDTO getUsuarioDTO() { return uDTO; }
    public String getToken() { return token; }
}