import java.util.*;
import java.time.Duration;
import java.time.Instant;
// check tile spawning 


public class PvZDriver {
 
   /*  public  boolean mover(Tile[][] lane ){
        int i = 0;
        int tile = 0;

        try{
        Thread.sleep(1000);
       } catch (InterruptedException a){
        System.out.println("broke");
         } 
        
        for(i = 0 ; i < MAX_LANES; i ++){
            for(tile = MAX_TILES - 1 ; tile >= 0 ; tile-- ){
                if(lane[i][tile].moveZombies(lane[i]) != true){// moves the zombies closer to the house
                    System.out.println("Zombie in lane " + (i + 1) +  "has enterd the house ");
                    return true;
                } 
                
               
            }
        }

        return false;


    }
*/


    public boolean tryToSpawn(int gameTime,int lastZombieSpawnTime ,Tile[][] lane){
        int i = 0;
        int laneNo;
        int tileNo;
        Random tilePicker = new Random(); // chooses which lane zombies will spawn
        

        
        tileNo = tilePicker.nextInt(PvZDriver.getMaxTiles());
        laneNo = tilePicker.nextInt(PvZDriver.getMaxLanes());

        
   
        if(gameTime >= 10 && gameTime <= 80){
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

    public boolean tryToSpawnSun(int gameTime, int lastSunSpawnTime, ArrayList<Sun> sun ){
        Random tilePicker = new Random(); // chooses which lane zombies will spawn
        int tileNo;
        int laneNo;

        
        tileNo = tilePicker.nextInt(PvZDriver.getMaxTiles());
        laneNo = tilePicker.nextInt(PvZDriver.getMaxLanes());

        if(gameTime % 8 == 0 && lastSunSpawnTime != gameTime){
            if(gameTime % 8 == 0 && lastSunSpawnTime != gameTime){
            Sun newSun = new Sun(laneNo, tileNo);
            sun.add(newSun);
            return true;
            }
        }

        return false;
    }


    public boolean updateBoard(boolean gameOver, Tile[][] lane,  double timeElapsed , ArrayList<Sun> sun ){
        int laneNo = 0;
        int tileNo = 0;
        int zombieNo = 0;
        int projNo = 0;
        Plant plant;
        Zombie zombies[];
        ArrayList<Projectile> projectiles;
        

          //updates plants
          for(laneNo = 0; laneNo < PvZDriver.getMaxLanes(); laneNo++){
                for(tileNo = 0; tileNo < PvZDriver.getMaxTiles(); tileNo++){
                    plant = lane[laneNo][tileNo].getPlant();
                    if(plant != null ){
                        plant.tryToAction(lane[laneNo][tileNo], timeElapsed, sun, lane[laneNo]);
                    }
                        
                }
           }  
           
           // updates projectiles
           for(laneNo = 0; laneNo < PvZDriver.getMaxLanes(); laneNo++){
                for(tileNo = 0; tileNo < PvZDriver.getMaxTiles(); tileNo++){
                    projectiles = lane[laneNo][tileNo].getProjectiles();
                    Iterator<Projectile> iterator = projectiles.iterator();
                            while(iterator.hasNext()){
                                 if(iterator.next().tryToHit(lane[laneNo], timeElapsed)){
                                    iterator.remove();
                                 }
                            }
                }
           }

        //updates zombies 
         for(laneNo = 0; laneNo < PvZDriver.getMaxLanes(); laneNo++){
                for(tileNo = 0; tileNo < PvZDriver.getMaxTiles(); tileNo++){
                    zombies = lane[laneNo][tileNo].getZombies();                   
                    for(zombieNo = 0; zombieNo < Tile.getMaxZombies(); zombieNo++ ){
                        
                        if(zombies[zombieNo] != null)
                        if(!(zombies[zombieNo].tryToMove(lane[laneNo], timeElapsed))){ // 
                            return true;
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

    public static void main(String[] args){
        int userSun = 0;
        double lastBoardUpdate = 0;
        int lastZombieSpawnTime = 0;
        int lastSunSpawnTime = 0;
        double startTime = System.currentTimeMillis();
        int i = 0;
        int tile = 0;        
        ArrayList<Sun> sun = new ArrayList<Sun>();
        
        
        boolean gameOver = false;
        GameClock clock = new GameClock();
        
        Tile[][] lane = new Tile[MAX_LANES][MAX_TILES];
        
        PvZDriver driver = new PvZDriver();

         
        // initializes the lanes and the tiles
        for(i = 0 ; i < MAX_LANES; i++){
            lane[i] = new Tile[MAX_TILES];

            for(tile = 0; tile < 9; tile++){
                lane[i][tile] = new Tile(i, tile);
            }
        }
  
  

        // testspawn peashooters 
        lane[0][0].placePlant(new Peashooter(0,0));
        lane[1][0].placePlant(new Peashooter(0,1));
        lane[2][0].placePlant(new Peashooter(0,2));
        lane[3][0].placePlant(new Peashooter(0,3));
        lane[4][0].placePlant(new Peashooter(0,4));

        // spawn sunflowers

        lane[0][2].placePlant(new Sunflower(0, 2));
        lane[0][1].placePlant(new Sunflower(0, 1));
    
   
       
        while(!(gameOver)){
          

            long currentTime = System.currentTimeMillis(); // currentTimeMillis has to be long 
            double timeElapsed = (double)(currentTime - lastBoardUpdate)/1000; // time since last bord update
            int gameTime = (int) (currentTime - startTime) / 1000; 

            if(driver.tryToSpawn(gameTime, lastZombieSpawnTime, lane)){
                lastZombieSpawnTime = gameTime;
            }
            if(driver.tryToSpawnSun(gameTime, lastSunSpawnTime, sun)){
                lastSunSpawnTime = gameTime;
            }
            
            gameOver = driver.updateBoard(gameOver, lane,  timeElapsed, sun);
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
