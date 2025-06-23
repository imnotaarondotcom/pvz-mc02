import java.util.ArrayList;

public class Player {

    private int totalSun = 0;

    
    
    private void placePlant(){
        
    }

    private void collectSun(ArrayList<Sun> sun){
        int collectedSun = 0;
        while(sun.size() != 0){
            totalSun += 25;
            collectedSun += 25;
            sun.removeLast();

        }
        GameClock.printTime();
        System.out.println("Collected " + collectedSun+" Sun");
    }
}
