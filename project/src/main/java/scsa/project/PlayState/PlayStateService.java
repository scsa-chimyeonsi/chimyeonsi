package scsa.project.PlayState;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scsa.project.PlayLog.SelectedOption;
import scsa.project.PlayState.dto.SummaryResponse;
import scsa.project.Scenario.Scenario;
import scsa.project.Scenario.ScenarioRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlayStateService {

    private final PlayStateRepository playStateRepository;
    private final ScenarioRepository scenarioRepository;

    public PlayStateResponseDto getPlayStateByUserId(Long userId) {
        return playStateRepository.findByUser_UserId(userId)
                .map(state -> PlayStateResponseDto.of(state, scenarioRepository.count()))
                .orElse(PlayStateResponseDto.notFound());
    }

    public SummaryResponse getSummary(Long userId) {
        PlayState playState = playStateRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("플레이 상태가 없습니다."));

        String endingType = "HAPPY";
        String endingTitle = "해피 엔딩";

        List<SummaryResponse.TimelineItem> timeline = playState.getPlayLogs()
                .stream()
                .map(log -> {
                    Scenario s = log.getScenario();
                    String selectedText = log.getSelectedOpt() == SelectedOption.A
                            ? s.getOptAText() : s.getOptBText();
                    int scoreChange = log.getSelectedOpt() == SelectedOption.A
                            ? s.getOptAScore() : s.getOptBScore();

                    return SummaryResponse.TimelineItem.builder()
                            .step_order(s.getStepOrder())
                            .scenario_id(s.getScenarioId())
                            .content(s.getContent())
                            .selected_opt(log.getSelectedOpt().name())
                            .selected_text(selectedText)
                            .score_change(scoreChange)
                            .created_at(log.getCreatedAt())
                            .build();
                })
                .toList();

        return SummaryResponse.builder()
                .state_id(playState.getStateId())
                .user_id(userId)
                .ending_type(endingType)
                .ending_title(endingTitle)
                .final_score(playState.getTotalScore())
                .timeline(timeline)
                .build();
    }
}