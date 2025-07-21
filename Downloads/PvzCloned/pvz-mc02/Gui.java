import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
    JFrame frame ;
   

    public Gui(){
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
        getImage();
        frame.setVisible(true);
        
    }

    public void init(){
     //   frame.setLayout(new GridBagLayout());
        GridBagLayout grid = new GridBagLayout();

        JPanel tilePanel = new JPanel(new FlowLayout());
        
        frame.add(tilePanel, BorderLayout.SOUTH);
    }

   public void getImage(){
    try{
     
       zom1 = ImageIO.read(new File("zombie1.png"));
       zom2 = ImageIO.read(new File("zombie2.png"));
        pea1 = ImageIO.read(new File("Peashooter1.png"));

    }catch(IOException e){
        e.printStackTrace();
    }
    
   }
    @Override
    public void paintComponent(Graphics g ){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g; 
        
        Entity entity;
        Iterator<Entity> entIterator;
        if(entities != null){
            entIterator = entities.iterator();

            while(entIterator.hasNext()){
                System.out.println("called");
                entity = entIterator.next();
                g2.drawImage(entity.getMovementAnimation()[0], x,500,100,200,null);
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

    private int tileSize; // size of each tile in pixels
    private int noRows;
    private int noCols;
    private int screenWidth;
    private int screenHeight;


    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
    }
  
}
