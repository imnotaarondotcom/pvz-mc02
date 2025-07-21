import java.awt.image.BufferedImage;

public interface Drawable {
    public void loadAnimation();
    public BufferedImage[] getMovementAnimation();
    public BufferedImage[] getAttackAnimation();
}
