import java.util.ArrayList;
import java.util.Random;
import java.util.Iterator;

/**
 * Handles the state and behavior of the game board including tile management,
 * entity updates, spawning of zombies and suns, and wave logic.
 *
 * Called and updated by the game controller.
 *
 * Supports different spawn intervals depending on game phase and difficulty.
 *
 * @author Lim, Israel Sy
 * @author Enriquez, Aaron Mikael Cruz
 * @version 2.0
 * @since 2025-06-27
 */
public class Board
{
    /** Tracks the time since the last zombie was spawned. */
    private double lastZombieSpawnTime = 0.0;
    /** Tracks the time since the last sun was spawned. */
    private double lastSunSpawnTime = 0.0;
    /** Flag indicating if the final zombie wave has been triggered. */
    private boolean finalWaveTriggered = false;
    /** Flag indicating if the game is over. */
    private boolean gameOver = false;

    /** Zombie spawn interval for easy difficulty. */
    private final double ZOMBIE_SPAWN_INTERVAL_EASY = 10.0;
    /** Zombie spawn interval for medium difficulty. */
    private final double ZOMBIE_SPAWN_INTERVAL_MEDIUM = 5.0;
    /** Zombie spawn interval for hard difficulty. */
    private final double ZOMBIE_SPAWN_INTERVAL_HARD = 3.0;
    /** Sun spawn interval. */
    private final double SUN_SPAWN_INTERVAL = 8.0;

    /** Manages the creation and cycling of zombie armor. */
    private ArmorManager armorManager;
    /** The current game level. */
    private int level;

    /** Maximum number of tiles per lane. */
    private int MAX_TILES;
    /** Maximum number of lanes on the board. */
    private int MAX_LANES;
    /** 2D array representing the game board tiles. */
    private Tile[][] lanes;
    /** Random number generator for tile selection. */
    private Random tilePicker;

    /**
     * Constructs the board with the specified number of lanes and tiles.
     *
     * @param l Number of lanes.
     * @param t Number of tiles per lane.
     */
    public Board(int l, int t)
    {
        MAX_LANES = l;
        MAX_TILES = t;
        lanes = new Tile[MAX_LANES][MAX_TILES];
        tilePicker = new Random();
    }

    /**
     * Updates all game entities on the board each frame.
     *
     * @param timeElapsed Time in seconds since last frame.
     * @return {@code true} if game over, {@code false} otherwise.
     */
    public boolean updateBoard(double timeElapsed)
    {
        updatePlants(timeElapsed);
        updateProjectiles(timeElapsed);

        lastZombieSpawnTime += timeElapsed;
        lastSunSpawnTime += timeElapsed;

        tryToSpawnZombie(GameClock.getTotalTimeSecondsInt());
        tryToSpawnSun(GameClock.getTotalTimeSecondsInt());

        if(updateZombies(timeElapsed))
        {
            gameOver = true;
            return true;
        }

        return false;
    }

    /**
     * Attempts to spawn zombies based on current game time and phase.
     * Handles both regular waves and the final wave.
     *
     * @param gameTime Current game time in seconds.
     * @return {@code true} if a zombie was spawned.
     */
    public boolean tryToSpawnZombie(int gameTime)
    {
        if (gameOver) return false;

        int i, laneNo;
        double spawnInterval = 1000; // Default high interval to prevent early spawns
        Armor zombieArmor;

        laneNo = tilePicker.nextInt(MAX_LANES);

        // Final Wave logic
        if (gameTime > 170 && !finalWaveTriggered)
        {
            System.out.println("*** Final Wave Incoming! ***");
            for(i = 0; i < (3 + (level * 2)); i++)
            {
                laneNo = tilePicker.nextInt(MAX_LANES);
                // First zombie in final wave is always a Flag zombie
                if (i == 0) {
                    zombieArmor = new Flag();
                } else {
                    zombieArmor = armorManager.getArmor();
                }
                lanes[laneNo][MAX_TILES - 1].spawnZombie(laneNo, MAX_TILES - 1, zombieArmor);
            }
            finalWaveTriggered = true;
            return true;
        }

        // Set spawn interval based on time range
        if (gameTime >= 30 && gameTime <= 80)
            spawnInterval = ZOMBIE_SPAWN_INTERVAL_EASY;
        else if (gameTime >= 81 && gameTime <= 140)
            spawnInterval = ZOMBIE_SPAWN_INTERVAL_MEDIUM;
        else if (gameTime > 140 && gameTime <= 170)
            spawnInterval = ZOMBIE_SPAWN_INTERVAL_HARD;

        // Regular zombie spawning
        if (!finalWaveTriggered && lastZombieSpawnTime >= spawnInterval)
        {
            laneNo = tilePicker.nextInt(MAX_LANES);
            zombieArmor = armorManager.getArmor();
            lanes[laneNo][MAX_TILES - 1].spawnZombie(laneNo, MAX_TILES - 1, zombieArmor);
            lastZombieSpawnTime = 0; // Reset spawn timer
            return true;
        }

        return false;
    }

    /**
     * Attempts to spawn sun on a random tile at regular intervals.
     *
     * @param gameTime Current game time in seconds.
     * @return {@code true} if a sun was spawned.
     */
    public boolean tryToSpawnSun(int gameTime)
    {
        if (gameOver) return false;

        while (lastSunSpawnTime >= SUN_SPAWN_INTERVAL)
        {
            int tileNo = tilePicker.nextInt(MAX_TILES);
            int laneNo = tilePicker.nextInt(MAX_LANES);

            Sun newSun = new Sun(laneNo, tileNo);
            lanes[laneNo][tileNo].addSun(newSun);
            lastSunSpawnTime = 0; // Reset spawn timer

            return true;
        }

        return false;
    }

    /**
     * Updates all plants on the board and removes any dead ones.
     *
     * @param timeElapsed Time since last update.
     */
    public void updatePlants(double timeElapsed)
    {
        if (gameOver) return;

        for(int laneNo = 0; laneNo < MAX_LANES; laneNo++)
        {
            for(int tileNo = 0; tileNo < MAX_TILES; tileNo++)
            {
                Tile currentTile = lanes[laneNo][tileNo];
                Plant plant = currentTile.getPlant();

                if(plant != null)
                {
                    plant.tryToAction(this, timeElapsed);

                    if(plant.getHealth() <= 0)
                    {
                        currentTile.removePlant();
                        GameClock.printTime();
                        System.out.printf("Plant at lane %d tile %d died\n", laneNo + 1, tileNo + 1);
                    }
                }
            }
        }
    }

    /**
     * Updates all projectiles on the board.
     * Handles projectile movement and collision with zombies.
     *
     * @param timeElapsed Time since last update.
     */
    public void updateProjectiles(double timeElapsed)
    {
        if (gameOver) return;

        for(int laneNo = 0; laneNo < MAX_LANES; laneNo++)
        {
            for(int tileNo = 0; tileNo < MAX_TILES; tileNo++)
            {
                Tile currentTile = lanes[laneNo][tileNo];
                ArrayList<Projectile> projectiles = currentTile.getProjectiles();
                Iterator<Projectile> iterator = projectiles.iterator();

                while(iterator.hasNext())
                {
                    Projectile projectile = iterator.next();

                    if(currentTile.hasZombie())
                    {
                        if(projectile.hasHitZombie(currentTile))
                        {
                            iterator.remove(); // Remove projectile if it hit a zombie
                        }
                        else
                        {
                            projectile.updatePosition(timeElapsed);
                        }
                    }
                    else
                    {
                        if(projectile.isReadyToMove(timeElapsed))
                        {
                            if(projectile.getTileNo() < MAX_TILES)
                            {
                                iterator.remove(); // Remove from current tile
                                lanes[laneNo][projectile.getTileNo()].placeProjectile(projectile); // Place on next tile
                            }
                            else
                            {
                                iterator.remove(); // Remove projectile that exited screen
                            }
                        }
                        else
                        {
                            projectile.updatePosition(timeElapsed);
                        }
                    }
                }
            }
        }
    }

    /**
     * Updates zombies: state, movement, and attacks. Also checks for game over.
     *
     * @param timeElapsed Time since last update.
     * @return {@code true} if a zombie enters the house (game over).
     */
    public boolean updateZombies(double timeElapsed)
    {
        if (gameOver) return true;

        for(int laneNo = 0; laneNo < MAX_LANES; laneNo++)
        {
            for(int tileNo = 0; tileNo < MAX_TILES; tileNo++)
            {
                ArrayList<Zombie> zombies = lanes[laneNo][tileNo].getZombies();
                Plant plant = lanes[laneNo][tileNo].getPlant();
                Iterator<Zombie> zombieIterator = zombies.iterator();

                while(zombieIterator.hasNext())
                {
                    Zombie zombie = zombieIterator.next();

                    if (!zombie.isAlive())
                    {
                        zombieIterator.remove();
                        GameClock.printTime();
                        System.out.printf("Zombie died at lane %d tile %d\n", laneNo + 1, tileNo + 1);
                        continue; // Move to the next zombie
                    }

                    if(plant != null && !plant.getName().equals("cherrybomb"))
                    {
                        zombie.updateState("eat"); // Zombie tries to eat the plant
                        if(zombie.isReadyToAttack(timeElapsed))
                        {
                            zombie.attack(plant);
                        }
                    }
                    else
                    {
                        zombie.updateState("walk"); // Zombie walks if no plant or plant is cherrybomb

                        if(zombie.isReadyToMove(timeElapsed))
                        {
                            if(zombie.inLastTile()) // Zombie is at the last tile before the house
                            {
                                zombie.move(); // Move within the last tile
                            }
                            else if(zombie.getTileNo() == -1) // Zombie has moved past the first tile (into the house)
                            {
                                if(zombie.isReadyToMove(timeElapsed)) // Check again if ready to move to ensure it's not a single frame check
                                {
                                    GameClock.printTime();
                                    System.out.printf("Zombies at lane %d tile %d entered house\n", laneNo + 1, tileNo + 1);
                                    System.out.println("*** GAME OVER | ZOMBIES WIN ***");
                                    return true; // Game over
                                }
                            }
                            else // Zombie moves to the previous tile
                            {
                                GameClock.printTime();
                                System.out.printf("Zombie from lane %d tile %d moved to tile %d\n", laneNo + 1, tileNo + 1, tileNo);
                                zombieIterator.remove(); // Remove from current tile
                                lanes[laneNo][tileNo - 1].placeZombie(zombie); // Place on new tile
                                zombie.move(); // Update zombie's internal tile position
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Initializes the level and tiles using the provided level number.
     * Sets up the {@link ArmorManager} and loads level-specific armor data.
     *
     * @param level Game level to load.
     */
    public void loadLevel(int level)
    {
        this.level = level;
        armorManager = new ArmorManager();
        armorManager.loadLevelData(level);

        for(int i = 0 ; i < MAX_LANES; i++)
        {
            lanes[i] = new Tile[MAX_TILES];
            for(int tile = 0; tile < MAX_TILES; tile++)
            {
                lanes[i][tile] = new Tile(i, tile);
            }
        }
    }

    /**
     * Resets the board state for a new game.
     */
    public void resetBoard()
    {
        for (int laneNum = 0; laneNum < MAX_LANES; laneNum++)
        {
            for (int tileNum = 0; tileNum < MAX_TILES; tileNum++)
            {
                lanes[laneNum][tileNum] = new Tile(laneNum, tileNum); // replace old tiles
            }
        }

        gameOver = false;
        finalWaveTriggered = false;
        lastZombieSpawnTime = 0.0;
        lastSunSpawnTime = 0.0;
    }

    /**
     * Retrieves the current game level.
     * @return The current level.
     */
    public int getLevel()
    {
        return level;
    }

    /**
     * Retrieves the array of tiles for a specific lane.
     * @param lane_n The lane number.
     * @return An array of {@link Tile} objects for the specified lane.
     */
    public Tile[] getSpecificLane(int lane_n)
    {
        return lanes[lane_n];
    }

    /**
     * Retrieves all lanes on the board.
     * @return A 2D array of {@link Tile} objects representing all lanes.
     */
    public Tile[][] getAllLanes()
    {
        return lanes;
    }

    /**
     * Retrieves the maximum number of lanes on the board.
     * @return The maximum number of lanes.
     */
    public int getMaxLanes()
    {
        return MAX_LANES;
    }

    /**
     * Retrieves the maximum number of tiles per lane.
     * @return The maximum number of tiles.
     */
    public int getMaxTiles()
    {
        return MAX_TILES;
    }

    /**
     * Returns a tile at the given lane and tile index.
     *
     * @param laneNo The lane number.
     * @param tileNo The tile number within the lane.
     * @return The {@link Tile} at the given position.
     */
    public Tile getTile(int laneNo, int tileNo)
    {
        return lanes[laneNo][tileNo];
    }

    /**
     * Retrieves all in-game entities (zombies, suns, projectiles, plants) for rendering or update.
     *
     * @return A list of all {@link Entity} objects currently on the board.
     */
    public ArrayList<Entity> getEntities()
    {
        ArrayList<Entity> entities = new ArrayList<>();
        for(int row = 0; row < getMaxLanes(); row++)
        {
            for(int col = 0; col < getMaxTiles(); col++)
            {
                Tile tile = lanes[row][col];
                Iterator<Zombie> zIterator = tile.getZombies().iterator();
                Iterator<Sun> sIterator = tile.getSunList().iterator();
                Iterator<Projectile> projIterator = tile.getProjectiles().iterator();
                Plant plant = tile.getPlant();

                while(zIterator.hasNext())
                {
                    Zombie z = zIterator.next();
                    entities.add(new Entity(z.getType(), z.getState(), z.getLaneNo(), z.getTileNo(), z.getPosition(), z.getSize()));
                }

                while(sIterator.hasNext())
                {
                    Sun s = sIterator.next();
                    entities.add(new Entity(s.getName(), s.getState(), s.getLaneNo(), s.getTileNo(), s.getPosition(), s.getSize(), s.getPositionY()));
                }

                while(projIterator.hasNext())
                {
                    Projectile p = projIterator.next();
                    entities.add(new Entity(p.getType(), p.getState(), p.getLaneNo(), p.getTileNo(), p.getPosition(), p.getSize(), p.getPositionY()));
                }

                if(plant != null)
                {
                    entities.add(new Entity(plant.getName(), plant.getState(), plant.getLaneNo(), plant.getTileNo(), plant.getPosition(), plant.getSize()));
                }
            }
        }

        return entities;
    }
}
