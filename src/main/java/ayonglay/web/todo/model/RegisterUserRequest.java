package ayonglay.web.todo.model;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterUserRequest {

    @Size(max = 20, min = 5)
    private String username;

    @Size(max = 20, min = 5)
    private String password;
}
