package academy;

import academy.game.Dictionary;
import academy.game.GameCore;
import academy.gameConfig.AppConfig;
import academy.visualization.VisualizationCore;
import academy.visualization.VisualizationInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;


@Command(name = "Hangman", version = "Hangman 1.0", mixinStandardHelpOptions = true)
public class Application implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    private static final ObjectReader YAML_READER = new ObjectMapper(new YAMLFactory()).findAndRegisterModules().reader();
    private static Boolean letterLengthFlag = true;
    private static Boolean isLetter = true;

    @Option(names = {"-s", "--font-size"}, description = "Font size")
    int fontSize;

    @Parameters(paramLabel = "<word>", description = "Words pair for testing mode")
    private String[] words;

    @Option(names = {"-c", "--config"}, description = "Path to YAML config file")
    private File configPath;

    @Option(names = {"-d", "--dictionary"}, description = "Path to dictionary YAML file")
    private File dictionaryPath;


    public static void main(String[] args) {
        int exitCode = new CommandLine(new Application()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        if (words != null && words.length == 2) {
            runNonInteractiveMode(words[0], words[1]);
        } else {
            AppConfig config = loadConfig();
            LOGGER.atInfo().addKeyValue("config", config).log("Config content");
            runInteractiveMode(config);
        }
    }

    private void runInteractiveMode(AppConfig config) {
        try {
            Dictionary dictionary = new Dictionary();
            if (dictionaryPath != null) {
                dictionary.load(dictionaryPath.getPath());
            } else {
                dictionary.load("words.yaml");
            }

            VisualizationInterface visualizer = new VisualizationCore();
            GameCore game = new GameCore();

            String category = selectCategory(dictionary);
            String difficulty = selectDifficulty();

            String secretWord = dictionary.getRandomWord(category, difficulty);
            int maxAttempts = calculateMaxAttempts(difficulty, secretWord.length());


            game.initializeGame(secretWord, maxAttempts);

            runGameLoop(game, visualizer);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void runGameLoop(GameCore game, VisualizationInterface visualizer) {
        Scanner scanner = new Scanner(System.in);
        while (!game.isGameOver()) {

            visualizer.consoleDisplayGameState(game, letterLengthFlag, isLetter);
            letterLengthFlag = true;
            isLetter = true;
            visualizer.consoleMessage("Enter a letter: ");
            String input = scanner.nextLine().trim();
            char letter = input.charAt(0);
            if (input.length() != 1 && !Character.isDigit(letter)) {
                letterLengthFlag = false;
                continue;
            }
            if (Character.isDigit(letter)) {
                isLetter = false;
                continue;
            }

            boolean isCorrect = game.processGuess(letter);
        }

        if (game.isGameOver()) {
            if (game.isGameWon()) {
                visualizer.consoleWin(game.getSecretWord());
            } else {
                visualizer.consoleLose(game.getSecretWord());
            }
        }

        scanner.close();
    }

    private void runNonInteractiveMode(String secretWord, String guessWord) {
        try {
            String result = GameCore.playAutomated(secretWord, guessWord);
            System.out.println(result);
        } catch (Exception e) {
            System.err.println("Ошибка в неинтерактивном режиме: " + e.getMessage());
        }
    }

    private String selectCategory(Dictionary dictionary) {
        Scanner scanner = new Scanner(System.in);
        Set<String> categories = dictionary.getCategories();
        VisualizationInterface visualizer = new VisualizationCore();
        visualizer.consoleWelcome();
        System.out.println("Select a category:");
        List<String> categoryList = new ArrayList<>(categories);

        for (int i = 0; i < categoryList.size(); i++) {
            System.out.println((i + 1) + ". " + categoryList.get(i));
        }

        while (true) {
            System.out.print("Enter the number of category:");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= categoryList.size()) {
                    return categoryList.get(choice - 1);
                } else {
                    System.out.println("Incorrect choice! Enter the number from 1 to " + categoryList.size());
                }
            } catch (NumberFormatException e) {
                System.out.println("Please, enter the number!");
            }
        }
    }

    private String selectDifficulty() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nSelect the difficultly level:");
        System.out.println("1. Easy");
        System.out.println("2. Medium");
        System.out.println("3. Hard");

        while (true) {
            System.out.print("Select the difficultly: ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    return "easy";
                case "2":
                    return "medium";
                case "3":
                    return "hard";
                default:
                    System.out.println("Incorrect choice! Enter 1, 2 or 3");
            }
        }
    }

    private int calculateMaxAttempts(String difficulty, int wordLength) {
        return switch (difficulty.toLowerCase()) {
            case "easy" -> {
                int attempts = Math.min(7, wordLength + 1);
                yield attempts;
            }
            case "medium" -> {
                int attempts = Math.min(6, wordLength + 1);
                yield attempts;
            }
            case "hard" -> {
                int attempts = Math.min(4, wordLength - 1);
                yield attempts;
            }
            default -> 6;
        };
    }

    private AppConfig loadConfig() {
        if (configPath == null) return new AppConfig(12, words);
        try {
            return YAML_READER.readValue(configPath, AppConfig.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
