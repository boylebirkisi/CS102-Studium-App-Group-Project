package cs102groupproject.SharedObjects;

public class OwnedFurniture {
    private String furnitureID;
    private String userID;
    private boolean isUsedNow;
    private String Color;

    public OwnedFurniture(String furnitureID, String userID, boolean isUsedNow) {
        this.furnitureID = furnitureID;
        this.userID = userID;
        this.isUsedNow = isUsedNow;
    }
}   
