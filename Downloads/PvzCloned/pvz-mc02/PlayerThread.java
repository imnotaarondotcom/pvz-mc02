import java.util.*;

/**
 * This class handles player input and manages player-specific actions within the game,
 * such as planting plants and collecting sun, operating concurrently in its own thread.
 * @author Lim, Israel Sy
 * @author Enriquez, Aaron Mikael Cruz
 * @version 1.0
 * @since 2025-06-27
 */

public class PlayerThread implements Runnable {
    /** The game board lanes, represented as a 2D array of Tiles. */
    volatile Tile[][] lane;
    volatile Board board;

    /** The player's current total sun currency. */
    private int totalSun = 50; // Initial sun

    /** Scanner instance for reading player input. */
    private Scanner sc;

    /**
     * Constructs a PlayerThread with access to the game's lanes.
     * @param lane The 2D array of Tiles representing the game lanes.
     */
    public PlayerThread(Board board){
        this.board = board;
        this.lane = board.getLanes();
    }

    /**
     * The main execution loop for the player's thread.
     * Handles continuous user input for planting plants or collecting sun.
     */
    public void run(){
        sc = new Scanner(System.in);
        Tile userTile; // Tile chosen by the user for an action
        int userInput;
        int laneNo; // Current lane index (column)
        int tileNo; // Current tile index (row)
        int collectedSun = 0;


        System.out.println("Controls");
        while(true){
            System.out.println("1. To place Peashooter \n2. To place Sunflower\n3. To collect sun");
            userInput = Integer.parseInt(sc.next());
            // Get current game time in seconds
            double currentTime = System.currentTimeMillis() / 1000.0; // Use 1000.0 for double division

            if(userInput == 1){ // Player wants to place a Peashooter
                if(totalSun >= Peashooter.getCost()){ // Check if player has enough sun
                    // Check if Peashooter is off cooldown for planting
                    if(currentTime - Peashooter.getTimeSinceLastPlant() >= Peashooter.getCooldown()){
                        userTile = getTileToPlace(); 
                        if(userTile != null && userTile.getPlant() == null){ // Check if tile is valid and empty
                            // Place new Peashooter and update game state
                            userTile.placePlant(new Peashooter(userTile.getLaneNo(), userTile.getTileNo()));
                            totalSun -= Peashooter.getCost();
                            Peashooter.setTimeSinceLastPlant(currentTime); // Reset Peashooter's planting cooldown
                        } else if (userTile != null) {
                            System.out.println("Tile is occupied!");
                        }
                    } else {
                        System.out.println("Still in cooldown!");
                    }
                } else {
                    System.out.println("Not enough sun!");
                }
            } else if(userInput == 2){ // Player wants to place a Sunflower
                if(totalSun >= Sunflower.getCost()){ // Check if player has enough sun
                    // Check if Sunflower is off cooldown for planting
                    if(currentTime - Sunflower.getTimeSinceLastPlant() >= Sunflower.getCooldown()){
                        userTile = getTileToPlace(); // No parameters needed anymore, scanner is a field
                        if(userTile != null && userTile.getPlant() == null){ // Check if tile is valid and empty
                            // Place new Sunflower and update game state
                            userTile.placePlant(new Sunflower(userTile.getLaneNo(), userTile.getTileNo()));
                            totalSun -= Sunflower.getCost();
                            Sunflower.setTimeSinceLastPlant(currentTime); // Reset Sunflower's planting cooldown
                        } else if (userTile != null) {
                            System.out.println("Tile is occupied!");
                        }
                    } else {
                        System.out.println("Still in cooldown");
                    }
                } else {
                    System.out.println("Not enough sun!");
                }
            } else if(userInput == 3){ // Player wants to collect sun
                // Iterate through all tiles on the board to collect sun
                for(laneNo = 0; laneNo < Board.getMaxLanes(); laneNo++){
                    for(tileNo = 0; tileNo < Board.getMaxTiles(); tileNo++){
                        collectedSun += collectSun(lane[laneNo][tileNo].getSunList());
                    }
                }
                GameClock.printTime(); // Print current game time
                System.out.printf("Collected sun is : %d \nTotal sun is : %d\n",collectedSun, totalSun);
                collectedSun = 0; // Reset collected sun for next cycle
            }
        }
    }

    /**
     * Prompts the user to enter a lane and tile number for plant placement
     * and returns the corresponding Tile object.
     * Uses the class's shared Scanner instance.
     * @return The chosen {@link Tile} object, or null if input is invalid (no validation implemented).
     */
    public Tile getTileToPlace(){ // Removed parameters as laneNo/tileNo are now local to run() or directly assigned
        int selectedLaneNo = 0;
        int selectedTileNo = 0;


        while(selectedLaneNo > Board.getMaxLanes() || selectedLaneNo < 1){
            System.out.println("Enter lane to place:");
            selectedLaneNo = sc.nextInt(); // Use the class's scanner
            if(selectedLaneNo > Board.getMaxLanes() || selectedLaneNo < 1 ){
                 System.out.println("not a valid lane");
            } 
        }
        
         while(selectedTileNo > Board.getMaxTiles() || selectedTileNo < 1){
            System.out.println("Enter tile to place:");
            selectedTileNo = sc.nextInt(); // Use the class's scanner
            if(selectedTileNo > Board.getMaxTiles() || selectedTileNo < 1 ){
                System.out.println("not a valid tile");
            }
         }

        // Returns the Tile based on user input (adjusting for 0-based array indexing)
        return lane[selectedLaneNo - 1][selectedTileNo - 1];
    }

    
    

    /**
     * Collects all sun objects from a given list and adds their value to the total sun.
     * @param sun An ArrayList of {@link Sun} objects to be collected.
     * @return The total amount of sun collected from the provided list.
     */
    public int collectSun(ArrayList<Sun> sun){
        int collectedSun = 0;
        if(sun.size() != 0){ // Check if there's any sun to collect
            while(sun.size() != 0){ // Continue as long as there's sun in the list
                totalSun += Sun.getValue(); // Add sun value to player's total
                collectedSun += Sun.getValue(); // Add to current collection count
                sun.remove(sun.size() - 1); // Remove collected sun from the list
            }
        }
        return collectedSun; // Return sun collected in this specific call
    }

    public int getTotalSun(){
        return totalSun;
    }
}