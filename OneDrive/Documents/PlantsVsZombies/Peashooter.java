import java.util.ArrayList;

public class Peashooter extends Plant {
        private static final int COST = 100;
        private static double timeSinceLastPlant = -7.5;
        private static double cooldown = 7.5;
    /**
     * 
     * @param l - lane peashooter will occupy 
     * @param t - tile peashooter will occupy
     */
    public Peashooter(int l, int t){
        super(l,t);
        
        
        damage = 20;
        health = 300;
        directDamage = 10;
        damageFalloff = 1;
        speed = 1.425;
        timeSinceLastAttack = 0;
        
        name = "peaShooter";
       
        
        
    }
    @Override
    public void tryToAction(Tile t, double elapsedTime, Tile[] tiles){
        if(!isLaneClear(tiles)){
            updateTime(elapsedTime);
            if(timeSinceLastAttack >= speed){
                    //GameClock.printTime();
                    //System.out.printf("%s has attacked at lane no %d tile no %d\n",name, laneNo + 1, tileNo + 1 );
                    action(t);
                    timeSinceLastAttack = 0;
                }
        }
    }
    @Override
    public void action(Tile t){
        t.placeProjectile(new Projectile(laneNo,tileNo,damage, damageFalloff, directDamage));
        
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
