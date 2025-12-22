package cs102groupproject.SharedObjects;

import javafx.scene.paint.Color;

public class OwnedFurniture {
    private int furnitureID;
    private int userID;
    private Furniture furniture;
    private boolean isUsedNow;
    private Color color;          
    private int offsetX;
    private int offsetY;
    private PlaceType placeType;
    private String placeId; 

    public OwnedFurniture(int furnitureID, int userID, boolean isUsedNow, PlaceType placeType) {
        this.furnitureID = furnitureID;
        this.userID = userID;
        this.isUsedNow = isUsedNow;
        this.offsetX = 0;
        this.offsetY = 0;
        this.placeType = placeType;
    }

    public int getFurnitureID() {
        return furnitureID;
    }

    public int getUserID() {
        return userID;
    }

    public boolean getIsUsedNow() {
        return isUsedNow;
    }

    public void setIsUsedNow(boolean isUsedNow) {
        this.isUsedNow = isUsedNow;
    }

    public Color getColor() {
        return color;
    }

    public Furniture getFurniture() {     
        return furniture;
    }

    public int getXPosition() {
        return offsetX;
    }

    public void setxPosition(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getYPosition() {
        return offsetY;
    }

    public void setyPosition(int offsetY) {
        this.offsetY = offsetY;
    }

    public PlaceType getPlaceType() {
        return placeType;
    }

    public String getPlaceId() {      
        return placeId;
    }

    public void setPlaceId(String placeId) { 
        this.placeId = placeId;
    }

    public void setPlaceType(PlaceType placeType) {
        this.placeType = placeType;
    }


}
