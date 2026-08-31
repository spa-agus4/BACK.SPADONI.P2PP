package BACKEND.SPADONI.P2PP.exceptions;

public class ClienteInhabilitadoException extends RuntimeException{
    
    public ClienteInhabilitadoException(String mensaje) {
        super(mensaje);
    }
}