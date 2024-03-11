package ayonglay.web.todo.repository;

import ayonglay.web.todo.entity.Todo;
import ayonglay.web.todo.entity.User;
import ayonglay.web.todo.security.BCrypt;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TodoRepository todoRepository;

    @BeforeEach
    void setUp() {
//        todoRepository.deleteAll();
//        userRepository.deleteAll();
    }



    @Test
    void test() {
        User user = new User();
        user.setUsername("kevin");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        userRepository.save(user);

        for (int i = 0; i < 10; i++) {
            Todo todo = new Todo();
            todo.setDescription("Belajar ke - " + (i + 1));
            todo.setStatus(true);
            todo.setUser(user);
            todoRepository.save(todo);
        }
    }

    @Test
    void test2() {
        User user = userRepository.findById("kevin").orElseThrow();
        List<Todo> todos = todoRepository.getTodosByUser(user);
        todos.forEach(todo -> {
            System.out.println(todo.getStatus());
        });
    }
}