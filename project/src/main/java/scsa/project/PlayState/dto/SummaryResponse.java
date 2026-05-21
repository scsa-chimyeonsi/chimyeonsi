package scsa.project.PlayState.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SummaryResponse {
    private Long state_id;
    private Long user_id;
    private String ending_type;
    private String ending_title;
    private Integer final_score;
    private List<TimelineItem> timeline;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class TimelineItem {
        private Integer step_order;
        private Long scenario_id;
        private String content;
        private String selected_opt;
        private String selected_text;
        private Integer score_change;
        private LocalDateTime created_at;
    }



}
