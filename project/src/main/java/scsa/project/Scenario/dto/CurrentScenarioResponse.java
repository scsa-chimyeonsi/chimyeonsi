package scsa.project.Scenario.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CurrentScenarioResponse {
    private Long state_id;
    private Long user_id;
    private Integer current_step;
    private Long total_steps;
    private Integer total_score;
    private ScenarioDto scenario;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ScenarioDto {
        private Long scenario_id;
        private Integer step_order;
        private String content;
        private String opt_a_text;
        private String opt_b_text;
    }
}
