import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Represents a Zombie in the game, defining its movement, attack behavior, and health.
 * Zombies advance towards the player's house and attack plants in their path.
 * @author Lim, Israel Sy
 * @author Enriquez, Aaron Mikael Cruz
 * @version 1.0
 * @since 2025-06-27
 */
public class Zombie
{
    /** The speed at which the zombie attacks (time between attacks in seconds). */
    private final double ATTACK_SPEED;

    /** The speed at which the zombie moves across a tile. */
    private double speed;

    /** The damage inflicted by the zombie per attack. */
    private final int DAMAGE;

    /** Tracks the time elapsed since the zombie's last attack. */
    private double timeSinceLastAttack;

    /** The current health of the zombie. */
    private int health;

    /** The precise fractional position of the zombie within its current tile (0 to Tile.getTileLength()). */
    private double position;

    /** The current tile index (column) the zombie is occupying. */
    private int tileNo;

    /** The current lane index (row) the zombie is occupying. */
    private int laneNo;
    /** The type of zombie (e.g., "zombie", "zombiecone"). */
    private String type;

    /** The current state the zombie is in (e.g., "walkA", "eatB"). */
    private String state;

    /** The visual size or scale of the zombie. */
    private double size;

    /** The armor currently equipped by the zombie, or null if none. */
    private Armor armor;

    /**
     * Constructs a new Zombie object.
     * Initializes the zombie's stats, health, and starting position.
     * Equips the provided armor if any.
     *
     * @param laneNo The lane index (row) the zombie will occupy.
     * @param tileNo The tile index (column) the zombie will occupy.
     * @param armor The {@link Armor} object to equip, or null for no armor.
     */
    public Zombie(int laneNo, int tileNo, Armor armor)
    {
        size = 1;
        this.tileNo = tileNo;
        this.laneNo = laneNo;
        this.armor = armor;
        type = "zombie";
        speed = 0.2; // seconds to pass a tile
        equipArmor(); // Apply armor effects to zombie stats

        ATTACK_SPEED = 1;
        DAMAGE = 100;
        timeSinceLastAttack = 0;
        health = 270;
        position = 1; // Start at the right edge of the tile

        state = "walkA"; // Initial state: full zombie walking
    }

    /**
     * Equips armor to the zombie, modifying its type and speed based on the armor's properties.
     */
    public void equipArmor()
    {
        if(armor != null)
        {
            type = type.concat(armor.getName()); // Update type for visual representation (e.g., "zombiecone")
            speed += armor.getSpeedBuff(); // Apply speed buff from armor
        }
    }

    /**
     * Moves the zombie to the previous tile (towards the player's house).
     * Carries over any fractional position to maintain smooth movement.
     */
    public void move()
    {
        this.resetPosition( Tile.getTileLength() - position); // Carry over fractional position to next tile
        tileNo = tileNo - 1; // Move to the previous tile (towards the house)
    }

    /**
     * Checks if the zombie is ready to move to the next tile based on its speed and elapsed time.
     * Updates the zombie's position.
     * @param elapsedTime The time elapsed since the last update.
     * @return {@code true} if the zombie has crossed a tile boundary and is ready to move to the next tile; {@code false} otherwise.
     */
    public boolean isReadyToMove(double elapsedTime)
    {
        this.updatePosition(elapsedTime);
        return position <= 0; // Zombie moves to the next tile when its position is <= 0
    }

    /**
     * Checks if the zombie is ready to perform an attack based on its attack speed and elapsed time.
     * Updates the internal attack cooldown timer.
     * @param timeElapsed The time elapsed since the last update.
     * @return {@code true} if the zombie's attack cooldown has finished; {@code false} otherwise.
     */
    public boolean isReadyToAttack(double timeElapsed)
    {
        updateAttackCooldown(timeElapsed);
        if(timeSinceLastAttack >= ATTACK_SPEED)
        {
            resetAttackCooldown(timeSinceLastAttack - ATTACK_SPEED); // Keep any excess time for next cooldown
            return true;
        }
        return false;
    }

    /**
     * Checks if the zombie is still alive (health is greater than or equal to 0).
     * @return {@code true} if the zombie is alive; {@code false} otherwise.
     */
    public boolean isAlive()
    {
        return health >= 0;
    }

    /**
     * Makes the zombie attack a given plant, inflicting damage.
     * Prints a message to the console detailing the attack.
     * @param p The {@link Plant} to be attacked.
     */
    public void attack(Plant p)
    {
        p.takeDamage(DAMAGE);
        GameClock.printTime();
        System.out.printf("Zombie attacked %s at lane %d tile %d. Updated health: %d\n",
                              p.getName(), (laneNo + 1), (tileNo + 1) , p.getHealth());
    }

    /**
     * Applies damage to the zombie. If the zombie has hittable armor, damage is applied to the armor first.
     * If armor breaks, its remaining damage carries over to the zombie's health.
     * Updates the zombie's state after taking damage.
     *
     * @param damage The amount of damage to inflict.
     */
    public void takeDamage(int damage)
    {
        if (armor != null && armor.isHittable())
        {
            armor.takeDamage(damage);
            armor.updateState(); // Update armor's visual state

            if(armor.getHealth() <= 0)
            {
                type = "zombie"; // revert to normal zombie sprites after losing armor
                health += armor.getHealth(); // takes the extra damage if armor breaks (armor.getHealth() will be negative)
                armor = null; // Remove the armor
            }
        }
        else
        {
            health -= damage; // Apply damage directly to zombie if no armor or armor is not hittable
        }

        GameClock.printTime();
        System.out.printf("Zombie at lane %d tile %d hit. Updated health: %d\n",
                              (laneNo + 1), (tileNo), health);
    }

    /**
     * Updates the visual state of the zombie based on its current health and armor status.
     * The state determines which animation frames are displayed.
     *
     * @param stateType The base state type (e.g., "walk", "eat").
     */
    public void updateState(String stateType)
    {
        if (armor == null || !armor.isHittable()) // If no armor or armor is not hittable (like flag)
        {
            if (health > 180)
            {
                state = stateType.concat("A"); // full zombie body
            }
            else if (health > 90 && health <= 180)
            {
                state = stateType.concat("B"); // zombie lost an arm
            }
            else if (health <= 90) // zombies with non-hittable armor (like flag) will lose their armor sprite
            {
                type = "zombie"; // Ensure type is "zombie" for head-loss animation
                state = stateType.concat("C"); // zombie lost head
            }
        }
        else // Zombie has hittable armor
        {
            state = stateType.concat(armor.getState()); // State is determined by armor's state (e.g., "walkA", "walkB", "walkC" for cone)
        }
    }

    /**
     * Updates the zombie's position based on its speed and elapsed time.
     * This method continuously moves the zombie within its current tile.
     * @param elapsedTime The time elapsed since the last update.
     */
    public void updatePosition(double elapsedTime){
        position -= speed * elapsedTime;
    }

    /**
     * Updates the internal cooldown timer for the zombie's attacks.
     * @param timeElapsed The time elapsed since the last update.
     */
    public void updateAttackCooldown(double timeElapsed){
        timeSinceLastAttack += timeElapsed;
    }

    /**
     * Resets the zombie's attack cooldown, allowing for precise control over attack timing.
     * @param cd The new cooldown value to set.
     */
    public void resetAttackCooldown(double cd)
    {
        timeSinceLastAttack = cd;
    }

    /**
     * Resets the zombie's position within its current tile.
     * @param p The new position value.
     */
    public void resetPosition(double p)
    {
        position = p;
    }

    /**
     * Returns the current fractional position of the zombie within its tile.
     * @return The zombie's position (0.0 to Tile.getTileLength()).
     */
    public double getPosition()
    {
        return position;
    }

    /**
     * Returns the current movement speed of the zombie.
     * @return The zombie's speed.
     */
    public double getSpeed()
    {
        return speed;
    }

    /**
     * Returns the current health of the zombie.
     * @return The zombie's health.
     */
    public int getHealth()
    {
        return health;
    }

    /**
     * Returns the current tile index (column) the zombie is occupying.
     * @return The zombie's tile number.
     */
    public int getTileNo()
    {
        return tileNo;
    }

    /**
     * Returns the current lane index (row) the zombie is occupying.
     * @return The zombie's lane number.
     */
    public int getLaneNo()
    {
        return laneNo;
    }

    /**
     * Checks if the zombie is currently in the first tile of the game board (closest to the house).
     * @return {@code true} if the zombie is in the first tile; {@code false} otherwise.
     */
    public boolean inLastTile()
    {
        return tileNo == 0 ;
    }

    /**
     * Returns the current state of the zombie.
     * @return The zombie's state string.
     */
    public String getState()
    {
        return state;
    }

    /**
     * Returns the type of the zombie.
     * @return The zombie's type string.
     */
    public String getType()
    {
        return type;
    }

    /**
     * Returns the visual size or scale of the zombie.
     * @return The zombie's size.
     */
    public double getSize()
    {
        return size;
    }

    /**
     * Provides a string representation of the zombie's current stats (health, speed, damage).
     * @return A formatted string with zombie statistics.
     */
    @Override
    public String toString()
    {
        return String.format("       Health : %d Speed : %.2f Damage : %d", this.health, this.speed, this.DAMAGE );
    }
}
