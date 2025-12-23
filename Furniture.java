package cs102groupproject.SharedObjects;

public class Furniture {
    private int ID;
    private String name;
    private String description;
    private int height;
    private int width;
    private int soloPrice;
    private int groupPrice;
    private String category;
    private String imagePath;


    public Furniture(int ID, String name, String description, int height, int width, int soloPrice, int groupPrice, String category, String imagePath) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.height = height;
        this.width = width;
        this.soloPrice = soloPrice;
        this.groupPrice = groupPrice;
        this.category = category;
        this.imagePath = imagePath;
    }

    public int getID() {
        return ID;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getSoloPrice() {
        return soloPrice;
    }

    public void setSoloPrice(int soloPrice) {
        this.soloPrice = soloPrice;
    }

    public int getGroupPrice() {
        return groupPrice;
    }

    public void setGroupPrice(int groupPrice) {
        this.groupPrice = groupPrice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    
    @Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Furniture)) return false;
    Furniture f = (Furniture) o;
    return this.ID == f.ID;
}

@Override
public int hashCode() {
    return Integer.hashCode(ID);
}

public PlaceType getPlaceType() {
    return PlaceType.valueOf(category);
}

}