package ayonglay.web.todo.restcontroller;

import ayonglay.web.todo.entity.User;
import ayonglay.web.todo.model.*;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testLoginSuccess() throws Exception {
        LoginUserRequest request = LoginUserRequest.builder()
                .username("kevin")
                .password("rahasia2")
                .build();

        mockMvc.perform(
                        post("/api/auth/login")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andExpectAll(status().is(200))
                .andDo(result -> {
                    WebResponse<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<TokenResponse>>() {
                    });
                    System.out.println(response.getData().getToken());
                    System.out.println(response.getData().getExpiredAt());
                }).andDo(result -> {
                    String token = result.getResponse().getCookie("token").getValue();
                    System.out.println(token);
                });
    }

    @Test
    void testLoginFailed() throws Exception {
        LoginUserRequest request = LoginUserRequest.builder()
                .username("kevin")
                .password("salah")
                .build();

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isUnauthorized())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });

                    Assertions.assertNotNull(response.getErrors());
                    System.out.println(response.getErrors());
                });
    }

    @Test
    void testLogoutSuccess() throws Exception {
        User user = userRepository.findById("kevin").orElseThrow();
        Cookie cookie = new Cookie("token", user.getToken());
        mockMvc.perform(
                        delete("/api/auth/logout")
                                .cookie(cookie)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpectAll(status().is(200))
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });
                    Assertions.assertEquals(response.getData(), "ok");
                    Cookie[] cookies = result.getResponse().getCookies();
                    System.out.println(cookies.length);
                });
    }

    @Test
    void testLogoutFailed() throws Exception {
        Cookie cookie = new Cookie("token", "salah");
        mockMvc.perform(delete("/api/auth/logout")
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
    void testRegisterSuccess() throws Exception {
        RegisterUserRequest request = RegisterUserRequest.builder()
                .username("eko_kurniawan")
                .password("khannedy")
                .build();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isOk())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });

                    Assertions.assertEquals("ok", response.getData());
                });
    }

    @Test
    void testRegisterFailedInvalidUsername() throws Exception {
        RegisterUserRequest request = RegisterUserRequest.builder()
                .username("eko")
                .password("rahasia")
                .build();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isBadRequest())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });

                    Assertions.assertNotNull(response.getErrors());
                    System.out.println(response.getErrors());
                });
    }

    @Test
    void testRegisterFailedInvalidUsername2() throws Exception {
        RegisterUserRequest request = RegisterUserRequest.builder()
                .username("eko_kurniawan")
                .password("rahasia")
                .build();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isBadRequest())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });

                    Assertions.assertNotNull(response.getErrors());
                    System.out.println(response.getErrors());
                });
    }

    @Test
    void testRegisterFailedInvalidPasword() throws Exception {
        RegisterUserRequest request = RegisterUserRequest.builder()
                .username("eko_khannedy")
                .password("eko")
                .build();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isBadRequest())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });

                    Assertions.assertNotNull(response.getErrors());
                    System.out.println(response.getErrors());
                });
    }

    @Test
    void testChangePasswordSuccess() throws Exception {
        ChangePasswordUserRequest request = ChangePasswordUserRequest.builder()
                .oldPassword("rahasia")
                .newPassword("rahasia2")
                .repeatPassword("rahasia2").build();

        User user = userRepository.findById("kevin").orElseThrow();

        Cookie cookie = new Cookie("token", user.getToken());

        mockMvc.perform(put("/api/users/password")
                        .cookie(cookie)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isOk())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });

                    Assertions.assertEquals("ok", response.getData());
                });
    }

    @Test
    void testChangePasswordFailedWrongPassword() throws Exception {
        ChangePasswordUserRequest request = ChangePasswordUserRequest.builder()
                .oldPassword("rahasia") //harusnya rahasia2 karena sudah diganti ditest diatas
                .newPassword("gagal")
                .repeatPassword("gagal")
                .build();

        User user = userRepository.findById("kevin").orElseThrow();
        Cookie cookie = new Cookie("token", user.getToken());

        mockMvc.perform(put("/api/users/password")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .cookie(cookie))
                .andExpectAll(status().isUnauthorized())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });

                    Assertions.assertEquals("Wrong Password", response.getErrors());
                });
    }

    @Test
    void testChangePasswordFailedPasswordNotMatch() throws Exception {
        ChangePasswordUserRequest request = ChangePasswordUserRequest.builder()
                .oldPassword("rahasia2")
                .newPassword("rahasia3")
                .repeatPassword("salah3")
                .build();

        User user = userRepository.findById("kevin").orElseThrow();
        Cookie cookie = new Cookie("token", user.getToken());

        mockMvc.perform(put("/api/users/password")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isBadRequest())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });

                    Assertions.assertNotNull(response.getErrors());
                    System.out.println(response.getErrors());
                });
    }

    @Test
    void testChangePasswordInvalidNewPassword() throws Exception {
        ChangePasswordUserRequest request = ChangePasswordUserRequest.builder()
                .oldPassword("rahasia2")
                .newPassword("awi")
                .repeatPassword("awi")
                .build();

        User user = userRepository.findById("kevin").orElseThrow();
        Cookie cookie = new Cookie("token", user.getToken());

        mockMvc.perform(put("/api/users/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .cookie(cookie))
                .andExpectAll(status().isBadRequest())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });

                    Assertions.assertNotNull(response.getErrors());
                    System.out.println(response.getErrors());
                });
    }
}