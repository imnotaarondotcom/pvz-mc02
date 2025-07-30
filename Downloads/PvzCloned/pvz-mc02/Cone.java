/**
 * Represents a Cone armor type, extending the abstract {@link Armor} class.
 * This armor provides additional health to a zombie and changes its visual
 * state based on remaining health.
 *
 * @author Lim, Israel Sy
 * @author Enriquez, Aaron Mikael Cruz
 * @version 1.0
 * @since 2025-07-30
 */
public class Cone extends Armor
{
    /**
     * Constructs a new Cone armor instance.
     * Initializes health, speed buff, and name for the cone armor.
     */
    public Cone()
    {
        super(370, 0, "cone"); // health, speedBuff, name
    }

    /**
     * Updates the state of the Cone armor based on its current health.
     * The state changes from "A" (undamaged) to "B" (damaged) and then to "C" (severely damaged/broken).
     */
    public void updateState()
    {
        if (getHealth() > 240)
        {
            state = "A";
        }
        else if (getHealth() > 110)
        {
            state = "B";
        }
        else
        {
            state = "C";
        }
    }
}
