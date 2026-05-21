package scsa.project.PlayLog;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scsa.project.PlayLog.dto.SubmitOptionRequest;
import scsa.project.PlayLog.dto.SubmitOptionResponse;


@RestController
@RequestMapping("/play-logs")
@RequiredArgsConstructor
public class PlayLogController {

    private final PlayLogService playLogService;

    @PostMapping("/users/{userId}")
    public ResponseEntity<SubmitOptionResponse> submitOption(
            @PathVariable Long userId,
            @RequestBody SubmitOptionRequest request) {

        SubmitOptionResponse response = playLogService.submitOption(userId, request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/states/{stateId}")
    public ResponseEntity<java.util.List<scsa.project.PlayLog.dto.PlayLogResponse>> getLogsByState(
            @PathVariable Long stateId) {

        java.util.List<scsa.project.PlayLog.dto.PlayLogResponse> responses = playLogService.getLogsByStateId(stateId);
        return ResponseEntity.ok(responses);
    }
}
