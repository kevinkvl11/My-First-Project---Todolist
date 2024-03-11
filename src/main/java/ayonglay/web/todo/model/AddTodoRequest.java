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
public class AddTodoRequest {

    @Size(max = 50, min = 1)
    private String description;

}
