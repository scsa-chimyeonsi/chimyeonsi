package scsa.project.PlayState;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scsa.project.Scenario.ScenarioRepository;

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
}
