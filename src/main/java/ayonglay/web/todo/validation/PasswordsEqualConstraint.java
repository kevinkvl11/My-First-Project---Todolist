package ayonglay.web.todo.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PasswordsEqualConstraintValidator.class)
public @interface PasswordsEqualConstraint {

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
