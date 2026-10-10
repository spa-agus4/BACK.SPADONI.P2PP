package BACKEND.SPADONI.P2PP.DTO;

import BACKEND.SPADONI.P2PP.validations.AtLeastOneContact;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@AtLeastOneContact
public record ClienteDatosDTO(
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, message = "El nombre del cliente debe tener al menos 3 caracteres")
    @Size(max = 80, message = "El nombre no puede superar los 80 caracteres")
    String nombre,
        
    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 150) 
    String direccion,
    
    @Email(message = "El formato del email no es válido")
    @Size(max = 100) 
    String email,
    
    @Size(max = 20) 
    String telefono
) {}