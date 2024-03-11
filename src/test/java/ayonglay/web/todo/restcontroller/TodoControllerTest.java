package ayonglay.web.todo.restcontroller;

import ayonglay.web.todo.entity.User;
import ayonglay.web.todo.model.AddTodoRequest;
import ayonglay.web.todo.model.TodoResponse;
import ayonglay.web.todo.model.UpdateTodoRequest;
import ayonglay.web.todo.model.WebResponse;
import ayonglay.web.todo.repository.TodoRepository;
import ayonglay.web.todo.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TodoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoController todoController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TodoRepository todoRepository;


    @Test
    void testGetTodos() throws Exception {
        User user = userRepository.findById("kevin").orElseThrow();
        Cookie cookie = new Cookie("token", user.getToken());
        mockMvc.perform(get("/api/todos/kevin")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isOk())
                .andDo(result -> {
                    WebResponse<List<TodoResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<List<TodoResponse>>>() {
                    });

                    List<TodoResponse> listTodo = response.getData();
                    Assertions.assertNotNull(listTodo);
                    String jsonString = objectMapper.writeValueAsString(response);
                    System.out.println(jsonString);
                });
    }

    @Test
    void testGetTodosUnauthorized() throws Exception {
        User user = userRepository.findById("kevin").orElseThrow();
        user.setTokenExpiredAt(System.currentTimeMillis() - 1000);
        userRepository.save(user);
        Cookie cookie = new Cookie("token", user.getToken());

        mockMvc.perform(get("/api/todos/kevin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookie))
                .andExpectAll(status().isUnauthorized())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });

                    Assertions.assertEquals("Unauthorized", response.getErrors());
                });
    }

    @Test
    void testGetTodosUnauthorized2() throws Exception {
        User user = userRepository.findById("kevin").orElseThrow();
        Cookie cookie = new Cookie("token", user.getToken());

        mockMvc.perform(get("/api/todos/eko")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookie))
                .andExpectAll(status().isUnauthorized())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });

                    Assertions.assertEquals("Unauthorized", response.getErrors());
                });
    }

    @Test
    void testAddTodoSuccess() throws Exception {
        User user = userRepository.findById("kevin").orElseThrow();
        Cookie cookie = new Cookie("token", user.getToken());

        AddTodoRequest request = AddTodoRequest.builder()
                .description("Makan kenyang")
                .build();

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .cookie(cookie))
                .andExpectAll(status().isOk())
                .andDo(result -> {
                    WebResponse<TodoResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<TodoResponse>>() {
                    });

                    Assertions.assertEquals("Makan kenyang", response.getData().getDescription());
                });
    }

    @Test
    void testAddTodoFailedUnauthorized() throws Exception {
        User user = userRepository.findById("kevin").orElseThrow();
        Cookie cookie = new Cookie("token", "salah");

        AddTodoRequest request = AddTodoRequest.builder()
                .description("Makan kenyang Failed")
                .build();

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .cookie(cookie))
                .andExpectAll(status().isUnauthorized())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });

                    Assertions.assertEquals("Unauthorized", response.getErrors());
                });
    }

    @Test
    void testAddTodoFailedUnauthorized2() throws Exception {
        User user = userRepository.findById("kevin").orElseThrow();
        user.setTokenExpiredAt(System.currentTimeMillis() - 1000);
        userRepository.save(user);
        Cookie cookie = new Cookie("token", user.getToken());

        AddTodoRequest request = AddTodoRequest.builder()
                .description("Makan kenyang Failed")
                .build();

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .cookie(cookie))
                .andExpectAll(status().isUnauthorized())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });

                    Assertions.assertEquals("Unauthorized", response.getErrors());
                });
    }

    @Test
    void testAddTodoFailedInvalidRequest() throws Exception {
        User user = userRepository.findById("kevin").orElseThrow();
        Cookie cookie = new Cookie("token", user.getToken());
        AddTodoRequest request = AddTodoRequest.builder()
                .description("")
                .build();

        mockMvc.perform(post("/api/todos")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isBadRequest())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });

                    Assertions.assertNotNull(response.getErrors());
                    System.out.println(response.getErrors());
                });
    }

    @Test
    void testAddTodoFailedInvalidRequest2() throws Exception {
        User user = userRepository.findById("kevin").orElseThrow();
        Cookie cookie = new Cookie("token", user.getToken());
        AddTodoRequest request = AddTodoRequest.builder()
                .description("sdsasdsdsssdad sdsasdsdsssdad sdsasdsdsssdad sdsasdsdsssdad sdsasdsdsssdad sdsasdsdsssdad")
                .build();

        mockMvc.perform(post("/api/todos")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isBadRequest())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });

                    Assertions.assertNotNull(response.getErrors());
                    System.out.println(response.getErrors());
                });
    }

    @Test
    void testUpdateTodoSuccess() throws Exception {
        User user = userRepository.findById("kevin").orElseThrow();
        Cookie cookie = new Cookie("token", user.getToken());
        UpdateTodoRequest request = UpdateTodoRequest.builder()
                .status(false)
                .build();

        mockMvc.perform(put("/api/todos/51")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isOk())
                .andDo(result -> {
                    WebResponse<TodoResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<TodoResponse>>() {
                    });

                    Assertions.assertNotNull(response.getData());
                    Assertions.assertFalse(response.getData().getStatus());
                });
    }

    @Test
    void testUpdateTodoFailedUnauthorized() throws Exception {
        User user = userRepository.findById("kevin").orElseThrow();
        Cookie cookie = new Cookie("token", "salah");
        UpdateTodoRequest request = UpdateTodoRequest.builder()
                .status(false)
                .build();

        mockMvc.perform(put("/api/todos/51")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isUnauthorized())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });

                    Assertions.assertNotNull(response.getErrors());
                });
    }

    @Test
    void testUpdateTodoFailedUnauthorized2() throws Exception {
        User user = userRepository.findById("kevin").orElseThrow();
        user.setTokenExpiredAt(System.currentTimeMillis() - 1000);
        userRepository.save(user);
        Cookie cookie = new Cookie("token", user.getToken());
        UpdateTodoRequest request = UpdateTodoRequest.builder()
                .status(false)
                .build();

        mockMvc.perform(put("/api/todos/51")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isUnauthorized())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });

                    Assertions.assertNotNull(response.getErrors());
                });
    }

    @Test
    void testUpdateTodoFailedUnauthorized3() throws Exception {
        User kevin = userRepository.findById("kevin").orElseThrow();

        Cookie cookie = new Cookie("token", kevin.getToken());
        UpdateTodoRequest request = UpdateTodoRequest.builder()
                .status(true)
                .build();

        mockMvc.perform(put("/api/todos/63")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isUnauthorized())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });

                    Assertions.assertNotNull(response.getErrors());
                });
    }

    @Test
    void testDeleteTodoSuccess() throws Exception {
        User user = userRepository.findById("kevin").orElseThrow();
        Cookie cookie = new Cookie("token", user.getToken());

        mockMvc.perform(delete("/api/todos/51")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isOk())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });

                    Assertions.assertEquals("ok", response.getData());
                });
    }

    @Test
    void testDeleteTodoFailedUnauthorized() throws Exception {
        User user = userRepository.findById("kevin").orElseThrow();
        Cookie cookie = new Cookie("token", "salah");

        mockMvc.perform(delete("/api/todos/52")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isUnauthorized())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });

                    Assertions.assertEquals("Unauthorized", response.getErrors());
                });
    }

    @Test
    void testDeleteTodoFailedUnauthorized2() throws Exception {
        User user = userRepository.findById("kevin").orElseThrow();
        Cookie cookie = new Cookie("token", user.getToken());

        mockMvc.perform(delete("/api/todos/63")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isUnauthorized())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });

                    Assertions.assertEquals("Unauthorized", response.getErrors());
                });
    }

    @Test
    void testDeleteTodoFailedUnauthorized4() throws Exception {
        User user = userRepository.findById("kevin").orElseThrow();
        user.setTokenExpiredAt(System.currentTimeMillis() - 1000);
        userRepository.save(user);
        Cookie cookie = new Cookie("token", user.getToken());

        mockMvc.perform(delete("/api/todos/56")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isUnauthorized())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });

                    Assertions.assertEquals("Unauthorized", response.getErrors());
                });
    }
}