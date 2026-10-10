package BACKEND.SPADONI.P2PP.DTO;

public class UsuarioListadoDTO {

    private UsuarioDTO usuario;
    private RespuestaDatosClienteDTO datosCliente;

    
    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }

    public RespuestaDatosClienteDTO getDatosCliente() {
        return datosCliente;
    }

    public void setDatosCliente(RespuestaDatosClienteDTO datosCliente) {
        this.datosCliente = datosCliente;
    }
}