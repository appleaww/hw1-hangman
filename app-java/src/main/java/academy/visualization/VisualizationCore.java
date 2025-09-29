package academy.visualization;


import academy.game.GameCore;
import java.util.Set;

public class VisualizationCore implements VisualizationInterface {

    @Override
    public void consoleWelcome() {
        System.out.println("Welcome to the game Hangman!");
    }

    @Override
    public void consoleDisplayGameState(GameCore gameCore, Boolean flag, Boolean isLetterFlag) {
        clearDisplay();
        int wrongAttempts = gameCore.getWrongAttempts();
        int maxAttempts = gameCore.getMaxAttempts();
        HangmanStages stage = HangmanStages.getError(wrongAttempts, maxAttempts);
        System.out.println(stage.drawing);
        System.out.println("Word: " + gameCore.getGuessedWord());
        System.out.println("Mistakes: " + gameCore.getWrongAttempts() + " / " + gameCore.getMaxAttempts());
        Set<Character> usedLetters = gameCore.getUsedLetters();
        if (!usedLetters.isEmpty()) {
            System.out.println("Used letters: " + usedLetters);
        }
        if(!flag) System.out.println("Please enter exactly one letter!");
        if(!isLetterFlag) System.out.println("Please enter a letter, not a digit!");

        System.out.println();
    }


    @Override
    public void consoleWin(String word) {
        clearDisplay();
        System.out.println("Congratulations, you won the game!");
        System.out.println("The hidden word: " + word);
    }

    @Override
    public void consoleLose(String word) {
        clearDisplay();
        System.out.println(HangmanStages.STAGE_6.drawing);
        System.out.println("Unfortunately, you lost the game");
        System.out.println("The hidden word: " + word);
    }

    @Override
    public void consoleMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void clearDisplay() {
        for (int i = 0; i < 30; i++) {
            System.out.println();
        }
    }
    @Override
    public void clearDisplayWithMessage() {
        for (int i = 0; i < 30; i++) {
            System.out.println();
        }
        System.out.println("Please enter exactly one letter!");
    }

}
