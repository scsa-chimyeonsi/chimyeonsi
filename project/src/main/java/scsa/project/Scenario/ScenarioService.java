package scsa.project.Scenario;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import scsa.project.PlayState.PlayState;
import scsa.project.PlayState.PlayStateRepository;
import scsa.project.Scenario.dto.CurrentScenarioResponse;

@Service
@RequiredArgsConstructor
public class ScenarioService {

    private final ScenarioRepository scenarioRepository;
    private final PlayStateRepository playStateRepository;

    public CurrentScenarioResponse getCurrentScenario(Long userId) {
        PlayState playState = playStateRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("플레이 상태가 존재하지 않습니다."));

        Scenario scenario = scenarioRepository.findByStepOrder(playState.getCurrentStep())
                .orElseThrow(() -> new IllegalArgumentException("해당 단계의 시나리오가 존재하지 않습니다."));

        long totalSteps = scenarioRepository.count();

        return CurrentScenarioResponse.builder()
                .state_id(playState.getStateId())
                .user_id(userId)
                .current_step(playState.getCurrentStep())
                .total_steps(totalSteps)
                .total_score(playState.getTotalScore())
                .scenario(CurrentScenarioResponse.ScenarioDto.builder()
                        .scenario_id(scenario.getScenarioId())
                        .step_order(scenario.getStepOrder())
                        .content(scenario.getContent())
                        .opt_a_text(scenario.getOptAText())
                        .opt_b_text(scenario.getOptBText())
                        .build())
                .build();
    }
}
