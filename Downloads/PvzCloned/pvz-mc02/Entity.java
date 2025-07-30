import java.awt.image.BufferedImage;

/**
 * Represents a game entity with position, size, type, and state.
 * This may include characters, projectiles, or other game objects
 * that are placed on a tile-lane based grid system.
 *
 * Each entity has a horizontal (X) and optional vertical (Y) position to
 * allow for effects like floating or offset visuals (e.g., projectiles).
 *
 * @author Enriquez, Aaron Mikael Cruz
 * @version 1.0
 * @since 2025-07-30
 */
public class Entity
{
    /** The tile number the entity is currently on. */
    private int tileNo;
    /** The lane number the entity is currently in. */
    private int laneNo;
    /** The horizontal (X) position offset within its tile. */
    private double positionX;
    /** The visual size or scale of the entity. */
    private double size;
    /** The vertical (Y) position offset above its tile. */
    private double positionY;
    /** The type of the entity (e.g., "Zombie", "Plant", "Sun"). */
    private String type;
    /** The current state of the entity (e.g., "Idle", "Attacking", "Exploding"). */
    private String state;

    /**
     * Constructs a basic entity with no vertical offset (positionY = 0).
     *
     * @param type      - The type of the entity.
     * @param state     - The current state.
     * @param laneNo    - The lane number the entity occupies.
     * @param tileNo    - The tile number the entity occupies.
     * @param positionX - The X-position (normalized within tile).
     * @param size      - The visual size or hitbox scale.
     */
    public Entity(String type, String state, int laneNo, int tileNo, double positionX, double size)
    {
        this.positionY = 0;
        this.type = type;
        this.state = state;
        this.tileNo = tileNo;
        this.laneNo = laneNo;
        this.positionX = positionX;
        this.size = size;
    }

    /**
     * Constructs an entity with a specified vertical offset (e.g., floating projectile).
     *
     * @param type      - The type of the entity.
     * @param state     - The current state of the entity.
     * @param laneNo    - The lane number the entity occupies.
     * @param tileNo    - The tile number the entity occupies.
     * @param positionX - The X-position of the entity.
     * @param size      - The visual size of the entity.
     * @param positionY - The height offset above the tile.
     */
    public Entity(String type, String state, int laneNo, int tileNo, double positionX, double size, double positionY)
    {
        this(type, state, laneNo, tileNo, positionX, size);
        this.positionY = positionY;
    }

    /**
     * Retrieves the tile number the entity is on.
     * @return The tile number.
     */
    public int getTileNo()
    {
        return tileNo;
    }

    /**
     * Retrieves the lane number the entity is in.
     * @return The lane number.
     */
    public int getLaneNo()
    {
        return laneNo;
    }

    /**
     * Retrieves the horizontal (X) position offset of the entity within its tile.
     * @return The X-position.
     */
    public double getPositionX()
    {
        return positionX;
    }

    /**
     * Retrieves the current state of the entity.
     * @return The current state.
     */
    public String getState()
    {
        return state;
    }

    /**
     * Retrieves the type of the entity.
     * @return The entity type.
     */
    public String getType()
    {
        return type;
    }

    /**
     * Retrieves the size or scale of the entity.
     * @return The size.
     */
    public double getSize()
    {
        return size;
    }

    /**
     * Retrieves the vertical (Y) offset of the entity.
     * @return The Y-offset.
     */
    public double getPositionY()
    {
        return positionY;
    }
}
