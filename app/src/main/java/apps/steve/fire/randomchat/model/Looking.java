package apps.steve.fire.randomchat.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Steve on 8/06/2016.
 */
public class Looking implements Parcelable{

    private int edadMin;
    private int edadMax;
    private String genero;

    public Looking() {
    }

    public Looking(int edadMin, int edadMax, String genero) {
        this.edadMin = edadMin;
        this.edadMax = edadMax;
        this.genero = genero;
    }

    public Looking(Parcel in) {
        this.edadMin = in.readInt();
        this.edadMax = in.readInt();
        this.genero = in.readString();
    }

    public static final Creator<Looking> CREATOR = new Creator<Looking>() {
        @Override
        public Looking createFromParcel(Parcel in) {
            return new Looking(in);
        }

        @Override
        public Looking[] newArray(int size) {
            return new Looking[size];
        }
    };

    public int getEdadMin() {
        return edadMin;
    }

    public void setEdadMin(int edadMin) {
        this.edadMin = edadMin;
    }

    public int getEdadMax() {
        return edadMax;
    }

    public void setEdadMax(int edadMax) {
        this.edadMax = edadMax;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(edadMin);
        dest.writeInt(edadMax);
        dest.writeString(genero);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("edadMin", edadMin);
        result.put("edadMax", edadMax);
        result.put("genero", genero);
        return result;
    }
}
