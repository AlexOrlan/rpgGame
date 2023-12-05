package code;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Map {
    private LinkedHashMap<String, Location> locationMap = new LinkedHashMap<>();
    private String currentLocation;

    public Map() {
        this.currentLocation = "town_central_square";
        initMap();
    }

    public void initMap() {
        String name = "town_central_square";
        ArrayList<LocationButton> buttons = new ArrayList<LocationButton>();
        buttons.add(new LocationButton("alchemy_shop", "Лавка алхимика", 1));
        buttons.add(new LocationButton("mercenary_district", "Обучение у наемников", 1));
        buttons.add(new LocationButton("dark_forest", "Темный лес", 1));
        buttons.add(new LocationButton("abandoned_graveyard", "Заброшенное кладбище", 4));
        buttons.add(new LocationButton("rocky_plains", "Каменистые равнины", 7));
        buttons.add(new LocationButton("mercenary_apply", "Записаться в наемники", 10));
        Location location = new Location(name, buttons);
        locationMap.put(name, location);

        name = "alchemy_shop";
        buttons = new ArrayList<LocationButton>();
        buttons.add(new LocationButton("alchemy_buy", "Купить", 1));
        buttons.add(new LocationButton("town_central_square", "В город", 1));
        location = new Location(name, buttons);
        locationMap.put(name, location);

        name = "mercenary_district";
        buttons = new ArrayList<LocationButton>();
        buttons.add(new LocationButton("mercenary_train", "Научиться", 1));
        buttons.add(new LocationButton("town_central_square", "В город", 1));
        location = new Location(name, buttons);
        locationMap.put(name, location);

        name = "dark_forest";
        buttons = new ArrayList<LocationButton>();
        buttons.add(new LocationButton("forest_search", "Искать монстра", 1));
        buttons.add(new LocationButton("town_central_square", "В город", 1));
        location = new Location(name, buttons);
        locationMap.put(name, location);

        name = "forest_search";
        buttons = new ArrayList<LocationButton>();
        buttons.add(new LocationButton("attack_monster", "Атаковать", 1));
        buttons.add(new LocationButton("dark_forest", "Уйти", 1));
        location = new Location(name, buttons);
        locationMap.put(name, location);

        name = "abandoned_graveyard";
        buttons = new ArrayList<LocationButton>();
        buttons.add(new LocationButton("graveyard_search", "Искать монстра", 1));
        buttons.add(new LocationButton("town_central_square", "В город", 1));
        location = new Location(name, buttons);
        locationMap.put(name, location);

        name = "graveyard_search";
        buttons = new ArrayList<LocationButton>();
        buttons.add(new LocationButton("attack_monster", "Атаковать", 1));
        buttons.add(new LocationButton("abandoned_graveyard", "Уйти", 1));
        location = new Location(name, buttons);
        locationMap.put(name, location);

        name = "rocky_plains";
        buttons = new ArrayList<LocationButton>();
        buttons.add(new LocationButton("plains_search", "Искать монстра", 1));
        buttons.add(new LocationButton("town_central_square", "В город", 1));
        location = new Location(name, buttons);
        locationMap.put(name, location);

        name = "plains_search";
        buttons = new ArrayList<LocationButton>();
        buttons.add(new LocationButton("attack_monster", "Атаковать", 1));
        buttons.add(new LocationButton("rocky_plains", "Уйти", 1));
        location = new Location(name, buttons);
        locationMap.put(name, location);

        name = "mercenary_apply";
        buttons = new ArrayList<LocationButton>();
        buttons.add(new LocationButton("game_ending", "Конец игры", 1));
        buttons.add(new LocationButton("town_central_square", "В город", 1));
        location = new Location(name, buttons);
        locationMap.put(name, location);

        name = "game_ending";
        buttons = new ArrayList<LocationButton>();
        location = new Location(name, buttons);
        locationMap.put(name, location);
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public ArrayList<LocationButton> getLocationButtons(String name) {
        return locationMap.get(name).getButtons();
    }
}
