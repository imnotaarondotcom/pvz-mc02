import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.Timer;



public class Controller  extends MouseAdapter implements ActionListener {
    
    public Controller(PvzGui g , Board b){
        player = new Player(b);
        gui = g;
        board = b;
        gameOver = false;
        addMouseListener();
        setActionListener();
    }

    // anonymous method
    public void addMouseListener()
    {
        // anonymous class for selection panel
        MouseAdapter selection = new MouseAdapter() 
        {              
            @Override
            public void mousePressed(MouseEvent e)
            {
                System.out.printf(" Selectors tap Border %d\n",  (int) e.getX() / (gui.getScreenWidth() / gui.getNoBorders()) + 1);
                plantNo = (int) e.getX() / (gui.getScreenWidth() / gui.getNoBorders()); 
            }
        };
        gui.setMouseListener(this , selection);
    }

    public void setActionListener(){
        gui.setActionListener(this);
    }

     @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Restart"))
        System.out.println("pressed");
    }


       @Override
    public void mousePressed(MouseEvent e){
        BoardPanel boardPanel = gui.getBoardPanel();

        int laneNo = (int) e.getY()  / boardPanel.getTileY();
        int tileNo = (int) e.getX() / boardPanel.getTileX() ;
  
            player.tryToplacePlant(plantNo, laneNo, tileNo);
            //board.getTile(laneNo,tileNo).placePlant(new Peashooter(laneNo, tileNo));
        

        System.out.printf("Lane %d, TIle %d\n", (int) e.getY()  / boardPanel.getTileY() + 1, (int) e.getX() / boardPanel.getTileX() + 1);

        player.collectSun(board.getTile(laneNo, tileNo).getSunList());
        gui.updateSunCount(player.getTotalSun());
        plantNo = 0;
    }

    public void updateSelectionPanel(){

    }

    

    public void start()
    {
        System.out.println("*** PLANTS VS. ZOMBIES *** \nPress 1 To Play");
     
        int input = 0;
        GameClock clock = new GameClock();
        
        startTime = System.currentTimeMillis();

       
        gui.getBoardPanel().setBoardSize(Board.getMaxLanes(), Board.getMaxTiles());
        
       
  
        Thread inputThread = new Thread( new PlayerThread(board));
        inputThread.setDaemon(true);
        inputThread.start();

        gui.setBoardSize(board.getMaxLanes(), board.getMaxTiles());
        
        // Initially 0
        lastTimeUpdate = System.currentTimeMillis();

        while(!updateModel()){ 
            gui.getBoardPanel().setEntities(board.getEntities());
            gui.updateCooldownState(player.getCooldownState());
            gui.updateView(board.getEntities(), player.getCooldownState(), player.getTotalSun());
            
        }
       
    }

    // returns true if game over
    public boolean updateModel(double timeElapsed)
    {
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
        
        gameOver = board.updateBoard( timeElapsed , gameTime);
        lastBoardUpdate = currentTime;

        

        try{ 
            Thread.sleep(100);
        } catch(InterruptedException e){
            Thread.currentThread().interrupt();
            System.err.println("Game loop interrupted: " + e.getMessage());
        }

        return gameOver;
    }



    private Player player;
    private PvzGui gui;
    private Board board;
    private double lastBoardUpdate;
    private int lastZombieSpawnTime;
    private int lastSunSpawnTime;
    private double startTime; 
    private int plantNo; // selection panels plant number
   
     
    }
