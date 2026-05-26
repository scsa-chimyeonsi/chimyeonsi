package scsa.project.PlayLog;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scsa.project.PlayLog.dto.NextScenarioResponse;
import scsa.project.PlayLog.dto.SubmitOptionRequest;
import scsa.project.PlayLog.dto.SubmitOptionResponse;
import scsa.project.PlayState.PlayState;
import scsa.project.PlayState.PlayStateRepository;
import scsa.project.Scenario.Scenario;
import scsa.project.Scenario.ScenarioRepository;

import java.util.List;
import java.util.stream.Collectors;
import scsa.project.PlayLog.dto.PlayLogResponse;

@Service
@RequiredArgsConstructor
public class PlayLogService {

    private final PlayStateRepository playStateRepository;
    private final ScenarioRepository scenarioRepository;
    private final PlayLogRepository playLogRepository;

    @Transactional
    public SubmitOptionResponse submitOption(Long userId, SubmitOptionRequest request) {

        // 1. user_id로 play_states 조회
        PlayState playState = playStateRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("진행 중인 게임 상태가 없습니다."));

        // 2. scenario_id로 현재 scenarios 조회
        Scenario currentScenario = scenarioRepository.findById(request.scenarioId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시나리오입니다."));

        // 3. selected_opt에 따라 점수 및 다음 시나리오 ID 확인
        int scoreChange = 0;
        Long nextScenarioId = null;

        if (request.selectedOpt() == SelectedOption.A) {
            scoreChange = currentScenario.getOptAScore();
            nextScenarioId = currentScenario.getNextAId();
        } else if (request.selectedOpt() == SelectedOption.B) {
            scoreChange = currentScenario.getOptBScore();
            nextScenarioId = currentScenario.getNextBId();
        }

        // 4. play_logs INSERT
        PlayLog playLog = PlayLog.builder()
                .selectedOpt(request.selectedOpt())
                .playState(playState)
                .scenario(currentScenario)
                .build();
        playLogRepository.save(playLog);

        // 5. play_states UPDATE (total_score, current_step)
        playState.addScore(scoreChange); // PlayState 엔티티에 해당 메서드 구현 필요
        playState.incrementStep();       // PlayState 엔티티에 해당 메서드 구현 필요

        // 6. 다음 시나리오 조회 및 결과 결정
        String resultType;
        String message = "선택이 완료되었습니다.";
        NextScenarioResponse nextScenarioResponse = null;

        if (nextScenarioId != null) {
            // Case 1: 다음 ID가 있는 경우
            Scenario nextScenario = scenarioRepository.findById(nextScenarioId)
                    .orElseThrow(() -> new IllegalArgumentException("다음 시나리오를 찾을 수 없습니다."));

            // ScenarioType Enum을 String으로 변환하여 확인
            String scenarioTypeStr = nextScenario.getType().name();

            if ("CONTINUE".equals(scenarioTypeStr)) {
                resultType = "CONTINUE";
            } else {
                // GAME_OVER 또는 ENDING인 경우
                resultType = scenarioTypeStr;
                // 별도의 엔딩 메시지 필드가 없으므로, 해당 시나리오의 content를 메시지로 활용
                message = nextScenario.getContent();
            }

            // 다음 시나리오 정보 DTO 매핑 (getScenarioId, getContent 사용)
            nextScenarioResponse = new NextScenarioResponse(
                    nextScenario.getScenarioId(),
                    nextScenario.getContent(),
                    scenarioTypeStr,
                    nextScenario.getOptAText(),
                    nextScenario.getOptBText()
            );
        } else {
            // Case 2: 다음 ID가 NULL인 경우 (끝)
            resultType = "ENDING";
            message = "모든 시나리오가 종료되었습니다.";
            nextScenarioResponse = null;
        }

        // 7. 최종 결과 반환
        return new SubmitOptionResponse(
                resultType,
                scoreChange,
                playState.getTotalScore(),
                playState.getCurrentStep(),
                message,
                nextScenarioResponse
        );
    }

    @Transactional(readOnly = true)
    public List<PlayLogResponse> getLogsByStateId(Long stateId) {
        PlayState playState = playStateRepository.findById(stateId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게임 상태가 존재하지 않습니다."));

        // JOIN FETCH로 시나리오를 한 번에 가져와 N+1 문제를 회피
        List<PlayLog> logs = playLogRepository.findByPlayStateIdWithScenario(stateId);

        return logs.stream()
                .map(pl -> {
                    Scenario s = pl.getScenario();
                    String selectedText = null;
                    Integer scoreChange = null;
                    if (pl.getSelectedOpt() == SelectedOption.A) {
                        selectedText = s.getOptAText();
                        scoreChange = s.getOptAScore();
                    } else if (pl.getSelectedOpt() == SelectedOption.B) {
                        selectedText = s.getOptBText();
                        scoreChange = s.getOptBScore();
                    }

                    return new PlayLogResponse(
                            pl.getLogId(),
                            playState.getStateId(),
                            s.getScenarioId(),
                            s.getStepOrder(),
                            s.getContent(),
                            pl.getSelectedOpt().name(),
                            selectedText,
                            scoreChange,
                            pl.getCreatedAt()
                    );
                })
                .collect(Collectors.toList());
    }
}

