import java.util.ArrayList;

/**
 * This class defines the behavior and properties of a Sunflower plant in the game.
 * It is responsible for handling its sun production logic, cooldowns, and interactions within the game environment.
 * @author Lim, Israel Sy
 * @author Enriquez, Aaron Mikael Cruz
 * @version 1.0
 * @since 2025-06-27
 */

public class Sunflower extends Plant {
    
    /** Sun cost to plant a Sunflower. */
    private static final int COST = 50;

    /** Cooldown in seconds before another Sunflower can be planted. */
    private static final double COOLDOWN = 7.5;

    /** Timestamp of the last Sunflower planted, for cooldown tracking. */
    private static double timeSinceLastPlant = -7.5;

    /**
     * Constructs a Sunflower.
     * @param l Lane number for placement.
     * @param t Tile number for placement.
     */
    public Sunflower(int l , int t){
        // Calls superclass constructor (Plant's name, speed, health, lane, tile)
        super("Sunflower", 24, 300, l, t);
    }

    /**
     * Executes the Sunflower's action: producing a Sun collectible.
     * @param t Current tile of the plant.
     */
    @Override
    public void action(Tile t){
        Sun temp = new Sun(LANE_NO, TILE_NO, this);
        t.addSun(temp);
    }

    /**
     * Attempts to make the Sunflower produce sun.
     * Produces sun if its internal production cooldown is met.
     * @param t Current tile of the plant.
     * @param elapsedTime Time elapsed since last update.
     * @param tiles All tiles in the plant's lane (unused for Sunflower's action).
     */
    @Override
    public void tryToAction(Tile t, double elapsedTime, Tile[] tiles){
        updateTime(elapsedTime);
        if(timeSinceLastAttack >= SPEED){
            action(t);
            timeSinceLastAttack = 0;
        }
    }

    /**
     * Returns the sun cost of a Sunflower.
     * @return Sunflower's sun cost.
     */
    public static int getCost(){
        return COST;
    }

    /**
     * Returns the planting cooldown for a Sunflower.
     * @return Sunflower's cooldown in seconds.
     */
    public static double getCooldown(){
        return COOLDOWN;
    }
    
    /**
     * Returns the timestamp of the last Sunflower planted.
     * @return Last plant time in seconds.
     */
    public static double getTimeSinceLastPlant(){
        return timeSinceLastPlant;
    }

    /**
     * Sets the timestamp for the last Sunflower planted.
     * @param time Current game time when plant was placed.
     */
    public static void setTimeSinceLastPlant(double time){
        timeSinceLastPlant = time;
    }
}