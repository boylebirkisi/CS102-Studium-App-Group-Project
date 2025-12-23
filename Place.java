package cs102groupproject.SharedObjects;

import java.util.Set;

public class Place {

    private String id;
    private PlaceType type;
    private int maxItem;
    private int x;
    private int y;
    private Set<String> allowedFurnitureTypes;

    public Place(String id, PlaceType type, int maxItem, int x, int y, Set<String> allowedFurnitureTypes) {
        this.id = id;
        this.type = type;
        this.maxItem = maxItem;
        this.x = x;
        this.y = y;
        this.allowedFurnitureTypes = allowedFurnitureTypes;
    }

    public PlaceType getType() {
        return type;
    }

    public int getMaxItem() {
        return maxItem;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getId() {
        return id;
    }

    public boolean canAccept(Furniture furniture, int currentCount) {
        return allowedFurnitureTypes.contains(furniture.getCategory())
                && currentCount < maxItem;
    }

}
