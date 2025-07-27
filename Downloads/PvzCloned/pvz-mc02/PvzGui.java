import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;

public class PvzGui extends JFrame{
    

    public PvzGui(){
         super("Plants Vs Zombies");
        noBorders = 6;

        topPanelHeight = 200;
        screenHeight = 1080 ;
        screenWidth = 1920;

        this.setSize(screenWidth, screenHeight);   
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
        this.setResizable(false);   
        setBackground(Color.black);

        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0 , 0 ,screenWidth, screenHeight);
        layeredPane.setLayout(null);
        
        

        // creates the panel above the board
        topPanel = new PlantSelectionPanel(screenWidth, topPanelHeight, noBorders);
        topPanel.setBounds(0 , 0 ,screenWidth, topPanelHeight);
        layeredPane.add(topPanel, JLayeredPane.DEFAULT_LAYER);

        boardPanel = new BoardPanel(topPanelHeight, screenHeight, screenWidth);
        boardPanel.setBounds(0,topPanelHeight, screenWidth, screenHeight - topPanelHeight);
        layeredPane.add(boardPanel, JLayeredPane.DEFAULT_LAYER );


        int panelHeight = 100;
        int panelWidth = 100;

        levelPanel = new LevelSelector(screenWidth, screenHeight, panelWidth, panelHeight);
        levelPanel.setBounds(screenWidth / 2 - panelWidth , screenHeight / 2 - panelHeight , panelWidth * 2, panelHeight * 2);
        
        layeredPane.add(levelPanel, JLayeredPane.MODAL_LAYER);

        
       
        
        this.add(layeredPane);
        this.setVisible(true);
    }

    

    public void setMouseListener(MouseListener board, MouseListener selection){
        boardPanel.addMouseListener(board);
        topPanel.addMouseListener(selection);
    }

    public void setActionListener(ActionListener e){
        levelPanel.setActionListener(e);
    }
    public void updateView(ArrayList<Entity> entitites, ArrayList<Boolean> cdList, int sunCount ){
        boardPanel.setEntities(entitites);
        boardPanel.repaint();
        topPanel.updateCooldownState(cdList);
        topPanel.repaint();
        topPanel.updateSunCount(sunCount);
    }

    

    public void updateSunCount(int sunCount){
        topPanel.updateSunCount(sunCount);
    }

    public void updateCooldownState(ArrayList<Boolean> cdList){
        topPanel.updateCooldownState(cdList);
        topPanel.repaint();
    }

    public int getScreenWidth(){
        return screenWidth;
    }

    public int getScreenHeight(){
        return screenHeight;
    }

    public int getNoBorders(){
        return noBorders;
    }

    public BoardPanel getBoardPanel(){
        return boardPanel;
    }

    public void setBoardSize(){

    }

    

    private int noBorders;
    private int topPanelHeight;
    private int screenHeight;
    private int screenWidth;
    private PlantSelectionPanel topPanel;
    private BoardPanel boardPanel;
    private LevelSelector levelPanel;
    private JLayeredPane layeredPane;
}
