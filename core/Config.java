package core;
/**
 * Configuration centralisée pour la simulation.
 */
public class Config {
    public static final int COLS = 30;
    public static final int ROWS = 20;
    public static final int CELL_SIZE = 28;
    public static final int INITIAL_TREASURES = 25;
    public static final int INITIAL_ANIMALS = 8;
    public static final int INITIAL_OBSTACLES = 40;
    public static final int INITIAL_COGNITIVE = 3;
    public static final int INITIAL_REACTIVE = 5;
    // initial communicants is just an initial hint; actual placement respects zones*COMM_PER_ZONE
    public static final int INITIAL_COMM_HINT = 8;

    // NEW: zones + communicant limit per zone
    public static final int ZONES = 10;
    public static final int COMM_PER_ZONE = 1; // M: change this to allow more communicants per zone

    // threading sleeps (ms)
    public static final int AGENT_SLEEP_MS = 200;
    public static final int ANIMAL_SLEEP_MS = 300;

    public static final int TIMER_MS = 200; // UI refresh
}
