package BACKEND.SPADONI.P2PP.exceptions;

public class ProyectoNoEncontradoException extends RuntimeException{

    public ProyectoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}