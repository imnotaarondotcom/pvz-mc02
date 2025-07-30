import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages the creation and cycling of Armor objects based on a level file.
 *
 * Supports armor types like "cone" and "flag", while "normal" means no armor.
 *
 * @author Lim, Israel Sy
 * @author Enriquez, Aaron Mikael Cruz
 * @version 1.0
 * @since 2025-07-30
 */
public class ArmorManager
{
    /**
     * A map storing template {@link Armor} instances, keyed by their type name.
     * Used to create new armor objects.
     */
    private final Map<String, Armor> manager;

    /**
     * An ordered list of armor types to be spawned, read from a level file.
     * This list dictates the sequence of armor for incoming entities.
     */
    private final ArrayList<String> armorList;

    /**
     * The current index in the {@code armorList}, used to cycle through
     * available armor types for entities.
     */
    private int index;

    /**
     * Initializes the armor manager with predefined armor mappings.
     * Sets up the {@code "normal"}, {@code "cone"}, and {@code "flag"} armor types.
     */
    public ArmorManager()
    {
        armorList = new ArrayList<>();
        manager = new HashMap<>();

        manager.put("normal", null);     // Represents zombies without armor
        manager.put("cone", new Cone()); // Template for Cone armor
        manager.put("flag", new Flag()); // Template for Flag armor

        index = 0; // Initialize the cycling index
    }

    /**
     * Returns a new {@link Armor} instance based on the current armor type in the
     * {@code armorList} cycle. The method increments its internal index to
     * rotate through the loaded armor types.
     *
     * @return A new instance of the appropriate {@link Armor} subclass,
     * or {@code null} if the current type is "normal" (no armor).
     */
    public Armor getArmor()
    {
        // Get the armor type from the list, cycling back to the beginning if the end is reached
        String armorType = armorList.get(index++ % armorList.size());

        // If the armor type is null or "normal", return null as no armor is needed
        if (armorType == null || "normal".equals(armorType))
        {
            return null;
        }

        Armor template = manager.get(armorType);

        // Return a new instance of the specific armor type
        if (template instanceof Cone)
        {
            return new Cone();
        }
        else if (template instanceof Flag)
        {
            return new Flag();
        }

        // Return null if the armor type is unrecognized
        return null;
    }

    /**
     * Loads armor types from a specified level file. Each line in the file
     * is expected to represent one zombie's armor type (e.g., "cone", "normal", "flag").
     * The loaded types populate the internal {@code armorList}.
     *
     * @param level The level number, used to construct the file path
     * (e.g., "levels/level1.txt" for level 1).
     */
    public void loadLevelData(int level)
    {
        String path = "levels/level" + level + ".txt";
        File file = new File(path);

        try (BufferedReader br = new BufferedReader(new FileReader(file)))
        {
            String armorString;
            // Read each line until the end of the file
            while ((armorString = br.readLine()) != null)
            {
                // Add the trimmed and lowercased armor string to the list
                armorList.add(armorString.trim().toLowerCase());
            }
        }
        catch (IOException e)
        {
            System.out.println(path + " not found");
        }
    }
}
