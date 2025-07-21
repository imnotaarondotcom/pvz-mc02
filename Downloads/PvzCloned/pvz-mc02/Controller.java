import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Scanner;

public class Controller implements ActionListener{
    private Gui gui;
    private Board board;
        double lastBoardUpdate;
        int lastZombieSpawnTime;
        int lastSunSpawnTime;
        double startTime; 

    public Controller(Gui g , Board b){
        gui = g;
        board = b;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public void start(){
        boolean gameOver = false;
     
        int input = 0;
        GameClock clock = new GameClock();

        System.out.println("*** PLANTS VS. ZOMBIES *** \nPress 1 To Play");
       

        GameClock.setTime();
      
        
        startTime = System.currentTimeMillis();

       
        gui.setBoardSize(board.getMaxLanes(), board.getMaxTiles());
       
       
        
        Thread inputThread = new Thread( new PlayerThread(board));
        inputThread.setDaemon(true);
        inputThread.start();

        

        while(!updateModel()){ 
            gui.setEntities(board.getEntities());
           
        
            gui.repaint();
            
        }
       
    }

    

    /**updates the entities that are in the board and returns if the game has ended
     * 
     * @return - returns 0 if the game has not ended and 1 otherwise
     */
    public boolean updateModel(){
        boolean gameOver = false;
        long currentTime = System.currentTimeMillis();
            double timeElapsed = (double)(currentTime - lastBoardUpdate)/1000.0;
            int gameTime = (int) ((currentTime - startTime) / 1000.0);
            
            if(board.tryToSpawn(gameTime, lastZombieSpawnTime)){
                lastZombieSpawnTime = gameTime;
            }
            if(board.tryToSpawnSun(gameTime, lastSunSpawnTime)){
                lastSunSpawnTime = gameTime;
            }
            
            gameOver = board.updateBoard( timeElapsed);
            lastBoardUpdate = currentTime;

            if(gameTime >= 180 && !gameOver){
                GameClock.printTime();
                System.out.println("Player Wins");    
                gameOver = true;
            } 

            try{ 
                Thread.sleep(100);
            } catch(InterruptedException e){
                Thread.currentThread().interrupt();
                System.err.println("Game loop interrupted: " + e.getMessage());
            }

            return gameOver;
        }
     
    }
