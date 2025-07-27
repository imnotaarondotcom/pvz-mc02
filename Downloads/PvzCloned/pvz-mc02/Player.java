import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Player {
    private int totalSun;
    private Map<Integer , Plant > plantList;
    private Board board;

    public Player(Board board){
        totalSun = 500 ;
        this.board = board;

        plantList = new HashMap<>();
        plantList.put(1,new Peashooter(0, 0));
        plantList.put(2,new Sunflower(0, 0));
        plantList.put(3,new Cherrybomb(0, 0));
        
       
    }
    public int getTotalSun(){
        return totalSun;
    }


    public void tryToplacePlant(int plantNo , int laneNo, int tileNo){
        Plant pickedPlant = plantList.get(plantNo);
        double currentTime = System.currentTimeMillis() / 1000; 
        Tile userTile;
   
        
        if(pickedPlant instanceof Peashooter){ // Player wants to place a Peashooter
            
                if(totalSun >= Peashooter.getCost()){ // Check if player has enough sun
                    // Check if Peashooter is off cooldown for planting
                    if(currentTime - Peashooter.getTimeSinceLastPlant() >= Peashooter.getCooldown()){
                        userTile = board.getTile(laneNo, tileNo);
                        if(userTile != null && userTile.getPlant() == null){ // Check if tile is valid and empty
                            // Place new Peashooter and update game state
                            userTile.placePlant(new Peashooter(userTile.getLaneNo(), userTile.getTileNo()));
                            totalSun -= Peashooter.getCost();
                            Peashooter.setTimeSinceLastPlant(currentTime); // Reset Peashooter's planting cooldown
                            System.out.println("total sun is" + totalSun);
                        } else if (userTile != null) {
                            System.out.println("Tile is occupied!");
                        }
                    } else {
                        System.out.println("Still in cooldown!");
                    }
                } else {
                    System.out.println("Not enough sun!");
                }

           

        } 
        else if(pickedPlant instanceof Sunflower){ // Player wants to place a Peashooter
                
                    if(totalSun >= Sunflower.getCost()){ // Check if player has enough sun
                        // Check if Peashooter is off cooldown for planting
                        if(currentTime - Sunflower.getTimeSinceLastPlant() >= Sunflower.getCooldown()){
                            userTile = board.getTile(laneNo, tileNo);
                            if(userTile != null && userTile.getPlant() == null){ // Check if tile is valid and empty
                                // Place new Peashooter and update game state
                                userTile.placePlant(new Sunflower(userTile.getLaneNo(), userTile.getTileNo()));
                                totalSun -= Sunflower.getCost();
                                Sunflower.setTimeSinceLastPlant(currentTime); // Reset Peashooter's planting cooldown
                            } else if (userTile != null) {
                                System.out.println("Tile is occupied!");
                            }
                        } else {
                            System.out.println("Still in cooldown!");
                        }
                    } else {
                        System.out.println("Not enough sun!");
                    }

            

        }

        else if(pickedPlant instanceof Cherrybomb){ // Player wants to place a Peashooter
                
                    if(totalSun >= Cherrybomb.getCost()){ // Check if player has enough sun
                        // Check if Peashooter is off cooldown for planting
                        if(currentTime - Cherrybomb.getTimeSinceLastPlant() >= Cherrybomb.getCooldown()){
                            userTile = board.getTile(laneNo, tileNo);
                            if(userTile != null && userTile.getPlant() == null){ // Check if tile is valid and empty
                                // Place new Peashooter and update game state
                                userTile.placePlant(new Cherrybomb(userTile.getLaneNo(), userTile.getTileNo()));
                                totalSun -= Cherrybomb.getCost();
                                Cherrybomb.setTimeSinceLastPlant(currentTime); // Reset Peashooter's planting cooldown
                            } else if (userTile != null) {
                                System.out.println("Tile is occupied!");
                            }
                        } else {
                            System.out.println("Still in cooldown!");
                        }
                    } else {
                        System.out.println("Not enough sun!");
                    }

            

        }

 

    }

     public void collectSun(ArrayList<Sun> sun){
        int collectedSun = 0;
        if(sun.size() != 0){ // Check if there's any sun to collect
            while(sun.size() != 0){ // Continue as long as there's sun in the list
                
                collectedSun += Sun.getValue(); // Add to current collection count
                sun.remove(sun.size() - 1); // Remove collected sun from the list
            }
        }
        this.totalSun += collectedSun;
    }


    public ArrayList<Boolean> getCooldownState(){
        ArrayList<Boolean> cdList = new ArrayList<>();// list of cooldown
        long currentTime = System.currentTimeMillis() / 1000;

        //  System.out.println(currentTime - Peashooter.getTimeSinceLastPlant());
                if(Peashooter.getCooldown() > currentTime - Peashooter.getTimeSinceLastPlant()){
                    cdList.add(true);
                  
                }
                else{
                    cdList.add(false);
                }
                if(Sunflower.getCooldown() > currentTime - Sunflower.getTimeSinceLastPlant()){
                    cdList.add(true);
                }
                else{
                    cdList.add(false);
                }
                if(Cherrybomb.getCooldown() >currentTime - Cherrybomb.getTimeSinceLastPlant()){
                    cdList.add(true);
                }
                else{
                    cdList.add(false);
                }
                
        
        return cdList;
    
    }   
}
