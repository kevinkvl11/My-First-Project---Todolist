package ayonglay.web.todo.repository;

import ayonglay.web.todo.entity.Todo;
import ayonglay.web.todo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Integer> {
    public List<Todo> getTodosByUser(User user);
}
