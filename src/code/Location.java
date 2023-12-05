package code;

import java.util.ArrayList;

public class Location {
    private String name;
    private ArrayList<LocationButton> buttons = new ArrayList<LocationButton>();

    public Location (String name, ArrayList<LocationButton> buttons) {
        this.name = name;
        this.buttons = buttons;
    }

    public ArrayList<LocationButton> getButtons() {
        return this.buttons;
    }
}
