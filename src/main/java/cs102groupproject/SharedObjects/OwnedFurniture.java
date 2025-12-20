package cs102groupproject.SharedObjects;

public class OwnedFurniture {
    private String furnitureID;
    private String userID;
    private boolean isUsedNow;
    private String Color;
    private int xPosition;
    private int yPosition;

    public OwnedFurniture(String furnitureID, String userID, boolean isUsedNow) {
        this.furnitureID = furnitureID;
        this.userID = userID;
        this.isUsedNow = isUsedNow;
        xPosition = 0;
        yPosition = 0;
    }

    public String getFurnitureID() {
        return furnitureID;
    }
    public String getUserID() {
        return userID;
    }
    public boolean getIsUsedNow() {
        return isUsedNow;
    }
    public void setIsUsedNow(boolean isUsedNow) {
        this.isUsedNow = isUsedNow;
    }
    public String getColor() {
        return Color;
    }
    public int getXPosition() {
        return xPosition;
    }
    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }
    public int getYPosition() {
        return yPosition;
    }
    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }
}   
