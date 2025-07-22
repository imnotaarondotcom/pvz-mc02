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
     * Loads the tile images
     * @param fileName -  name of directory path 
     * @param noTiles - number of tiles to load
     */
    public void loadTile(String fileName, int noTiles){
        BufferedImage[] tileType = new BufferedImage[noTiles];
        int i = 0;
        for(i =0 ; i < noTiles; i++){
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
     * gets the tile images to be used for the level
     * @param tileType - file name of tile to get
     * @return - returns images of tiles 
     */

    public BufferedImage[] getTiles(String tileType){

        if(tiles.containsKey(tileType)){
           
             return tiles.get(tileType);
              

        }
        else{
            System.out.println("Image " + tileType + "not found\n");
            return null;
        }
       
    }

   
    
}
