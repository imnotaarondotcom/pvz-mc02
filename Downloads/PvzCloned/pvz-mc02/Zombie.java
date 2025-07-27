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

public class Zombie {
    /** The speed at which the zombie attacks (time between attacks in seconds). */
    private final double ATTACK_SPEED;

    /** The speed at which the zombie moves across a tile (units per second). */
    private  double SPEED;

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
    // type of zombie

    private String type;

    // state the zombie is in (Attacking , Moving)
    private String state;

    private double size;




    private Armor armor;

    

    /**
     * Constructs a new Zombie object.
     * Initializes the zombie's stats, health, and starting position.
     * @param laneNo The lane index (row) the zombie will occupy.
     * @param tileNo The tile index (column) the zombie will occupy.
     */
    public Zombie(int laneNo, int tileNo, Armor armor)
    {
        size = 1;
        this.tileNo = tileNo ;
        this.laneNo = laneNo ;
        this.armor = armor;
        ATTACK_SPEED = 1;
        SPEED = 0.2; // tiles passes per second
        DAMAGE = 100;
        timeSinceLastAttack = 0;
        health = 270;
        position = 1;

      //  this.modifier = modifier;
        type = "zombie";
        equipArmor();
        

        state = "walkA"; // walkA = zombie w/ full body, walkB = zombie w/ lost leg, etc.
    }

    /**
     * Moves the zombie to the next tile by decrementing its tile number.
     * Resets its internal position within the new tile based on any overflow.
     */
    public void move()
    {
        updateState("walk");
        this.resetPosition( Tile.getTileLength() - position); // Carry over fractional position to next tile
        tileNo = tileNo - 1; // Move to the previous tile (towards the house)
    }

    /**
     * Updates the zombie's position and checks if it's ready to move to the next tile.
     * A zombie is ready to move if its position within the current tile exceeds the tile's length.
     * @param elapsedTime The time elapsed since the last update in seconds.
     * @return True if the zombie has traversed the current tile and is ready to move to the next, false otherwise.
     */
    public boolean isReadyToMove(double elapsedTime)
    {
        updateState("walk");
        this.updatePosition(elapsedTime);
        return position <= 0;
    }
    

    /**
     * Updates the zombie's attack cooldown and checks if it's ready to attack.
     * A zombie is ready to attack if enough time has passed since its last attack,
     * based on its {@code ATTACK_SPEED}.
     * @param timeElapsed The time elapsed since the last update in seconds.
     * @return True if the zombie's attack cooldown has reset and it can perform an attack, false otherwise.
     */
    public boolean isReadyToAttack(double timeElapsed)
    {
        updateState("eat");
        updateAttackCooldown(timeElapsed);
        if(timeSinceLastAttack > ATTACK_SPEED ){
            resetAttackCooldown(timeSinceLastAttack % ATTACK_SPEED); // Keep any excess time for next cooldown
            return true;
        }
        return false;
    }

    public boolean isAlive(){
        return health >= 0;
    }
    
    /**
     * Makes the zombie attack a given plant, inflicting damage.
     * @param p The {@link Plant} to be attacked.
     */
    public void attack(Plant p){
        p.takeDamage(DAMAGE);
        GameClock.printTime();
        System.out.printf("Zombie attacked %s at lane %d tile %d. Updated health: %d\n",
                          p.getName(), (laneNo + 1), (tileNo + 1) , p.getHealth());
    }

    /**
     * Applies damage to the zombie, reducing its health.
     * @param damage The amount of damage to inflict.
     */
    public void takeDamage(int damage)
    {
        if (armor != null && armor.isHittable())
        {
            armor.takeDamage(damage);
            armor.updateState();
            if(armor.getHealth() <= 0){
                health += armor.getHealth(); // takes the extra damage if armor breaks 
                armor = null;
                
            }
        }
        else
        {
            health -= damage;
        }

        

        GameClock.printTime();
        System.out.printf("Zombie at lane %d tile %d hit. Updated health: %d\n",
                          (laneNo + 1), (tileNo), health);

    }
    
    // Reverts a special zombie to normal zombie if theyve taken enough damage
    // (E.g., Cone zombie loses its cone, flag zombie loses its flag)
    public void updateModifier()
    {
        // Conehead loses its cone
     
    }

    // Updates the state of the zombie
    public void updateState(String stateType)
    {
        // if walking
 
        if(armor == null || !armor.isHittable() ){

            if(armor == null){  // armor like flag is not removable
                type = "zombie";
            }
            
            if (health > 180) 
            {
                state = stateType.concat("A"); // full zombie
            }
            else if (health > 30 && health <= 180)
            {
                state = stateType.concat("B"); // lost arm
            }
            else 
            {
                state = stateType.concat("C"); // lost head
                armor = null;
            }
        }
        else{
            state = stateType.concat(armor.getState());
        }
        
    }


    public void equipArmor(){
        if(armor != null){
            type = type.concat(armor.getName());
            state = "walkA";
            SPEED += armor.getSpeedBuff();
        }
        
    }

    /**
     * Updates the zombie's fractional position within its current tile based on its speed.
     * @param elapsedTime The time elapsed since the last update in seconds.
     */
    public void updatePosition(double elapsedTime){
        position -= SPEED * elapsedTime;
    }

    /**
     * Updates the internal timer that tracks time since the last attack.
     * @param timeElapsed The time elapsed since the last update in seconds.
     */
    public void updateAttackCooldown(double timeElapsed){
        timeSinceLastAttack += timeElapsed;
    }

    /**
     * Resets the zombie's attack cooldown timer.
     * Any excess time from the last update is carried over to the new cooldown cycle.
     * @param cd The excess time (remainder after dividing by {@code ATTACK_SPEED}).
     */
    public void resetAttackCooldown(double cd){
        timeSinceLastAttack = cd;
    }

    /**
     * Resets the zombie's internal position within a tile.
     * Used when the zombie moves from one tile to another to set its starting position in the new tile.
     * @param p The new position within the tile (typically the remainder after moving past a tile).
     */
    public void resetPosition(double p){
        position = p;
    }

    /**
     * Returns the zombie's current fractional position within its tile.
     * @return The zombie's position.
     */
    public double getPosition(){
        return position;
    }

    /**
     * Returns the zombie's movement speed.
     * @return The zombie's speed.
     */
    public double getSpeed(){
        return SPEED;
    }

    /**
     * Returns the zombie's current health.
     * @return The zombie's health.
     */
    public int getHealth(){
        return health;
    }

    /**
     * Returns the tile index (column) the zombie is currently occupying.
     * @return The zombie's current tile number.
     */
    public int getTileNo(){
        return tileNo;
    }

    /**
     * Returns the lane index (row) the zombie is currently occupying.
     * @return The zombie's current lane number.
     */
    public int getLaneNo(){
        return laneNo;
    }
    
    /**
     * Checks if the zombie has reached the last tile of the lane (tile 0),
     * which typically signifies a game over condition.
     * @return True if the zombie is in the last tile, false otherwise.
     */
    public boolean inLastTile(){
        return tileNo == 0 ;
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

    /**
     * Provides a string representation of the zombie's current stats.
     * @return A formatted string showing health, speed, and damage.
     */
    @Override
    public String toString(){
        return String.format("       Health : %d Speed : %.2f Damage : %d", this.health, this.SPEED, this.DAMAGE );
    }


}