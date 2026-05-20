package scsa.project.PlayLog.dto;

public record SubmitOptionResponse(
        String resultType,
        int scoreChange,
        int totalScore,
        int currentStep,
        String message,
        NextScenarioResponse nextScenario
) {}