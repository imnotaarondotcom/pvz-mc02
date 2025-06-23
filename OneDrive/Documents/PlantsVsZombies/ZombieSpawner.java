import java.time.Duration;
import java.time.Instant;
import java.util.Random;

public class ZombieSpawner implements Runnable{
    private Tile[][] lane;
    

    public ZombieSpawner(Tile[][] lane ){
        this.lane = lane;
     
    }

    @Override
    public void run(){
    Instant start = Instant.now();

    while(true){ // moves the zombie from spawn to house

        try{
            Thread.sleep(1000);
        } catch(InterruptedException e){
            break;
        }
        
        
        Instant end;
        int gameTime = 0; // time elapsed for current game
        int i = 0;
        int laneNo;
        
        Random spawnerLane = new Random(); // chooses which lane zombies will spawn


        
        laneNo = spawnerLane.nextInt(PvZDriver.getMaxLanes());
        end = Instant.now();            
        gameTime = (int)Duration.between(start, end).toSeconds(); // calculates duration of round


        
        
        if(gameTime >= 10 && gameTime <= 80){
            if(gameTime % 10 == 0)
                lane[laneNo][8].spawnZombie(9, laneNo );
        }
        else if(gameTime >= 81 && gameTime <= 140){
            if(gameTime % 5 == 0){
                lane[laneNo][8].spawnZombie(9,laneNo );
            }
        }
        else if(gameTime >= 170){
            if(gameTime % 3 == 0){
                lane[laneNo][8].spawnZombie(9,laneNo );
            }
        }
        else if(gameTime >= 180)
        for(i = 0; i < 5 + (PvZDriver.getLevel() - 1) * 2; i++){
            laneNo = spawnerLane.nextInt(PvZDriver.getMaxLanes());
            lane[laneNo][8].spawnZombie(9,laneNo );
        }
    }
}
}
