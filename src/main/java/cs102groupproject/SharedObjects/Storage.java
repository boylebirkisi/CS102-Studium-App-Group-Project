package cs102groupproject.SharedObjects;

import java.util.ArrayList;
/**
 * Class representing a storage for owned furniture items.
 */
public class Storage extends TransferObject{

    private final ArrayList<Furniture> ownedFurnitures;

    public Storage() {
        ownedFurnitures = new ArrayList<>();
    }

    public ArrayList<Furniture> getOwnedFurnitures() {
        return ownedFurnitures;
    }

    public void addFurniture(Furniture furniture) {
        if (!ownedFurnitures.contains(furniture)) {
            ownedFurnitures.add(furniture);
        }
    }

    public void removeFurniture(Furniture furniture) {
        ownedFurnitures.remove(furniture);
    }

    public boolean contains(Furniture furniture) {
        return ownedFurnitures.contains(furniture);
    }
}
