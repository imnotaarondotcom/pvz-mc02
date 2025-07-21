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

        screenHeight = 1080;
        screenWidth = 1920;
        
        


        frame = new JFrame("Plants Vs Zombies");
        frame.setSize(screenWidth, screenHeight);
   
        setBackground(Color.black);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
        frame.setResizable(false);   
        

        init();
        frame.add(this);
        
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
     
        frame.add(tilePanel, BorderLayout.SOUTH);

        
    }

   public void getImage(){
    try{
     
       zom1 = ImageIO.read(new File("zombie1.png"));
       zom2 = ImageIO.read(new File("zombie2.png"));
        pea1 = ImageIO.read(new File("Peashooter1.png"));
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

        for(row = 0;  row < noLanes;  row++){
            for(col = 0; col < noTiles; col++){
                g2.drawImage(tileImage[0], col * tileY ,row * tileX , tileY, tileX , null);
                
            
            }
        }

     
        
       


        Entity entity;
        Iterator<Entity> entIterator;
        if(entities != null){
            entIterator = entities.iterator();

            while(entIterator.hasNext()){
               
                entity = entIterator.next();
                g2.drawImage(entity.getMovementAnimation()[0], tileY * entity.getTileNo(),tileX * entity.getLaneNo(),tileY,tileX,null);
            }
        }
        
        
       // g2.setColor(Color.blue);

        
        toggle = !(toggle);
         if(toggle){
            g2.drawImage(zom1, x,200,100,200,null);
         }
        
        else{
             g2.drawImage(zom2, x,200,100,200,null);
        }
        
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

    public void setTileSize(){
        tileX = screenWidth / noTiles;
        tileY = screenHeight / noLanes;

        System.out.printf("x %d y %d ", tileX, tileY);
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

    private JFrame frame ;
    private BufferedImage[] tileImage;
   


    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
    }
  
}
