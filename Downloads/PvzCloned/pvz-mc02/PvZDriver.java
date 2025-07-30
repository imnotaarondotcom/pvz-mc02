import java.util.*;
import javax.swing.*;
import java.time.Duration;
import java.time.Instant;

/**
 * This class is the main driver for the Plants vs. Zombies simulation game.
 * @author Lim, Israel Sy
 * @author Enriquez, Aaron Mikael Cruz
 * @version 2.0
 * @since 2025-06-27
 */

public class PvZDriver 
{
    /** Maximum number of lanes on the game board. */
    private static final int MAX_LANES = 5;

    /** Maximum number of tiles per lane on the game board. */
    private static final int MAX_TILES = 9;

    /**
     * Returns the maximum number of lanes on the game board.
     * @return The maximum number of lanes.
     */
    public static int getMaxLanes()
    {
        return MAX_LANES;
    }

    /**
     * Returns the maximum number of tiles per lane on the game board.
     * @return The maximum number of tiles.
     */
    public static int getMaxTiles()
    {
        return MAX_TILES;
    }


    /**
     * The main method to start and run the Plants vs. Zombies game simulation.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args)
    {
        Gui gui = new Gui();
        Board board = new Board(MAX_LANES, MAX_TILES);
        Controller controller = new Controller(gui, board);
        controller.goMainMenu();
    }
}
