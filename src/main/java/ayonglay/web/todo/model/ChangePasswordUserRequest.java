package ayonglay.web.todo.model;

import ayonglay.web.todo.validation.PasswordsEqualConstraint;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@PasswordsEqualConstraint(message = "Password not match")
public class ChangePasswordUserRequest {

    private String oldPassword;

    @Size(max = 20, min = 5)
    private String newPassword;
    
    private String repeatPassword;
}
