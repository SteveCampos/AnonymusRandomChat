package apps.steve.fire.randomchat.model;

/**
 * Created by Steve on 9/07/2016.
 */

public class Chater {

    private String typeChater;
    private String keyDevice;
    private int edad;
    private String genero;
    private Looking looking;



    public Chater() {

    }

    public Chater(Emisor emisor){
        this.typeChater = emisor.getClass().getSimpleName();
        this.keyDevice = emisor.getKeyDevice();
        this.edad = emisor.getEdad();
        this.genero = emisor.getGenero();
        this.looking = emisor.getLooking();
    }



    public Chater(String typeChater, String keyDevice, int edad, String genero, Looking looking) {
        this.typeChater = typeChater;
        this.keyDevice = keyDevice;
        this.edad = edad;
        this.genero = genero;
        this.looking = looking;
    }

    public String getTypeChater() {
        return typeChater;
    }

    public void setTypeChater(String typeChater) {
        this.typeChater = typeChater;
    }

    public String getKeyDevice() {
        return keyDevice;
    }

    public void setKeyDevice(String keyDevice) {
        this.keyDevice = keyDevice;
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

    public Looking getLooking() {
        return looking;
    }

    public void setLooking(Looking looking) {
        this.looking = looking;
    }
}
