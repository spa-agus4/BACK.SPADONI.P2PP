package BACKEND.SPADONI.P2PP.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DesarrolladorAltaDTO(
    @NotBlank @Size(min = 3, max = 80) String nombre,
    @Size(max = 200) String habilidades
) {}