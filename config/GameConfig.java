package config;

public class GameConfig {
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 800;

    public static final int BLOCK_SIZE = 50;

    public static final int LINE_COUNT = WINDOW_HEIGHT / BLOCK_SIZE;
    public static final int COLUMN_COUNT = WINDOW_WIDTH / BLOCK_SIZE;

    public static final int GAME_SPEED = 500;

    public static final int NbRounds = 5;

    // Constantes pour gérer le nombre d'explorateurs en tout genre
    public static final int NB_COMMUNICANTS = 3;

    public static final int NB_REACTIFS = 5;

    public static final int NB_COGNITIFS = 3;
    public static final int NB_GAME_ROUNDS = 300;

}
