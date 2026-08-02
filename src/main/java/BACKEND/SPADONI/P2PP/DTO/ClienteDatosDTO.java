package BACKEND.SPADONI.P2PP.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteDatosDTO(
    @NotBlank @Size(max = 80) String nombre,
    @NotBlank @Size(max = 150) String direccion,
    @NotBlank @Email @Size(max = 100) String email,
    @NotBlank @Size(max = 20) String telefono
) {}