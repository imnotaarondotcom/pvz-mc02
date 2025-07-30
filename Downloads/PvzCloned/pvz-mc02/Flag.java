/**
 * Represents a Flag armor type, extending the abstract {@link Armor} class.
 * This armor provides a speed buff to a zombie and is not hittable, meaning
 * it cannot be damaged directly.
 *
 * @author Lim, Israel Sy
 * @author Enriquez, Aaron Mikael Cruz
 * @version 1.0
 * @since 2025-07-30
 */
public class Flag extends Armor
{
    /**
     * Constructs a new Flag armor instance.
     * Initializes health (to 0 as it's not hittable), speed buff, and name for the flag armor.
     * Sets {@code isHittable} to {@code false}.
     */
    public Flag()
    {
        super(0, 0.20, "flag"); // health, speedBuff, name
        isHittable = false; // Flag armor cannot be damaged
    }

    /**
     * Overrides the abstract {@code updateState} method from {@link Armor}.
     * This method does nothing for Flag armor, as its state does not change.
     */
    @Override
    public void updateState()
    {
        // Flag armor's state does not change, so this method does nothing.
    }
}
