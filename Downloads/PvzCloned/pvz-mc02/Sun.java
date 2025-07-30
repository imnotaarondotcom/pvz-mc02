/**
 * Represents a Sun collectible in the game. Sun can be naturally spawned on the board
 * or produced by certain plants (e.g., Sunflowers). Collecting sun provides currency
 * for the player to plant more plants.
 * @author Lim, Israel Sy
 * @author Enriquez, Aaron Mikael Cruz
 * @version 2.0
 * @since 2025-06-27
 */

public class Sun {
    /** The tile number (column) where the sun is located. */
    private final int TILE_NO;

    /** The name of the sun entity. */
    private final String NAME;

    /** The current state of the sun (e.g., "idle"). */
    private String state;

    /** The visual size or scale of the sun. */
    private double size;

    /** The horizontal position offset of the sun within its tile. */
    private double position;

    /** The vertical (Y) offset of the sun above its tile. */
    private double positionY;

    /** The lane number (row) where the sun is located. */
    private final int LANE_NO;

    /** The monetary value of a single sun collectible. This is a constant for all sun. */
    private static final int VALUE = 50;

    /**
     * Constructs a new Sun object that spawns naturally on the game board.
     * Prints a message to the console indicating where the sun spawned.
     * @param l The lane index (row) where the sun will be placed.
     * @param t The tile index (column) where the sun will be spawned.
     */
    public Sun(int l, int t){
        positionY = 0.25;
        position = 0;
        size = 0.5;
        NAME = "sun";
        state = "idle";
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
        positionY = 0.25;
        position = 0;
        size = 0.5;
        NAME = "sun";
        state = "idle";
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

    /**
     * Returns the name of the sun entity.
     * @return The sun's name.
     */
    public String getName(){
        return NAME;
    }

    /**
     * Returns the current state of the sun.
     * @return The sun's state.
     */
    public String getState(){
        return state;
    }

    /**
     * Returns the lane number where the sun is located.
     * @return The sun's lane number.
     */
    public int getLaneNo(){
        return LANE_NO;
    }

    /**
     * Returns the tile number where the sun is located.
     * @return The sun's tile number.
     */
    public int getTileNo(){
        return TILE_NO;
    }

    /**
     * Returns the visual size or scale of the sun.
     * @return The sun's size.
     */
    public double getSize(){
        return size;
    }

    /**
     * Returns the horizontal position offset of the sun within its tile.
     * @return The sun's horizontal position.
     */
    public double getPosition(){
        return position;
    }

    /**
     * Returns the vertical (Y) offset of the sun.
     * @return The sun's Y-offset.
     */
    public double getPositionY(){
        return positionY;
    }
}