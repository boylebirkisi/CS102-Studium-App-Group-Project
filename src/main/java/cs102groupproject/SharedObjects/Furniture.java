package cs102groupproject.SharedObjects;

public class Furniture {
    private int id;
    private String name;
    private String description;
    private int height;
    private int width;
    private int soloPrice;
    private int groupPrice;
    private String category;
    private String imagePath;

    public Furniture(int id, String name, String description, int height, int width, int soloPrice, int groupPrice, String category, String imagePath) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.height = height;
        this.width = width;
        this.soloPrice = soloPrice;
        this.groupPrice = groupPrice;
        this.category = category;
        this.imagePath = imagePath;
    }

    public Furniture(String name, String description, int height, int width, int soloPrice, int groupPrice, String category, String imagePath) {
        id = -1; // Indicates that the furniture has not been assigned an ID yet
        this.name = name;
        this.description = description;
        this.height = height;
        this.width = width;
        this.soloPrice = soloPrice;
        this.groupPrice = groupPrice;
        this.category = category;
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
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
    
}