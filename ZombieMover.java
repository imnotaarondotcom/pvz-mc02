import java.util.ArrayList;
import java.util.Iterator;

/**
 * Manages the independent movement of all zombies on the game board in a separate thread.
 * This class iterates through all tiles, instructing zombies to advance towards the player's house.
 * It operates concurrently with the main game loop, ensuring continuous zombie progression.
 * @author Lim, Israel Sy
 * @author Enriquez, Aaron Mikael Cruz
 * @version 1.0
 * @since 2025-06-27
 */

public class ZombieMover implements Runnable {
    /** The 2D array representing the game lanes and tiles. */
    private Tile[][] lane;

    /**
     * Constructs a new ZombieMover instance.
     * @param lane The 2D array of {@link Tile} objects representing the game board.
     */
    public ZombieMover(Tile[][] lane) {
        this.lane = lane;
    }
    
    /**
     * The main execution method for the zombie movement thread.
     * This method runs in an infinite loop, periodically updating the positions of all zombies
     * across the game board. It handles zombie movement from one tile to the next
     * and logs relevant actions. The loop exits if the thread is interrupted.
     */
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100); // Pause for 100 milliseconds to control update rate
            } catch (InterruptedException e) {
                System.out.println("ZombieMover thread interrupted. Exiting.");
                Thread.currentThread().interrupt(); // Restore the interrupted status
                break; // Exit the loop
            }

            // Iterate through each lane
            int laneNo, tileNo;
            for (laneNo = 0; laneNo < PvZDriver.getMaxLanes(); laneNo++) {
                // Iterate tiles from right to left (where zombies are) to handle movement correctly
                for (tileNo = PvZDriver.getMaxTiles() - 1; tileNo >= 0; tileNo--) {
                    Tile currentTile = lane[laneNo][tileNo];
                    ArrayList<Zombie> zombiesOnTile = currentTile.getZombies();
                    
                    // Use an iterator to safely remove zombies from the list if they move
                    Iterator<Zombie> zombieIterator = zombiesOnTile.iterator();
                    
                    while (zombieIterator.hasNext()) {
                        Zombie zombie = zombieIterator.next();
                        double elapsedTime = 0.1; // Represents the 100ms sleep duration

                        // Zombies only move if there's no plant blocking them on the current tile
                        if (currentTile.getPlant() == null) { 
                            if (zombie.isReadyToMove(elapsedTime)) {
                                if (zombie.inLastTile()) {
                                    GameClock.printTime();
                                    System.out.printf("Zombie at lane %d tile %d entered house!\n", zombie.getLaneNo() + 1, zombie.getTileNo() + 1);
                                } else {
                                    zombie.move(); // Update zombie's internal tile number
                                    zombieIterator.remove(); // Remove zombie from the current tile's list
                                    lane[laneNo][zombie.getTileNo()].placeZombie(zombie); // Place zombie in the new tile
                                    GameClock.printTime();
                                    System.out.printf("Zombie moved from lane %d tile %d to tile %d\n", 
                                                      (laneNo + 1), (tileNo + 1), (zombie.getTileNo() + 1));
                                }
                            } 
                        }
                    }
                }
            }
        } 
    }
}