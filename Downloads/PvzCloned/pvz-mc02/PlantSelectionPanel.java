import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.Iterator;

public class PlantSelectionPanel extends JPanel{
    
    /**creates the selection panel for plants 
     * 
     * @param noBorders - no of borders in the plant selection panel
     * @param topPanelWidth - width of the panel to be created
     * @param topPanelHeight - length of the panel to be created
     */
    public PlantSelectionPanel(int topPanelWidth , int topPanelHeight , int noBorders){
        this.setLayout(null);
        cooldownState = new ArrayList<>();

        sunCountPanel = new JPanel(new BorderLayout());
        sunDisplayPanel = new JPanel(new FlowLayout());

        sunCountPanel.add(sunDisplayPanel, BorderLayout.SOUTH);
        
        sunCountPanel.setBounds(0 , 0 ,topPanelWidth / noBorders, topPanelHeight);
        sunCountPanel.setOpaque(false);
        sunDisplayPanel.setOpaque(false);

        sunCountLabel = new JLabel();
        sunCountLabel.setFont(new Font("Calibri", Font.HANGING_BASELINE, 35));


        sunDisplayPanel.add(sunCountLabel); 

        
        this.add(sunCountPanel);


        this.noBorders = noBorders;

        panelHeight = topPanelHeight;
        panelWidth = topPanelWidth;
        this.setPreferredSize(new Dimension(topPanelWidth,topPanelHeight));

        animations = new AnimationManager();
        animations.loadImages("borders" , "sunbox" , 1);
        animations.loadImages("borders", "border" , 1);
        animations.loadImages("icons", "peashooter_icon", 1);
        animations.loadImages("icons", "sunflower_icon", 1);
        animations.loadImages("icons", "cherrybomb_icon", 1);
    }
    @Override
    public void paintComponent(Graphics g){   
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        

      
            
        BufferedImage sun = animations.getImages("sunbox")[0];
        BufferedImage border = animations.getImages("border")[0];
        ArrayList<BufferedImage> icons = new ArrayList<BufferedImage>();
        icons.add(animations.getImages("peashooter_icon")[0]);
        icons.add(animations.getImages("sunflower_icon")[0]);
        icons.add(animations.getImages("cherrybomb_icon")[0]);

        int i = 1;
        Iterator<Boolean> iterator = cooldownState.iterator();
        g2.drawImage(sun, 0 , 0 , panelWidth / noBorders , panelHeight, null);

        for(i = 1 ; i < 6; i++){
            BufferedImage icon = null;
           
            
            

            g2.drawImage(border, panelWidth * i / noBorders , 0 , panelWidth / noBorders , panelHeight , null);

            if(i <= icons.size()){
                float alpha = 0.10f; // 50% transparent



                if(iterator.hasNext()){
                    
                    if(iterator.next()){
                        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                    }
                }
                

                icon = icons.get(i - 1);   
                g2.drawImage(icon, panelWidth * i / noBorders , 0 , panelWidth / noBorders , panelHeight , null);   

                // Restore full opacity
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                 
            }
             
        }
        

   
    }

    public void updateSunCount(int sunCount){
        sunCountLabel.setText(Integer.toString(sunCount));
    }

    public void updateCooldownState(ArrayList<Boolean> cdList){
        cooldownState = cdList;
    }
    private AnimationManager animations;
    private int panelHeight;
    private int panelWidth;
    private int noBorders;
    private JLabel sunCountLabel;
    private JPanel sunCountPanel;
    private JPanel sunDisplayPanel;
    private ArrayList<Boolean> cooldownState;
}
