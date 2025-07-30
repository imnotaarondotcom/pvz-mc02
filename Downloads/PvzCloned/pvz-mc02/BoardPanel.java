import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Renders the game board and all active entities.
 *
 * Handles drawing the grass tiles, placing animation frames for plants and zombies,
 * and syncing animation states using a frame counter.
 *
 * Called and updated by the game controller.
 *
 * @author Lim, Israel Sy
 * @author Enriquez, Aaron Mikael Cruz
 * @version 1.0
 * @since 2025-07-30
 */
public class BoardPanel extends JPanel
{
    /** The number of tiles per lane. */
    private int noTiles;
    /** The number of lanes on the board. */
    private int noLanes;
    /** The width of a single tile in pixels. */
    private int tileSizeX;
    /** The height of a single tile in pixels. */
    private int tileSizeY;
    /** The height of the game board area in pixels. */
    private int boardHeight;
    /** The width of the game board area in pixels. */
    private int boardWidth;

    /** Current animation frame index for zombies. */
    private int zombieCurrentFrame;
    /** Current animation frame index for sunflowers. */
    private int sunflowerCurrentFrame;
    /** Current animation frame index for peashooters. */
    private int peashooterCurrentFrame;
    /** Current animation frame index for cherry bombs. */
    private int cherrybombCurrentFrame;
    /** Current animation frame index for wall-nuts. */
    private int wallnutCurrentFrame;
    /** General current animation frame index. */
    private int currentFrame;
    /** Global frame counter for animation synchronization. */
    private int frameCount;

    /** Manages loading and providing animation images. */
    private AnimationManager animations;
    /** List of all entities to be rendered on the board. */
    private ArrayList<Entity> entities;

    /**
     * Constructs the board panel with dimensions and animation data.
     *
     * @param animManager The animation manager instance.
     * @param screenHeight Total screen height.
     * @param screenWidth Total screen width.
     * @param plantSelectionPanelHeight Height reserved for the plant selection UI.
     */
    public BoardPanel(AnimationManager animManager, int screenHeight, int screenWidth, int plantSelectionPanelHeight)
    {
        this.animations = animManager;
        setBoardSize(screenHeight, screenWidth, plantSelectionPanelHeight);
        noLanes = PvZDriver.getMaxLanes();
        noTiles = PvZDriver.getMaxTiles();

        currentFrame = 0;
        zombieCurrentFrame = 0;
        peashooterCurrentFrame = 0;
        sunflowerCurrentFrame = 0;
        cherrybombCurrentFrame = 0;
        wallnutCurrentFrame = 0;
        frameCount = 0;
    }

    /**
     * Draws the game board, grass tiles, and all animated entities.
     *
     * @param g The graphics context.
     */
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        BufferedImage[] tiles;

        // Draw grass tiles
        for (int row = 0; row < noLanes; row++)
        {
            for (int col = 0; col < noTiles; col++)
            {
                tiles = animations.getImages("Grass");
                if (tiles != null)
                {
                    g2.drawImage(tiles[0], col * tileSizeX, row * tileSizeY, tileSizeX, tileSizeY , null);
                    if ((col + row) % 2 == 0) // Alternate grass tile
                    {
                        g2.drawImage(tiles[1], col * tileSizeX, row * tileSizeY, tileSizeX, tileSizeY , null);
                    }
                }
            }
        }

        // Draw all animated entities
        if (entities != null)
        {
            boolean cherrybombFlag = false;

            for (Entity entity : entities)
            {
                // Calculate entity position on the panel
                int xPos = (int)(tileSizeX * entity.getTileNo() + entity.getPositionX() * tileSizeX);
                int yPos = tileSizeY * entity.getLaneNo() + (int)(tileSizeY * entity.getPositionY());

                BufferedImage[] animation = animations.getAnimation(entity.getType(), entity.getState());

                if (entity.getType().equals("cherrybomb"))
                {
                    cherrybombFlag = true;
                }

                if (animation != null)
                {
                    int width = (int)(tileSizeX * entity.getSize());
                    int height = (int)(tileSizeY * entity.getSize());

                    // Offset adjustments for specific entity types
                    if (entity.getType().equals("zombie") ||
                        entity.getType().equals("zombiecone") ||
                        entity.getType().equals("zombieflag") ||
                        entity.getType().equals("pea"))
                    {
                        xPos -= (int)(0.5 * tileSizeX);
                    }
                    else if (entity.getType().equals("cherrybomb") && entity.getState().equals("exploded"))
                    {
                        xPos -= tileSizeX;
                        yPos -= tileSizeY;
                    }
                    else if (entity.getType().equals("sun"))
                    {
                        xPos += (tileSizeX / 4);
                    }

                    currentFrame = getCurrentFrameForEntity(entity);
                    g2.drawImage(animation[currentFrame % animation.length], xPos, yPos, width, height, null);
                }
                else
                {
                    System.out.println("Animation not found.");
                }
            }

            frameCount++;
            updateCurrentFrames(frameCount, cherrybombFlag);
        }
    }

    /**
     * Updates the current animation frame counters based on a global frame count.
     * Different entities update at different frame rates.
     *
     * @param frameCount The total number of frames elapsed.
     * @param cherrybombFlag {@code true} if a cherrybomb is active.
     */
    private void updateCurrentFrames(int frameCount, boolean cherrybombFlag)
    {
        // 8-frame rate animation
        if ((frameCount % 8) == 0)
        {
            if (!cherrybombFlag)
            {
                cherrybombCurrentFrame = 0;
            }
            else
            {
                cherrybombCurrentFrame++;
            }

            zombieCurrentFrame++;
        }

        // 5-frame rate animation
        if ((frameCount % 5) == 0)
        {
            peashooterCurrentFrame++;
            sunflowerCurrentFrame++;
            wallnutCurrentFrame++;
        }
    }

    /**
     * Gets the current animation frame index for a given entity type.
     *
     * @param entity The entity to retrieve frame for.
     * @return The current animation frame index for the entity's type.
     */
    private int getCurrentFrameForEntity(Entity entity)
    {
        String entityType = entity.getType();

        switch (entityType)
        {
            case "zombie":
            case "zombiecone":
            case "zombieflag":
                return zombieCurrentFrame;
            case "sunflower":
                return sunflowerCurrentFrame;
            case "peashooter":
                return peashooterCurrentFrame;
            case "cherrybomb":
                return cherrybombCurrentFrame;
            case "wallnut":
                return wallnutCurrentFrame;
            default:
                return 0; // Default to first frame if type not recognized
        }
    }

    /**
     * Sets the size of the board and its tiles based on screen dimensions.
     *
     * @param screenHeight The total screen height.
     * @param screenWidth The total screen width.
     * @param plantSelectionPanelHeight The height of the plant selection area.
     */
    public void setBoardSize(int screenHeight, int screenWidth, int plantSelectionPanelHeight)
    {
        noLanes = PvZDriver.getMaxLanes();
        noTiles = PvZDriver.getMaxTiles();

        boardHeight = screenHeight - plantSelectionPanelHeight;
        boardWidth = screenWidth;

        setTileSize();
    }

    /**
     * Calculates and sets individual tile size based on board dimensions.
     * Prints the calculated tile and panel sizes to the console.
     */
    public void setTileSize()
    {
        tileSizeX = boardWidth / noTiles;
        tileSizeY = boardHeight / noLanes;

        System.out.printf("x %d y %d ", tileSizeX, tileSizeY);

        System.out.printf("Grid: %dx%d tiles | Tile size: %dx%d | Panel size: %dx%d%n",
                                 noTiles, noLanes,
                                 tileSizeX, tileSizeY,
                                 getWidth(), getHeight());
    }

    /**
     * Retrieves the width of a single tile in pixels.
     * @return The tile width.
     */
    public int getTileSizeX()
    {
        return tileSizeX;
    }

    /**
     * Retrieves the height of a single tile in pixels.
     * @return The tile height.
     */
    public int getTileSizeY()
    {
        return tileSizeY;
    }

    /**
     * Sets the list of currently active entities to be rendered.
     *
     * @param e The {@link ArrayList} of {@link Entity} objects.
     */
    public void setEntities(ArrayList<Entity> e)
    {
        entities = e;
    }

    /** Resets the internal global frame counter to zero. */
    public void resetFrameCount()
    {
        frameCount = 0;
    }
}
