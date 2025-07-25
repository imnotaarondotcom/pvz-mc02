/**
 * This class represents a projectile fired by plants in the game, such as peas from a Peashooter.
 * It manages the projectile's movement, damage calculation, and interaction with game elements.
 * @author Lim, Israel Sy
 * @author Enriquez, Aaron Mikael Cruz
 * @version 1.0
 * @since 2025-06-27
 */

public class Projectile {
    /** The base damage of the projectile. */
    private final int DAMAGE;

    /** The amount of damage falloff per tile traveled. */
    private final int DAMAGEFALLOFF;

    /** The direct hit damage bonus. */
    private final int DIRECTDAMAGE;

    /** The speed of the projectile in tiles per second. */
    private final double SPEED;

    /** The current position of the projectile within its current tile (0.0 to Tile.getTileLength()). */
    private double position;

    private double positionY;

    /** The current tile index (column) the projectile occupies. */
    private int tileNo;

    /** The current lane index (row) the projectile occupies. */
    private int laneNo;

    /** The number of tiles the projectile has traveled since being fired. */
    private int tilesTravelled;
    
    private String type;
    private String state;

    private double size;

    /**
     * Initializes a new projectile with its properties and starting location.
     * @param l The lane index where the projectile starts.
     * @param t The tile index where the projectile starts.
     * @param d The base damage of the projectile.
     * @param df The amount of damage falloff per tile traveled.
     * @param dd The direct hit damage bonus.
     */
    public Projectile(int l, int t, int d, int df, int dd){
        positionY = 0.25;
        size = 0.25;
        DAMAGE = d;
        position = 0.8; // Projectiles start at the beginning of their tile
        SPEED = 4.5; // Fixed speed of 2 tiles per second
        tileNo = t;
        laneNo = l;
        DAMAGEFALLOFF = df;
        DIRECTDAMAGE = dd;
        tilesTravelled = 0; // Starts with no tiles traveled

        type = "pea";
        state = "idle";
    }

    /**
     * Applies the projectile's calculated damage to a zombie.
     * Damage decreases based on how many tiles the projectile has traveled.
     * @param z The {@link Zombie} to be hit.
     */
    public void hit(Zombie z){
        z.takeDamage(DAMAGE + (DIRECTDAMAGE - DAMAGEFALLOFF * tilesTravelled));
    }

    /**
     * Updates the projectile's position within its current tile.
     * @param timeElapsed The time passed since the last update, in seconds.
     */
    public void updatePosition(double timeElapsed){
        position += timeElapsed * SPEED;
    }

    /**
     * Resets the projectile's position within the tile, typically when it moves to a new tile.
     * @param p The excess position that carried over from the previous tile.
     */
    public void resetPosition(double p){
        position = p;
    }

    /**
     * Updates the projectile's position and determines if it has moved to the next tile.
     * If it crosses a tile boundary, its tile number is incremented and internal position reset.
     * @param timeElapsed The time elapsed since the last update.
     * @return True if the projectile has entered a new tile, false otherwise.
     */
    public boolean isReadyToMove(double timeElapsed){
        updatePosition(timeElapsed); // Advance the projectile's position
        if(position >= Tile.getTileLength()){ // Check if it has moved past the current tile's length
            resetPosition(position % Tile.getTileLength()); // Calculate remaining position in the new tile
            tileNo++; // Move to the next tile
            tilesTravelled++; // Increment the count of tiles traversed
            return true;
        }
        return false;
    }

    public boolean hasHitZombie(Tile t){
        Zombie zombie = t.highestPosition();
        if(getPosition()  >=  zombie.getPosition() - 0.3  ){ 
            hit(zombie);
            return true;
        }
        return false;
    }
    /**
     * Checks if the projectile is currently in the last tile of the game board.
     * @return True if the projectile is in the last tile, false otherwise.
     */
    public boolean inLastTile(){
        return tileNo == PvZDriver.getMaxTiles();
    }

    /**
     * Returns the lane index of the projectile.
     * @return The projectile's lane number.
     */
    public int getLaneNo(){
        return laneNo;
    }

    /**
     * Returns the tile index (column) of the projectile.
     * @return The projectile's tile number.
     */
    public int getTileNo(){
        return tileNo;
    }

    /**
     * Returns the projectile's current position within its current tile.
     * @return The projectile's position as a double.
     */
    public double getPosition(){
        return position;
    }

    /**
     * Returns the base damage value of the projectile.
     * @return The projectile's base damage.
     */
    public int getDAMAGE(){
        return DAMAGE;
    }

    public String getState(){
        return state;
    }

    public String getType(){
        return type;
    }

    public double getSize(){
        return size;
    }

    public double getPositionY(){
        return positionY;
    }
}