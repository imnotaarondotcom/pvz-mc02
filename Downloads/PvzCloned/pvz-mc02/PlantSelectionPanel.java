import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class PlantSelectionPanel extends JPanel{
    
    /**creates the selection panel for plants 
     * 
     * @param noBorders - no of borders in the plant selection panel
     * @param topPanelWidth - width of the panel to be created
     * @param topPanelHeight - length of the panel to be created
     */
    public PlantSelectionPanel(int topPanelWidth , int topPanelHeight , int noBorders){

        this.noBorders = noBorders;

        panelHeight = topPanelHeight;
        panelWidth = topPanelWidth;
        this.setPreferredSize(new Dimension(topPanelWidth,topPanelHeight));

        animations = new AnimationManager();
        animations.loadImages("borders" , "sunbox" , 1);
        animations.loadImages("borders", "border" , 1);

    }
    @Override
    public void paintComponent(Graphics g){   
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;


        
        BufferedImage sun = animations.getImages("sunbox")[0];
        BufferedImage border = animations.getImages("border")[0];
        int i = 1;

        g2.drawImage(sun, 0 , 0 , panelWidth / noBorders , panelHeight, null);

        for(i = 1 ; i < 6; i++){
            g2.drawImage(border, panelWidth * i / noBorders , 0 , panelWidth / noBorders , panelHeight , null);
        }
        

   
    }
     AnimationManager animations;
     int panelHeight;
     int panelWidth;
     int noBorders;
}
