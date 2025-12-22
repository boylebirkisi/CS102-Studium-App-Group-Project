package cs102groupproject.SharedObjects;

import java.util.ArrayList;

public class OfficeLayout {
    private ArrayList<OwnedFurniture> placedItems;
    private int userId;

    public OfficeLayout(int userId) {
        this.userId = userId;
        this.placedItems = new ArrayList<>();
    }

    public ArrayList<OwnedFurniture> getPlacedItems() {return placedItems;}
    public void addPlacedItem(OwnedFurniture item) {placedItems.add(item);}
    public void removePlacedItem(OwnedFurniture item) {placedItems.remove(item);}
    public int getUserId() {return userId;}

    public boolean isSpaceEmpty(Furniture furniture, int x, int y) {
        for (OwnedFurniture item : placedItems) {
            if (item.getXPosition() <= x + furniture.getWidth() &&
                item.getYPosition() <= y + furniture.getHeight() &&
                item.getXPosition() >= x && item.getYPosition() >= y) 
            {
                return false;
            }
        }
        return true;
    }
}
