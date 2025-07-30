import java.time.Instant;
import java.time.Duration;

/**
 * This class provides a global game clock for tracking elapsed time in the game.
 * It is implemented using static methods for precise time management.
 *
 * @author Lim, Israel Sy
 * @author Enriquez, Aaron Mikael Cruz
 * @version 2.0
 * @since 2025-06-27
 */
public class GameClock
{
    /** The total elapsed game time in seconds. */
    private static double totalElapsedSeconds;

    /**
     * Constructs a new GameClock instance.
     * Resets the total elapsed time to zero.
     */
    public GameClock()
    {
        totalElapsedSeconds = 0;
    }

    /**
     * Retrieves the total elapsed game time as an integer number of seconds.
     * @return The total elapsed time in whole seconds.
     */
    public static int getTotalTimeSecondsInt()
    {
        return (int) totalElapsedSeconds;
    }

    /**
     * Retrieves the total elapsed game time as a double-precision floating-point number of seconds.
     * @return The total elapsed time in seconds, including fractions.
     */
    public static double getTotalTimeSecondsDouble()
    {
        return totalElapsedSeconds;
    }

    /**
     * Adds a specified duration to the total elapsed game time.
     * This method is typically called each frame with the delta time.
     * @param deltaSeconds The time in seconds to add.
     */
    public static void addTime(double deltaSeconds)
    {
        totalElapsedSeconds += deltaSeconds;
    }

    /**
     * Prints the total elapsed game time in [MM:SS] format to the console.
     */
    public static void printTime()
    {
        int minutes = (int) totalElapsedSeconds / 60;
        int seconds = (int) totalElapsedSeconds % 60;
        System.out.printf("[%02d:%02d] ", minutes, seconds);
    }
}
