package academy;

public class Game {
    public static String playAutomated(String secretWord, String guessWord) {
        if (secretWord.length() != guessWord.length()) {
            throw new IllegalArgumentException("Слова должны быть одинаковой длины");
        }

        char[] result = secretWord.toCharArray(); 
        boolean fullyCorrect = true;

        
        for (int i = 0; i < secretWord.length(); i++) {
            char currentChar = secretWord.charAt(i);

            
            if (guessWord.indexOf(currentChar) == -1) {
                result[i] = '*';
                fullyCorrect = false;
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

