package scsa.project.PlayState;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PlayStateResponseDto(
        Boolean exists,
        @JsonProperty("state_id") Long stateId,
        @JsonProperty("user_id") Long userId,
        @JsonProperty("current_step") Integer currentStep,
        @JsonProperty("total_score") Integer totalScore,
        @JsonProperty("updated_at") LocalDateTime updatedAt,
        @JsonProperty("total_steps") Long totalSteps
) {
    public static PlayStateResponseDto notFound() {
        return new PlayStateResponseDto(false, null, null, null, null, null, null);
    }

    public static PlayStateResponseDto of(PlayState state, long totalSteps) {
        return new PlayStateResponseDto(
                true,
                state.getStateId(),
                state.getUser().getUserId(),
                state.getCurrentStep(),
                state.getTotalScore(),
                state.getUpdatedAt(),
                totalSteps
        );
    }
}
