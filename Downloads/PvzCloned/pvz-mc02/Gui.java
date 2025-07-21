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
    

    public Gui(){
        tileImage = new BufferedImage[3];
        topPanelHeight = 200;
        screenHeight = 1080 ;
        screenWidth = 1920;
        
     

        frame = new JFrame("Plants Vs Zombies");
        frame.setSize(screenWidth, screenHeight);
   
        setBackground(Color.black);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
        frame.setResizable(false);   
      
        
       JPanel topPanel = new JPanel(new BorderLayout());
       topPanel.setPreferredSize(new Dimension(100,topPanelHeight));

       this.setLayout(new BorderLayout());

        init();
        frame.add(topPanel, BorderLayout.NORTH);
        this.setPreferredSize(new Dimension(screenWidth,screenHeight - topPanelHeight));
        frame.add(this, BorderLayout.CENTER );
        
      
        frame.setVisible(true);
        getImage();

      
        
    }

    public void init(){
     //   frame.setLayout(new GridBagLayout());
        
        GridBagLayout grid = new GridBagLayout();
        GridBagConstraints constraint = new GridBagConstraints();
        constraint.gridx = 100;
        constraint.gridy = 100;
        grid.addLayoutComponent(frame, constraint);
        JPanel tilePanel = new JPanel(new FlowLayout());
        
        tilePanel.setBackground( Color.blue);
       // frame.add(tilePanel, BorderLayout.SOUTH);
       
        
    }

   public void getImage(){
    try{
     
       zom1 = ImageIO.read(new File("zombie1.png"));
       zom2 = ImageIO.read(new File("zombie2.png"));
        pea1 = ImageIO.read(new File("Peashooter1.png"));
        tileImage[1] = ImageIO.read(new File("Grass2.png"));
        tileImage[0] = ImageIO.read(new File("Grass1.png"));
    }catch(IOException e){
        e.printStackTrace();
    }
    
   }
    @Override
    public void paintComponent(Graphics g ){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g; 
        int row = 0;
        int col = 0;
        int x = 0;
        int y = 0;

        for(row = 0;  row < noLanes;  row++){
            for(col = 0; col < noTiles; col++){
                //g2.drawImage(tileImage[0], col * tileX, row * tileY, tileX, tileY , null);
                g2.drawImage(tileImage[0], col * tileX, row * tileY, tileX, tileY , null);
                if((col + row) % 2 == 0){
                    g2.drawImage(tileImage[1], col * tileX, row * tileY, tileX, tileY , null);
                }
                
            
            }
        }

        Entity entity;
        Iterator<Entity> entIterator;
        if(entities != null){
            entIterator = entities.iterator();

            toggle = !(toggle);
            while(entIterator.hasNext()){
               
                entity = entIterator.next();
                x = (int)(tileX * entity.getTileNo() - entity.getPosition() * tileX  );
                y = tileY * entity.getLaneNo();

                if(toggle){
                    g2.drawImage(entity.getMovementAnimation()[0], x, y,tileX,tileY ,null);

                   
                }
                else{
                     g2.drawImage(entity.getMovementAnimation()[1], x, y,tileX,tileY ,null);

                }
                
            }
        }
        
        
       // g2.setColor(Color.blue);

        
      
      
    while(x <= 1000){
    g2.drawImage(pea1, x,200,100,100,null);
    x += 100;
    }
    x = 0;
      
    }

    /* 
      @Override
    public void run() {
       x = 0;
        while(true){
            repaint();
            toggle = !(toggle);
            x+= 10;

            try{
                Thread.sleep(1000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        
    }
*/

    public void setBoardSize(int l, int t){

        setNoLanes(l);
        setNoTiles(t);

        boardHeight = screenHeight - topPanelHeight ;
        boardWidth = screenWidth ;

        setTileSize();
    }

    public void setTileSize(){
        
        tileX = boardWidth / noTiles;
        tileY = boardHeight / noLanes;

        System.out.printf("x %d y %d ", tileX, tileY);


          System.out.printf("Grid: %dx%d tiles | Tile size: %dx%d | Panel size: %dx%d%n",
    noTiles, noLanes, 
    tileX, tileY,
    getWidth(), getHeight());
    }

    public void setNoLanes(int n){
        noLanes = n;
    }

    public void setNoTiles(int n){
        noTiles = n;
    }


    public int getx(){
        return x;
    }

    public void setx(int x){
        this.x = x;
    }

    public void setEntities(ArrayList<Entity> e){
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
   


    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
    }
  
}
