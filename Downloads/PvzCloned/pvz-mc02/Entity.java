import java.awt.image.BufferedImage;
import java.nio.Buffer;

public  class Entity 
{

    private int tileNo;
    private int laneNo;
    private double position;
    private String type;
    private String state; 

    

   

    public Entity(String type, String state, int laneNo, int tileNo, double position )
    {
        this.type = type;
        this.state = state;
        this.tileNo = tileNo;
        this.laneNo = laneNo;
        this.position = position;
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
}
