package BACKEND.SPADONI.P2PP.validations;

import BACKEND.SPADONI.P2PP.DTO.ClienteDatosDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AtLeastOneContactValidator implements ConstraintValidator<AtLeastOneContact, ClienteDatosDTO> {

    @Override
    public boolean isValid(ClienteDatosDTO dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return true;
        }

        // En lugar de getEmail() y getTelefono(), usás los métodos propios del record:
        boolean tieneEmail = dto.email() != null && !dto.email().trim().isEmpty();
        boolean tieneTelefono = dto.telefono() != null && !dto.telefono().trim().isEmpty();

        return tieneEmail || tieneTelefono;
    }
}