package academy;

import java.util.ArrayList;
import java.util.List;

public class Game {
    public static String playAutomated(String secretWord, String guessWord) {
        if (secretWord.length() != guessWord.length()) {
            throw new IllegalArgumentException("Слова должны быть одинаковой длины");
        }

        char[] result = new char[secretWord.length()];
        List<Character> availableLetters = new ArrayList<>();

        for (char c : guessWord.toCharArray()) {
            availableLetters.add(c);
        }

        boolean fullyCorrect = true;

        for (int i = 0; i < secretWord.length(); i++) {
            if (secretWord.charAt(i) == guessWord.charAt(i)) {
                result[i] = secretWord.charAt(i);

                availableLetters.remove((Character) secretWord.charAt(i));
            } else {
                result[i] = '*';
                fullyCorrect = false;
            }
        }
        for (int i = 0; i < secretWord.length(); i++) {
            if (result[i] != '*') continue;

            char currentChar = secretWord.charAt(i);

            if (availableLetters.contains(currentChar)) {
                result[i] = currentChar;

                availableLetters.remove((Character) currentChar);
            }
        }

        return new String(result) + ";" + (fullyCorrect ? "POS" : "NEG");
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java Game <secret_word> <guess_word>");
            System.exit(1);
        }

        try {
            String result = playAutomated(args[0], args[1]);
            System.out.println(result);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
