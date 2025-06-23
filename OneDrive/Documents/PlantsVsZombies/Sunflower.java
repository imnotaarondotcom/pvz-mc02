import java.util.ArrayList;
public class Sunflower extends Plant {

    
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
        cooldown = 7.5;
        speed = 24;
        timeSinceLastAttack = 0;
    }

    public void action(Tile t,  ArrayList<Sun> sun){
      Sun temp = new Sun(laneNo, tileNo, this);
       sun.add(temp);
    }
}
