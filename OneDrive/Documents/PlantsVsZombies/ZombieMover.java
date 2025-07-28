public class ZombieMover implements Runnable{
     private Tile[][] lane;


    public ZombieMover(Tile[][] lane ){
        this.lane = lane;

    }
   
    @Override
    public void run(){
        int laneNo = 0;
        int tileNo = 0;
        int zombieNo = 0;
        Zombie zombies[];
        while(true){
            try{
                Thread.sleep(1000);
            } catch(InterruptedException e){
                System.out.println("thread failed ");
            }
            

            
           for(laneNo = 0; laneNo < PvZDriver.getMaxLanes(); laneNo++){
                for(tileNo = 0; tileNo < PvZDriver.getMaxTiles(); tileNo++){
                    zombies = lane[laneNo][tileNo].getZombies();                   
                    for(zombieNo = 0; zombieNo < Tile.getMaxZombies(); zombieNo++ ){
                        
                        if(zombies[zombieNo] != null)
                        zombies[zombieNo].tryToMove(lane[laneNo]);
                    }
                }
           }         
        } 
    }
        
    
}
