package BACKEND.SPADONI.P2PP.exceptions;

public class CredencialesInvalidasException extends RuntimeException{
    
    public CredencialesInvalidasException(String mensaje){
        super(mensaje); 
    }
}