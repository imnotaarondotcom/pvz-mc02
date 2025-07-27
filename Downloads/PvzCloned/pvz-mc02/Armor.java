public abstract class  Armor {
    protected int health;
    protected double speedBuff;
    protected final String NAME;
    protected String state;
    protected boolean isHittable;

    public Armor(int health , double speedBuff, String name){
        this.health = health;
        this.speedBuff = speedBuff;
        NAME = name;
        state = "A";
        isHittable = true;
    }

    public String getName(){
        return NAME;
    }

    public String getState(){
        return state;
    }

    public int getHealth(){
        return health;
    }

    public void takeDamage(int d){
        health -= d;
    }

    public double getSpeedBuff(){
        return speedBuff;
    }

    public boolean isHittable(){
        return isHittable;
    }

    public abstract void updateState();

    





    

}
