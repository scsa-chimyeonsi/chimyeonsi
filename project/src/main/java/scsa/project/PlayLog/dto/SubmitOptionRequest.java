package scsa.project.PlayLog.dto;
import scsa.project.PlayLog.SelectedOption;

public record SubmitOptionRequest(
        Long scenarioId,
        SelectedOption selectedOpt
) {}
