package ayonglay.web.todo.validation;

import ayonglay.web.todo.model.ChangePasswordUserRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordsEqualConstraintValidator implements ConstraintValidator<PasswordsEqualConstraint, ChangePasswordUserRequest> {
    @Override
    public boolean isValid(ChangePasswordUserRequest request, ConstraintValidatorContext constraintValidatorContext) {
        return request.getNewPassword().equals(request.getRepeatPassword());
    }

    @Override
    public void initialize(PasswordsEqualConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
