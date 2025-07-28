public class Sun {
    private int tileNo;
    private int laneNo;
    private static final int value = 50; 
    /**
     * 
     * @param t - tile sun will be spawned
     * @param l - lane where sun will be placed
     */
    public Sun(int l, int t){
        tileNo = t;
        laneNo = l;
        GameClock.printTime();
        System.out.printf("Sun spawned at lane %d tile %d\n", laneNo + 1, tileNo + 1);
        
    }

    /**
     * 
     * @param l - lane where sun will spawn
     * @param t - tile where sun will spawn
     * @param p - name of the plant that produced the sun
     */
    public Sun(int l, int t, Plant p){
        tileNo = t;
        laneNo = l;
        GameClock.printTime();
        System.out.printf("Sun spawned by %s at lane %d tile %d\n", p.getName(), p.getLaneNo() + 1, p.getTileNo() + 1);
    }

    public static int getValue(){
        return value;
    }
}
