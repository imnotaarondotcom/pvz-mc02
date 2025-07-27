import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class LevelSelector extends JPanel {
    public LevelSelector(int screenWidth, int screenHeight , int panelWidth, int panelHeight){
       

        this.setLayout(new BorderLayout());
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));

        start = new JButton("Continue");
        restart = new JButton("Restart");
        start.setPreferredSize(new Dimension(panelWidth- 10, panelHeight));
        restart.setPreferredSize(new Dimension(panelWidth - 10,  panelHeight));
        buttonPanel.add(restart);
        buttonPanel.add(start);
       

        this.add(buttonPanel, BorderLayout.SOUTH);
        this.setVisible(false);
        
    }

    public void setActionListener(ActionListener e){
        start.addActionListener(e);
        restart.addActionListener(e);
        
    }

    private JButton start;
    private JButton restart;

}
