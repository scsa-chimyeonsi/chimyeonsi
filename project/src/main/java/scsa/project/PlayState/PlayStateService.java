package scsa.project.PlayState;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import scsa.project.Scenario.ScenarioRepository;
import scsa.project.User.User;
import scsa.project.User.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlayStateService {

    private final PlayStateRepository playStateRepository;
    private final ScenarioRepository scenarioRepository;
    private final UserRepository userRepository;

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
}
