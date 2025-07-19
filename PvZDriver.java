import java.util.*;
import java.time.Duration;
import java.time.Instant;

/**
 * This class is the main driver for the Plants vs. Zombies simulation game.
 * It initializes the game board, manages the primary game loop, handles the spawning
 * of zombies and sun, updates the state of all game entities, and determines
 * win/loss conditions.
 * @author Lim, Israel Sy
 * @author Enriquez, Aaron Mikael Cruz
 * @version 1.0
 * @since 2025-06-27
 */

public class PvZDriver {
    /** A shared Random instance for generating random numbers, e.g., for zombie and sun spawning. */
    private final Random TILEPICKER;

    /** Current game level, influencing zombie spawning. */
    private static int level = 1 ;

    /** Maximum number of lanes on the game board. */
    private static final int MAX_LANES = 5;

    /** Maximum number of tiles per lane on the game board. */
    private static final int MAX_TILES = 9;

    /** Maximum number of tiles per lane on the game board. */
    private static Tile[][] lane;

    /**
     * Constructs a new PvZDriver instance.
     * Initializes the random number generator used for various game mechanics.
     */
    public PvZDriver() {
        this.TILEPICKER = new Random();
        lane = new Tile[MAX_LANES][MAX_TILES];
    }

    /**
     * Attempts to spawn a zombie based on game time and difficulty.
     * Spawns zombies at the last tile of a randomly chosen lane.
     * @param gameTime The current elapsed game time in seconds.
     * @param lastZombieSpawnTime The game time when a zombie was last spawned.
     * @return True if a zombie was spawned, false otherwise.
     */
    public boolean tryToSpawnZombie(int gameTime, int lastZombieSpawnTime){
        int i;
        int laneNo;
        
        laneNo = TILEPICKER.nextInt(PvZDriver.getMaxLanes()); // Randomly pick a lane for the zombie

        if(gameTime >= 30 && gameTime <= 80){
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
                laneNo = TILEPICKER.nextInt(PvZDriver.getMaxLanes());
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
     * @return True if sun was spawned, false otherwise.
     */
    public boolean tryToSpawnSun(int gameTime, int lastSunSpawnTime){
        int tileNo;
        int laneNo;
    
        tileNo = TILEPICKER.nextInt(PvZDriver.getMaxTiles());
        laneNo = TILEPICKER.nextInt(PvZDriver.getMaxLanes());

        if(gameTime % 8 == 0 && lastSunSpawnTime != gameTime){
            Sun newSun = new Sun(laneNo, tileNo);
            lane[laneNo][tileNo].addSun(newSun);
            return true;
        }
        return false;
    }

    /**
     * Updates the state of the game board, including plant actions, projectile movements,
     * and zombie movements and attacks. Checks for game over conditions.
     * @param gameOver A boolean indicating if the game is already over.
     * @param timeElapsed The time elapsed since the last board update.
     * @return True if the game is now over, false otherwise.
     */
    public boolean updateBoard(boolean gameOver, double timeElapsed){
        int laneNo;
        int tileNo;
        int zombieNo;
        Plant plant;
        Tile currentTile;
        Zombie zombie;
        ArrayList<Zombie> zombies; 
        Projectile projectile;
        Iterator<Projectile> iterator;
        ArrayList<Projectile> projectiles;
        
        // Updates plants
        for(laneNo = 0; laneNo < PvZDriver.getMaxLanes(); laneNo++){
            for(tileNo = 0; tileNo < PvZDriver.getMaxTiles(); tileNo++){
                plant = lane[laneNo][tileNo].getPlant();
                if(plant != null ){
                    plant.tryToAction(lane[laneNo][tileNo], timeElapsed, lane[laneNo]);
                }
            }
        } 
        
        // Updates projectiles
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

        // Updates zombies
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

    /**
     * Returns the maximum number of lanes on the game board.
     * @return The maximum number of lanes.
     */
    public static int getMaxLanes(){
        return MAX_LANES;
    }

    /**
     * Returns the maximum number of tiles per lane on the game board.
     * @return The maximum number of tiles.
     */
    public static int getMaxTiles(){
        return MAX_TILES;
    }

    /**
     * Returns the current game level.
     * @return The current game level.
     */
    public static int getLevel(){
        return level;
    }

    /**
     * The main method to start and run the Plants vs. Zombies game simulation.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int input = 0;
        double lastBoardUpdate = 0;
        int lastZombieSpawnTime = 0;
        int lastSunSpawnTime = 0;
        double startTime;
        int i;
        int tile;         
        
        boolean gameOver = false;
        GameClock clock = new GameClock();

        System.out.println("*** PLANTS VS. ZOMBIES *** \nPress 1 To Play");
        while(input != 1){
            input = sc.nextInt();
        }

        GameClock.setTime();
        PvZDriver driver = new PvZDriver();

        for(i = 0 ; i < MAX_LANES; i++){
            lane[i] = new Tile[MAX_TILES];

            for(tile = 0; tile < MAX_TILES; tile++){
                lane[i][tile] = new Tile(i, tile);
            }
        }
        
        startTime = System.currentTimeMillis();
        
        Thread inputThread = new Thread( new PlayerThread(lane));
        inputThread.setDaemon(true);
        inputThread.start();

        while(!(gameOver)){
            long currentTime = System.currentTimeMillis();
            double timeElapsed = (double)(currentTime - lastBoardUpdate)/1000.0;
            int gameTime = (int) ((currentTime - startTime) / 1000.0);

            if(driver.tryToSpawnZombie(gameTime, lastZombieSpawnTime)){
                lastZombieSpawnTime = gameTime;
            }
            if(driver.tryToSpawnSun(gameTime, lastSunSpawnTime)){
                lastSunSpawnTime = gameTime;
            }
            
            gameOver = driver.updateBoard(gameOver, timeElapsed);
            lastBoardUpdate = currentTime;

            if(gameTime >= 180 && !gameOver){
                GameClock.printTime();
                System.out.println("Player Wins");    
                gameOver = true;
            } 

            try{ 
                Thread.sleep(100);
            } catch(InterruptedException e){
                Thread.currentThread().interrupt();
                System.err.println("Game loop interrupted: " + e.getMessage());
            }
        }
        sc.close();
    }
}