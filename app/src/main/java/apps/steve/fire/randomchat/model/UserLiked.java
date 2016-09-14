package apps.steve.fire.randomchat.model;

/**
 * Created by Steve on 17/07/2016.
 */

public class UserLiked {
    private int edad;
    private String genero;
    private String keyDevice;
    private boolean liked;

    public UserLiked() {
    }

    public UserLiked(int edad, String genero, String keyDevice, boolean liked) {
        this.edad = edad;
        this.genero = genero;
        this.keyDevice = keyDevice;
        this.liked = liked;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getKeyDevice() {
        return keyDevice;
    }

    public void setKeyDevice(String keyDevice) {
        this.keyDevice = keyDevice;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
