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


     /**
      *updates position and moves a zombie if position is more than tile length 
      * @param tiles - a lane of tiles where zombie is located 
      * @param elapsedTime - time elapsed since last update
      * @return - true if zombie was succesfully moved or updated and false if zombie has enterd the house
      */
     public boolean tryToMove(Tile[] tiles , double elapsedTime){

        /*  tries to attack a plant if possible before updating movement
            and returns true if it has attacked and cancels updating position*/ 
        if(tryToAttack(tiles, elapsedTime)){ 
            return true;
        }

        // if there is no plant continue moving 
        this.updatePosition(elapsedTime);

        
        if(!(inLastTile())){
            // moves the zombie if its position is greater than tile length
            if(position >= Tile.getTileLenght()){
                this.resetPosition( position % Tile.getTileLenght());
                tiles[tileNo].removeZombie(this);
                tiles[tileNo - 1].placeZombie(this);
                GameClock.printTime();
                System.out.println("Zombie from lane " + (laneNo + 1 ) + " Tile "  +( tileNo + 1 )+  " has moved to tile " + (tileNo ) );
                tileNo = tileNo - 1;
                return true;
            }
           
        }
            // if zombie can move but is in last tile return false in order to end the game 
        else{
             if(position >= Tile.getTileLenght()){
                GameClock.printTime();
                 System.out.println("zombies in  at lane " + (laneNo + 1));
                  return false;
             }
        }
        return true;

     }
     /**
      * attacks a plant if tile next to this zombe has a plant or current tile has a plant
      * @param tile - tiles of the lane the zombie is currnetly in 
      * @param timeElapsed - time since last update 
      * @return - returns true if there is 
      */
     public boolean tryToAttack(Tile[] tile, double timeElapsed){
        Plant currentTilePlant = tile[tileNo].getPlant();
        
        

        // if current tile has a plant attack it 
        if(currentTilePlant != null){
            updateAttackCooldown(timeElapsed);
            if(timeSinceLastAttack > attackSpeed ){
                attack(currentTilePlant);
                resetAttackCooldown(timeSinceLastAttack % attackSpeed);
                if(currentTilePlant.getHealth() <= 0){
                        tile[tileNo].removePlant();
                    }
                return true;
            }
            return true;
        }

        // if zombie is close to a plant in next tile attack it
        if(!inLastTile()){
            Plant nextPlant = tile[tileNo - 1].getPlant();
            
            if(position >= 800){
                if(nextPlant != null){
                    updateAttackCooldown(timeElapsed);                    
                    if(timeSinceLastAttack > attackSpeed){
                        attack(nextPlant);
                        resetAttackCooldown(timeSinceLastAttack % attackSpeed);
                        if(nextPlant.getHealth() <= 0){
                            tile[tileNo - 1].removePlant();
                        }
                        return true;
                    } 
                    return true;
                }
            }
            
            
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
        System.out.printf("Zombie attacked plant at lane %d tile %d  updated health : %d\n",( laneNo + 1), (tileNo + 1) , p.getHealth());
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

     public int getSpeed(){
         return speed;
     }

     public int getHealth(){
        return health;
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
