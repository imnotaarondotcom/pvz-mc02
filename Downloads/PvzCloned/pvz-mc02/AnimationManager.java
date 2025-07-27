import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.IOException;


import javax.imageio.ImageIO;
public class AnimationManager {
    private Map<String,BufferedImage[]> sprites;
     private Map<String,BufferedImage[]> tiles;
  
    public AnimationManager(){
        sprites = new HashMap<>();
        tiles = new HashMap<>();

    }

    /**
     * Loads an animation 
     * @param path  -  name of directory path
     * @param animation - name of animation 
     * @param frameCount - no of frames aniamtion has
     */
    public void loadAnimation(String path, String animation, int frameCount)
    {
        BufferedImage[] sprite = new BufferedImage[frameCount];
        String fileKey = animation;

        String filePath;
        int i = 0;
        for(i = 0; i < frameCount; i++)
        {
            filePath = path + "/" + animation + (i + 1) + ".png";
            File file = new File(filePath);

            if (!file.exists())
            {
                System.err.println("File does not exist: " + file.getAbsolutePath());
                continue;
            }

            try 
            {
                sprite[i] = ImageIO.read(file);
            } 
            catch(IOException e)
            {
                e.printStackTrace();
            }  
        } 
        sprites.put(fileKey, sprite);
    }

    /**
     * Loads the tile images
     * @param path -  name of directory path
     * @param fileName - name of file
     * @param noImages - number of tiles to load
     */
    public void loadImages(String path, String fileName, int noImages)
    {
        BufferedImage[] tileType = new BufferedImage[noImages];
        String filePath;

        int i = 0;
        for(i =0 ; i < noImages; i++)
        {
            filePath = path + "/" + fileName + (i + 1) + ".png";
            File file = new File(filePath);

            if (!file.exists())
            {
                System.err.println("File does not exist: " + file.getAbsolutePath());
                continue;
            }
            try 
            {
                tileType[i] = ImageIO.read(file);
            } 
            catch(IOException e)
            {
                e.printStackTrace();
            }  
        }  
        tiles.put(fileName, tileType);
    }
    /**
     * 
     * @param entity
     * @param animation
     * @return
     */
    public BufferedImage[] getAnimation(String entity,  String animation)
    {
        String fileKey = entity + "_" + animation;
        
        if(sprites.containsKey(fileKey))
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
     * gets the images to be used for the level
     * @param imageName - file name of image to get
     * @return - returns images assoicaited with imageName
     */
    public BufferedImage[] getImages(String tileType)
    {

        if(tiles.containsKey(tileType))
        {  
             return tiles.get(tileType);
        }
        else
        {
            System.out.println("Image " + tileType + "not found\n");
            return null;
        }
       
    }
}
