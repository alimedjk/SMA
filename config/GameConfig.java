package config;

/**
 * Copyright SEDAMOP - Software Engineering
 *
 * @author tianxiao.liu@cyu.fr
 *
 */
public class GameConfig {
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 800;
    public static final int BLOCK_SIZE = 50;
    public static final int NB_ZONE_ROW = 4;

    public static final int NB_ZONES_LIG = WINDOW_HEIGHT / (BLOCK_SIZE * NB_ZONE_ROW);
    public static final int NB_ZONES_COL = WINDOW_WIDTH / (BLOCK_SIZE * NB_ZONE_ROW);

    public static final int LINE_COUNT = WINDOW_HEIGHT / BLOCK_SIZE;
    public static final int COLUMN_COUNT = WINDOW_WIDTH / BLOCK_SIZE;

    public static final int GAME_SPEED = 400;

    public static final int NbRounds = 3;

    public static final int NB_COMMUNICANTS = 10;

    public static final int NB_REACTIFS = 5;

    public static final int NB_COGNITIFS = 5;

    public static final int NB_ANIMALS = 0;

    public static final int NB_OBSTACLES = 0;

    public static final int NB_TREASURES = 5;

    public static final String GAME_NAME = "Explorators Game";
}
