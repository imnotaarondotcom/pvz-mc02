import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents a Cherrybomb plant, an explosive plant that deals area-of-effect damage.
 * Cherrybombs have a high cost and a long cooldown, and explode shortly after being planted.
 *
 * @author Lim, Israel Sy
 * @author Enriquez, Aaron Mikael Cruz
 * @version 1.0
 * @since 2025-07-30
 */
public class Cherrybomb extends Plant{
    /** The cost in sun points to plant a Cherrybomb. */
    private static final int COST = 150;

    /** Cooldown in seconds before another Cherrybomb can be planted. */
    private static final double COOLDOWN = 20;

    /** Timestamp of the last Cherrybomb planted, for cooldown tracking. */
    private static double timeSinceLastPlant = -COOLDOWN;

    /** The damage dealt by the Cherrybomb upon explosion. */
    private final int DAMAGE;


    /**
     * Constructs a new Cherrybomb instance at the specified lane and tile.
     * Initializes its damage, sets its initial state to "exploding", and
     * defines its base properties inherited from {@link Plant}.
     *
     * @param l The lane number where the Cherrybomb is planted.
     * @param t The tile number within the lane where the Cherrybomb is planted.
     */
    public Cherrybomb(int l , int t)
    {
        super("cherrybomb", 0.8, 15, l, t); // name, speed, health, lane, tile
        DAMAGE = 1800;
        state = "exploding"; // Initial state
    }

    /**
     * Triggers the Cherrybomb's explosion, dealing damage to all zombies
     * within a 3x3 radius centered on the Cherrybomb's tile.
     *
     * @param board The game board, used to access surrounding tiles.
     */
    @Override
    public void action(Board board)
    {
        int row;
        int col;

        // Iterate through a 3x3 grid around the Cherrybomb's position
        for(row = LANE_NO - 1; row < LANE_NO + 2; row ++)
        {
            for(col = TILE_NO - 1; col < TILE_NO + 2; col++ )
            {
                // Check if the current row and column are within board boundaries
                if(row >= 0 && row < board.getMaxLanes() && col >= 0 && col < board.getMaxTiles())
                {
                    Tile t = board.getTile(row, col);
                    explode(t);
                }
            }
        }
    }

    /**
     * Applies explosion damage to all zombies on a given tile.
     *
     * @param t The tile whose zombies will take damage.
     */
    public void explode(Tile t)
    {
        for (Zombie zombie : t.getZombies())
        {
            zombie.takeDamage(DAMAGE);
        }
    }

    /**
     * Updates the Cherrybomb's state and triggers its explosion logic.
     * The Cherrybomb explodes once its internal timer (timeSinceLastAttack)
     * reaches its speed threshold. After exploding, its state changes to "exploded"
     * and it begins to "die" by taking damage each update.
     *
     * @param b The game board.
     * @param elapsedTime The time elapsed since the last update.
     */
    @Override
    public void tryToAction(Board b , double elapsedTime)
    {
        updateTime(elapsedTime);
        if(timeSinceLastAttack >= SPEED)
        {
            action(b); // Trigger explosion
            timeSinceLastAttack = 0;
            state = "exploded"; // Change state to exploded
            setSize(3.0); // Adjust size for explosion animation
        }
        if(state.equals("exploded"))
        {
            // Cherrybomb takes damage to represent its short lifespan after exploding
            takeDamage(1); // Health/damage determines how many updates it remains visible
        }
    }

    /**
     * Retrieves the static cooldown period for planting a Cherrybomb.
     * @return The cooldown in seconds.
     */
    public static double getCooldown(){
        return COOLDOWN;
    }

    /**
     * Retrieves the static sun cost for planting a Cherrybomb.
     * @return The sun cost.
     */
    public static int getCost(){
        return COST;
    }

    /**
     * Retrieves the static timestamp of when the last Cherrybomb was planted.
     * Used for cooldown tracking across all Cherrybomb instances.
     * @return The time since the last Cherrybomb was planted.
     */
    public static double getTimeSinceLastPlant(){
        return timeSinceLastPlant;
    }

    /**
     * Sets the static timestamp for when the last Cherrybomb was planted.
     * Used to reset the cooldown timer.
     * @param time The new timestamp.
     */
    public static void setTimeSinceLastPlant(double time){
        timeSinceLastPlant = time;
    }
}
