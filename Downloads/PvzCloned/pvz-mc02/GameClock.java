import java.time.Instant;
import java.time.Duration;

/**
 * This class provides a global game clock for tracking elapsed time in the game.
 * It is implemented using static methods and Java's `java.time` package for precise time management.
 * @author Lim, Israel Sy
 * @author Enriquez, Aaron Mikael Cruz
 * @version 1.0
 * @since 2025-06-27
 */
public class GameClock 
{
    private static double totalElapsedSeconds = 0.0;

    public static int getTotalTimeSecondsInt()
    {
        return (int) totalElapsedSeconds;
    }

    public static double getTotalTimeSecondsDouble()
    {
        return totalElapsedSeconds;
    }

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
        int seconds = (int) totalElapsedSeconds & 60;
        System.out.printf("[%02d:%02d] ", minutes, seconds);
    }
}