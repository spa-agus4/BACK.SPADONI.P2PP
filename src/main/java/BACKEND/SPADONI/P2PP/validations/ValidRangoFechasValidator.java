package BACKEND.SPADONI.P2PP.validations;

import BACKEND.SPADONI.P2PP.DTO.ProyectoDatosDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidRangoFechasValidator implements ConstraintValidator<ValidRangoFechas, ProyectoDatosDTO> {

    @Override
    public boolean isValid(ProyectoDatosDTO dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return true;
        }

        // Si alguna de las dos fechas es null, se considera válido el rango (por ejemplo, si finalización aún no se cargó)
        if (dto.fechaInicio() == null || dto.fechaFinalizacion() == null) {
            return true;
        }

        // Falla (devuelve false) si la fecha de inicio es posterior a la de finalización
        return !dto.fechaInicio().isAfter(dto.fechaFinalizacion());
    }
}