import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class serves as the abstract base for all plant types in the game.
 * It defines fundamental properties and behaviors common to all plants,
 * such as health, position, and basic interaction logic.
 * @author Lim, Israel Sy
 * @author Enriquez, Aaron Mikael Cruz
 * @version 2.0
 * @since 2025-06-27
 */
public abstract class Plant {
    /** The type of the plant. */
    protected final String NAME;

    /** The speed attribute of the plant (e.g., attack speed, production rate). */
    protected final double SPEED;

    /** The tile number (column) the plant occupies. */
    protected final int TILE_NO;

    /** The lane number (row) the plant occupies. */
    protected final int LANE_NO;

    /** The current health of the plant. */
    protected int health;

    /** Tracks the time elapsed since the plant's last action/attack. */
    protected double timeSinceLastAttack = 0;

    /** The current visual state of the plant (e.g., "idle", "attacking"). */
    protected String state;
    /** The normalized horizontal position within its tile (0.0 to 1.0). */
    private double position;

    /** The visual size or scale of the plant. */
    private double size;

    /**
     * Constructs a new Plant object.
     * @param n The name of the plant.
     * @param s The speed of the plant.
     * @param h The initial health of the plant.
     * @param lane The lane number the plant will occupy.
     * @param tile The tile number the plant will occupy.
     */
    public Plant(String n, double s, int h, int lane, int tile){
        NAME = n;
        SPEED = s;
        health = h;
        LANE_NO = lane;
        TILE_NO = tile;
        state = "idle";
        position = 0;
        size = 1;
    }

    /**
     * Defines the primary action of the plant.
     * Subclasses must implement this to specify what the plant does (e.g., shoot, produce sun).
     * @param b The game board.
     */
    public abstract void action(Board b);

    /**
     * Attempts to make the plant perform its action based on game logic and timers.
     * Subclasses must implement this to define when and how the plant tries to act.
     * @param b The game board.
     * @param elapsedTime The time elapsed since the last update.
     */
    public abstract void tryToAction(Board b, double elapsedTime);

    /**
     * Updates the plant's internal timer for its last action/attack.
     * @param elapsedTime The time elapsed since the last game update.
     */
    public void updateTime(double elapsedTime){
        timeSinceLastAttack += elapsedTime;
    }

    /**
     * Allows the plant to take damage, reducing its health.
     * @param damage The amount of damage the plant will take.
     */
    public void takeDamage(int damage){
        health -= damage;
    }

    /**
     * Checks if the lane the plant is in has no zombies in front of it.
     * @param tile An array of {@link Tile} objects representing the lane.
     * @return True if there are no zombies in the lane from the plant's tile onwards, false otherwise.
     */
    public boolean isLaneClear(Tile[] tile)
    {
        // Iterates through all tiles in the lane from the plant's position to the end
        for(int t = TILE_NO; t < PvZDriver.getMaxTiles(); t++ )
        {
            if(!(tile[t].noZombies() == 0))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the name of the plant.
     * @return The plant's name.
     */
    public String getName(){
        return NAME;
    }

    /**
     * Returns the current health of the plant.
     * @return The plant's health.
     */
    public int getHealth(){
        return health;
    }

    /**
     * Returns the lane number the plant occupies.
     * @return The plant's lane number.
     */
    public int getLaneNo(){
        return LANE_NO;
    }

    /**
     * Returns the normalized horizontal position of the plant within its tile.
     * @return The horizontal position (0.0 to 1.0).
     */
    public double getPosition(){
        return position;
    }

    /**
     * Returns the tile number the plant occupies.
     * @return The plant's tile number.
     */
    public int getTileNo(){
        return TILE_NO;
    }

    /**
     * Updates the current visual state of the plant.
     * @param state The new state string.
     */
    public void updateState(String state){
        this.state = state;
    }

    /**
     * Returns the current visual state of the plant.
     * @return The plant's state.
     */
    public String getState(){
        return state;
    }

    /**
     * Returns the current visual size or scale of the plant.
     * @return The plant's size.
     */
    public double getSize(){
        return size;
    }

    /**
     * Sets the visual size or scale of the plant.
     * @param s The new size value.
     */
    public void setSize(double s)
    {
        size = s;
    }
}
