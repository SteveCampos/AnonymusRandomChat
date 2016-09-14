package apps.steve.fire.randomchat.model;

import java.util.ArrayList;
import java.util.List;

import apps.steve.fire.randomchat.Constants;
import apps.steve.fire.randomchat.adapters.TrendAdapter;

/**
 * Created by Steve on 17/07/2016.
 */

public class UserTrend {
    public int edad;
    public String genero;
    public String keyDevice;
    private List<UserLiked> listUsersLikes = new ArrayList<>();
    private List<UserLiked> listUsersStars = new ArrayList<>();
    private List<UserLiked> listUsersHots = new ArrayList<>();
    private boolean likedByMe;
    private boolean hotedByMe;
    private boolean staredByMe;

    public UserTrend() {
    }

    public UserTrend(int edad, String genero, String keyDevice, List<UserLiked> listUsersLikes, List<UserLiked> listUsersStars, List<UserLiked> listUsersHots, boolean likedByMe, boolean hotedByMe, boolean staredByMe) {
        this.edad = edad;
        this.genero = genero;
        this.keyDevice = keyDevice;
        this.listUsersLikes = listUsersLikes;
        this.listUsersStars = listUsersStars;
        this.listUsersHots = listUsersHots;
        this.likedByMe = likedByMe;
        this.hotedByMe = hotedByMe;
        this.staredByMe = staredByMe;
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

    public List<UserLiked> getListUsersLikes() {
        return listUsersLikes;
    }

    public void setListUsersLikes(List<UserLiked> listUsersLikes) {
        this.listUsersLikes = listUsersLikes;
    }

    public List<UserLiked> getListUsersStars() {
        return listUsersStars;
    }

    public void setListUsersStars(List<UserLiked> listUsersStars) {
        this.listUsersStars = listUsersStars;
    }

    public List<UserLiked> getListUsersHots() {
        return listUsersHots;
    }

    public void setListUsersHots(List<UserLiked> listUsersHots) {
        this.listUsersHots = listUsersHots;
    }

    public boolean isLikedByMe() {
        return likedByMe;
    }

    public void setLikedByMe(boolean likedByMe) {
        this.likedByMe = likedByMe;
    }

    public boolean isHotedByMe() {
        return hotedByMe;
    }

    public void setHotedByMe(boolean hotedByMe) {
        this.hotedByMe = hotedByMe;
    }

    public boolean isStaredByMe() {
        return staredByMe;
    }

    public void setStaredByMe(boolean staredByMe) {
        this.staredByMe = staredByMe;
    }

    public static List<UserTrend> list() {
        List<UserTrend> userTrends = new ArrayList<>();

        List<UserLiked> likeds = new ArrayList<>();
        UserLiked liked = new UserLiked(
                18, Constants.Genero.CHICO.name(), "keydevice", true);
        likeds.add(liked);
        likeds.add(liked);
        likeds.add(liked);


        UserTrend userTrend = new UserTrend(
                18,
                Constants.Genero.CHICO.name(),
                "keydevice",
                likeds,
                likeds,
                likeds,
                true,
                true,
                true
        );
        userTrends.add(userTrend);
        return userTrends;
    }
}
