package cs102groupproject.SharedObjects; 

import java.util.ArrayList;
import java.util.List;

/**Represents the storage.
 * @author Gülşen Mercan
 * @date 24/12/2025
 */
public class Storage {
    private static Storage instance;
    private List<Furniture> ownedFurnitures = new ArrayList<>();

    private Storage() {}

    public static Storage getInstance() {
        if (instance == null) {
            instance = new Storage();
        }
        return instance;
    }

    /**
     * Adds furniture to the stored furniture list.
     * @param f furniture
     */
    public void addFurniture(Furniture f) {
        ownedFurnitures.add(f);
    }

    /**
     * Removes furniture from the stored list.
     * @param f furniture
     */
    public void removeFurniture(Furniture f) {
        ownedFurnitures.remove(f);
    }
    
    public List<Furniture> getOwnedFurnitures() {
        return ownedFurnitures;
    }
}
