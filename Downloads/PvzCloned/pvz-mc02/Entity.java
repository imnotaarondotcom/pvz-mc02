import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class Entity {
    private BufferedImage[] moveAnimation;
    private BufferedImage[] attackAnimation;
    private int tileNo;
    private int laneNo;
    private double position;

    public Entity(BufferedImage[] move,BufferedImage[] atk, int laneNo, int tileNo, double position ){
        this(move, laneNo, tileNo , position);
        attackAnimation = atk;
        
    }

    public Entity(BufferedImage[] move, int laneNo, int tileNo, double position ){
        moveAnimation = move;
        this.tileNo = tileNo;
        this.laneNo = laneNo;
        this.position = position;
    }

    public BufferedImage[] getMovementAnimation(){
        return moveAnimation;
    }
}
