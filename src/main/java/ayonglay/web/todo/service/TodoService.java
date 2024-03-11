package ayonglay.web.todo.service;

import ayonglay.web.todo.entity.Todo;
import ayonglay.web.todo.entity.User;
import ayonglay.web.todo.model.AddTodoRequest;
import ayonglay.web.todo.model.TodoResponse;
import ayonglay.web.todo.model.UpdateTodoRequest;
import ayonglay.web.todo.model.WebResponse;
import ayonglay.web.todo.repository.TodoRepository;
import ayonglay.web.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    public List<TodoResponse> get(String idUser, User user) {

        if (!idUser.equals(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        List<Todo> todos = todoRepository.getTodosByUser(user);

        List<TodoResponse> todosResponse = todos.stream().map(todo -> {
            return TodoResponse.builder()
                    .id(todo.getId())
                    .description(todo.getDescription())
                    .status(todo.getStatus())
                    .build();
        }).collect(Collectors.toList());

        return todosResponse;
    }

    @Transactional
    public TodoResponse create(AddTodoRequest request, User user) {
        validationService.validate(request);

        Todo todo = Todo.builder()
                .description(request.getDescription())
                .status(false)
                .user(user)
                .build();

        todoRepository.save(todo);

        TodoResponse response = TodoResponse.builder()
                .id(todo.getId())
                .description(todo.getDescription())
                .status(todo.getStatus())
                .build();

        return response;
    }

    @Transactional
    public TodoResponse update(Integer idTodo, UpdateTodoRequest request, User user) {

        Todo todo = todoRepository.findById(idTodo).orElseThrow(() -> {
            return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Todo Id");
        });

        validate(todo, user);

        todo.setStatus(request.getStatus());
        todoRepository.save(todo);

        TodoResponse todoResponse = TodoResponse.builder()
                .id(todo.getId())
                .description(todo.getDescription())
                .status(todo.getStatus())
                .build();

        return todoResponse;
    }

    @Transactional
    public String delete(Integer idTodo, User user) {
        Todo todo = todoRepository.findById(idTodo).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Todo Id");
        });

        validate(todo, user);

        todoRepository.delete(todo);
        return "ok";
    }


    public void validate(Todo todo, User user) {
        if (!todo.getUser().getUsername().equals(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
    }
}
