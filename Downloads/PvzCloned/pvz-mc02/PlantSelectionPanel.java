import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class PlantSelectionPanel extends JPanel{
    

    public PlantSelectionPanel(int topPanelWidth , int topPanelHeight){
    panelHeight = topPanelHeight;
    panelWidth = topPanelWidth;
    this.setPreferredSize(new Dimension(topPanelWidth,topPanelHeight));

    animations = new AnimationManager();
    animations.loadImages("sunbox" , 1);
    animations.loadImages("border" , 1);

    }
    @Override
    public void paintComponent(Graphics g){   
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        BufferedImage sun = animations.getImages("sunbox")[0];
        BufferedImage border = animations.getImages("border")[0];
        int i = 1;

        g2.drawImage(sun, 0 , 0 , panelWidth / 6 , panelHeight, null);

        for(i = 1 ; i < 6; i++){
            g2.drawImage(border, panelWidth * i / 6 , 0 , panelWidth / 6 , panelHeight , null);
        }
        

   
    }
     AnimationManager animations;
     int panelHeight;
     int panelWidth;
}
