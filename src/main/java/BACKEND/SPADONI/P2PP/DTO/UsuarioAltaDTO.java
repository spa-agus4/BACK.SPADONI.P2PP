package BACKEND.SPADONI.P2PP.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import BACKEND.SPADONI.P2PP.model.Rol;

public record UsuarioAltaDTO(
    @NotBlank @Size(min = 3, max = 30, message = "El nombre de usuario debe tener entre 3 y 30 caracteres") String nombreUsuario,
    @NotBlank @Size(min = 4, max = 50, message = "La contraseña debe tener entre 4 y 50 caracteres") String contrasena,
    @NotNull(message = "El rol es obligatorio") Rol rol
) {}