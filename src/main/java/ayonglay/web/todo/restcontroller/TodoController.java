package ayonglay.web.todo.restcontroller;

import ayonglay.web.todo.entity.User;
import ayonglay.web.todo.model.AddTodoRequest;
import ayonglay.web.todo.model.TodoResponse;
import ayonglay.web.todo.model.UpdateTodoRequest;
import ayonglay.web.todo.model.WebResponse;
import ayonglay.web.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping(path = "/api/todos/{idUser}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<List<TodoResponse>> get(@PathVariable("idUser") String idUser, User user) {
        List<TodoResponse> todoResponses = todoService.get(idUser, user);
        return WebResponse.<List<TodoResponse>>builder()
                .data(todoResponses)
                .build();
    }

    @PostMapping(path = "/api/todos",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<TodoResponse> create(@RequestBody AddTodoRequest request, User user) {
        TodoResponse todoResponse = todoService.create(request, user);

        return WebResponse.<TodoResponse>builder()
                .data(todoResponse)
                .build();
    }

    @PutMapping(path = "/api/todos/{idTodo}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<TodoResponse> update(@RequestBody UpdateTodoRequest request,
                                            @PathVariable("idTodo") Integer idTodo,
                                            User user) {
        TodoResponse todoResponse = todoService.update(idTodo, request, user);
        return WebResponse.<TodoResponse>builder()
                .data(todoResponse)
                .build();
    }

    @DeleteMapping(path = "/api/todos/{idTodo}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> delete(@PathVariable("idTodo") Integer idTodo, User user) {
        String response = todoService.delete(idTodo, user);
        return WebResponse.<String>builder()
                .data(response)
                .build();
    }

}
