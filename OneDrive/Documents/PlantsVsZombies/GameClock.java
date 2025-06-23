import java.time.Instant;
import java.time.Duration;
public class GameClock {
    private static Instant time;


    /**
     * intnitalizes the starting time of clock
     */
    public GameClock(){
        time = Instant.now();

    }

    /**
     * gets time played for the current round
     * @return - elapsed time during round
     */
    public static int getTime(){
        if(time != null)
        return (int)Duration.between(time, Instant.now()).toSeconds();
        else{
            System.out.println("clock not initialized");
            return 0;
        }

        
    }

    /**
     * prints the time since game clock was instantiated in minutes and seconds
     */
    public static void printTime(){
        System.out.printf("%02d:%02d ",getTime() /60 , getTime() % 60);
    }
}
