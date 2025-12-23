package cs102groupproject.SharedObjects;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

public class MarketPlace {

    private ArrayList<Furniture> availableItems;

    public MarketPlace() {
        availableItems = new ArrayList<>();
        loadAllImages();
    }

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

    private String changeNameForMarket(String fileName) {
        return fileName
                .replace(".png", "")
                .replace("_", " ");
    }

    private int generateSoloPrice(String fileName) {
        if (fileName.contains("small")) return 100;
        if (fileName.contains("medium")) return 200;
        if (fileName.contains("large")) return 300;
        return 150;
    }

    private String detectPlaceType(String fileName) {
        if (fileName.contains("desk")) return "DESK_ZONE";
        if (fileName.contains("wall") || fileName.contains("painting")) return "WALL_ZONE";
        if (fileName.contains("rug")) return "FLOOR_ZONE";
        return "ROOM_ZONE";
    }

    public ArrayList<Furniture> getAvailableItems() {
        return availableItems;
    }

    public void removeItem(Furniture f) {
        availableItems.remove(f);
    }

    public boolean buyFurniture(Furniture item, Storage storage, User user) {

        if (user.getSoloCurrency() < item.getSoloPrice())
            return false;

        for (Furniture owned : storage.getOwnedFurnitures()) {
            if (owned.getID() == item.getID()) {
                return false;
            }
        }

        user.setSoloCurrency(
                user.getSoloCurrency() - item.getSoloPrice()
        );

        storage.addFurniture(item);
        removeItem(item);
        return true;
    }
}
