import java.util.ArrayList;
import java.util.Random;
import java.util.Iterator;
public class Board 
{
    private double lastZombieSpawnTime = 0.0;
    private double lastSunSpawnTime = 0.0;
    private boolean finalWaveTriggered = false;
    private boolean gameOver = false;
    private final double ZOMBIE_SPAWN_INTERVAL_EASY = 10.0;
    private final double ZOMBIE_SPAWN_INTERVAL_MEDIUM = 5.0;
    private final double ZOMBIE_SPAWN_INTERVAL_HARD = 3.0;
    private final double SUN_SPAWN_INTERVAL = 8.0;

    public Board(int l, int t)
    {
        MAX_LANES = l;
        MAX_TILES = t;
        lane = new Tile[MAX_LANES][MAX_TILES];
        tilePicker = new Random();
        level = 1;
        initialize();
        
    }

    public void initialize(){
        armorManager = new ArmorManager();
        armorManager.loadLevelData(level);


        int i = 0;
        int tile = 0 ;
        for(i = 0 ; i < MAX_LANES; i++)
        {
            lanes[i] = new Tile[MAX_TILES];

            for(tile = 0; tile < MAX_TILES; tile++)
            {
                lanes[i][tile] = new Tile(i, tile);
            }
        }
    }

    

    public boolean updateBoard(double timeElapsed , int gameTime){
        updatePlants(timeElapsed);
        updateProjectiles(timeElapsed);
        if(updateZombies(timeElapsed)){
            winner = "zombies";
            return true;
        }
        else if(gameTime >= 180 ){
            winner = "player";
            nextLevel();
            GameClock.printTime();
            System.out.println("Player Wins");
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
        
        
        laneNo = tilePicker.nextInt(getMaxLanes()); // Randomly pick a lane for the zombie

        if(gameTime >= 10 && gameTime <= 80){
            if(gameTime % 10 == 0 && lastZombieSpawnTime != gameTime){
                lane[laneNo][MAX_TILES - 1].spawnZombie(laneNo, MAX_TILES - 1 , armorManager.getArmor());
                return true;
            }
        } else if(gameTime >= 81 && gameTime <= 140 ){
            if(gameTime % 5 == 0 && lastZombieSpawnTime != gameTime){
                lane[laneNo][MAX_TILES - 1].spawnZombie(laneNo, MAX_TILES - 1 ,  armorManager.getArmor());
                return true;
            }
        } else if(gameTime > 140 && gameTime <= 170){
            if(gameTime % 3 == 0 && lastZombieSpawnTime != gameTime){
                lane[laneNo][MAX_TILES - 1].spawnZombie(laneNo,MAX_TILES - 1 ,  armorManager.getArmor());
                return true;
            }
        } else if(gameTime > 170 && lastZombieSpawnTime <= 170 ){
            for(i = 0; i < 5 + (getLevel() - 1) * 2; i++){
                laneNo = tilePicker.nextInt(getMaxLanes());
                lane[laneNo][MAX_TILES - 1].spawnZombie(laneNo,MAX_TILES - 1 ,  armorManager.getArmor());
            }
            finalWaveTriggered = true; // set flag to true so it doesnt trigger again
            return true; // indicate zombies were spawned
        }

        // regular Zombie Spawning
        else if (gameTime >= 10 && gameTime <= 80)
        {
            spawnInterval = ZOMBIE_SPAWN_INTERVAL_EASY;
        }
        else if (gameTime >= 81 && gameTime <= 140)
        {
            spawnInterval = ZOMBIE_SPAWN_INTERVAL_MEDIUM;
        }
        else if (gameTime > 140 && gameTime <= 170)
        {
            spawnInterval = ZOMBIE_SPAWN_INTERVAL_HARD;
        }

        // only spawn regular zombies if final wave hasn't been triggered
        if (!finalWaveTriggered && spawnInterval > 0) { // only attempt to spawn if there's a valid interval
            if (lastZombieSpawnTime >= spawnInterval)
            {
                laneNo = tilePicker.nextInt(PvZDriver.getMaxLanes());
                lanes[laneNo][MAX_TILES - 1].spawnZombie(laneNo, MAX_TILES - 1);
                lastZombieSpawnTime -= spawnInterval; // subtract the interval
                return true; // a zombie was spawned
            }
        }

        return false; // no Zombie spawned
    }
    
    public boolean tryToSpawnSun(int gameTime)
    {
        // dont spawn sun if game over
        if (gameOver)
        {
            return false;
        }
        int tileNo;
        int laneNo;
    
        tileNo = tilePicker.nextInt(getMaxTiles());
        laneNo = tilePicker.nextInt(getMaxLanes());

        while (lastSunSpawnTime >= SUN_SPAWN_INTERVAL)
        {
            tileNo = tilePicker.nextInt(PvZDriver.getMaxTiles());
            laneNo = tilePicker.nextInt(PvZDriver.getMaxLanes());

            Sun newSun = new Sun(laneNo, tileNo);
            lanes[laneNo][tileNo].addSun(newSun);
            lastSunSpawnTime -= SUN_SPAWN_INTERVAL;
            return true; // sun spawned
        }
        return false; // no sun spawned
    }

    public void updatePlants(double timeElapsed)
    {
        // dont update if game over
        if (gameOver)
        {
            return;
        }

        int laneNo = 0;
        int tileNo = 0;
        Tile currentTile;
        Plant plant;
        for(laneNo = 0; laneNo <getMaxLanes(); laneNo++)
        {
            for(tileNo = 0; tileNo < getMaxTiles(); tileNo++)
            {
                plant = lanes[laneNo][tileNo].getPlant();
                currentTile = lanes[laneNo][tileNo];
                if(plant != null )
                {
                    plant.tryToAction(this, timeElapsed);
                    if(plant.getHealth() <= 0)
                    {
                        currentTile.removePlant();
                        GameClock.printTime();
                        System.out.printf("Plant at lane %d tile %d died\n", plant.getLaneNo() + 1, plant.getTileNo() + 1);
                    }
                }
            }
        } 
    }

 
    public void updateProjectiles(double timeElapsed){

        int laneNo = 0;
        int tileNo = 0;
        Tile currentTile;
        Iterator<Projectile> iterator;
        ArrayList<Projectile> projectiles;
        Projectile projectile;
        

        for(laneNo = 0; laneNo < getMaxLanes(); laneNo++){
            for(tileNo = 0; tileNo < getMaxTiles(); tileNo++){
                currentTile = lane[laneNo][tileNo];  
                
                projectiles = lanes[laneNo][tileNo].getProjectiles();
                iterator = projectiles.iterator();
                
                while(iterator.hasNext())
                {
                    projectile = iterator.next();
                    
                    if(currentTile.hasZombie())
                    {
                        if(projectile.hasHitZombie(currentTile))
                        {
                            // remove projectile if it hit zombie
                            iterator.remove();
                        } 
                        else 
                        {
                            // otherwise keep moving
                            projectile.updatePosition(timeElapsed);
                        }
                    } 
                    else 
                    {
                        if(projectile.isReadyToMove(timeElapsed))
                        {
                            if(projectile.getTileNo() < MAX_TILES)
                            {
                                iterator.remove();
                                lanes[laneNo][projectile.getTileNo()].placeProjectile(projectile);
                            } 
                            else 
                            {
                                iterator.remove();
                            }
                        } 
                        else 
                        {
                            projectile.updatePosition(timeElapsed);
                        }
                    }
                }
            }
        }
    }

     /**
     * returns true if zombies have entered the house
     * @param timeElapsed
     * @return
     */
    public boolean updateZombies(double timeElapsed)
    {
        if (gameOver)
        {
            return true;
        }

        int laneNo = 0;
        int tileNo = 0;
        ArrayList<Zombie> zombies;
        Zombie zombie;
        Plant plant;
        
         for(laneNo = 0; laneNo < getMaxLanes(); laneNo++){
            for(tileNo = 0; tileNo < getMaxTiles(); tileNo++){
                zombies = lane[laneNo][tileNo].getZombies();
                plant = lane[laneNo][tileNo].getPlant();    
                       
                
                // Use an iterator to safely remove zombies from the list if they move
                Iterator<Zombie> zombieIterator = zombies.iterator();

                while(zombieIterator.hasNext())
                {
                    zombie = zombieIterator.next();
                    // If a plant exists in current tile, zombie tries to attack it
                    // wont move if there is
                    if(plant != null)
                    { 
                        if(zombie.isReadyToAttack(timeElapsed))
                        {
                            zombie.attack(plant);   
                        }
                    } 
                    else 
                    { 
                        // If no plant, then update position of the zombie
                        if(zombie.isReadyToMove(timeElapsed))
                        {
                            
                            if(zombie.inLastTile()){ 

                                zombie.move();

                            }
                            else if(zombie.getTileNo() == -1)
                            {
                                if(zombie.isReadyToMove(timeElapsed)){
                                    GameClock.printTime();
                                    System.out.printf("Zombies at lane %d tile %d entered house\n", zombie.getLaneNo() + 1 , zombie.getTileNo() + 1);
                                    System.out.println("*** GAME OVER | ZOMBIES WIN ***");
                                    System.out.println("tileNo is " + tileNo);
                                    return true;
                                }
                                
                            }
                            else{
                                GameClock.printTime();
                                System.out.println("Zombie from lane " + (laneNo + 1 ) + " Tile "  +
                                ( tileNo + 1 )+  " has moved to tile " + (tileNo ) );
                                zombieIterator.remove(); // Remove from current tile
                                lanes[laneNo][tileNo - 1].placeZombie(zombie); // Place in new tile
                                zombie.move(); // updates zombies tile no
                                
                            }
                            
                        }
                        else 
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

 

    public Tile getTile(int laneNo, int tileNo)
    {
        return lanes[laneNo][tileNo];
    }

    public ArrayList<Entity> getEntities(){
        ArrayList<Entity> entities = new ArrayList<Entity>();
        Iterator<Sun> sIterator;
        Sun sun;
        Entity tempEntity;
        Zombie zombie;
        Projectile projectile;
        Iterator<Zombie> zIterator;
        Iterator<Projectile> projIterator;
        Plant plant;
        int row = 0;
        int col = 0;

        for(row = 0; row < getMaxLanes(); row++)
        {
            for(col = 0; col < getMaxTiles(); col++)
            {
                zIterator = lanes[row][col].getZombies().iterator();
                sIterator = lanes[row][col].getSunList().iterator();
                projIterator = lanes[row][col].getProjectiles().iterator();
                plant = lanes[row][col].getPlant();

                // iterator for each zombie
                while(zIterator.hasNext()) 
                {  
                    zombie = zIterator.next();  
                    tempEntity = new Entity(zombie.getType(), zombie.getState(), zombie.getLaneNo(), zombie.getTileNo(), zombie.getPosition(), zombie.getSize());
                    entities.add(tempEntity);
                }

                // iterator for each projectile
                while(projIterator.hasNext()) 
                {  
                    projectile = projIterator.next();  
                    tempEntity = new Entity(projectile.getType(), projectile.getState(), projectile.getLaneNo(), 
                        projectile.getTileNo(), projectile.getPosition(), projectile.getSize() , projectile.getPositionY());
                        
                    entities.add(tempEntity);
                }

                if(plant != null)
                {
                    tempEntity = new Entity(plant.getName(), plant.getState(), plant.getLaneNo(), plant.getTileNo(), plant.getPosition(), plant.getSize());
                    entities.add(tempEntity);
                }

                 // iterator for each sun
                while(sIterator.hasNext())
                {
                    sun = sIterator.next();
                    tempEntity = new Entity(sun.getName(), sun.getState(),sun.getLaneNo(), sun.getTileNo(), sun.getPosition(), sun.getSize(), sun.getPositionY());
                    entities.add(tempEntity);
                }
            }
        }

        return entities;
     }

    public void nextLevel(){
        level += 1;
    }

    public void setMaxLanes(int l){
        MAX_LANES = l;
    }

    public void setMaxTiles(int t){
        MAX_TILES = t;
    }

    public String getWinner(){
        return winner;
    }

    public int getLevel(){
        return level;
    }
   public Tile[][] getLanes(){
        return this.lane;
    }

    public static int getMaxLanes(){
        return MAX_LANES;
    }

    public static int getMaxTiles(){
        return MAX_TILES;
    }
   



    private static int MAX_TILES;
    private static int MAX_LANES;
    private Tile[][] lane;
    private Random tilePicker;
    private int level;
    private String winner;
    private ArmorManager armorManager;
    
    

}
