import view.MainGui;

public class TestGUI {
    public static void main(String[] args) {
        MainGui mainGui = new MainGui("TREASURE MAP");
        Thread guiThread = new Thread(mainGui);
        guiThread.start();
    }
}


