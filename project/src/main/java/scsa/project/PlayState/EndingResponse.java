package scsa.project.PlayState;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EndingResponse(
        @JsonProperty("state_id") Long stateId,
        @JsonProperty("user_id") Long userId,
        @JsonProperty("ending_type") String endingType,
        @JsonProperty("ending_title") String endingTitle,
        @JsonProperty("ending_message") String endingMessage,
        @JsonProperty("final_score") Integer finalScore,
        @JsonProperty("steps_played") int stepsPlayed,
        @JsonProperty("opt_a_count") long optACount,
        @JsonProperty("opt_b_count") long optBCount
) {
    public static EndingResponse of(PlayState state, EndingType type, long optACount, long optBCount) {
        return new EndingResponse(
                state.getStateId(),
                state.getUser().getUserId(),
                type.name(),
                type.title,
                type.message,
                state.getTotalScore(),
                (int) (optACount + optBCount),
                optACount,
                optBCount
        );
    }
}
