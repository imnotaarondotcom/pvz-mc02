import java.util.ArrayList;

/**
 * Represents a single tile on the game board in Plants vs. Zombies.
 * Each tile can contain a plant, multiple zombies, projectiles, and sun collectibles.
 * It manages the entities within its boundaries and facilitates their interactions.
 * @author Lim, Israel Sy
 * @author Enriquez, Aaron Mikael Cruz
 * @version 1.0
 * @since 2025-06-27
 */

public class Tile {
    /** The lane index (row) of this tile. */
    private final int LANE_NO;

    /** The tile index (column) of this tile. */
    private final int TILE_NO;

    /** The conceptual length of a tile, used for movement calculations. */
    private final static int TILE_LENGTH = 1;

    /** The plant currently placed on this tile, or null if empty. */
    private Plant placedPlant;

    /** A dynamic list to hold zombies currently occupying this tile. */
    private ArrayList<Zombie> zombies;

    /** A dynamic list to hold projectiles currently traversing this tile. */
    private ArrayList<Projectile> projectiles;

    /** A dynamic list to hold sun collectibles currently available on this tile. */
    private ArrayList<Sun> sun;

    /**
     * Creates a new tile and initializes its contents and position.
     * @param lane The lane number of the created tile.
     * @param tile The tile number (column) for the created tile.
     */
    public Tile(int lane , int tile){
        zombies = new ArrayList<>();
        sun = new ArrayList<>();
        projectiles = new ArrayList<>();
        TILE_NO = tile ;
        LANE_NO = lane ;
    }

    /**
     * Spawns a zombie on this tile if it's the farthest tile from the player's house.
     * This method is typically called only for initial wave spawning.
     * @param laneNo The lane number for the new zombie.
     * @param tileNo The tile number for the new zombie.
     */
    public void spawnZombie(int laneNo, int tileNo){
        if(TILE_NO == PvZDriver.getMaxTiles() - 1){ // Check if this is the designated spawn tile

            // ZOMBIE TO BE SPAWNED
            Zombie z = new Zombie(laneNo, tileNo , "flag");
            
            GameClock.printTime();
            placeZombie(z);
            System.out.println("Zombie Has Been Spawned at Lane No " + (LANE_NO + 1) + " Tile No " + (TILE_NO + 1) );
            System.out.println(z.toString());
        } else {
            System.out.println("Failed to spawn - not the farthest tile.");
        }
    }

    /**
     * Places a plant on this tile if it is not already occupied.
     * @param p The plant to be placed.
     * @return True if the plant is successfully placed, false otherwise.
     */
    public boolean placePlant(Plant p){
        if(placedPlant == null){
            GameClock.printTime();
            System.out.printf("%s placed at lane %d tile %d\n",p.getName(), (LANE_NO + 1),(TILE_NO + 1));
            placedPlant = p;
            return true;
        }
        return false;
    }

    /**
     * Adds a zombie to this tile. Used for both spawning and moving zombies between tiles.
     * @param z The zombie to be placed.
     */
    public void placeZombie(Zombie z){
        zombies.add(z);
    }
    
    /**
     * Places a projectile into this tile's list of active projectiles.
     * @param p The projectile to be placed.
     */
    public void placeProjectile(Projectile p){
        projectiles.add(p);
    }

    /**
     * Removes a specific zombie from this tile.
     * @param z The zombie to be removed.
     */
    public void removeZombie(Zombie z){
        zombies.remove(z);
    }

    /**
     * Removes the plant currently occupying this tile.
     */
    public void removePlant(){
        if (placedPlant != null) {
            GameClock.printTime();
            System.out.printf("Plant removed at lane %d tile %d\n", LANE_NO + 1, TILE_NO + 1);
            placedPlant = null;
        }
    }

    /**
     * Counts the number of zombies currently present on this tile.
     * @return The number of zombies in the tile.
     */
    public int noZombies(){
        return zombies.size(); // Use ArrayList's size method for conciseness
    }

    /**
     * Returns the zombie with the "highest" (i.e., furthest left, closest to the house) position within this tile.
     * This is useful for determining which zombie a plant should target first.
     * @return The zombie with the highest position, or null if no zombies are present.
     */
    public Zombie highestPosition(){
        if (zombies.isEmpty()) { // Check if the list is empty first
            return null;
        }
        
        Zombie highestZombie = null;
        // Zombies move from right to left, so a lower position value means closer to the house (further left).
        double minPosition = Double.MAX_VALUE; 

        for(Zombie z : zombies){ // Enhanced for-loop for readability
            if(z.getPosition() < minPosition){
                minPosition = z.getPosition();
                highestZombie = z;
            }
        }
        return highestZombie;
    }
    
    /**
     * Returns the list containing all zombies on this tile.
     * @return An {@link ArrayList} of {@link Zombie} objects.
     */
    public ArrayList<Zombie> getZombies(){
        return zombies;
    }

    /**
     * Returns the list of projectiles currently on this tile.
     * @return An {@link ArrayList} of {@link Projectile} objects.
     */
    public ArrayList<Projectile> getProjectiles(){
        return projectiles;
    }

    /**
     * Adds a sun collectible to this tile's list of suns.
     * @param sun The {@link Sun} object to be added.
     */
    public void addSun(Sun sun){
        this.sun.add(sun);
    }
    
    /**
     * Checks if this tile has any zombies occupying it.
     * @return True if there is at least one zombie, false otherwise.
     */
    public boolean hasZombie(){
        return !zombies.isEmpty(); // Concise check if ArrayList is not empty
    }

    /**
     * Returns the plant currently occupying this tile.
     * @return The {@link Plant} object, or null if no plant is occupying the tile.
     */
    public Plant getPlant(){
        return placedPlant;
    } 

    /**
     * Returns the conceptual length of a single tile, used for game world measurements.
     * @return The length of a tile.
     */
    public static int getTileLength(){
        return TILE_LENGTH;
    }

    /**
     * Returns the list of sun collectibles currently on this tile.
     * @return An {@link ArrayList} of {@link Sun} objects.
     */
    public ArrayList<Sun> getSunList(){
        return sun;
    }

    /**
     * Returns the lane index (row) of this tile.
     * @return The lane number of the tile.
     */
    public int getLaneNo(){
        return LANE_NO;
    }

    /**
     * Returns the tile index (column) of this tile.
     * @return The tile number of the tile.
     */
    public int getTileNo(){
        return TILE_NO;
    }
}