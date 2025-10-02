package academy.game;


import java.util.*;


public class GameCore {
    private String secretWord;
    private String originalSecretWord;
    private char[] guessedLetters;
    private int maxAttempts;
    private int attemptsLeft;
    private int wrongAttempts;
    private final Set<Character> usedLetters;
    private boolean gameOver;
    private boolean gameWon;

    public String getSecretWord() {
        return originalSecretWord;
    }

    public int getAttemptsLeft(){
        return attemptsLeft;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }


    public int getWrongAttempts() {
        return wrongAttempts;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public String getGuessedWord() {
        char[] result = new char[originalSecretWord.length()];
        for (int i = 0; i < originalSecretWord.length(); i++) {
            char originalChar = originalSecretWord.charAt(i);
            char lowerChar = Character.toLowerCase(originalChar);

            if (guessedLetters[i] != '_') {
                result[i] = originalChar;
            } else {
                result[i] = '_';
            }
        }
        return new String(result);
    }

    public Set<Character> getUsedLetters() {
        return new HashSet<>(usedLetters);
    }

    public GameCore() {
        this.usedLetters = new HashSet<>();
    }

    public void initializeGame(String secretWord, int maxAttempts) {
        if (secretWord == null || secretWord.trim().isEmpty()) {
            throw new IllegalArgumentException("Secret word cannot be null or empty");
        }
        if (secretWord.length() < 2) {
            throw new IllegalArgumentException("Secret word must be at least 2 characters long");
        }
        this.originalSecretWord = secretWord;
        this.secretWord = secretWord.toLowerCase();
        this.maxAttempts = maxAttempts;
        this.attemptsLeft = maxAttempts;
        this.wrongAttempts = 0;
        this.guessedLetters = new char[secretWord.length()];
        Arrays.fill(this.guessedLetters, '_');
        this.gameOver = false;
        this.gameWon = false;
        this.usedLetters.clear();
    }

    public boolean processGuess(char letter) {
        if (gameOver) return false;

        letter = Character.toLowerCase(letter);

        if (usedLetters.contains(letter)) {
            return false;
        }

        usedLetters.add(letter);
        boolean isCorrect = false;

        for (int i = 0; i < secretWord.length(); i++) {
            if (secretWord.charAt(i) == letter) {
                guessedLetters[i] = letter;
                isCorrect = true;
            }
        }

        if (!isCorrect) {
            attemptsLeft--;
            wrongAttempts++;
        }

        checkGameStatus();

        return isCorrect;
    }

    private void checkGameStatus() {
        if (!new String(guessedLetters).contains("_")) {
            gameWon = true;
            gameOver = true;
            return;
        }

        if (attemptsLeft <= 0) {
            gameWon = false;
            gameOver = true;
        }
    }
    public ValidationResult validateInput(String input) {
        if (input.length() != 1) {
            return new ValidationResult(false, "Please enter exactly one character");
        }

        char letter = input.charAt(0);
        if (Character.isDigit(letter)) {
            return new ValidationResult(false, "Please enter a letter, not a digit");
        }

        if (!Character.isLetter(letter)) {
            return new ValidationResult(false, "Please enter a valid letter");
        }

        return new ValidationResult(true, "");
    }
    public record ValidationResult(boolean isValid, String errorMessage) {}

    public static String playAutomated(String secretWord, String guessWord) {
        if (secretWord.length() != guessWord.length()) {
            throw new IllegalArgumentException("Слова должны быть одинаковой длины");
        }

        char[] result = new char[secretWord.length()];
        for (int i = 0; i < secretWord.length(); i++) {
            char currentChar = secretWord.charAt(i);
            if (guessWord.contains(String.valueOf(currentChar))) {
                result[i] = secretWord.charAt(i);
            } else {
                result[i] = '_';
            }
        }

        boolean fullyCorrect = secretWord.equals(guessWord);

        return new String(result) + ";" + (fullyCorrect ? "POS" : "NEG");
    }
}




