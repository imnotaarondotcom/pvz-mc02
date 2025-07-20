import java.util.ArrayList;
import java.util.Random;
import java.util.Iterator;
public class Board {

    public Board(int l, int t){
        MAX_LANES = l;
        MAX_TILES = t;
        lane = new Tile[MAX_LANES][MAX_TILES];
        initialize();
        tilePicker = new Random();
        
    }

    public void initialize(){
        int i = 0;
        int tile = 0 ;
        for(i = 0 ; i < MAX_LANES; i++){
            lane[i] = new Tile[MAX_TILES];

            for(tile = 0; tile < MAX_TILES; tile++){
                lane[i][tile] = new Tile(i, tile);
            }
        }
    }

    public boolean updateBoard(double timeElapsed){
        updatePlants(timeElapsed);
        updateProjectiles(timeElapsed);
        if(updateZombies(timeElapsed)){
            return true;
        }
        return false;
    }

    /**
     * Attempts to spawn a zombie based on game time and difficulty.
     * Spawns zombies at the last tile of a randomly chosen lane.
     * @param gameTime The current elapsed game time in seconds.
     * @param lastZombieSpawnTime The game time when a zombie was last spawned.
     * @param lane The 2D array of Tiles representing the game lanes.
     * @return True if a zombie was spawned, false otherwise.
     */
    public boolean tryToSpawn(int gameTime,int lastZombieSpawnTime ){
        int i;
        int laneNo;
        
        
        laneNo = tilePicker.nextInt(PvZDriver.getMaxLanes()); // Randomly pick a lane for the zombie

        if(gameTime >= 10 && gameTime <= 80){
            if(gameTime % 10 == 0 && lastZombieSpawnTime != gameTime){
                lane[laneNo][MAX_TILES - 1].spawnZombie(laneNo, MAX_TILES - 1);
                return true;
            }
        } else if(gameTime >= 81 && gameTime <= 140 ){
            if(gameTime % 5 == 0 && lastZombieSpawnTime != gameTime){
                lane[laneNo][MAX_TILES - 1].spawnZombie(laneNo, MAX_TILES - 1 );
                return true;
            }
        } else if(gameTime > 140 && gameTime <= 170){
            if(gameTime % 3 == 0 && lastZombieSpawnTime != gameTime){
                lane[laneNo][MAX_TILES - 1].spawnZombie(laneNo,MAX_TILES - 1 );
                return true;
            }
        } else if(gameTime > 170 && lastZombieSpawnTime <= 170 ){
            for(i = 0; i < 5 + (PvZDriver.getLevel() - 1) * 2; i++){
                laneNo = tilePicker.nextInt(PvZDriver.getMaxLanes());
                lane[laneNo][MAX_TILES - 1].spawnZombie(laneNo,MAX_TILES - 1 );
            }
            return true;
        }

        return false;
    }
    
     /**
     * Attempts to spawn sun collectibles on random tiles at regular intervals.
     * @param gameTime The current elapsed game time in seconds.
     * @param lastSunSpawnTime The game time when sun was last spawned.
     * @param lane The 2D array of Tiles representing the game lanes.
     * @return True if sun was spawned, false otherwise.
     */
    public boolean tryToSpawnSun(int gameTime, int lastSunSpawnTime ){
        int tileNo;
        int laneNo;
    
        tileNo = tilePicker.nextInt(PvZDriver.getMaxTiles());
        laneNo = tilePicker.nextInt(PvZDriver.getMaxLanes());

        if(gameTime % 8 == 0 && lastSunSpawnTime != gameTime){
            Sun newSun = new Sun(laneNo, tileNo);
            lane[laneNo][tileNo].addSun(newSun);
            return true;
        }
        return false;
    }


    public void updatePlants(double timeElapsed){
        int laneNo = 0;
        int tileNo = 0;
        Tile currentTile;
        Plant plant;
        for(laneNo = 0; laneNo < PvZDriver.getMaxLanes(); laneNo++){
            for(tileNo = 0; tileNo < PvZDriver.getMaxTiles(); tileNo++){
                plant = lane[laneNo][tileNo].getPlant();
                currentTile = lane[laneNo][tileNo];
                if(plant != null ){
                    plant.tryToAction(lane[laneNo][tileNo], timeElapsed, lane[laneNo]);
                    if(plant.getHealth() <= 0){
                        currentTile.removePlant();
                        GameClock.printTime();
                        System.out.printf("Plant at lane %d tile %d died\n", plant.getLaneNo() + 1, plant.getTileNo() + 1);
                    }
                }
                 
            }
        } 
    }

 /*unedited   public void updateProjectiles(double timeElapsed){

        int laneNo = 0;
        int tileNo = 0;
        Tile currentTile;
        Iterator<Projectile> iterator;
        ArrayList<Projectile> projectiles;
        Projectile projectile;
        Zombie zombie;

        for(laneNo = 0; laneNo < PvZDriver.getMaxLanes(); laneNo++){
            for(tileNo = 0; tileNo < PvZDriver.getMaxTiles(); tileNo++){
                currentTile = lane[laneNo][tileNo];  
                
                projectiles = lane[laneNo][tileNo].getProjectiles();
                iterator = projectiles.iterator();
                
                while(iterator.hasNext()){
                    projectile = iterator.next();
                    
                    if(currentTile.hasZombie()){
                        zombie = currentTile.highestPosition();
                        if(projectile.getPosition() + Tile.getTileLength() - zombie.getPosition() >= Tile.getTileLength()){
                            projectile.hit(zombie);
                            iterator.remove();
                            if(zombie.getHealth() <= 0){
                                currentTile.removeZombie(zombie);
                                GameClock.printTime();
                                System.out.printf("Zombie died at lane %d tile %d\n", zombie.getLaneNo() + 1, zombie.getTileNo() + 1);
                            }
                        } else {
                            projectile.updatePosition(timeElapsed);
                        }
                    } else {
                        if(projectile.isReadyToMove(timeElapsed)){
                            if(projectile.getTileNo() < MAX_TILES){
                                iterator.remove();
                                lane[laneNo][projectile.getTileNo()].placeProjectile(projectile);
                            } else {
                                iterator.remove();
                            }
                        } else {
                            projectile.updatePosition(timeElapsed);
                        }
                    }
                }
            }
        }
    }
*/ 
    public void updateProjectiles(double timeElapsed){

        int laneNo = 0;
        int tileNo = 0;
        Tile currentTile;
        Iterator<Projectile> iterator;
        ArrayList<Projectile> projectiles;
        Projectile projectile;
        Zombie zombie;

        for(laneNo = 0; laneNo < PvZDriver.getMaxLanes(); laneNo++){
            for(tileNo = 0; tileNo < PvZDriver.getMaxTiles(); tileNo++){
                currentTile = lane[laneNo][tileNo];  
                
                projectiles = lane[laneNo][tileNo].getProjectiles();
                iterator = projectiles.iterator();
                
                while(iterator.hasNext()){
                    projectile = iterator.next();
                    
                    if(currentTile.hasZombie()){
                        if(projectile.hasHitZombie(currentTile)){
                        
                            iterator.remove();

                       /*      if(zombie.getHealth() <= 0){    /* move zombie removal to zombie updater  
                                currentTile.removeZombie(zombie);
                                GameClock.printTime();
                                System.out.printf("Zombie died at lane %d tile %d\n", zombie.getLaneNo() + 1, zombie.getTileNo() + 1);
                            }  until here */

                        } 
                        else {
                            projectile.updatePosition(timeElapsed);
                        }
                    } 
                    else {
                        if(projectile.isReadyToMove(timeElapsed)){
                            if(projectile.getTileNo() < MAX_TILES){
                                iterator.remove();
                                lane[laneNo][projectile.getTileNo()].placeProjectile(projectile);
                            } 
                            else {
                                iterator.remove();
                            }
                        } 
                        else {
                            projectile.updatePosition(timeElapsed);
                        }
                    }
                }
            }
        }
    }

  
// to do remove zombies if its dead 


    /**
     * returns true if zombies have entered the house
     *
     */
     /*
    public boolean updateZombies(double timeElapsed){

        int laneNo = 0;
        int tileNo = 0;
        ArrayList<Zombie> zombies;
        Zombie zombie;
        Plant plant;
        Tile currentTile; 
         for(laneNo = 0; laneNo < PvZDriver.getMaxLanes(); laneNo++){
            for(tileNo = 0; tileNo < PvZDriver.getMaxTiles(); tileNo++){
                zombies = lane[laneNo][tileNo].getZombies();
                plant = lane[laneNo][tileNo].getPlant();    
                currentTile = lane[laneNo][tileNo];          
                
                // Use an iterator to safely remove zombies from the list if they move
                Iterator<Zombie> zombieIterator = zombies.iterator();

                while(zombieIterator.hasNext()){
                    zombie = zombieIterator.next();
                    if(plant != null){ // If a plant exists in current tile, zombie tries to attack it
                        if(zombie.isReadyToAttack(timeElapsed)){
                            zombie.attack(plant);
                            if(plant.getHealth() <= 0){
                                currentTile.removePlant();
                                GameClock.printTime();
                                System.out.printf("Plant at lane %d tile %d died\n", plant.getLaneNo() + 1, plant.getTileNo() + 1);
                            }
                        }
                    } else { // If no plant, then update position of the zombie
                        if(zombie.isReadyToMove(timeElapsed)){
                            if(zombie.inLastTile()){
                                GameClock.printTime();
                                System.out.printf("Zombies at lane %d tile %d entered house\n", zombie.getLaneNo() + 1 , zombie.getTileNo() + 1);
                                System.out.println("*** GAME OVER | ZOMBIES WIN ***");
                                return true;
                            }
                            zombie.move();
                            zombieIterator.remove(); // Remove from current tile
                            lane[laneNo][tileNo - 1].placeZombie(zombie); // Place in new tile
                            GameClock.printTime();
                            System.out.println("Zombie from lane " + (laneNo + 1 ) + " Tile "  +
                                ( tileNo + 1 )+  " has moved to tile " + (tileNo ) );
                        } 
                    }    
                }
            }
        }    
        return false;
    }
*/  
     /**
     * returns true if zombies have entered the house
     * @param timeElapsed
     * @return
     */
    public boolean updateZombies(double timeElapsed){

        int laneNo = 0;
        int tileNo = 0;
        ArrayList<Zombie> zombies;
        Zombie zombie;
        Plant plant;
        Tile currentTile; 
         for(laneNo = 0; laneNo < PvZDriver.getMaxLanes(); laneNo++){
            for(tileNo = 0; tileNo < PvZDriver.getMaxTiles(); tileNo++){
                zombies = lane[laneNo][tileNo].getZombies();
                plant = lane[laneNo][tileNo].getPlant();    
                currentTile = lane[laneNo][tileNo];          
                
                // Use an iterator to safely remove zombies from the list if they move
                Iterator<Zombie> zombieIterator = zombies.iterator();

                while(zombieIterator.hasNext()){
                    zombie = zombieIterator.next();
                    if(plant != null){ // If a plant exists in current tile, zombie tries to attack it
                        if(zombie.isReadyToAttack(timeElapsed)){
                            zombie.attack(plant);   
                        }
                    } else { // If no plant, then update position of the zombie
                        if(zombie.isReadyToMove(timeElapsed)){
                            if(zombie.inLastTile()){
                                GameClock.printTime();
                                System.out.printf("Zombies at lane %d tile %d entered house\n", zombie.getLaneNo() + 1 , zombie.getTileNo() + 1);
                                System.out.println("*** GAME OVER | ZOMBIES WIN ***");
                                return true;
                            }
                            zombie.move();
                            zombieIterator.remove(); // Remove from current tile
                            lane[laneNo][tileNo - 1].placeZombie(zombie); // Place in new tile
                            GameClock.printTime();
                            System.out.println("Zombie from lane " + (laneNo + 1 ) + " Tile "  +
                                ( tileNo + 1 )+  " has moved to tile " + (tileNo ) );
                        } 
                        if(!zombie.isAlive()){
                            zombieIterator.remove();
                            GameClock.printTime();
                            System.out.printf("Zombie died at lane %d tile %d\n", zombie.getLaneNo() + 1, zombie.getTileNo() + 1);
                        }
                    }    
                }
            }
        }    
        return false;
    }

    public Tile[][] getLanes(){
        return this.lane;
    }

    public int getMaxLanes(){
        return MAX_LANES;
    }

    public int getMaxTiles(){
        return MAX_TILES;
    }

    public ArrayList<Entity> getEntities(){
        ArrayList<Entity> entities = new ArrayList<Entity>();
        Entity tempEntity;
        Zombie zombie;
        Iterator<Zombie> zIterator;
        int row = 0;
        int col = 0;

        for(row = 0; row < getMaxLanes(); row++){
            for(col = 0; col < getMaxTiles(); col++){
                zIterator = lane[row][col].getZombies().iterator();

                while(zIterator.hasNext()) {   // iterator for each zombie
                    zombie = zIterator.next();  
                    tempEntity = new Entity(zombie.getMovementAnimation(), zombie.getLaneNo(), zombie.getTileNo(), zombie.getPosition());
                    entities.add(tempEntity);
                }
            }
        }

        return entities;
     }
    

    private int MAX_TILES;
    private int MAX_LANES;
    private Tile[][] lane;
    private Random tilePicker;

}
