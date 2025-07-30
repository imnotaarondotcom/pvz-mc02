import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages the player's sun currency, plant inventory, and plant placement actions.
 * This class handles the logic for trying to place plants on the board,
 * collecting sun, and resetting player-specific game state.
 *
 * @author Lim, Israel Sy
 * @author Enriquez, Aaron Mikael Cruz
 * @version 1.0
 * @since 2025-07-30
 */
public class Player {
    /** The total amount of sun currency the player currently possesses. */
    private static int totalSun;
    /** A map storing template {@link Plant} instances, keyed by their selection number. */
    private final Map<Integer, Plant> plantList;
    /** Reference to the game board for plant placement and interaction. */
    private final Board board;
    /** The current game level the player is on. */
    private int currentLevel;

    /**
     * Constructs a new Player instance.
     * Initializes sun count, sets up the board reference, and populates
     * the available plants based on the current level.
     *
     * @param board The game board instance.
     * @param level The current game level.
     */
    public Player(Board board, int level) {
        this.totalSun = 50; // Starting sun
        this.board = board;
        this.currentLevel = level;
        this.plantList = new HashMap<>();

        // Initialize plant templates based on level
        initializePlantsForLevel(level);
    }

    /**
     * Populates the {@code plantList} with available plant templates based on the game level.
     * More plant types become available at higher levels.
     *
     * @param level The current game level.
     */
    private void initializePlantsForLevel(int level) {
        // Always available plants
        plantList.put(1, new Sunflower(0, 0));
        plantList.put(2, new Peashooter(0, 0));

        // Level 2+ plants
        if (level >= 2)
        {
            plantList.put(3, new Wallnut(0, 0));
        }

        // Level 3+ plants
        if (level >= 3)
        {
            plantList.put(4, new Cherrybomb(0, 0));
        }
    }

    /**
     * Retrieves the current total sun count of the player.
     * @return The total sun.
     */
    public static int getTotalSun() {
        return totalSun;
    }

    /**
     * Attempts to place a plant or use a shovel at the specified tile.
     * Checks for valid selection, occupied tiles, and sufficient sun/cooldown.
     *
     * @param plantNo The index of the selected plant (or shovel if last button).
     * @param laneNo The lane number where the action is attempted.
     * @param tileNo The tile number within the lane where the action is attempted.
     * @return {@code true} if the plant was successfully placed or removed; {@code false} otherwise.
     */
    public boolean tryToPlacePlant(int plantNo, int laneNo, int tileNo) {
        // Shovel is always the last button (plantList.size() + 1)
        if (plantNo > plantList.size()) {
            return useShovel(laneNo, tileNo);
        }

        Plant pickedPlant = plantList.get(plantNo);
        if (pickedPlant == null) {
            System.out.println("Invalid plant selection!");
            return false;
        }

        Tile userTile = board.getTile(laneNo, tileNo);
        if (userTile == null) {
            System.out.println("Invalid tile!");
            return false;
        }

        if (userTile.getPlant() != null) {
            System.out.println("Tile is occupied!");
            return false;
        }

        // Handle plant-specific placement using polymorphism
        if (pickedPlant instanceof Peashooter) {
            return placePlant(new Peashooter(laneNo, tileNo), Peashooter.getCost(), Peashooter.class);
        } else if (pickedPlant instanceof Sunflower) {
            return placePlant(new Sunflower(laneNo, tileNo), Sunflower.getCost(), Sunflower.class);
        } else if (pickedPlant instanceof Cherrybomb) {
            return placePlant(new Cherrybomb(laneNo, tileNo), Cherrybomb.getCost(), Cherrybomb.class);
        } else if (pickedPlant instanceof Wallnut) {
            return placePlant(new Wallnut(laneNo, tileNo), Wallnut.getCost(), Wallnut.class);
        }
        return false;
    }

    /**
     * Attempts to remove a plant from a tile using the shovel.
     *
     * @param laneNo The lane number of the tile.
     * @param tileNo The tile number within the lane.
     * @return {@code true} if a plant was successfully removed; {@code false} otherwise.
     */
    private boolean useShovel(int laneNo, int tileNo) {
        Tile userTile = board.getTile(laneNo, tileNo);
        if (userTile == null) {
            System.out.println("Invalid tile!");
            return false;
        }

        Plant plantToRemove = userTile.getPlant();
        if (plantToRemove != null) {
            userTile.removePlant();
            System.out.println("Plant removed successfully");
            return true;
        } else {
            System.out.println("No plant to remove at this tile");
            return false;
        }
    }

    /**
     * Places a plant on the board after checking for sun cost and cooldown.
     * Uses reflection to access static cooldown and last plant time methods.
     *
     * @param <T> The type of plant being placed.
     * @param plant The new plant instance to place.
     * @param cost The sun cost of the plant.
     * @param plantClass The Class object of the plant type (for static method access).
     * @return {@code true} if the plant was successfully placed; {@code false} otherwise.
     */
    private <T extends Plant> boolean placePlant(T plant, int cost, Class<T> plantClass) {
        try {
            // Check cooldown
            double cooldown = (double) plantClass.getMethod("getCooldown").invoke(null);
            double lastPlantTime = (double) plantClass.getMethod("getTimeSinceLastPlant").invoke(null);

            if (GameClock.getTotalTimeSecondsDouble() - lastPlantTime < cooldown) {
                System.out.println("Still in cooldown!");
                return false;
            }

            if (totalSun < cost) {
                System.out.println("Not enough sun!");
                return false;
            }

            // Place the plant
            board.getTile(plant.getLaneNo(), plant.getTileNo()).placePlant(plant);
            totalSun -= cost;

            // Update last plant time
            plantClass.getMethod("setTimeSinceLastPlant", double.class)
                     .invoke(null, GameClock.getTotalTimeSecondsDouble());

            System.out.println("Total sun: " + totalSun);
            return true;
        } catch (Exception e) {
            System.out.println("Error placing plant: " + e.getMessage());
            return false;
        }
    }

    /**
     * Collects sun from a list of {@link Sun} objects, adding their value to the total sun count.
     * Clears the list of collected suns.
     *
     * @param suns An {@link ArrayList} of {@link Sun} objects to collect.
     */
    public void collectSun(ArrayList<Sun> suns) {
        if (suns.isEmpty()) {
            return;
        }

        int collectedValue = suns.size() * Sun.getValue();
        totalSun += collectedValue;
        suns.clear(); // Remove collected suns from the list

        System.out.println("Collected " + collectedValue + " sun. Total sun: " + totalSun);
    }

    /**
     * Resets the static cooldown timers for all plant types.
     * This is typically called at the start of a new game or level.
     */
    public void reset() {
        Peashooter.setTimeSinceLastPlant(-(Peashooter.getCooldown()));
        Sunflower.setTimeSinceLastPlant(-(Sunflower.getCooldown()));
        Cherrybomb.setTimeSinceLastPlant(-(Cherrybomb.getCooldown()));
        Wallnut.setTimeSinceLastPlant(-(Wallnut.getCooldown()));
    }

    /**
     * Retrieves the current game level the player is on.
     * @return The current level.
     */
    public int getCurrentLevel() {
        return currentLevel;
    }
}
