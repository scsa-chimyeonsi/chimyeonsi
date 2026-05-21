package scsa.project.PlayState;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import scsa.project.PlayLog.PlayLog;
import scsa.project.PlayLog.PlayLogRepository;
import scsa.project.PlayLog.SelectedOption;
import scsa.project.PlayState.dto.SummaryResponse;
import scsa.project.Scenario.Scenario;
import scsa.project.Scenario.ScenarioRepository;
import scsa.project.Scenario.ScenarioType;
import scsa.project.User.User;
import scsa.project.User.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlayStateService {

    private final PlayStateRepository playStateRepository;
    private final ScenarioRepository scenarioRepository;
    private final UserRepository userRepository;
    private final PlayLogRepository playLogRepository;

    public PlayStateResponseDto getPlayStateByUserId(Long userId) {
        return playStateRepository.findByUser_UserId(userId)
                .map(state -> PlayStateResponseDto.of(state, scenarioRepository.count()))
                .orElse(PlayStateResponseDto.notFound());
    }

    @Transactional
    public PlayStateResponseDto resetOrCreatePlayState(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        PlayState state = playStateRepository.findByUser_UserId(userId)
                .map(existing -> {
                    existing.getPlayLogs().clear();
                    existing.setCurrentStep(1);
                    existing.setTotalScore(0);
                    return playStateRepository.save(existing);
                })
                .orElseGet(() -> playStateRepository.save(
                        PlayState.builder().user(user).build()
                ));

        return PlayStateResponseDto.forReset(state);
    }

    private Scenario getResultScenario(List<PlayLog> logs) {
        if (logs.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game not started");
        }

        PlayLog lastLog = logs.get(logs.size() - 1);
        Scenario lastScenario = lastLog.getScenario();

        Long nextId = lastLog.getSelectedOpt() == SelectedOption.A
                ? lastScenario.getNextAId()
                : lastScenario.getNextBId();

        if (nextId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game is not finished yet");
        }

        Scenario resultScenario = scenarioRepository.findById(nextId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Result scenario not found"));

        if (resultScenario.getType() == ScenarioType.CONTINUE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game is not finished yet");
        }

        return resultScenario;
    }

    public EndingResponse getEnding(Long userId) {
        PlayState state = playStateRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Play state not found"));

        List<PlayLog> logs = playLogRepository.findByPlayStateIdWithScenario(state.getStateId());
        Scenario resultScenario = getResultScenario(logs);

        long optACount = logs.stream().filter(l -> l.getSelectedOpt() == SelectedOption.A).count();
        long optBCount = logs.stream().filter(l -> l.getSelectedOpt() == SelectedOption.B).count();

        EndingType endingType = resultScenario.getType() == ScenarioType.GAME_OVER
                ? EndingType.GAME_OVER
                : EndingType.ENDING;

        return EndingResponse.of(state, endingType, resultScenario.getContent(), optACount, optBCount);
    }

    public SummaryResponse getSummary(Long userId) {
        PlayState state = playStateRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Play state not found"));

        List<PlayLog> logs = playLogRepository.findByPlayStateIdWithScenario(state.getStateId());
        Scenario resultScenario = getResultScenario(logs);

        EndingType endingType = resultScenario.getType() == ScenarioType.GAME_OVER
                ? EndingType.GAME_OVER
                : EndingType.ENDING;

        List<SummaryResponse.TimelineItem> timeline = logs.stream()
                .map(pl -> {
                    Scenario s = pl.getScenario();
                    String selectedText = pl.getSelectedOpt() == SelectedOption.A
                            ? s.getOptAText() : s.getOptBText();
                    Integer scoreChange = pl.getSelectedOpt() == SelectedOption.A
                            ? s.getOptAScore() : s.getOptBScore();

                    return SummaryResponse.TimelineItem.builder()
                            .step_order(s.getStepOrder())
                            .scenario_id(s.getScenarioId())
                            .content(s.getContent())
                            .selected_opt(pl.getSelectedOpt().name())
                            .selected_text(selectedText)
                            .score_change(scoreChange)
                            .created_at(pl.getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList());

        return SummaryResponse.builder()
                .state_id(state.getStateId())
                .user_id(state.getUser().getUserId())
                .ending_type(resultScenario.getType().name())
                .ending_title(resultScenario.getContent())
                .ending_message(endingType.message)
                .final_score(state.getTotalScore())
                .timeline(timeline)
                .build();
    }
}