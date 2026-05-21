package scsa.project.PlayLog.dto;

import java.time.LocalDateTime;

public record PlayLogResponse(
        Long log_id,
        Long state_id,
        Long scenario_id,
        Integer step_order,
        String content,
        String selected_opt,
        String selected_text,
        Integer score_change,
        LocalDateTime created_at
) {}
