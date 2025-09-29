package academy.visualization;

public enum HangmanStages {
    STAGE_0(0,
            "   +-----| \n" +
            "   |     | \n" +
            "         | \n" +
            "         | \n" +
            "         | \n" +
            "=========| \n"
    ),
    STAGE_1(1,
            "   +-----| \n" +
            "   |     | \n" +
            "   O     | \n" +
            "         | \n" +
            "         | \n" +
            "=========| \n"),
    STAGE_2(2,
            "   +-----| \n" +
            "   |     | \n" +
            "   O     | \n" +
            "   |     | \n" +
            "         | \n" +
            "=========| \n"),
    STAGE_3(3,
            "   +-----| \n" +
            "   |     | \n" +
            "   O     | \n" +
            "  /|     | \n" +
            "         | \n" +
            "=========| \n"),
    STAGE_4(4,
            "   +-----| \n" +
            "   |     | \n" +
            "   O     | \n" +
            "  /|\\    | \n" +
            "         | \n" +
            "=========| \n"),
    STAGE_5(5,
            "   +-----| \n" +
            "   |     | \n" +
            "   O     | \n" +
            "  /|\\    | \n" +
            "  /      | \n" +
            "=========| \n"),
    STAGE_6(6,
            "   +-----| \n" +
            "   |     | \n" +
            "   O     | \n" +
            "  /|\\    | \n" +
            "  / \\    | \n" +
            "=========| \n");
    private final int stage;
    public final String drawing;

    HangmanStages(int stage, String drawing) {
        this.stage = stage;
        this.drawing = drawing;
    }

    public int getStage(){
        return this.stage;
    }

    public static HangmanStages getError(int wrongAttempts, int maxAttempts) {
        if (maxAttempts <= 0 || wrongAttempts < 0) {
            return STAGE_0;
        }
        if (wrongAttempts >= maxAttempts) {
            return STAGE_6;
        }

        double progress = (double) wrongAttempts / maxAttempts;
        int stageIndex = (int) Math.round(progress * 6);

        stageIndex = Math.min(Math.max(stageIndex, 0), 6);
        return values()[stageIndex];
    }

}
