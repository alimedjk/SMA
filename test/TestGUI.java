package test;
import config.GameConfig;
import gui.MainGui;

public class TestGUI {
    public static void main(String[] args) {
        MainGui mainGui = new MainGui(GameConfig.GAME_NAME);
        Thread guiThread = new Thread(mainGui);
        guiThread.start();
    }
}


