import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;

public class Gui extends JPanel  {
    

    public Gui()
    {
        init();
    }


    public void init()
    {
        animations = new AnimationManager();

        // Environment
        animations.loadImages("environment", "Grass", 2);

        // Icons
        animations.loadAnimation("icons", "sun_idle", 1);


        // Normal Zombie animations
        // walk
        animations.loadAnimation("zombie_walk", "zombie_walkA", 7);
        animations.loadAnimation("zombie_walk", "zombie_walkB", 7);
        animations.loadAnimation("zombie_walk", "zombie_walkC", 7);

        // eat
        animations.loadAnimation("zombie_eat", "zombie_eatA", 7);
        animations.loadAnimation("zombie_eat", "zombie_eatB", 7);
        animations.loadAnimation("zombie_eat", "zombie_eatC", 5);

        // Conehead Zombie animations
        // walk
        animations.loadAnimation("zombie_walk", "zombiecone_walkA", 7);
        animations.loadAnimation("zombie_walk", "zombiecone_walkB", 7);
        animations.loadAnimation("zombie_walk", "zombiecone_walkC", 7);

        // eat
        animations.loadAnimation("zombie_eat", "zombiecone_eatA", 7);
        animations.loadAnimation("zombie_eat", "zombiecone_eatB", 7);
        animations.loadAnimation("zombie_eat", "zombiecone_eatC", 7);

        // Flag Zombie animations
        // walk
        animations.loadAnimation("zombie_walk", "zombieflag_walkA", 7);
        animations.loadAnimation("zombie_walk", "zombieflag_walkB", 7);

        // eat
        animations.loadAnimation("zombie_eat", "zombieflag_eatA", 7);
        animations.loadAnimation("zombie_eat", "zombieflag_eatB", 7);

        // Zombie die animation (NOT IMPLEMENTED YET, IDK IF WE SHOULD)
        animations.loadAnimation("zombie_die", "zombie_die", 6);

        // Peashooter idle animation
        animations.loadAnimation("plants/peashooter", "peashooter_idle", 8);

        // Sunflower idle animation
        animations.loadAnimation("plants/sunflower", "sunflower_idle", 10);

        // Pea (projectile) idle (just one frame)
        animations.loadAnimation("plants/peashooter", "pea_idle", 1);

        // Pea hit animation
        animations.loadAnimation("plants/peashooter", "pea_hit", 2);

        noBorders = 6;

        topPanelHeight = 200;
        screenHeight = 1080 ;
        screenWidth = 1920;

        frameCount = 0;
        currentFrame = 0;



        frame = new JFrame("Plants Vs Zombies");
        frame.setSize(screenWidth, screenHeight);

        setBackground(Color.black);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
        frame.setResizable(false);   


        // creates the panel above the board
        topPanel = new PlantSelectionPanel(screenWidth, topPanelHeight, noBorders);
       
        frame.add(topPanel);


        this.setLayout(new BorderLayout());


        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(topPanel, BorderLayout.NORTH);
        this.setPreferredSize(new Dimension(screenWidth,screenHeight - topPanelHeight));
        frame.add(this, BorderLayout.CENTER );
        frame.setVisible(true);
    }


     
    public void setMouseListener(MouseListener board, MouseListener selection){
        this.addMouseListener(board);
        topPanel.addMouseListener(selection);
    }

    
    public void mousePressed(MouseEvent e){
        System.out.println(e.getX() + " "+ e .getY());
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g; 
        BufferedImage[] tiles;
        int row = 0;
        int col = 0;
        int x = 0;
        int y = 0;

        for(row = 0;  row < noLanes;  row++){
            for(col = 0; col < noTiles; col++){
                 tiles = animations.getImages("Grass");  
                if(tiles != null){
                  

                    
                    g2.drawImage(tiles[0], col * tileX, row * tileY, tileX, tileY , null);

                    if((col + row) % 2 == 0)
                    {
                      g2.drawImage(tiles[1], col * tileX, row * tileY, tileX, tileY , null);
                    }
                }
            }
        }

        Entity entity;
        Iterator<Entity> entIterator;
        if(entities != null)
        {
            entIterator = entities.iterator();
            BufferedImage[] animation;
            toggle = !(toggle);
            while(entIterator.hasNext())
            {
                entity = entIterator.next();
                x = (int)(tileX * entity.getTileNo() - entity.getPosition() * tileX  );
                y = tileY * entity.getLaneNo();

                animation = animations.getAnimation(entity.getType(), entity.getModifier(), entity.getState());
                    
                if (animation != null)
                {
                    g2.drawImage(animation[currentFrame % animation.length ], x, y, (int)(tileX), (int)(tileY), null);          
                }
                else
                {
                    System.out.println("Animation not found.");
                }
            }
        }

        if(frameCount >= 3)
        {
            frameCount = 0;
            currentFrame++;
        }
        if(currentFrame >=1000){
            currentFrame = 0;
        }
          frameCount++;
    }

    public void setBoardSize(int l, int t)
    {

        setNoLanes(l);
        setNoTiles(t);

        boardHeight = screenHeight - topPanelHeight ;
        boardWidth = screenWidth ;

        setTileSize();
    }

    public void setTileSize()
    {
        tileX = boardWidth / noTiles;
        tileY = boardHeight / noLanes;

        System.out.printf("x %d y %d ", tileX, tileY);


        System.out.printf("Grid: %dx%d tiles | Tile size: %dx%d | Panel size: %dx%d%n",
                            noTiles, noLanes, 
                            tileX, tileY,
                            getWidth(), getHeight());
    }

    public void setNoLanes(int n)
    {
        noLanes = n;
    }

    public void setNoTiles(int n)
    {
        noTiles = n;
    }

    public int getTileX(){
        return tileX;
    }
    
    public int getTileY(){
        return tileY;
    }

    public void setEntities(ArrayList<Entity> e)
    {
        entities = e;
    }

    public int getNoBorders(){
        return noBorders;
    }

    public int getScreenWidth(){
        return screenWidth;
    }

    public int getScreenHeight(){
        return screenHeight;
    }

    private int x;
    private boolean toggle; // temps

    private int noTiles;
    private int noLanes;

    
    private ArrayList<Entity> entities;

    private int tileX; // size of each tile in pixels
    private int tileY;
    private int screenWidth;
    private int screenHeight;
    private int topPanelHeight;

    private int boardHeight;
    private int boardWidth;

    private JFrame frame ;
    

    private int frameCount;
    private int currentFrame;
   
    private AnimationManager animations;

    private PlantSelectionPanel topPanel;
    private int noBorders;
  
  
}
