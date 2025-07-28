import java.awt.image.BufferedImage;
import java.nio.Buffer;

public  class Entity 
{

    private int tileNo;
    private int laneNo;
    private double position;
    private double size;
    private double positionY; // how high the object is from the tile 
    private String type;
    private String state; 

    

<<<<<<< HEAD
    public Entity(String type, String state, int laneNo, int tileNo, double position , double size )
=======
    public Entity(String type, String modifier, String state, int laneNo, int tileNo, double position , double size)
>>>>>>> d7605d28088ad779271db2bc8d8289fe5e4407bd
    {
        positionY = 0;
        this.type = type;
       
        this.state = state;
        this.tileNo = tileNo;
        this.laneNo = laneNo;
        this.position = position;
        this.size = size;
    }

<<<<<<< HEAD
    public Entity(String type,  String state, int laneNo, int tileNo, double position , double size, double positionY )
=======
    // Entity for projectile
    public Entity(String type, String modifier, String state, int laneNo, int tileNo, double position , double size, double positionY )
>>>>>>> d7605d28088ad779271db2bc8d8289fe5e4407bd
    {
        this(type,  state, laneNo, tileNo, position, size);
        this.positionY = positionY;
        
    }



    public int getTileNo(){
        return tileNo ;
    }

    public int getLaneNo(){
        return laneNo ;
    }

    public double getPosition(){
        return position;
    }

    public String getState(){
        return state;
    }

    public String getType(){
        return type;
    }

  

    public double getSize(){
        return size;
    }

    public double getPositionY(){
        return positionY;
    }
}
