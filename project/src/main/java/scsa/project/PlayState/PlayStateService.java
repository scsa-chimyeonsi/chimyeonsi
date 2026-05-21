package scsa.project.PlayState;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import scsa.project.PlayLog.PlayLog;
import scsa.project.PlayLog.PlayLogRepository;
import scsa.project.PlayLog.SelectedOption;
import scsa.project.Scenario.ScenarioRepository;
import scsa.project.User.User;
import scsa.project.User.UserRepository;

import java.util.List;

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

    public EndingResponse getEnding(Long userId) {
        PlayState state = playStateRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Play state not found"));

        long totalScenarios = scenarioRepository.count();
        if (state.getCurrentStep() <= totalScenarios) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game is not finished yet");
        }

        List<PlayLog> logs = playLogRepository.findByPlayState(state);
        long optACount = logs.stream().filter(l -> l.getSelectedOpt() == SelectedOption.A).count();
        long optBCount = logs.stream().filter(l -> l.getSelectedOpt() == SelectedOption.B).count();

        EndingType endingType = EndingType.of(state.getTotalScore());
        return EndingResponse.of(state, endingType, optACount, optBCount);
    }
}
