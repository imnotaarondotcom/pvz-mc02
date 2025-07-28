public class Zombie {
     private double attackSpeed;
     private double timeSinceLastAttack;
     private int speed;
     private int damage;
     private int health;
     private double position;
     private int tileNo;
     private int laneNo;
   
     /**
      * 
      * @param laneNo - lane zombie will occupy
      * @param tileNo - tile zombie wil occupy
      */
     public Zombie(int laneNo, int tileNo){
        this.tileNo = tileNo;
        this.laneNo = laneNo ;
        attackSpeed = 1;
        timeSinceLastAttack = 0;
        speed = 4;
        damage = 100;
        health = 200;
        position = 0;

     }

     public void move(){
        this.resetPosition( position % Tile.getTileLenght());  
                tileNo = tileNo - 1;
     }


     /**
      *updates position and moves a zombie if position is more than tile length 
      * @param elapsedTime - time elapsed since last update
      * @return - true if zombie is ready to move
      */
     public boolean isReadyToMove( double elapsedTime){

       
        this.updatePosition(elapsedTime);

        
       
        
            if(position >= Tile.getTileLenght()){
                
                return true;
            }
           
        
            
       
        return false;

     }
     /**
      * attacks a plant if time elapsed sice last attack is higher than attack speed
      * @param plant - plant to attack
      * @param timeElapsed - time since last update 
      * @return - returns true if zombie is ready to attack
      */
     public boolean isReadyToAttack(double timeElapsed){
  
            updateAttackCooldown(timeElapsed);
            if(timeSinceLastAttack > attackSpeed ){
                
                resetAttackCooldown(timeSinceLastAttack % attackSpeed);
                return true;

            }
            
        


        return false;
     }
     
     /**
      * makes the zombie attack a plant
      * @param p - Plant to be attacked
      */
     public void attack(Plant p){
        p.takeDamage(damage);
        GameClock.printTime();
        System.out.printf("Zombie attacked %s at lane %d tile %d  updated health : %d\n",p.getName(), ( laneNo + 1), (tileNo + 1) , p.getHealth());
     }

     /**
      * makes a zombie get hit by a projectile
      * @param damage - damage of projectile 
      */

     public void takeDamage(int damage){
        health -= damage;
        GameClock.printTime();
        System.out.printf("Zombie at lane %d tile %d hit updated health : %d\n", (laneNo + 1), (tileNo + 1), health);
     }

    

     /**
      * updates zombies poisition
      * @param elapsedTime - time elapsed since last update
      */
     public void updatePosition(double elapsedTime){
         position += speed * elapsedTime;
     }

     /**
      * updates last time zombie attacked
      * @param timeElapsed - time since last update 
      */

     public void updateAttackCooldown(double timeElapsed){
        timeSinceLastAttack += timeElapsed;
     }

     /**
      * resets the time zombie has last attacked to 0 plus the excess time from last update
      * @param cd - excess time from last update
      */

     public void resetAttackCooldown(double cd){
        timeSinceLastAttack = cd;
     }

     /**
      * resets a zombie position with excess amount from update
      * @param p - excess amount after moving from tile
      */
     public void resetPosition(double p){
         position = p;
     }


     public double getPosition(){
      return position;
     }

      public int getSpeed(){
         return speed;
     }

     public int getHealth(){
        return health;
     }

     public int getTileNo(){
        return tileNo;
     }

     public int getLaneNo(){
        return laneNo;
     }
     
     /**
      * used to check if zombie is in last tile of the lane
      * @return - returns true if zombie is in last tile of lane
      */
     public boolean inLastTile(){
        if(tileNo == 0){
            return true;
        }
        return false;
     }


     @Override
     public String toString(){
        return String.format("Health : %d Speed : %d Damage : %d",this.health, this.speed,this.damage );
     }

    
}
