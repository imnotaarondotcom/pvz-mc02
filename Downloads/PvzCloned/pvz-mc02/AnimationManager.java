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
    public void loadAnimation(String path, String animation, int frameCount){
        BufferedImage[] sprite = new BufferedImage[frameCount];
        String fileKey = path.concat("_").concat(animation);
        String filePath;
        int i = 0;
        for(i = 0; i < frameCount; i++){
            filePath = path.concat("_").concat(animation).concat(Integer.toString(i + 1)).concat(".png");

            try{
                sprite[i] = ImageIO.read(new File(filePath));
            }catch(IOException e){
                e.printStackTrace();
            }
            
        } 
       // System.out.println("path is \n\n\n" + fileKey);
        sprites.put(fileKey, sprite );
    }

    /**
     * Loads the images associated with file name
     * @param fileName -  name of directory path 
     * @param noImages - number of tiles to load
     */
    public void loadImages(String fileName, int noImages){
        BufferedImage[] tileType = new BufferedImage[noImages];
        int i = 0;
        for(i =0 ; i < noImages; i++){
            String filePath = fileName.concat(Integer.toString(i + 1)).concat(".png");
            try{
                tileType[i] = ImageIO.read(new File(filePath));
            }catch(IOException e){
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
    public BufferedImage[] getAnimation(String entity, String animation){
        String fileKey = entity.concat("_").concat(animation);
        if(sprites.containsKey(fileKey)){
            return sprites.get(fileKey);
        }
        else{
            System.out.println(fileKey + " not Found");
            return null;
        }
        
    }

    /**
     * gets the images to be used for the level
     * @param imageName - file name of image to get
     * @return - returns images assoicaited with imageName
     */

    public BufferedImage[] getImages(String imageName){

        if(tiles.containsKey(imageName)){
           
             return tiles.get(imageName);
              

        }
        else{
            System.out.println("Image " + imageName + "not found\n");
            return null;
        }
       
    }

   
    
}
