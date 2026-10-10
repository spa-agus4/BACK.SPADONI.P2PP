package BACKEND.SPADONI.P2PP.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import org.springframework.validation.ObjectError;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Los ResponseStatusException (404, 409, etc.) siguen respondiendo igual que ahora
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> manejarStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(Map.of("message", ex.getReason() != null ? ex.getReason() : "Ocurrió un error"));
    }

    // Falla un @Valid/@NotBlank/@Size/@Email o un Custom Validator a nivel de DTO
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarValidacion(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getAllErrors().stream()
                .findFirst()
                .map(ObjectError::getDefaultMessage)
                .orElse("Datos inválidos");
        return ResponseEntity.badRequest().body(Map.of("message", mensaje));
    }

    // Cualquier otra cosa no prevista (BD caída, NullPointerException, lo que sea) cae acá
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> manejarErrorInesperado(Exception ex) {
        log.error("Error inesperado", ex); // el detalle real queda en el log del server, nunca en la respuesta
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Ocurrió un error inesperado. Intentá más tarde."));
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<Map<String, String>> manejarCredencialesInvalidas(CredencialesInvalidasException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(AltaClienteNoPermitidaException.class)
    public ResponseEntity<Map<String, String>> manejarAltaCliente(AltaClienteNoPermitidaException ex) {
        return ResponseEntity
                .badRequest()
                .body(Map.of("mensaje", ex.getMessage()));
    }

    @ExceptionHandler(InvalidRolException.class)
    public ResponseEntity<Map<String, String>> manejarRolInvalido(InvalidRolException ex) {
        return ResponseEntity
                .badRequest()
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(NombreUsuarioExistenteException.class)
    public ResponseEntity<Map<String, String>> manejarNombreUsuarioExistente(NombreUsuarioExistenteException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(AutoEliminacionNoPermitidaException.class)
    public ResponseEntity<Map<String, String>> manejarAutoEliminacion(AutoEliminacionNoPermitidaException ex) {
        return ResponseEntity
                .badRequest()
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> manejarUsuarioNoEncontrado(UsuarioNoEncontradoException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(DesarrolladorNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> manejarDesarrolladorNoEncontrado(DesarrolladorNoEncontradoException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(AccesoProyectoDenegadoException.class)
    public ResponseEntity<Map<String, String>> manejarAccesoProyectoDenegado(AccesoProyectoDenegadoException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(NombreDesarrolladorExistenteException.class)
    public ResponseEntity<Map<String, String>> manejarNombreDesarrolladorExistente(NombreDesarrolladorExistenteException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(DesarrolladorNoDisponibleException.class)
    public ResponseEntity<Map<String, String>> manejarDesarrolladorNoDisponible(DesarrolladorNoDisponibleException ex) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("message", ex.getMessage()));
    }
    
    @ExceptionHandler(ProyectoNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> manejarProyectoNoEncontrado(ProyectoNoEncontradoException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", ex.getMessage()));
    }
    
    @ExceptionHandler(ClienteInhabilitadoException.class)
    public ResponseEntity<Map<String, String>> manejarClienteInhabilitado(ClienteInhabilitadoException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("message", ex.getMessage()));
    }
    
    @ExceptionHandler(ClienteNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> manejarClienteNoEncontrado(ClienteNoEncontradoException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", ex.getMessage()));
    }
    
    @ExceptionHandler(ClienteSinCredenciales.class)
    public ResponseEntity<Map<String, String>> manejarClienteSinCredenciales(ClienteSinCredenciales ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", ex.getMessage()));
    }
}