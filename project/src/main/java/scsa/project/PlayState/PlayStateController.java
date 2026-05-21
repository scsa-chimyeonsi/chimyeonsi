package scsa.project.PlayState;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scsa.project.PlayState.dto.SummaryResponse;

@RestController
@RequestMapping("/play-states")
@RequiredArgsConstructor
public class PlayStateController {

    private final PlayStateService playStateService;

    @GetMapping("/users/{user_id}")
    public ResponseEntity<PlayStateResponseDto> getPlayState(@PathVariable("user_id") Long userId) {
        return ResponseEntity.ok(playStateService.getPlayStateByUserId(userId));
    }

    @PostMapping("/users/{user_id}")
    public ResponseEntity<PlayStateResponseDto> resetOrCreatePlayState(@PathVariable("user_id") Long userId) {
        return ResponseEntity.ok(playStateService.resetOrCreatePlayState(userId));
    }

    @GetMapping("/users/{user_id}/ending")
    public ResponseEntity<EndingResponse> getEnding(@PathVariable("user_id") Long userId) {
        return ResponseEntity.ok(playStateService.getEnding(userId));
    }

    @GetMapping("/users/{user_id}/summary")
    public ResponseEntity<SummaryResponse> getSummary(@PathVariable("user_id") Long userId) {
        return ResponseEntity.ok(playStateService.getSummary(userId));
    }
}