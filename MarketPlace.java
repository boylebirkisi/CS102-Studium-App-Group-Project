package cs102groupproject.SharedObjects;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

/**
 * Represents the marketplace.
 * @author Gülşen Mercan 
 * @date 24/12/2025
 */
public class MarketPlace {

    private ArrayList<Furniture> availableItems;

    public MarketPlace() {
        availableItems = new ArrayList<>();
        loadAllImages();
    }

    /**Loads all images from the images folder. */
    private void loadAllImages() {
        try {
            URL url = getClass().getResource("/images");
            if (url == null) {
                System.out.println(" /images folder not found");
                return;
            }

            File folder = new File(url.toURI());
            int idCounter = 1;

            for (File file : folder.listFiles()) {

                if (!file.getName().endsWith(".png")) continue;

                String fileName = file.getName();

                String name = changeNameForMarket(fileName);
                String description = "Decorative item";

                int width = 50;
                int height = 50;

                int soloPrice = generateSoloPrice(fileName);
                int groupPrice = soloPrice + 20;

                String placeType = detectPlaceType(fileName);
                String imagePath = "/images/" + fileName;

                availableItems.add(
                    new Furniture(
                        idCounter++,
                        name,
                        description,
                        width,
                        height,
                        soloPrice,
                        groupPrice,
                        placeType,
                        imagePath
                    )
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Changes the names of image files for better understanding.
     * @param fileName image file
     * @return new file name
     */
    private String changeNameForMarket(String fileName) {
        return fileName
                .replace(".png", "")
                .replace("_", " ");
    }

    /**
     * Generates prices based on furniture sizes.
     * @param fileName image file
     * @return price
     */
    private int generateSoloPrice(String fileName) {
        if (fileName.contains("small")) return 100;
        if (fileName.contains("medium")) return 200;
        if (fileName.contains("large")) return 300;
        return 150;
    }

    /**
     * Detects place type based on furniture types.
     * @param fileName image
     * @return place type
     */
    private String detectPlaceType(String fileName) {
        if (fileName.contains("computer")) return "DESK_ZONE";
        if (fileName.contains("wall") || fileName.contains("painting")) return "WALL_LEFT";
        if (fileName.contains("rug") || fileName.contains("lamp")) return "FLOOR";
        if(fileName.contains("lamp")) return "DESK_ZONE";
        return null;
    }

    public ArrayList<Furniture> getAvailableItems() {
        return availableItems;
    }

    public void removeItem(Furniture f) {
        availableItems.remove(f);
    }

    /**
     * Allows the user to buy a furniture item.
     * @param item
     * @param storage
     * @param user
     * @return true if payment was successfuly done
     */
    public boolean buyFurniture(Furniture item, Storage storage, User user) {

        if (user.getSoloCurrency() < item.getSoloPrice())
            return false;

        for (Furniture owned : storage.getOwnedFurnitures()) {
            if (owned.getID() == item.getID()) {
                return false;
            }
        }

        user.setSoloCurrency(user.getSoloCurrency() - item.getSoloPrice());
        storage.addFurniture(item);
        removeItem(item);
        return true;
    }
}