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
import java.util.*;
import javax.swing.*;
import java.time.Duration;
import java.time.Instant;

public class PvZDriver {
    /** A shared Random instance for generating random numbers, e.g., for zombie and sun spawning. */
    private final Random tilePicker;

    /** Current game level, influencing zombie spawning. */
    private static int level = 1 ;

    /** Maximum number of lanes on the game board. */
    private static final int MAX_LANES = 5;

    /** Maximum number of tiles per lane on the game board. */
    private static final int MAX_TILES = 9;

    /**
     * Constructs a new PvZDriver instance.
     * Initializes the random number generator used for various game mechanics.
     */
    public PvZDriver() {
        this.tilePicker = new Random();
        
    }


    /**
     * The main method to start and run the Plants vs. Zombies game simulation.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args){
        PvzGui gui = new PvzGui();      
        Board board = new Board(5,9); // remember to change
        Controller controller = new Controller(gui, board);
      //  board.getTile(3,8).spawnZombie(3,9);
      //  board.getTile(4,8).spawnZombie(4,9);
        controller.start();

       
    }
}