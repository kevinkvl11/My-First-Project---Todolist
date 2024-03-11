package ayonglay.web.todo.restcontroller;

import ayonglay.web.todo.entity.User;
import ayonglay.web.todo.model.*;
import ayonglay.web.todo.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping(path = "/api/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> register(@RequestBody RegisterUserRequest request) {
        userService.register(request);
        return WebResponse.<String>builder()
                .data("ok")
                .build();
    }

    @PostMapping(path = "/api/auth/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<TokenResponse> login(@RequestBody LoginUserRequest request, HttpServletResponse response) {
        TokenResponse tokenResponse = userService.login(request);
        Cookie cookie = new Cookie("token", tokenResponse.getToken());
        cookie.setMaxAge(600000);
        cookie.setPath("/");
        response.addCookie(cookie);
        return WebResponse.<TokenResponse>builder()
                .data(tokenResponse)
                .build();
    }

    @PutMapping(path = "/api/users/password")
    public WebResponse<String> updatePassword(@RequestBody ChangePasswordUserRequest request, User user) {
        userService.updatePassword(user, request);
        return WebResponse.<String>builder()
                .data("ok")
                .build();
    }

    @DeleteMapping(path = "/api/auth/logout",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> logout(User user, HttpServletResponse response, HttpServletRequest request) {
        userService.logout(user);
        Cookie cookie = Arrays.stream(request.getCookies()).filter((result) -> "token".equals(result.getName())).findFirst().orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        });
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return WebResponse.<String>builder()
                .data("ok")
                .build();
    }

    @GetMapping(path = "/api/users",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<UserResponse> getUser(User user) {
        UserResponse userResponse = userService.getUser(user.getToken());

        return WebResponse.<UserResponse>builder()
                .data(userResponse)
                .build();
    }
}
