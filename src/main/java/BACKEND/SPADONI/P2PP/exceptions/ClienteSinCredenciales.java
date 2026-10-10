package BACKEND.SPADONI.P2PP.exceptions;

public class ClienteSinCredenciales extends RuntimeException {

    public ClienteSinCredenciales(String mensaje) {
        super(mensaje);
    }
}