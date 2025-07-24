import java.util.ArrayList;
import java.util.Iterator;

public class Cherrybomb extends Plant{
    private static final int COST = 150;

    /** Cooldown in seconds before another Peashooter can be planted. */
    private static final double COOLDOWN = 20;

    /** Timestamp of the last Peashooter planted, for cooldown tracking. */
    private static double timeSinceLastPlant = -COOLDOWN;

    /** Base damage per projectile. */
    private final int DAMAGE;

    



    

    public Cherrybomb(int l , int t){
        
        super("cherrybomb", 1, 100,l , t );
        DAMAGE = 1000; 
        state = "exploding";
      


    }

    @Override
    public void action(Board board) {
        int row = 0;
        int col = 0;
        

        
        for(row = LANE_NO - 1; row < LANE_NO + 2; row ++){
            for(col = TILE_NO - 1; col < TILE_NO + 2; col++ ){
                if(row >= 0 && row < board.getMaxLanes() && col >= 0 && col < board.getMaxTiles()){
                    Tile t = board.getTile(row, col);
                    explode(t);
                }
            }
        }

       
    }

    public void explode(Tile t){
        Iterator<Zombie> zIterator = t.getZombies().iterator();
        Zombie zombie;
        while(zIterator.hasNext()){
            zombie = zIterator.next();
            zombie.takeDamage(DAMAGE);
        }


    }

    @Override
    public void tryToAction(Board b , double elapsedTime) {
         updateTime(elapsedTime);
            if(timeSinceLastAttack >= SPEED){
                action(b);
                timeSinceLastAttack = 0;
                state = "exploded";
            }
            if(state.equals("exploded")){
                takeDamage(10); // health / damge is number of updates cherry will be alive
            }
    }

    public static double getCooldown(){
        return COOLDOWN;
    }
    
    public static int getCost(){
        return COST;
    }

    public static double getTimeSinceLastPlant(){
        return timeSinceLastPlant;
    }

    public static void setTimeSinceLastPlant(double time){
        timeSinceLastPlant = time;
    }

 


}

