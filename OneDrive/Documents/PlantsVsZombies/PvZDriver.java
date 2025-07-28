import java.util.*;
import java.time.Duration;
import java.time.Instant;
// check tile spawning 


public class PvZDriver {
 



    public boolean tryToSpawn(int gameTime,int lastZombieSpawnTime ,Tile[][] lane){
        int i = 0;
        int laneNo;
        int tileNo;
        Random tilePicker = new Random(); // chooses which lane zombies will spawn
        

        
        tileNo = tilePicker.nextInt(PvZDriver.getMaxTiles());
        laneNo = tilePicker.nextInt(PvZDriver.getMaxLanes());

        
   
        if(gameTime >= 30 && gameTime <= 80){
            if(gameTime % 10 == 0 && lastZombieSpawnTime != gameTime){
                lane[laneNo][8].spawnZombie(laneNo, MAX_TILES  - 1);
                return true;
            }
                
        }
        else if(gameTime >= 81 && gameTime <= 140 ){
            if(gameTime % 5 == 0 && lastZombieSpawnTime != gameTime){
                lane[laneNo][8].spawnZombie(laneNo, MAX_TILES - 1 );
                return true;
            }
        }
        else if(gameTime > 140 && gameTime <= 170){
            if(gameTime % 3 == 0 && lastZombieSpawnTime != gameTime){
                lane[laneNo][8].spawnZombie(laneNo,MAX_TILES - 1 );
                return true;
            }
        }
        else if(gameTime > 170 && lastZombieSpawnTime <= 170 ){ // checks if wave has been spawned already
            
            for(i = 0; i < 5 + (PvZDriver.getLevel() - 1) * 2; i++){
                laneNo = tilePicker.nextInt(PvZDriver.getMaxLanes());
                lane[laneNo][8].spawnZombie(laneNo,MAX_TILES - 1 );
            }
            return true;
        }

        return false;
    }

    public boolean tryToSpawnSun(int gameTime, int lastSunSpawnTime, Tile lane[][]){
        Random tilePicker = new Random(); // chooses which lane zombies will spawn
        int tileNo;
        int laneNo;

        
        tileNo = tilePicker.nextInt(PvZDriver.getMaxTiles());
        laneNo = tilePicker.nextInt(PvZDriver.getMaxLanes());

        if(gameTime % 8 == 0 && lastSunSpawnTime != gameTime){
            if(gameTime % 8 == 0 && lastSunSpawnTime != gameTime){
            Sun newSun = new Sun(laneNo, tileNo);
            lane[laneNo][tileNo].addSun(newSun);
            return true;
            }
        }

        return false;
    }


    public boolean updateBoard(boolean gameOver, Tile[][] lane,  double timeElapsed  ){
        int laneNo = 0;
        int tileNo = 0;
        int zombieNo = 0;
        int projNo = 0;
        Plant plant;
        Tile currentTile;
        Tile nextTile;
        Zombie zombie;
        Zombie zombies[];
        Projectile projectile;
        Iterator<Projectile> iterator;
        ArrayList<Projectile> projectilesToMove = new ArrayList<Projectile>();
        ArrayList<Projectile> projectiles;
        

          //updates plants
          for(laneNo = 0; laneNo < PvZDriver.getMaxLanes(); laneNo++){
                for(tileNo = 0; tileNo < PvZDriver.getMaxTiles(); tileNo++){
                    plant = lane[laneNo][tileNo].getPlant();
                    if(plant != null ){
                        plant.tryToAction(lane[laneNo][tileNo], timeElapsed, lane[laneNo]);
                    }
                        
                }
           }  
           
           // updates projectiles
           for(laneNo = 0; laneNo < PvZDriver.getMaxLanes(); laneNo++){
                for(tileNo = 0; tileNo < PvZDriver.getMaxTiles(); tileNo++){
                    currentTile = lane[laneNo][tileNo];   
                    
                    projectiles = lane[laneNo][tileNo].getProjectiles();
                     iterator = projectiles.iterator();
                        while(iterator.hasNext()){
                            projectile = iterator.next();
                      
                               


                            if(currentTile.hasZombie()){
                                zombie = currentTile.highestPosition();
                                if(zombie.getPosition() + projectile.getPosition() >= Tile.getTileLenght()){
                                    projectile.hit(zombie);
                                    iterator.remove();
                                    if(zombie.getHealth() <= 0){
                                        currentTile.removeZombie(zombie);
                                        GameClock.printTime();
                                        System.out.printf("Zombie died at lane %d tile %d\n", zombie.getLaneNo() + 1, zombie.getTileNo() + 1);
                                    }
                                }
                                // if projectile cannot hit the zombie yet just update the positon
                                else projectile.updatePosition(timeElapsed);
                            }
                            else{
                                if(projectile.isReadyToMove(timeElapsed)){ // tile number of projectile is increased
                                    if(projectile.getTileNo() <= MAX_TILES - 1){
                                        projectilesToMove.add(projectile);
                                        iterator.remove();
                                        lane[laneNo][projectile.getTileNo()].placeProjectile(projectile);
                                        

                                        

                                    }
                                    else{
                                        iterator.remove();
                                    }
                                   
                                }
                            }

                            
                        }
/* 
                        // move the projectile needed to move
                    if(tileNo != MAX_TILES - 1){
                        nextTile = lane[laneNo][tileNo + 1];
                        iterator = projectilesToMove.iterator();
                        while(iterator.hasNext()){
                            projectile = iterator.next();
                            nextTile.placeProjectile(projectile);
                        }
                        projectilesToMove.clear();
                    }
                    */
                }
           }

        //updates zombies 
         for(laneNo = 0; laneNo < PvZDriver.getMaxLanes(); laneNo++){
                for(tileNo = 0; tileNo < PvZDriver.getMaxTiles(); tileNo++){
                    zombies = lane[laneNo][tileNo].getZombies();
                    plant = lane[laneNo][tileNo].getPlant();    
                    currentTile = lane[laneNo][tileNo];               
                    for(zombieNo = 0; zombieNo < Tile.getMaxZombies(); zombieNo++ ){
                        zombie = zombies[zombieNo];
                        if(zombies[zombieNo] != null){
                            // if a plant exists in current tile try to attack it
                            if(plant != null){ 
                                if(zombie.isReadyToAttack( timeElapsed)){
                                    zombie.attack(plant);
                                    if(plant.getHealth() <= 0){ // destorys plant if it has no more hp
                                        currentTile.removePlant();
                                    }
                                }
                                
                            }
                            // if no plant then update position of the zombie
                            else{
                                if(zombie.isReadyToMove( timeElapsed)){

                                     if(zombie.inLastTile()){
                                        GameClock.printTime();
                                        System.out.printf("Zombies at lane %d tile %d entered house\n", zombie.getLaneNo() + 1 , zombie.getTileNo() + 1);
                                        System.out.println("Zombies win");
                                        return true;
                                    }
                                        zombie.move();
                                        lane[laneNo][tileNo].removeZombie(zombie);
                                        lane[laneNo][tileNo - 1].placeZombie(zombie);
                                        GameClock.printTime();
                                        System.out.println("Zombie from lane " + (laneNo + 1 ) + " Tile "  +
                                            ( tileNo + 1 )+  " has moved to tile " + (tileNo ) );
                                    
                                    // ends game if zombie moves in last tile
                                } 
                            }  
                        }
                    }
                }
           }  
           return false;

    }

    

    public static int getMaxLanes(){
        return MAX_LANES;
    }

    public static int getMaxTiles(){
        return MAX_TILES;
    }

    public static int getLevel(){
        return level;
    }

    

    public static void printBorder(){
        int i = 0;
        for(i = 0 ; i < 52; i++){
            System.out.print("=");
        }
        System.out.print("\n");
        
    }

    

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int input = 0;
        double lastBoardUpdate = 0;
        int lastZombieSpawnTime = 0;
        int lastSunSpawnTime = 0;
        double startTime;
        int i = 0;
        int tile = 0;        
        
        boolean gameOver = false;
        GameClock clock = new GameClock();

        System.out.println("Welcome! \nPress 1 To Play");
        while(input != 1){
            input = sc.nextInt();
        }

        GameClock.setTime();
        
        Tile[][] lane = new Tile[MAX_LANES][MAX_TILES];
        
        PvZDriver driver = new PvZDriver();

         
        // initializes the lanes and the tiles
        for(i = 0 ; i < MAX_LANES; i++){
            lane[i] = new Tile[MAX_TILES];

            for(tile = 0; tile < 9; tile++){
                lane[i][tile] = new Tile(i, tile);
            }
        }
        startTime = System.currentTimeMillis();
        Thread inputThread = new Thread( new PlayerThread(lane));
        inputThread.setDaemon(true);
        inputThread.start();
       
        
        // testspawn peashooters 
        /* 
        lane[0][0].placePlant(new Peashooter(0,0));      
        lane[1][0].placePlant(new Peashooter(1,0));
        lane[2][0].placePlant(new Peashooter(2,0));
        lane[3][0].placePlant(new Peashooter(3,0));
        lane[4][0].placePlant(new Peashooter(4,0));

        // spawn sunflowers

        lane[0][2].placePlant(new Sunflower(0, 2));
        lane[0][1].placePlant(new Sunflower(0, 1));
         */
     
        
         


        while(!(gameOver)){
          

            long currentTime = System.currentTimeMillis(); // currentTimeMillis has to be long 
            double timeElapsed = (double)(currentTime - lastBoardUpdate)/1000; // time since last bord update
            int gameTime = (int) (currentTime - startTime) / 1000; 

            if(driver.tryToSpawn(gameTime, lastZombieSpawnTime, lane)){
                lastZombieSpawnTime = gameTime;
            }
            if(driver.tryToSpawnSun(gameTime, lastSunSpawnTime,lane)){
                lastSunSpawnTime = gameTime;
            }
            
            gameOver = driver.updateBoard(gameOver, lane,  timeElapsed);
            lastBoardUpdate = currentTime;
        
            
            if(gameTime == 180 ){
                GameClock.printTime();
                System.out.println("Player Wins");  
                gameOver = true;
            } 




            try{ Thread.sleep(100);

            } catch(InterruptedException e){

            };

        }

        








/* 
        ZombieMover zombieMove = new ZombieMover(lane);
        ZombieSpawner zombieSpawn = new ZombieSpawner(lane);
        Thread zombieMover = new Thread(zombieMove);
        Thread zombieSpawner = new Thread(zombieSpawn);
        zombieSpawner.start();
        zombieMover.start();
       
*/
    }
    private static int level = 1 ;
    private static final int MAX_LANES = 5;
    private static final int MAX_TILES = 9;
}
