package BACKEND.SPADONI.P2PP.exceptions;

public class AccesoProyectoDenegadoException extends RuntimeException{
    
    public AccesoProyectoDenegadoException(String mensaje) {
        super(mensaje);
    }
}