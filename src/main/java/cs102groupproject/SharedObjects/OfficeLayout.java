/*package cs102groupproject.SharedObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OfficeLayout {
    private ArrayList<OwnedFurniture> placedItems;
    private List<Place> places;
    private String userId;

    public OfficeLayout(String userId) {
        this.userId = userId;
        this.placedItems = new ArrayList<>();
        this.places = List.of(
            new Place(
                PlaceType.DESK_ZONE,
                2,
                120,
                300,
                Set.of("DESK", "CHAIR", "LAMP")
            ),
            new Place(
                PlaceType.WALL_LEFT,
                1,
                40,
                100,
                Set.of("PAINTING", "CLOCK")
            ),
            new Place(
                PlaceType.WINDOW,
                1,
                600,
                90,
                Set.of("PLANT")
            )
        );
    }

    public List<Place> getPlaces() {
        return places;
    }

    public ArrayList<OwnedFurniture> getPlacedItems() {return placedItems;}
    public void addPlacedItem(OwnedFurniture item) {placedItems.add(item);}
    public void removePlacedItem(OwnedFurniture item) {placedItems.remove(item);}
    public String getUserId() {return userId;}

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

    public boolean canPlaceFurniture(Furniture furniture, Place place){
        long countInPlace = placedItems.stream().filter(item -> item.getPlaceType() == place.getType()).count();
        return place.canAccept(furniture.getCategory(), (int) countInPlace);
    }
} */

package cs102groupproject.SharedObjects;

import java.util.Set;

public class OfficeLayout {

    public static final Place DESK_PLACE = new Place(
            "DESK_1", PlaceType.DESK_ZONE, 1, 400, 300, Set.of("desk")
    );

    public static final Place FLOOR_PLACE = new Place(
            "FLOOR_1", PlaceType.FLOOR, 5, 100, 500, Set.of("chair", "table")
    );

    public static final Place WALL_LEFT_PLACE = new Place(
            "WALL_LEFT_1", PlaceType.WALL_LEFT, 3, 0, 100, Set.of("painting", "shelf")
    );

    public static final Place WALL_RIGHT_PLACE = new Place(
            "WALL_RIGHT_1", PlaceType.WALL_RIGHT, 3, 700, 100, Set.of("painting", "shelf")
    );

    public static final Place WINDOW_PLACE = new Place(
            "WINDOW_1", PlaceType.WINDOW, 2, 350, 50, Set.of("curtain")
    );

    public static OwnedFurniture deskItem;
    public static OwnedFurniture floorItem;

    static {
        Furniture deskFurniture = new Furniture(001, "Desk", "Wooden desk", 120, 60, 100, 80, "desk", "/images/desk.png");
        Furniture chairFurniture = new Furniture(002, "Chair", "Office chair", 50, 50, 50, 30, "chair", "/images/chair.png");

        deskItem = new OwnedFurniture(001, 01, false, PlaceType.DESK_ZONE);
        deskItem = new OwnedFurniture(002, 01, false, PlaceType.DESK_ZONE);

        deskItem.setPlaceId(DESK_PLACE.getId());
        deskItem.setPlaceType(DESK_PLACE.getType());

        floorItem.setPlaceId(FLOOR_PLACE.getId());
        floorItem.setPlaceType(FLOOR_PLACE.getType());
    }
}
