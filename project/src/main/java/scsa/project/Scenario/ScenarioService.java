package scsa.project.Scenario;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import scsa.project.PlayLog.PlayLog;
import scsa.project.PlayLog.PlayLogRepository;
import scsa.project.PlayLog.SelectedOption;
import scsa.project.PlayState.PlayState;
import scsa.project.PlayState.PlayStateRepository;
import scsa.project.Scenario.dto.CurrentScenarioResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScenarioService {

    private final ScenarioRepository scenarioRepository;
    private final PlayStateRepository playStateRepository;
    private final PlayLogRepository playLogRepository;

    public CurrentScenarioResponse getCurrentScenario(Long userId) {
        PlayState playState = playStateRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("플레이 상태가 존재하지 않습니다."));

        Scenario scenario;

        List<PlayLog> logs = playLogRepository.findByPlayStateIdWithScenario(playState.getStateId());

        if (logs.isEmpty()) {
            // 첫 시작 → step_order 1번으로
            scenario = scenarioRepository.findByStepOrder(1)
                    .orElseThrow(() -> new IllegalArgumentException("첫 번째 시나리오가 존재하지 않습니다."));
        } else {
            // 마지막 로그의 next_id로 찾기
            PlayLog lastLog = logs.get(logs.size() - 1);
            Long nextId = lastLog.getSelectedOpt() == SelectedOption.A
                    ? lastLog.getScenario().getNextAId()
                    : lastLog.getScenario().getNextBId();

            scenario = scenarioRepository.findById(nextId)
                    .orElseThrow(() -> new IllegalArgumentException("다음 시나리오를 찾을 수 없습니다."));
        }

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