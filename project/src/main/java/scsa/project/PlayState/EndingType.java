package scsa.project.PlayState;

public enum EndingType {
    HAPPY("해피 엔딩", "축하해요! 좋은 결과에 도달했어요."),
    NORMAL("노멀 엔딩", "평범하지만 나쁘지 않은 결말이에요."),
    BAD("배드 엔딩", "아쉽네요. 다음엔 더 좋은 선택을 해보세요.");

    public final String title;
    public final String message;

    EndingType(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public static EndingType of(int score) {
        if (score >= 60) return HAPPY;
        if (score >= 30) return NORMAL;
        return BAD;
    }
}
