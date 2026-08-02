package BACKEND.SPADONI.P2PP.DTO;

import jakarta.validation.constraints.Size;

public record DesarrolladorEdicionDTO(
    @Size(min = 3, max = 80) String nombre,
    @Size(max = 200) String habilidades,
    Boolean disponible
) {}