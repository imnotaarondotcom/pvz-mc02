/**
 * Represents a piece of armor that can be equipped by an entity (e.g., zombies).
 * Armor provides additional health, may affect movement speed, and has a state
 * that can change during gameplay (e.g., breaking, active, destroyed).
 *
 * This class is abstract and intended to be extended by specific armor types
 * (e.g., {@code Conehead}, {@code Buckethead}).
 *
 * @author Lim, Israel Sy
 * @author Enriquez, Aaron Mikael Cruz
 * @version 1.0
 * @since 2025-07-30
 */
public abstract class Armor
{
    /** The current health of the armor. */
    protected int health;

    /** The speed modifier provided by this armor. */
    protected double speedBuff;

    /** The name of the armor (e.g., "cone", "flag"). */
    protected final String NAME;

    /** The current state of the armor. This can change based on gameplay events. */
    protected String state;

    /** A boolean flag indicating whether the armor can take damage. */
    protected boolean isHittable;

    /**
     * Constructs a new Armor instance with specified health, speed bonus, and name.
     * Initializes the armor's state to "A" and sets it as hittable by default.
     *
     * @param health    - The initial health value this armor provides or represents.
     * @param speedBuff - The speed bonus (or penalty) added to the entity wearing this armor.
     * @param name      - The unique name of the armor (e.g., "Cone Armor", "Bucket Armor").
     */
    public Armor(int health, double speedBuff, String name)
    {
        this.health = health;
        this.speedBuff = speedBuff;
        this.NAME = name;
        this.state = "A";         // Default state "A" 
        this.isHittable = true; // Can be damaged by default
    }

    /**
     * Retrieves the name of the armor.
     *
     * @return A {@link String} representing the name of the armor.
     */
    public String getName()
    {
        return NAME;
    }

    /**
     * Retrieves the current state of the armor.
     *
     * @return A {@link String} representing the current state.
     */
    public String getState()
    {
        return state;
    }

    /**
     * Retrieves the remaining health of the armor.
     *
     * @return An {@code int} representing the current health points of the armor.
     */
    public int getHealth()
    {
        return health;
    }

    /**
     * Reduces the armor's health by the specified amount of damage.
     * This method directly modifies the armor's {@code health} attribute.
     * Subclasses may override or call this method and then trigger {@code updateState()}.
     *
     * @param d - The integer amount of damage to subtract from the armor's health.
     */
    public void takeDamage(int d)
    {
        health -= d;
    }

    /**
     * Retrieves the speed bonus (or penalty) provided by the armor.
     * This value affects the movement speed of the entity equipped with this armor.
     *
     * @return A {@code double} representing the speed modifier.
     */
    public double getSpeedBuff()
    {
        return speedBuff;
    }

    /**
     * Checks whether the armor can currently be damaged.
     *
     * @return {@code true} if the armor is currently susceptible to damage; {@code false} otherwise.
     */
    public boolean isHittable()
    {
        return isHittable;
    }

    /**
     * Updates the armor's internal state based on its current health, damage taken,
     * or other relevant game logic.
     */
    public abstract void updateState();
}

