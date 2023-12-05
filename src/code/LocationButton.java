package code;

public class LocationButton {
    private String name;
    private String buttonText;
    private int levelReq;

    public LocationButton(String name, String buttonText, int levelReq) {
        this.name = name;
        this.buttonText = buttonText;
        this.levelReq = levelReq;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public int getLevelReq() {
        return levelReq;
    }

    public void setLevelReq(int levelReq) {
        this.levelReq = levelReq;
    }
}
