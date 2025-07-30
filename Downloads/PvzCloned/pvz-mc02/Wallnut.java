import java.util.ArrayList;

/**
 * This class defines the behavior and properties of a Wall-nut plant in the game.
 * Wall-nuts are high-health defensive plants that do not attack but absorb damage.
 * Their visual state changes based on their remaining health.
 *
 * @author Lim, Israel Sy
 * @author Enriquez, Aaron Mikael Cruz
 * @version 1.0
 * @since 2025-06-27
 */
public class Wallnut extends Plant
{
    /** Sun cost to plant a Wall-nut. */
    private static final int COST = 50;
    /** Cooldown in seconds before another Wall-nut can be planted. */
    private static final double COOLDOWN = 30;
    /** Timestamp of the last Wall-nut planted, for cooldown tracking. */
    private static double timeSinceLastPlant = -COOLDOWN; // Initialize to negative cooldown to be ready at start

    /**
     * Constructs a Wall-nut.
     * Initializes its health, sets its initial state, and defines its base properties
     * inherited from {@link Plant}. Wall-nuts have a speed of 0 as they do not perform timed actions.
     *
     * @param l Lane number for placement.
     * @param t Tile number for placement.
     */
    public Wallnut(int l , int t)
    {
        super("wallnut", 0, 1500, l, t); // name, speed (0 for no action), health, lane, tile
        state = "idleA"; // Initial undamaged state
    }

    /**
     * Defines the Wall-nut's primary action.
     * Wall-nuts do not perform any active actions, so this method is empty.
     *
     * @param b The game board (not used for Wall-nut's action).
     */
    @Override
    public void action(Board b)
    {
        // Wall-nuts do not perform active actions
    }

    /**
     * Attempts to make the Wall-nut perform its action.
     * For Wall-nuts, this method primarily updates its visual state based on health.
     *
     * @param b The game board (not directly used for state update, but required by abstract method).
     * @param elapsedTime Time elapsed since last update (not directly used for state update).
     */
    @Override
    public void tryToAction(Board b , double elapsedTime)
    {
        int hp = getHealth();
        if (hp > 1000)
        {
            updateState("idleA"); // Healthy state
        }
        else if (hp > 500 && hp <= 1000)
        {
            updateState("idleB"); // Moderately damaged state
        }
        else if (hp > 0 && hp <= 500)
        {
            updateState("idleC"); // Severely damaged state
        }
        // If hp <= 0, the plant is considered dead and will be removed by the board.
    }

    /**
     * Returns the sun cost of a Wall-nut.
     * @return Wall-nut's sun cost.
     */
    public static int getCost()
    {
        return COST;
    }

    /**
     * Returns the planting cooldown for a Wall-nut.
     * @return Wall-nut's cooldown in seconds.
     */
    public static double getCooldown()
    {
        return COOLDOWN;
    }

    /**
     * Returns the timestamp of the last Wall-nut planted.
     * @return Last plant time in seconds.
     */
    public static double getTimeSinceLastPlant(){
        return timeSinceLastPlant;
    }

    /**
     * Sets the timestamp for the last Wall-nut planted.
     * @param time Current game time when plant was placed.
     */
    public static void setTimeSinceLastPlant(double time)
    {
        timeSinceLastPlant = time;
    }
}
