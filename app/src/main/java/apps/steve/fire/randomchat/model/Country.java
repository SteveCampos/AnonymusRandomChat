package apps.steve.fire.randomchat.model;

/**
 * Created by Steve on 7/10/2016.
 */

public class Country {

    private int drawable;
    private String name;
    private String ID;

    public Country(int drawable, String name, String ID) {
        this.drawable = drawable;
        this.name = name;
        this.ID = ID;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
