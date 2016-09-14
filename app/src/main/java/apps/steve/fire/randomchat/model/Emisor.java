package apps.steve.fire.randomchat.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Steve on 19/05/2016.
 */
public class Emisor implements Parcelable{


    private String keyDevice;
    private int edad;
    private String genero;
    private Looking looking;


    protected Emisor(Parcel in) {
        keyDevice = in.readString();
        edad = in.readInt();
        genero = in.readString();
        looking = in.readParcelable(Looking.class.getClassLoader());
    }

    public static final Creator<Emisor> CREATOR = new Creator<Emisor>() {
        @Override
        public Emisor createFromParcel(Parcel in) {
            return new Emisor(in);
        }

        @Override
        public Emisor[] newArray(int size) {
            return new Emisor[size];
        }
    };

    public Looking getLooking() {
        return looking;
    }

    public void setLooking(Looking looking) {
        this.looking = looking;
    }

    public Emisor() {
    }

    public Emisor(String keyDevice, int edad, String genero, Looking looking) {
        this.keyDevice = keyDevice;
        this.edad = edad;
        this.genero = genero;
        this.looking = looking;
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

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(keyDevice);
        dest.writeInt(edad);
        dest.writeString(genero);
        dest.writeParcelable(looking, flags);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("keyDevice", keyDevice);
        result.put("edad", edad);
        result.put("genero", genero);
        result.put("looking", looking.toMap());
        return result;
    }
}
