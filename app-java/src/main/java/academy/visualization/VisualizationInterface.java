package academy.visualization;

import academy.game.GameCore;

public interface VisualizationInterface {
    void consoleWelcome();
    void consoleWin(String sentence);
    void consoleLose(String sentence);
    void consoleMessage(String message);
    void clearDisplay();
    void consoleDisplayGameState(GameCore gameCore, String errorMessage);
}
