package academy;

public class Game {
    public static String playAutomated(String secretWord, String guessWord) {
        if (secretWord == null || guessWord == null) {
            throw new IllegalArgumentException("Слова не могут быть null");
        }
        if (secretWord.length() != guessWord.length()) {
            throw new IllegalArgumentException("Слова должны быть одинаковой длины");
        }

        StringBuilder result = new StringBuilder();
        boolean fullyCorrect = true;

        for (int i = 0; i < secretWord.length(); i++) {
            if (secretWord.charAt(i) == guessWord.charAt(i)) {
                result.append(secretWord.charAt(i));
            } else {
                result.append('*');
                fullyCorrect = false;
            }
        }

        return result.toString() + ";" + (fullyCorrect ? "POS" : "NEG");
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
