package scsa.project.User.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class UserResponse {
    private Long user_id;
    private String email;
    private String nickname;
    private LocalDateTime created_at;
}
