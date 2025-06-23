public class Projectile {
    private int damage;
    private double position;
    private int speed; // how fast the projectile travels
    private int tileNo;
    private int laneNo;
    /**
     * 
     * @param l - lane the projectile will occupy
     * @param t - tile the projectile will occupy
     * @param d - damage of the projectile
     */
    public Projectile(int l, int t, int d){
        damage = d;
        position = 0;
        speed = 20;
        tileNo = t;
        laneNo = l;
    }

    /**
     * used to hit zombie with projectiles damage
     * @param z - zombie to be hit
     */
    public void hit(Zombie z){
        z.takeDamage(damage);
    }

    /**
     * updates the position of the projectile
     * @param timeElapsed - time since last update
     */
    public void updatePosition(double timeElapsed){
        position += timeElapsed * speed;
    }

    /**
     * resets position of projectile when entering a new tile
     * @param p - excess position from last update
     */
    public void resetPosition(double p){
        position = p;
    }


    /**
     * 
     * @param t - lane of projectile
     * @param timeElapsed - time elapssed since last update
     * @param return - returns true if projectile should be removed in the current tile 
     */ 
    public boolean tryToHit(Tile[] t, double timeElapsed){
        Zombie target;
        target = t[tileNo].highestPosition();
        updatePosition(timeElapsed);
        if(t[tileNo].noZombies() == 0){
            
            if(position >= Tile.getTileLenght()){
                if(tileNo != PvZDriver.getMaxTiles() - 1){
                    resetPosition(position % Tile.getTileLenght());
                    t[tileNo + 1].placeProjectile(this);
                  //  t[tileNo].removeProjectile(this);
                  //  GameClock.printTime();
                  //  System.out.printf("Projectile has moved from tile %d to tile %d Position %f  :\n", (tileNo + 1) , (tileNo +2), position);
                    tileNo++;
                    return true;
                }
                else{
                   // t[tileNo].removeProjectile(this);
                    //System.out.println("pea out of bounds lane " + laneNo);
                    return true;
                }
                
            }
           
        }

        else if(target != null)
            if (target.getPosition() + position >= Tile.getTileLenght() ){ // checks if projectile and zombie is in contact 
                target.takeDamage(damage);
               // t[tileNo].removeProjectile(this);

                if(target.getHealth() <= 0){
                    t[tileNo].removeZombie(target);
                    System.out.printf("Zombie at lane %d tile %d died\n", (laneNo + 1), (tileNo + 1));
                    
                }
                return true;
            }

        return false;
    }

    public int getLaneNo(){
        return laneNo;
    }
    public int getTileNo(){
        return tileNo;
    }
}
