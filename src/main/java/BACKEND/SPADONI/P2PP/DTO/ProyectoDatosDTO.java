package BACKEND.SPADONI.P2PP.DTO;

import BACKEND.SPADONI.P2PP.model.CanalSolicitud;
import BACKEND.SPADONI.P2PP.model.EstadoProceso;
import BACKEND.SPADONI.P2PP.model.EstadoProyecto;
import BACKEND.SPADONI.P2PP.validations.ValidRangoFechas;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@ValidRangoFechas
public record ProyectoDatosDTO(
    @NotBlank
    @Size(min = 3, message = "El nombre del proyecto debe tener al menos 3 caracteres")
    @Size(max = 100, message = "El nombre del proyecto debe tener máximo 100 caracteres")
    String nombre,
        
    @NotNull(message = "El cliente es obligatorio.")
    Long clienteId,
    
    @NotNull CanalSolicitud canalSolicitud,
    
    LocalDate fechaInicio,
    LocalDate fechaFinalizacion,
    
    @PositiveOrZero(message = "El presupuesto no puede ser negativo.")
    Double presupuesto,
    
    @NotNull EstadoProyecto estadoProyecto,
    @NotNull EstadoProceso estadoProceso
) {}