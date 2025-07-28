

/**
 * This class defines the behavior and properties of a Peashooter plant in the game.
 * It is responsible for handling its attack logic, cooldowns, and interactions within the game environment.
 * @author Lim, Israel Sy
 * @author Enriquez, Aaron Mikael Cruz
 * @version 1.0
 * @since 2025-06-27
 */
public class Peashooter extends Plant {

    /** Sun cost to plant a Peashooter. */
    private static final int COST = 100;

    /** Cooldown in seconds before another Peashooter can be planted. */
    private static final double COOLDOWN = 7.5;

    /** Timestamp of the last Peashooter planted, for cooldown tracking. */
    private static double timeSinceLastPlant = -COOLDOWN;

    /** Base damage per projectile. */
    private final int DAMAGE;

    /** Direct hit damage component. */
    private final int DIRECTDAMAGE;

    /** Damage falloff value. */
    private final int DAMAGEFALLOFF;

    /**
     * Constructs a Peashooter.
     * @param l Lane number for placement.
     * @param t Tile number for placement.
     */
    public Peashooter(int l, int t){
        // Calls superclass constructor (Plant's name, speed, health, lane, tile)
        super("peashooter", 1.425, 300, l ,t);
     
        DAMAGE = 25;
        DIRECTDAMAGE = 15;
        DAMAGEFALLOFF = 1;
        updateState("idle");
    }

    @Override
    public void tryToAction(Board board, double elapsedTime)
    {
        // Get tiles of current lane
        Tile[] lane = board.getSpecificLane(LANE_NO);
        
        // Attack if no zombies
        if(!isLaneClear(lane))
        {
            updateTime(elapsedTime);
            if(timeSinceLastAttack >= SPEED)
            {
                action(board);
                timeSinceLastAttack = 0;
            }
        }
    }

    /**
     * Executes the Peashooter's attack action: creates and places a projectile.
     * @param b Current board of plant
     */
    @Override
    public void action(Board b){
        Tile t = b.getTile(LANE_NO, TILE_NO);
        t.placeProjectile(new Projectile(LANE_NO, TILE_NO , DAMAGE, DAMAGEFALLOFF, DIRECTDAMAGE));
        updateState("idle"); // peashooter doesnt have an action animation
    }

    /**
     * Returns the sun cost of a Peashooter.
     * @return Peashooter's sun cost.
     */
    public static int getCost(){
        return COST;
    }

    /**
     * Returns the planting cooldown for a Peashooter.
     * @return Peashooter's cooldown in seconds.
     */
    public static double getCooldown(){
        return COOLDOWN;
    }

    /**
     * Returns the timestamp of the last Peashooter planted.
     * @return Last plant time in seconds.
     */
    public static double getTimeSinceLastPlant(){
        return timeSinceLastPlant;
    }

    /**
     * Sets the timestamp for the last Peashooter planted.
     * @param time Current game time when plant was placed.
     */
    public static void setTimeSinceLastPlant(double time){
        timeSinceLastPlant = time;
    }

}