package BACKEND.SPADONI.P2PP.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidRangoFechasValidator.class)
public @interface ValidRangoFechas {
    String message() default "La fecha de inicio no puede ser posterior a la de finalización.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}