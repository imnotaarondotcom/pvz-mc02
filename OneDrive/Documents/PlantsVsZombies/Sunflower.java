import java.util.ArrayList;
public class Sunflower extends Plant {
    private static final int COST = 50;
    private static double timeSinceLastPlant = -7.5;
    private static double cooldown = 7.5;
    
   /**
    * 
    * @param l - lane the sunflower will occupy
    * @param t - tile the sunflower will occupy
    */
    public Sunflower(int l , int t){
        super(l,t);
        name = "Sunflower";
        health = 300;
        damage = 0;
        
        speed = 24;
        timeSinceLastAttack = 0;
        timeSinceLastPlant = 0;
    }

    public void action(Tile t){
      Sun temp = new Sun(laneNo, tileNo, this);
       t.addSun(temp);
    }

    public void tryToAction(Tile t, double elapsedTime, Tile[] tiles){
         updateTime(elapsedTime);
            if(timeSinceLastAttack >= speed){
                
                

                action(t);
                timeSinceLastAttack = 0;
            }
    }

    public static int getCost(){
        return COST;
    }

    public static double getTimeSinceLastPlaced(){
        return timeSinceLastPlant;
    }

    public static double getCooldown(){
        return cooldown;
    }

    public static void setTimeSinceLastPlaced(double time){
        timeSinceLastPlant = time;
    }

}

    