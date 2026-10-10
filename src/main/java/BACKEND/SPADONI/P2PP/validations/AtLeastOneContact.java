package BACKEND.SPADONI.P2PP.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE}) // Se aplica a nivel de Clase (DTO)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AtLeastOneContactValidator.class) // Vincula con la clase lógica
public @interface AtLeastOneContact {
    String message() default "Debe cargar al menos un método de contacto (email o teléfono).";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}