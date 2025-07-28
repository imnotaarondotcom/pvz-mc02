public class Projectile {
    private int damage;
    private int damageFalloff;
    private int directDamage;
    private double position;
    private int speed; // how fast the projectile travels
    private int tileNo;
    private int laneNo;
    private int tilesTravelled;
    /**
     * intitializes the projectile with damage line number and tile number
     * @param l - lane the projectile will occupy
     * @param t - tile the projectile will occupy
     * @param d - damage of the projectile
     * @param df - falloff damage
     */
    public Projectile(int l, int t, int d, int df, int dd){
        damage = d;
        position = 0;
        speed = 40;
        tileNo = t;
        laneNo = l;
        damageFalloff = df; 
        directDamage = dd;
        tilesTravelled = 0;
    }

    /**
     * used to hit zombie with projectiles damage
     * @param z - zombie to be hit
     */
    public void hit(Zombie z){
        
        z.takeDamage(damage + ( directDamage - damageFalloff * tilesTravelled ));
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
     * updates projectile position and returns whether it can move to the next tile
     * @param timeElapsed - time elapsed since last update
     * @return - true if projectile can go to the next tile
     */
    public boolean isReadyToMove(double timeElapsed){
        updatePosition(timeElapsed);
        if(position >= Tile.getTileLenght()){
            resetPosition(position % Tile.getTileLenght());
            tileNo++;
            tilesTravelled++;
            return true;
        }
        return false;
    }

    public boolean inLastTile(){
        return tileNo == PvZDriver.getMaxTiles();
    }

    public int getLaneNo(){
        return laneNo;
    }
    public int getTileNo(){
        return tileNo;
    }

    public double getPosition(){
        return position;
    }
    public int getDamage(){
        return damage;
    }
}
