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
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;

public class Gui extends JPanel implements ActionListener {
    

    public Gui()
    {
        init();
    }

    public void init()
    {
        animations = new AnimationManager();
        animations.loadTile("environment", "Grass", 2);

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

        tileImage = new BufferedImage[3];
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
        frame.setResizable(true);   
      
        
       JPanel topPanel = new JPanel(new BorderLayout());
       topPanel.setPreferredSize(new Dimension(100, topPanelHeight));

        this.setLayout(new BorderLayout());
      
        frame.add(topPanel, BorderLayout.NORTH);
        this.setPreferredSize(new Dimension(screenWidth,screenHeight - topPanelHeight));
        frame.add(this, BorderLayout.CENTER );
        
      
        frame.setVisible(true);
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

        for(row = 0;  row < noLanes;  row++)
        {
            for(col = 0; col < noTiles; col++)
            {
                tiles = animations.getTiles("Grass");  
                if(tiles != null)
                {
                    /* drawImage parameters
                        tiles[0]: image to be drawn
                        col * tileX: x coordinate of top left corner of image
                        row * tileY: y coordinate of top right corner of image
                        tileX, tileY: width and height of image
                        null: image observer
                    */
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


    public int getx()
    {
        return x;
    }

    public void setx(int x)
    {
        this.x = x;
    }

    public void setEntities(ArrayList<Entity> e)
    {
        entities = e;
    }

    private int x;
    private boolean toggle; // temps

    private int noTiles;
    private int noLanes;

    private BufferedImage pea1;
    private BufferedImage zom1;
    private BufferedImage zom2;
    private ArrayList<Entity> entities;

    private int tileX; // size of each tile in pixels
    private int tileY;
    private int screenWidth;
    private int screenHeight;
    private int topPanelHeight;

    private int boardHeight;
    private int boardWidth;

    private JFrame frame ;
    private BufferedImage[] tileImage;

    private int frameCount;
    private int currentFrame;
   
    private AnimationManager animations;

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
    }
  
}
