import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Scanner;



public class Controller  extends MouseAdapter{
    
    public Controller(Gui g , Board b){
        player = new Player(b);
        gui = g;
        board = b;
        addMouseListener();
    }

    // anonymous method
    public void addMouseListener(){
        // anonymous class for selection panel
        MouseAdapter selection = new MouseAdapter() {
                
            @Override
            public void mousePressed(MouseEvent e){
                System.out.printf(" Selectors tap Border %d\n",  (int) e.getX() / (gui.getScreenWidth() / gui.getNoBorders()) + 1);
                plantNo = (int) e.getX() / (gui.getScreenWidth() / gui.getNoBorders()); 
                
            }
        };
        
        
        gui.setMouseListener(this , selection);
    }

       @Override
    public void mousePressed(MouseEvent e){
        int laneNo = (int) e.getY()  / gui.getTileY();
        int tileNo = (int) e.getX() / gui.getTileX() ;
  
            player.tryToplacePlant(plantNo, laneNo, tileNo);
            //board.getTile(laneNo,tileNo).placePlant(new Peashooter(laneNo, tileNo));
        

        System.out.printf("Lane %d, TIle %d\n", (int) e.getY()  / gui.getTileY() + 1, (int) e.getX() / gui.getTileX() + 1);

        player.collectSun(board.getTile(laneNo, tileNo).getSunList());
        gui.updateSunCount(player.getTotalSun());
        plantNo = 0;
    }

    

    public void start(){
       
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
            gui.updateSunCount(player.getTotalSun());
        
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



    private Player player;
    private Gui gui;
    private Board board;
    private double lastBoardUpdate;
    private int lastZombieSpawnTime;
    private int lastSunSpawnTime;
    private double startTime; 
    private int plantNo; // selection panels plant number
     
    }
