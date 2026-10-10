package BACKEND.SPADONI.P2PP.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DesarrolladorAltaDTO(
        
    @NotBlank
    @Size(min = 3, message = "El nombre del desarrollador debe tener al menos 3 caracteres")
    @Size (max = 80, message = "El nombre del desarrollador debe tener maximo 80 caracteres")
    String nombre,
        
    @Size(max = 200)
    String habilidades
) {}