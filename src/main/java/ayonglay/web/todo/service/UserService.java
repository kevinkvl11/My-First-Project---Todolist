package ayonglay.web.todo.service;

import ayonglay.web.todo.entity.User;
import ayonglay.web.todo.model.*;
import ayonglay.web.todo.repository.UserRepository;
import ayonglay.web.todo.security.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public void register(RegisterUserRequest request) {
        validationService.validate(request);

        if (userRepository.existsById(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exist");
        }
        User user = User.builder()
                .username(request.getUsername())
                .password(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()))
                .build();

        userRepository.save(user);
    }

    @Transactional
    public TokenResponse login(LoginUserRequest request) {
        User user = userRepository.findById(request.getUsername()).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong");
        });

        if (BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            user.setToken(UUID.randomUUID().toString());
            user.setTokenExpiredAt(System.currentTimeMillis() + 600000L);
            userRepository.save(user);

            return TokenResponse.builder()
                    .token(user.getToken())
                    .expiredAt(user.getTokenExpiredAt())
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong");
        }

    }

    @Transactional
    public void updatePassword(User user, ChangePasswordUserRequest request) {
        validationService.validate(request);

        if (BCrypt.checkpw(request.getOldPassword(), user.getPassword())) {
            user.setPassword(BCrypt.hashpw(request.getNewPassword(), BCrypt.gensalt()));
            userRepository.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong Password");
        }
    }

    public void logout(User user) {
        user.setToken(null);
        user.setTokenExpiredAt(null);

        userRepository.save(user);
    }

    public UserResponse getUser(String token) {
        User user = userRepository.findFirstByToken(token).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        });

        return UserResponse.builder()
                .username(user.getUsername())
                .build();
    }
}
