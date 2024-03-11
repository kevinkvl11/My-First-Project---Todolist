package ayonglay.web.todo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TodoResponse {

    private Integer id;

    private String description;

    private Boolean status;
}
