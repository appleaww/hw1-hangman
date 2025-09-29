package academy.visualization;

import academy.game.GameCore;

public interface VisualizationInterface {
    void consoleWelcome();
    void consoleDisplayGameState(GameCore gameCore, Boolean flag, Boolean isLetterFlag);
    void consoleWin(String sentence);
    void consoleLose(String sentence);
    void consoleMessage(String message);
    void clearDisplay();
    void clearDisplayWithMessage();
}
