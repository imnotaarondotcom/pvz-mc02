import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class ArmorManager {
    private Map<String, Armor> manager;
    private ArrayList<String> armorList;
    private int index; 
    

    public ArmorManager(){
        armorList = new ArrayList<>();
        index = 0;
        manager = new HashMap<>();
        manager.put("normal", null );
        manager.put("cone", new Cone());
        manager.put("flag", new Flag());
    }

    public Armor getArmor(){
        
        return manager.get(armorList.get(index++ % armorList.size()));
        
    }

    public void loadLevelData(int level){
        String path = "levels/level".concat(Integer.toString(level)).concat(".txt");
        String armorString;
        File f = new File(path);

        try{
            FileReader fileReader = new FileReader(f);
            BufferedReader br = new BufferedReader(fileReader);

             while((armorString = br.readLine()) != null){
                armorList.add(armorString);
             }
             br.close();
        }catch (IOException e){
            System.out.println(path + "not found");
        }

       
        
    }
}
