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
public class GameClock {
    /** Stores the starting or reference point in time for the game clock. */
    private static Instant time;

    /** Initializes the game clock, setting its start time to the current moment. */
    public GameClock(){
        time = Instant.now();
    }

    /**
     * Retrieves the elapsed time in seconds since the clock was initialized.
     * @return Elapsed time in seconds, or 0 if clock uninitialized.
     */
    public static int getTime(){
        if(time != null) {
            return (int)Duration.between(time, Instant.now()).toSeconds();
        } else {
            System.out.println("Clock not initialized.");
            return 0;
        }
    }

    /** Resets the game clock to the current moment, restarting the timer from zero. */
    public static void setTime(){
        time = Instant.now();
    }

    /**
     * Prints the elapsed game time in [MM:SS] format to the console.
     */
    public static void printTime(){
        int totalSeconds = getTime();
        System.out.printf("[%02d:%02d] ", totalSeconds / 60 , totalSeconds % 60);
    }
}