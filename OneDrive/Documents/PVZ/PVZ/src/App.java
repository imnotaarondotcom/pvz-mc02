public class App {
    public static void main(String[] args) throws Exception {
        NormalZombie zombie1 = new NormalZombie();
        Peashooter poppy = new Peashooter();

        zombie1.setSpeed(5);
        System.out.println("This Zombies Health is "+ zombie1.getHealth()+ " And their Speed is "+ zombie1.getSpeed());

        System.out.println("poppy's health is " + poppy.health);

        poppy.shoot(zombie1);

         System.out.println("This Zombies Health is "+ zombie1.getHealth());


    }
}
