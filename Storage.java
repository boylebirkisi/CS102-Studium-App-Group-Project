package cs102groupproject.SharedObjects;

import java.util.ArrayList;

public class Storage {
    ArrayList<Furniture> ownedFurnitures;

    public Storage() {
        ownedFurnitures = new ArrayList<>();
    }

    public ArrayList<Furniture> getOwnedFurnitures() {return ownedFurnitures;}
    public void addFurniture(Furniture furniture) {ownedFurnitures.add(furniture);}
    public void removeFurniture(Furniture furniture) {ownedFurnitures.remove(furniture);}
}
