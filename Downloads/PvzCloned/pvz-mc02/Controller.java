import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.Timer;

public class Controller  extends MouseAdapter
{
    public Controller(Gui g , Board b)
    {
        player = new Player(b);
        gui = g;
        board = b;
        gameOver = false;
        addMouseListener();
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

    @Override
    public void mousePressed(MouseEvent e)
    {
        int laneNo = (int) e.getY()  / gui.getTileY();
        int tileNo = (int) e.getX() / gui.getTileX() ;

        player.tryToplacePlant(plantNo, laneNo, tileNo);
        //board.getTile(laneNo,tileNo).placePlant(new Peashooter(laneNo, tileNo));
    
        System.out.printf("Lane %d, TIle %d\n", (int) e.getY()  / gui.getTileY() + 1, (int) e.getX() / gui.getTileX() + 1);

        player.collectSun(board.getTile(laneNo, tileNo).getSunList());
        gui.updateSunCount(player.getTotalSun());
        plantNo = 0;
    }

    

    public void start()
    {
        System.out.println("*** PLANTS VS. ZOMBIES *** \nPress 1 To Play");
     
        int input = 0;
        GameClock clock = new GameClock();
        
        Thread inputThread = new Thread(new PlayerThread(board));
        inputThread.setDaemon(true);
        inputThread.start();

        gui.setBoardSize(board.getMaxLanes(), board.getMaxTiles());
        
        // Initially 0
        lastTimeUpdate = System.currentTimeMillis();

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
    }

    // returns true if game over
    public boolean updateModel(double timeElapsed)
    {
        boolean gameOver = false;
        int gameTime = GameClock.getTotalTimeSecondsInt();
 
        gameOver = board.updateBoard(timeElapsed);

        if(gameTime >= 180 && !gameOver)
        {
            GameClock.printTime();
            System.out.println("Player Wins");    
            gameOver = true;
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
        private Timer animationTimer;
        private long lastTimeUpdate;
        private boolean gameOver;

    }
