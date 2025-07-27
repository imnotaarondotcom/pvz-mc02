public class Cone extends Armor {

    public Cone(){
        super(370, 0, "cone");
    }

    public void updateState(){
        if(this.getHealth() > 240 ){
            this.state = "A";
        }
        else if(this.getHealth() > 110){
            this.state = "B";
        }
        else{
            this.state = "C";
        }
    }
}
