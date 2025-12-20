package cs102groupproject.SharedObjects;

public class Furniture {
    private String id;
    private String name;
    private String description;
    private int height;
    private int width;
    private int soloPrice;
    private int groupPrice;
    private String category;
    private String imagePath;

    public Furniture(String name, String description, int soloPrice, int groupPrice, String category, String imagePath) {
        this.name = name;
        this.description = description;
        this.soloPrice = soloPrice;
        this.groupPrice = groupPrice;
        this.category = category;
        this.imagePath = imagePath;
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
    
}