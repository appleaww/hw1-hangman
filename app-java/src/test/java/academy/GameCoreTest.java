package academy;

import academy.game.GameCore;
import academy.game.Dictionary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

class GameCoreTest {
    private GameCore game;
    private Dictionary dictionary;

    @BeforeEach
    void setUp() {
        game = new GameCore();
        dictionary = new Dictionary();
        dictionary.load("words.yaml");
    }

    @Test
    void testWordSelectionFromDictionary() {
        String word = dictionary.getRandomWord("Животные", "easy");
        assertNotNull(word);
        assertFalse(word.isEmpty());

        Set<String> categories = dictionary.getCategories();
        assertTrue(categories.contains("Животные"));
    }

    @Test
    void testGameStateAfterEachInput() {
        game.initializeGame("яблоко", 5);

        assertEquals("******", game.getGuessedWord());
        assertEquals(5, game.getAttemptsLeft());
        assertFalse(game.isGameOver());

        game.processGuess('я');
        assertEquals("я*****", game.getGuessedWord());
        assertEquals(5, game.getAttemptsLeft());
        assertFalse(game.isGameOver());

        game.processGuess('з');
        assertEquals("я*****", game.getGuessedWord());
        assertEquals(4, game.getAttemptsLeft());
        assertFalse(game.isGameOver());
    }

    @Test
    void testCaseInsensitiveProcessing() {
        game.initializeGame("ТЕСТ", 5);

        assertTrue(game.processGuess('т'));
        assertFalse(game.processGuess('Т'));
        assertTrue(game.processGuess('Е'));
        assertTrue(game.processGuess('с'));

        assertEquals("тест", game.getGuessedWord());

        Set<Character> usedLetters = game.getUsedLetters();
        assertTrue(usedLetters.contains('т'));
        assertTrue(usedLetters.contains('е'));
        assertTrue(usedLetters.contains('с'));
        assertEquals(3, usedLetters.size());
    }

    @Test
    void testInvalidWordLength() {
        assertThrows(IllegalArgumentException.class, () -> {
            game.initializeGame("а", 5);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            game.initializeGame("", 5);
        });

        assertDoesNotThrow(() -> {
            game.initializeGame("проверка", 5);
        });
    }

    @Test
    void testGameOverAfterMaxAttempts() {
        game.initializeGame("тест", 3);

        assertFalse(game.processGuess('ч'));
        assertFalse(game.processGuess('щ'));
        assertFalse(game.processGuess('ю'));

        assertTrue(game.isGameOver());
        assertFalse(game.isGameWon());
        assertEquals(0, game.getAttemptsLeft());
    }

    @Test
    void testStateChangesOnGuess() {
        game.initializeGame("сом", 4);

        Set<Character> initialUsed = game.getUsedLetters();
        assertEquals(0, initialUsed.size());

        assertTrue(game.processGuess('с'));
        assertEquals(1, game.getUsedLetters().size());
        assertTrue(game.getUsedLetters().contains('с'));
        assertEquals("с**", game.getGuessedWord());

        assertFalse(game.processGuess('ч'));
        assertEquals(2, game.getUsedLetters().size());
        assertEquals(3, game.getAttemptsLeft());
        assertEquals("с**", game.getGuessedWord());
    }

    @Test
    void testWinGame() {
        game.initializeGame("да", 3);

        game.processGuess('д');
        game.processGuess('а');

        assertTrue(game.isGameOver());
        assertTrue(game.isGameWon());
        assertEquals("да", game.getGuessedWord());
    }

    @Test
    void testRepeatedLetter() {
        game.initializeGame("тест", 3);

        assertTrue(game.processGuess('т'));
        int attemptsAfterFirst = game.getAttemptsLeft();
        String wordAfterFirst = game.getGuessedWord();

        assertFalse(game.processGuess('т'));

        assertEquals(attemptsAfterFirst, game.getAttemptsLeft());
        assertEquals(wordAfterFirst, game.getGuessedWord());
    }

    @Test
    void testGameNotOverInitially() {
        game.initializeGame("слово", 5);

        assertFalse(game.isGameOver());
        assertFalse(game.isGameWon());
        assertEquals(5, game.getAttemptsLeft());
    }
}
