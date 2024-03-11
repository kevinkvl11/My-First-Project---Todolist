package ayonglay.web.todo.interceptor;

import ayonglay.web.todo.entity.User;
import ayonglay.web.todo.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.Objects;

@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie cookie = Arrays.stream(request.getCookies()).filter(result -> "token".equals(result.getName())).findFirst().orElseGet(() -> {
            return null;
        });


        if (!request.getRequestURI().equalsIgnoreCase("/login")){


            if (Objects.isNull(cookie)) {
                response.sendRedirect("/login");
                return false;
            }

            User user = userRepository.findFirstByToken(cookie.getValue()).orElseGet(() -> {
                return null;
            });

            if (Objects.isNull(user)){
                response.sendRedirect("/login");
                return false;
            }

            if (System.currentTimeMillis() > user.getTokenExpiredAt()) {
                response.sendRedirect("/login");
                return false;
            }

            return true;

        } else {

            if (Objects.isNull(cookie)){
                return true;
            }

            User user = userRepository.findFirstByToken(cookie.getValue()).orElseGet(() -> {
                return null;
            });

            if (Objects.isNull(user)){
                return true;
            }

            if (System.currentTimeMillis() >     user.getTokenExpiredAt()) {
                return true;
            }

            response.sendRedirect("/home");
            return false;

        }

    }
}
