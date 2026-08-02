package BACKEND.SPADONI.P2PP.DTO;

import BACKEND.SPADONI.P2PP.model.CanalSolicitud;
import BACKEND.SPADONI.P2PP.model.EstadoProceso;
import BACKEND.SPADONI.P2PP.model.EstadoProyecto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record ProyectoDatosDTO(
    @NotBlank @Size(min = 3, max = 100) String nombre,
    @NotNull Long clienteId,
    @NotNull CanalSolicitud canalSolicitud,
    LocalDate fechaInicio,
    LocalDate fechaFinalizacion,
    @PositiveOrZero Double presupuesto,
    @NotNull EstadoProyecto estadoProyecto,
    @NotNull EstadoProceso estadoProceso
) {}
