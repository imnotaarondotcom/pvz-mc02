import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.*;

/**
 * Represents a button in the plant selection panel that allows the player to select and plant a specific plant type.
 * This button displays the plant's icon, manages its cooldown visually, and tracks its cost and readiness.
 *
 * @author Lim, Israel Sy
 * @author Enriquez, Aaron Mikael Cruz
 * @version 1.0
 * @since 2025-07-30
 */
public class PlantButton extends JButton
{
    /** The icon image displayed on the button. */
    private BufferedImage icon;
    /** The timestamp (in milliseconds) when the button was last clicked. */
    private long lastClicked = 0;
    /** The fill percentage of the cooldown overlay (1.0 = ready, 0.0 = just clicked). */
    private float fillPercent = 1f;
    /** The unique index identifying the plant type associated with this button. */
    private int plantIndex;
    /** The cooldown duration for this plant button in milliseconds. */
    private double cooldownTime;
    /** The sun cost to plant the associated plant. */
    private int cost;
    /** Flag indicating if this button is currently selected by the player. */
    private boolean isSelected = false;

    /**
     * Constructs a new PlantButton.
     *
     * @param icon The {@link BufferedImage} to be displayed as the button's icon.
     * @param index The unique integer index representing the plant type.
     * @param cooldown The cooldown time in seconds for this plant.
     * @param cost The sun cost to plant this plant.
     */
    public PlantButton(BufferedImage icon, int index, double cooldown, int cost)
    {
        this.icon = icon;
        this.plantIndex = index;
        this.cooldownTime = cooldown * 1000; // convert to milliseconds
        this.cost = cost;

        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
    }

    /**
     * Updates the visual state of the plant button, particularly its cooldown overlay.
     * This method should be called regularly (e.g., every game frame).
     */
    public void updatePlantButton()
    {
        // Update fillPercent only if button is not ready and not selected
        if (!isReady() && !isSelected)
        {
            long elapsed = System.currentTimeMillis() - lastClicked;
            fillPercent = Math.min(1f, (float) elapsed / (float) cooldownTime);
        }

        repaint(); // Request a repaint to update the visual
    }

    /**
     * Checks if the plant associated with this button is ready to be planted.
     * This means its cooldown has finished and the player has enough sun.
     *
     * @return {@code true} if the plant is ready to be planted; {@code false} otherwise.
     */
    public boolean isReady()
    {
        return (fillPercent == 1f) && (Player.getTotalSun() >= cost);
    }

    /**
     * Overrides the default {@code paintComponent} method to draw the button's icon,
     * selection tint, and cooldown overlay.
     *
     * @param g The {@link Graphics} context used for drawing.
     */
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (icon != null)
        {
            g.drawImage(icon, 0, 0, getWidth(), getHeight(), null);
        }

        if (isSelected)
        {
            g.setColor(new Color(255, 165, 0, 80)); // orange tint
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        // Draw cooldown overlay
        if (fillPercent < 1f)
        {
            g.setColor(new Color(0, 0, 0, 120)); // Semi-transparent black
            int fillHeight = (int) ((1f - fillPercent) * getHeight());
            g.fillRect(0, 0, getWidth(), fillHeight);
        }
    }

    /**
     * Retrieves the unique index of the plant associated with this button.
     * @return The plant's index.
     */
    public int getPlantIndex()
    {
        return plantIndex;
    }

    /**
     * Checks if this button is currently selected.
     * @return {@code true} if selected; {@code false} otherwise.
     */
    public boolean getIsSelected()
    {
        return isSelected;
    }

    /**
     * Marks the button as clicked, starting its cooldown.
     * Resets {@code lastClicked} to the current time and {@code fillPercent} to 0.
     */
    public void setClicked()
    {
        lastClicked = System.currentTimeMillis();
        fillPercent = 0f;
    }

    /**
     * Sets the selection status of the button.
     * @param trueOrFalse {@code true} to select the button, {@code false} to deselect.
     */
    public void setIsSelected(boolean trueOrFalse)
    {
        isSelected = trueOrFalse;
    }

    /**
     * Sets the fill percentage of the cooldown overlay.
     * @param percent The new fill percentage (0.0 to 1.0).
     */
    public void setFillPercent(float percent)
    {
        fillPercent = percent;
    }

    /**
     * Resets the button's state, making it fully ready and unselected.
     * This is typically called at the start of a new game or level.
     */
    public void resetButton()
    {
        fillPercent = 1f;
        lastClicked = 0;
        isSelected = false;
    }
}
