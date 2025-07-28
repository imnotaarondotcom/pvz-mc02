/**
 * Represents a Sun collectible in the game. Sun can be naturally spawned on the board
 * or produced by certain plants (e.g., Sunflowers). Collecting sun provides currency
 * for the player to plant more plants.
 * @author Lim, Israel Sy
 * @author Enriquez, Aaron Mikael Cruz
 * @version 1.0
 * @since 2025-06-27
 */

public class Sun {
    /** The tile number (column) where the sun is located. */
    private final int TILE_NO;

    /** The lane number (row) where the sun is located. */
    private final int LANE_NO;

    /** The monetary value of a single sun collectible. This is a constant for all sun. */
    private static final int VALUE = 25; 
    
    /**
     * Constructs a new Sun object that spawns naturally on the game board.
     * Prints a message to the console indicating where the sun spawned.
     * @param l The lane index (row) where the sun will be placed.
     * @param t The tile index (column) where the sun will be spawned.
     */
    public Sun(int l, int t){
        TILE_NO = t;
        LANE_NO = l;
        GameClock.printTime();
        System.out.printf("Sun spawned at lane %d tile %d\n", LANE_NO + 1, TILE_NO + 1);
        
    }

    /**
     * Constructs a new Sun object that is produced by a specific plant.
     * Prints a message to the console indicating which plant produced the sun and its location.
     * @param l The lane index (row) where the sun will spawn.
     * @param t The tile index (column) where the sun will spawn.
     * @param p The {@link Plant} object that produced this sun.
     */
    public Sun(int l, int t, Plant p){
        TILE_NO = t;
        LANE_NO = l;
        GameClock.printTime();
        System.out.printf("Sun spawned by %s at lane %d tile %d\n", p.getName(), p.getLaneNo() + 1, p.getTileNo() + 1);
    }

    /**
     * Returns the fixed monetary value of a Sun collectible.
     * @return The integer value of sun.
     */
    public static int getValue(){
        return VALUE;
    }
}