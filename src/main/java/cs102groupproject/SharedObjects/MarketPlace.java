package cs102groupproject.SharedObjects;

import java.util.ArrayList;

public class MarketPlace {

    private ArrayList<Furniture> furnitureList;

    public MarketPlace() {
        furnitureList = new ArrayList<>();
        furnitureList.add(
            new Furniture(
                1,
                "Small Plant",
                "A nice plant",
                50,
                50,
                100,
                80,
                "DESK_ZONE",
                "/images/computer_middle_01.png"
            )
        );
    }

    public ArrayList<Furniture> getAvailableItems() {
        return furnitureList;
    }

    public void removeItem(Furniture f) {
        furnitureList.remove(f);
    }

    public boolean buyFurniture(Furniture item, Storage storage, User user) {

        if (user.getSoloCurrency() < item.getSoloPrice())
            return false;

        for (Furniture owned : storage.getOwnedFurnitures()) {
            if (owned.getID() == item.getID()) {
                return false;
            }
        }

        user.setSoloCurrency(
                user.getSoloCurrency() - item.getSoloPrice()
        );

        storage.addFurniture(item);
        return true;
    }

}
