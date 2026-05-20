package scsa.project.User;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scsa.project.User.dto.LoginRequest;
import scsa.project.User.dto.RegisterRequest;
import scsa.project.User.dto.UserResponse;

import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // POST /users/register
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @RequestBody RegisterRequest req) {
        return ResponseEntity.ok(userService.register(req));
    }

    // GET /users/check-email?email=xxx
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(
            @RequestParam String email) {
        boolean available = userService.isEmailAvailable(email);
        return ResponseEntity.ok(Map.of("available", available));
    }

    // POST /users/login
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody LoginRequest req) {
        return ResponseEntity.ok(userService.login(req));
    }

    // GET /users/{user_id}
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }
}