import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Manages loading and accessing animation and image assets for the game.
 * This class organizes image frames for sprite animations and also handles
 * static image resources like tiles or UI elements.
 *
 * @author Lim, Israel Sy
 * @author Enriquez, Aaron Mikael Cruz
 * @version 1.0
 * @since 2025-07-30
 */
public class AnimationManager
{
    /**
     * A map to store sequences of {@link BufferedImage} arrays, where each array
     * represents an animation (a series of sprite frames). The key is typically
     * a combination of entity name and animation name (e.g., "peashooter_idle).
     */
    private Map<String, BufferedImage[]> sprites;

    /**
     * A map to store sequences of {@link BufferedImage} arrays, where each array
     * represents a set of static images (e.g., UI elements, tiles). The key
     * is the base name provided when loading the images.
     */
    private Map<String, BufferedImage[]> images;

    /**
     * Constructs an AnimationManager with empty sprite and image maps.
     * Initializes the internal data structures used to store loaded assets.
     */
    public AnimationManager()
    {
        sprites = new HashMap<>();
        images = new HashMap<>();
    }

    /**
     * Loads a sequence of images from a specified directory path as an animation
     * and stores them in the sprite map. Each image file is expected to be named
     * in a sequential format (e.g., "animation1.png", "animation2.png", etc.).
     *
     * @param path        - The directory path where the animation frames are stored.
     * @param animation   - The base name of the animation image files (e.g., "player_idle").
     * @param frameCount  - The total number of frames in the animation sequence.
     */
    public void loadAnimation(String path, String animation, int frameCount)
    {
        BufferedImage[] sprite = new BufferedImage[frameCount];
        String fileKey = animation;

        for (int i = 0; i < frameCount; i++)
        {
            String filePath = path + "/" + animation + (i + 1) + ".png";
            File file = new File(filePath);

            if (!file.exists())
            {
                System.err.println("File does not exist: " + file.getAbsolutePath());
                continue; // Skip to the next frame if the file is missing
            }

            try
            {
                sprite[i] = ImageIO.read(file);
            }
            catch (IOException e)
            {
                // Print the stack trace for debugging if an error occurs during image reading
                e.printStackTrace();
            }
        }
        // Store the loaded animation frames under the specified key
        sprites.put(fileKey, sprite);
    }

    /**
     * Loads a general sequence of static images (e.g., tiles, UI assets) from a directory
     * and stores them under a common key in the images map. Similar to loadAnimation,
     * image files are expected to be sequentially named.
     *
     * @param path     - The directory path where the images are stored.
     * @param baseName - The base name of the image files.
     * @param count    - The number of images to load for this set.
     */
    public void loadImages(String path, String baseName, int count)
    {
        BufferedImage[] imageSet = new BufferedImage[count];

        for (int i = 0; i < count; i++)
        {
            String filePath = path + "/" + baseName + (i + 1) + ".png";
            File file = new File(filePath);

            if (!file.exists())
            {
                System.err.println("File does not exist: " + file.getAbsolutePath());
                continue; // Skip to the next image if the file is missing
            }

            try
            {
                imageSet[i] = ImageIO.read(file);
            }
            catch (IOException e)
            {
                // Print the stack trace for debugging if an error occurs during image reading
                e.printStackTrace();
            }
        }
        // Store the loaded image set under the specified base name
        images.put(baseName, imageSet);
    }

    /**
     * Retrieves animation frames for a specific entity's animation. The method
     * constructs a key by combining the entity and animation names to look up
     * the corresponding {@link BufferedImage} array in the sprites map.
     *
     * @param entity    - The name of the entity.
     * @param animation - The name of the specific animation for the entity.
     * @return An array of {@link BufferedImage} representing the animation frames,
     * or {@code null} if the requested animation is not found in the map.
     */
    public BufferedImage[] getAnimation(String entity, String animation)
    {
        String fileKey = entity + "_" + animation;

        if (sprites.containsKey(fileKey))
        {
            return sprites.get(fileKey);
        }
        else
        {
            System.out.println(fileKey + " not Found");
            return null;
        }
    }

    /**
     * Retrieves a general image set by its base name. This method is used to
     * access static image resources that were loaded using the {@code loadImages} method.
     *
     * @param name - The base name associated with the image set.
     * @return An array of {@link BufferedImage} representing the image set,
     * or {@code null} if the image set with the given name is not found.
     */
    public BufferedImage[] getImages(String name)
    {
        if (images.containsKey(name))
        {
            return images.get(name);
        }
        else
        {
            System.out.println("Image set \"" + name + "\" not found\n");
            return null;
        }
    }
}
