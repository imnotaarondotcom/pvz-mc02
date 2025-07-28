import java.util.ArrayList;

public class Tile {
   
    

    /**
     * creates a tile and initializes max number of projectiles, intializes the sun array, and zombies it can contain
     * and intializes its tile number
     * @param lane - lane Number of created tile
     * @param tile - tile number for the created tile
     */
    public Tile(int lane , int tile){
        zombies = new Zombie[MAX_ZOMBIES];
        sun = new ArrayList<Sun>();
 
        projectiles = new ArrayList<Projectile>();
        TILE_NO = tile ;
        LANE_NO = lane ;
    }
    /**
     * spawns a zombie if tile is the farthest tile from the players house
     */
    public void spawnZombie(int laneNo, int tileNo){
        if(TILE_NO == PvZDriver.getMaxTiles() - 1){
        Zombie z = new Zombie(laneNo, tileNo);
        PvZDriver.printBorder();
        GameClock.printTime();
        placeZombie(z);
        System.out.println("Zombie Has Been Spawned at Lane No " + (LANE_NO + 1) + " Tile No " + (TILE_NO + 1) );
        PvZDriver.printBorder();
        System.out.println(z.toString());
        }
        
        else 
         System.out.println("Failed to spawn " );
    }




    /**
     * places a plant if the tile is not yet occupied by a plant
     * @param p - plant to be placed
     * @return - returns true if a plant is successfully placed false otherwise
     */

    public boolean placePlant(Plant p){
        if(placedPlant == null){
            System.out.printf("%s placed at lane %d tile %d\n",p.getName(), (LANE_NO + 1),(TILE_NO + 1));
            placedPlant = p;
            return true;
        }
        return false;
    }

    /**
     * places a zombie in a tile used for moving zombies
     * @param z - zombie to be placed
     */
    
    public void placeZombie(Zombie z){
        int i = 0;
        for(i = 0; i < zombies.length ; i ++){
            if(zombies[i] == null){
                zombies[i] = z;
                
                break;
            }
        }
    }
    
  

    /**
     * places a projectile in the current tile
     * @param p - projectile to be placed
     */

    public void placeProjectile(Projectile p){
        projectiles.add(p);
    }

  


    /**
     * removes a zombie form the current tile
     * @param z - zombie to be removed
     */

    public void removeZombie(Zombie z){
        int i = 0;
        for(i = 0; i < MAX_ZOMBIES ; i ++){
            if(zombies[i] == z){
                zombies[i] = null;
            }
        }
    }

    /**
     * removes the plant inside the current tile
     */

    public void removePlant(){
        placedPlant = null;
        System.out.printf("Plant removed at lane %d tile %d\n", LANE_NO + 1, TILE_NO + 1);
    }

    /**
     * returns the no of zombies in the current tile
     * @return - no of zombies in the tile
     */

    public int noZombies(){
        int i = 0;
        int count = 0;
        for(i = 0; i < MAX_ZOMBIES ; i ++){
            if(zombies[i] != null)
            count++;
            
        }

        return count;
    }

    /**
     * returns the zombie with the highest position in the tile
     * @return - zombie with highest position
     */

    public Zombie highestPosition(){
        int i = 0;
        int highest = -1;

        for(i = 0; i < MAX_ZOMBIES; i++){
            if(zombies[i] != null){
                highest = i;
                break;
            }
        }

        for(i = 0; i < MAX_ZOMBIES; i++){
            if(zombies[i] != null && zombies[highest] != null)
            if(zombies[i].getPosition() > zombies[highest].getPosition()){
                highest = i;
            }
        }

        if(highest == -1){
          return null;
        }
      

        return zombies[highest];
    }

    
    /**
     * returns array of zombies in the tile
     * @return - zombies inside the tile
     */
    public Zombie[] getZombies(){
        return zombies;
    }

    /**
     * return a list of projectiels inside the tile
     * @return - Array list of projectiles in the tile
     */
    public ArrayList<Projectile> getProjectiles(){
        return projectiles;
        
    }

    public void addSun(Sun sun){
        this.sun.add(sun);
    }

    
    /**
     * checks if tile has zombies occupying it
     * @return - true if there is at least one zombie
     */
    public boolean hasZombie(){
        return noZombies() != 0;
    }

   
    /**
     * returns the plant occupying the tile
     * @return - returns the current plant occupying the tile returns null if no plant is occupying
     */
    public Plant getPlant(){
        return placedPlant;
    } 

    public static int getMaxZombies(){
        return MAX_ZOMBIES;
    }

    public static int getMaxProjectiles(){
        return MAX_PROJECTILES;
    }

    public static int getTileLenght(){
        return TILE_LENGTH;
    }

    public ArrayList<Sun> getSunList(){
        return sun;
    }

    public int getLaneNo(){
        return LANE_NO;
    }
    public int getTileNo(){
        return TILE_NO;
    }

    

    private final int LANE_NO;
    private final int TILE_NO;
    private final static int TILE_LENGTH = 20;
    private Plant placedPlant;
    private Zombie[] zombies;
    private ArrayList<Projectile> projectiles;
    private final static int MAX_ZOMBIES = 10;
    private final static int MAX_PROJECTILES = 10;
    private ArrayList<Sun> sun;

}
