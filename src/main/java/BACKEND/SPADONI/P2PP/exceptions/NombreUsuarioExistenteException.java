package BACKEND.SPADONI.P2PP.exceptions;

public class NombreUsuarioExistenteException extends RuntimeException {

    public NombreUsuarioExistenteException(String mensaje) {
        super(mensaje);
    }
}