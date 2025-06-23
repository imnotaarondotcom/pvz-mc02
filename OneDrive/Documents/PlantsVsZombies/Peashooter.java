import java.util.ArrayList;

public class Peashooter extends Plant {

    /**
     * 
     * @param l - lane peashooter will occupy 
     * @param t - tile peashooter will occupy
     */
    public Peashooter(int l, int t){
        super(l,t);
        cost = 100;
        cooldown = 7.5;
        damage = 20;
        health = 300;
        directDamage = 5;
        speed = 1.425;
        timeSinceLastAttack = 0;
        name = "peaShooter";
       
        
        
    }

    @Override
    public void action(Tile t, ArrayList<Sun> sun){
        t.placeProjectile(new Projectile(laneNo,tileNo,damage));
        
    }
    
   
  
}
