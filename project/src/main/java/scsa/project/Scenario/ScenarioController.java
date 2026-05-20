package scsa.project.Scenario;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scsa.project.Scenario.dto.CurrentScenarioResponse;

@RestController
@RequestMapping("/scenarios")
@RequiredArgsConstructor
public class ScenarioController {

    private final ScenarioService scenarioService;

    // GET /scenarios/current/users/{user_id}
    @GetMapping("/current/users/{userId}")
    public ResponseEntity<CurrentScenarioResponse> getCurrentScenario(
            @PathVariable Long userId) {
        return ResponseEntity.ok(scenarioService.getCurrentScenario(userId));
    }
}
