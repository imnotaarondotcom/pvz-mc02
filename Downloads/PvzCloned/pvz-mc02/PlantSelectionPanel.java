import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.*;

/**
 * Manages the top panel of the game UI, which includes the sun counter and
 * buttons for selecting and planting various plant types. The available plants
 * change based on the current game level.
 *
 * @author Lim, Israel Sy
 * @author Enriquez, Aaron Mikael Cruz
 * @version 1.0
 * @since 2025-07-30
 */
public class PlantSelectionPanel extends JPanel
{
    /** Manages all game animations and images. */
    private AnimationManager animations;
    /** The height of this panel. */
    private int panelHeight;
    /** The width of this panel. */
    private int panelWidth;
    /** The number of border divisions for layout. */
    private int noBorders;
    /** Label displaying the current sun count. */
    private JLabel sunCountLabel;
    /** Panel containing the sun count label. */
    private JPanel sunCountPanel;
    /** Panel for displaying the sun icon and count. */
    private JPanel sunDisplayPanel;
    /** List of all {@link PlantButton} instances in this panel. */
    private ArrayList<PlantButton> plantButtons;

    /** Background image for the sun counter box. */
    private BufferedImage sunboxImg;
    /** Image for the borders around plant selection slots. */
    private BufferedImage borderImg;

    /**
     * Constructs a new PlantSelectionPanel.
     *
     * @param animations The {@link AnimationManager} for loading images.
     * @param topPanelWidth The total width of this panel.
     * @param topPanelHeight The total height of this panel.
     * @param noBorders The number of border divisions for layout.
     * @param level The current game level, which determines available plants.
     */
    public PlantSelectionPanel(AnimationManager animations, int topPanelWidth, int topPanelHeight, int noBorders, int level)
    {
        this.setLayout(null); // Use absolute positioning
        this.animations = animations;
        this.noBorders = noBorders;
        this.panelHeight = topPanelHeight;
        this.panelWidth = topPanelWidth;
        this.setPreferredSize(new Dimension(topPanelWidth, topPanelHeight));

        // Load extra images
        this.sunboxImg = animations.getImages("sunbox")[0];
        this.borderImg = animations.getImages("border")[0];

        // Sun counter setup
        sunCountPanel = new JPanel(new BorderLayout());
        sunDisplayPanel = new JPanel(new FlowLayout());
        sunCountLabel = new JLabel();
        sunCountLabel.setFont(new Font("Arial", Font.BOLD, 40));
        sunDisplayPanel.add(sunCountLabel);
        sunDisplayPanel.setOpaque(false); // Make transparent to show background image

        sunCountPanel.add(sunDisplayPanel, BorderLayout.SOUTH);
        sunCountPanel.setBounds(0, 0, topPanelWidth / noBorders, topPanelHeight);
        sunCountPanel.setOpaque(false);

        this.add(sunCountPanel);

        // Plant buttons setup based on level
        plantButtons = new ArrayList<>();
        createPlantButtonsForLevel(level);
    }

    /**
     * Populates the panel with {@link PlantButton}s based on the current game level.
     * Different plants become available at higher levels.
     *
     * @param level The current game level.
     */
    private void createPlantButtonsForLevel(int level)
    {
        ArrayList<String> plantsForLevel = new ArrayList<>();

        // Always include these basic plants
        plantsForLevel.add("sunflower_icon");
        plantsForLevel.add("peashooter_icon");

        if (level >= 2)
        {
            plantsForLevel.add("wallnut_icon");
        }

        if (level >= 3)
        {
            plantsForLevel.add("cherrybomb_icon");
        }

        // Always add shovel as the last button
        plantsForLevel.add("shovel");

        // Create buttons
        for (int i = 0; i < plantsForLevel.size(); i++)
        {
            String iconName = plantsForLevel.get(i);
            BufferedImage icon = animations.getImages(iconName)[0];
            int plantCost = getPlantCost(iconName);
            double plantCooldown = getPlantCooldown(iconName);

            PlantButton btn = new PlantButton(icon, i + 1, plantCooldown, plantCost);
            // Position buttons horizontally based on their index
            btn.setBounds((panelWidth * (i + 1)) / noBorders, 0, panelWidth / noBorders, panelHeight);
            btn.setOpaque(false);
            this.add(btn);
            plantButtons.add(btn);
        }
    }

    /**
     * Overrides the default {@code paintComponent} method to draw the background
     * images for the sunbox and plant slot borders.
     *
     * @param g The {@link Graphics} context used for drawing.
     */
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Draw sunbox background
        g2.drawImage(sunboxImg, 0, 0, panelWidth / noBorders, panelHeight, null);

        int i, x;
        // Draw borders for each plant slot (index 0 reserved for sunbox)
        for (i = 1; i <= 5; i++)
        {
            x = (panelWidth * i) / noBorders;
            g2.drawImage(borderImg, x, 0, panelWidth / noBorders, panelHeight, null);
        }
    }

    /**
     * Updates the displayed sun count in the UI.
     * @param sunCount The current total sun count.
     */
    public void updateSunCount(int sunCount)
    {
        sunCountLabel.setText(Integer.toString(sunCount));
    }

    /**
     * Resets the selection status of all plant buttons, unselecting any currently selected button.
     */
    public void resetSelectedButtons()
    {
        for (PlantButton pb : plantButtons)
        {
            pb.setIsSelected(false);
        }
    }

    /**
     * Retrieves the cooldown time for a specific plant type.
     *
     * @param p The name of the plant icon (e.g., "peashooter_icon").
     * @return The cooldown time in seconds.
     */
    public double getPlantCooldown(String p)
    {
        switch (p)
        {
            case "peashooter_icon":
                return Peashooter.getCooldown();
            case "sunflower_icon":
                return Sunflower.getCooldown();
            case "wallnut_icon":
                return Wallnut.getCooldown();
            case "cherrybomb_icon":
                return Cherrybomb.getCooldown();
            default:
                return 0; // Default for unrecognized plant or shovel
        }
    }

    /**
     * Retrieves the sun cost for a specific plant type.
     *
     * @param p The name of the plant icon (e.g., "peashooter_icon").
     * @return The sun cost.
     */
    public int getPlantCost(String p)
    {
        switch (p)
        {
            case "peashooter_icon":
                return Peashooter.getCost();
            case "sunflower_icon":
                return Sunflower.getCost();
            case "wallnut_icon":
                return Wallnut.getCost();
            case "cherrybomb_icon":
                return Cherrybomb.getCost();
            default:
                return 0; // Default for unrecognized plant or shovel
        }
    }

    /**
     * Retrieves the list of all {@link PlantButton}s managed by this panel.
     * @return An {@link ArrayList} of {@link PlantButton} objects.
     */
    public ArrayList<PlantButton> getPlantButtons()
    {
        return plantButtons;
    }
}
