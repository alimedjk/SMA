package config;

public class GameConfig {
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 800;
    public static final int BLOCK_SIZE = 50;
    public static final int NB_ZONE_ROW = 4;

    public static final int NB_ZONES_LIG = WINDOW_HEIGHT / (BLOCK_SIZE * NB_ZONE_ROW);
    public static final int NB_ZONES_COL = WINDOW_WIDTH / (BLOCK_SIZE * NB_ZONE_ROW);

    public static final int NB_ZONE = 16;


    public static final int LINE_COUNT = WINDOW_HEIGHT / BLOCK_SIZE;
    public static final int COLUMN_COUNT = WINDOW_WIDTH / BLOCK_SIZE;

    public static final int GAME_SPEED = 250;

    public static final int NbRounds = 5;

    // Constantes pour gérer le nombre d'explorateurs en tout genre
    public static final int NB_COMMUNICANTS = 5;

    public static final int NB_REACTIFS = 5;

    public static final int NB_COGNITIFS = 5;

    public static final int NB_GAME_ROUNDS = 300;

    public static final int NB_ANIMALS = 10;

    public static final int NB_OBSTACLES = 15;

    public static final int NB_TREASURES = 15;

    public static final String GAME_NAME = "Odyssey Zero";

}
