import java.util.*;
public abstract class Plant {
    protected double speed; // time it takes to attack
    protected double cooldown; // regenerate rate
    protected double timeSinceLastAttack;
    protected int damage;
    protected int health;
    protected int directDamage;
    protected int cost;
    protected String name;
    protected String type;
    
    protected int tileNo;
    protected int laneNo;
    
    /**
     * 
     * @param l - lane plant will occupy
     * @param t - tile plant will occupy
     */
    public Plant(int l, int t){
        laneNo = l;
        tileNo = t;

    }

    public abstract void action(Tile t, ArrayList<Sun> sun);

    /**
     * updates plants last attack time
     * @param elapsedTime - time since last update
     */
     public void updateTime(double elapsedTime){
        timeSinceLastAttack += elapsedTime;
    }
    
    /**
     * 
     * @param t - tile plant is currently in 
     * @param elapsedTime - time since last update
     * @param sun - array of sun existing in the board (normally used for sunflowers)
     * @param tiles - tiles of the lane the plant is currently in 
     */
    public void tryToAction(Tile t, double elapsedTime, ArrayList<Sun> sun, Tile[] tiles){
        if(this instanceof Peashooter){
            if(!(isLaneClear(tiles))){
                updateTime(elapsedTime);
                if(timeSinceLastAttack >= speed){
                    //GameClock.printTime();
                   // System.out.printf("%s has attacked at lane no %d tile no %d\n",name, laneNo + 1, tileNo + 1 );
                    action(t, sun);
                    timeSinceLastAttack = 0;
                }
            }
                
        }
        else
        if(this instanceof Sunflower){
            updateTime(elapsedTime);
                if(timeSinceLastAttack >= speed){
                    GameClock.printTime();
                    System.out.printf("%s has spawned sun at lane no %d tile no %d\n",name, laneNo + 1, tileNo + 1 );
                    action(t, sun);
                    timeSinceLastAttack = 0;
                }
        }
        
    }

    /**
     * allows the plant to take damage
     * @param damage - damage the plant will take
     */
    public void takeDamage(int damage){
        health -= damage;
    }

    /**
     * checks if the lane plant is in has no zombies
     * @param tile - tiles of the lane the plant is currently in
     * @return - return true if there are no zombies in the lane and false otherwise
     */

    public boolean isLaneClear(Tile[] tile){
        int t; // tile No

        for(t = 0; t < PvZDriver.getMaxTiles(); t++ ){
            if(!(tile[t].noZombies() == 0)){ // if tile is not empty
                return false;
            }   
        }

        return true;
    }

    public String getName(){
        return name;
    }

    public int getHealth(){
        return health;
    }

    public int getLaneNo(){
        return laneNo;
    }
    public int getTileNo(){
        return tileNo;
    }
        
    

    

}
