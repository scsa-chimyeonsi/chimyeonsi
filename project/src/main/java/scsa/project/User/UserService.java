package scsa.project.User;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import scsa.project.User.dto.LoginRequest;
import scsa.project.User.dto.RegisterRequest;
import scsa.project.User.dto.UserResponse;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 회원가입
    public UserResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다."); //중복 이메일 방지
        }
        User user = User.builder()
                .email(req.getEmail())
                .password(req.getPassword())
                .nickname(req.getNickname())
                .build();
        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    // 이메일 중복 확인
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }

    // 로그인
    public Map<String, Object> login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail()) //이메일 틀림
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 틀렸습니다."));
        if (!req.getPassword().equals(user.getPassword())) { // 비번 틀림
            throw new IllegalArgumentException("이메일 또는 비밀번호가 틀렸습니다.");
        }
        return Map.of(
                "user_id",        user.getUserId(),
                "nickname",       user.getNickname(),
                "has_play_state", user.getPlayState() != null
        );
    }

    // 사용자 정보 조회
    public UserResponse getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        return toResponse(user);
    }

    // Entity → DTO 변환
    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .user_id(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .created_at(user.getCreatedAt())
                .build();
    }
}