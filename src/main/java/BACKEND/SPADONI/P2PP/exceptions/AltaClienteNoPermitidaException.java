package BACKEND.SPADONI.P2PP.exceptions;

public class AltaClienteNoPermitidaException extends RuntimeException {

    public AltaClienteNoPermitidaException(String mensaje) {
        super(mensaje);
    }
}