package cs102groupproject.SharedObjects;

import java.util.ArrayList;

public class MarketPlace {
    private ArrayList<Furniture> furnitureList;

    public MarketPlace() {
        furnitureList = new ArrayList<>();
    }

    public ArrayList<Furniture> getAvailableItems() {return furnitureList;}

    public boolean buyFurniture(Furniture item, User user)
    {
        {
            user.setSoloCurrency(user.getSoloCurrency() - item.getSoloPrice());
            furnitureList.remove(item);
            return true;
        }
    }
}
