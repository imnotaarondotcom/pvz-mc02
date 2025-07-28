import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.Timer;

<<<<<<< HEAD


public class Controller  extends MouseAdapter implements ActionListener {
    
    public Controller(PvzGui g , Board b){
=======
public class Controller  extends MouseAdapter
{
    public Controller(Gui g , Board b)
    {
>>>>>>> d7605d28088ad779271db2bc8d8289fe5e4407bd
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

<<<<<<< HEAD
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
=======
    @Override
    public void mousePressed(MouseEvent e)
    {
        int laneNo = (int) e.getY()  / gui.getTileY();
        int tileNo = (int) e.getX() / gui.getTileX() ;

        player.tryToplacePlant(plantNo, laneNo, tileNo);
        //board.getTile(laneNo,tileNo).placePlant(new Peashooter(laneNo, tileNo));
    
        System.out.printf("Lane %d, TIle %d\n", (int) e.getY()  / gui.getTileY() + 1, (int) e.getX() / gui.getTileX() + 1);
>>>>>>> d7605d28088ad779271db2bc8d8289fe5e4407bd

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
        
<<<<<<< HEAD
        startTime = System.currentTimeMillis();

       
        gui.getBoardPanel().setBoardSize(Board.getMaxLanes(), Board.getMaxTiles());
        
       
  
        Thread inputThread = new Thread( new PlayerThread(board));
=======
        Thread inputThread = new Thread(new PlayerThread(board));
>>>>>>> d7605d28088ad779271db2bc8d8289fe5e4407bd
        inputThread.setDaemon(true);
        inputThread.start();

        gui.setBoardSize(board.getMaxLanes(), board.getMaxTiles());
        
        // Initially 0
        lastTimeUpdate = System.currentTimeMillis();

<<<<<<< HEAD
        while(!updateModel()){ 
            gui.getBoardPanel().setEntities(board.getEntities());
            gui.updateCooldownState(player.getCooldownState());
            gui.updateView(board.getEntities(), player.getCooldownState(), player.getTotalSun());
            
        }
       
=======
        int delay = 1000 / 60; // 60 FPS
        // This runs 60 times a second
        animationTimer = new Timer(delay, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                long currentTime = System.currentTimeMillis();
                double timeElapsed = (currentTime - lastTimeUpdate) / 1000.0; // calculate to delta seconds (time elapsed between frames)
                lastTimeUpdate = currentTime; // update for next frame

                // THIS IS WHAT UPDATES THE TIME
                // time is updated 60 times a second
                GameClock.addTime(timeElapsed);

                gameOver = updateModel(timeElapsed);

                gui.setEntities(board.getEntities());
                gui.updateSunCount(player.getTotalSun());
                gui.repaint(); // triggers paintComponent
            }
        });
        animationTimer.start(); // start the animation loop
>>>>>>> d7605d28088ad779271db2bc8d8289fe5e4407bd
    }

    // returns true if game over
    public boolean updateModel(double timeElapsed)
    {
        boolean gameOver = false;
<<<<<<< HEAD
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
=======
        int gameTime = GameClock.getTotalTimeSecondsInt();
 
        gameOver = board.updateBoard(timeElapsed);

        if(gameTime >= 180 && !gameOver)
        {
            GameClock.printTime();
            System.out.println("Player Wins");    
            gameOver = true;
        } 
>>>>>>> d7605d28088ad779271db2bc8d8289fe5e4407bd

        return gameOver;
    }



<<<<<<< HEAD
    private Player player;
    private PvzGui gui;
    private Board board;
    private double lastBoardUpdate;
    private int lastZombieSpawnTime;
    private int lastSunSpawnTime;
    private double startTime; 
    private int plantNo; // selection panels plant number
   
     
=======
        private Player player;
        private Gui gui;
        private Board board;
        private double lastBoardUpdate;
        private int lastZombieSpawnTime;
        private int lastSunSpawnTime;
        private double startTime; 
        private int plantNo; // selection panels plant number
        private Timer animationTimer;
        private long lastTimeUpdate;
        private boolean gameOver;

>>>>>>> d7605d28088ad779271db2bc8d8289fe5e4407bd
    }
