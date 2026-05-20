package scsa.project.PlayLog.dto;

public record NextScenarioResponse(
        Long scenarioId,
        String content,
        String type
) {}