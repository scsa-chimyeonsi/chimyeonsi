package scsa.project.PlayState;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/play-states")
@RequiredArgsConstructor
public class PlayStateController {

    private final PlayStateService playStateService;

    @GetMapping("/users/{user_id}")
    public ResponseEntity<PlayStateResponseDto> getPlayState(@PathVariable("user_id") Long userId) {
        return ResponseEntity.ok(playStateService.getPlayStateByUserId(userId));
    }
}
