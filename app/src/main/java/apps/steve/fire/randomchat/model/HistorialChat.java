package apps.steve.fire.randomchat.model;

import java.util.ArrayList;
import java.util.List;

import apps.steve.fire.randomchat.fragments.ChatsFragment;

/**
 * Created by Steve on 9/07/2016.
 */

public class HistorialChat {
    private String keyChat;
    private boolean star;
    private boolean hot;
    private boolean like;
    private ChatMessage lastMessage;
    private Chater me;
    private Chater emisor;
    private int noReaded;

    public HistorialChat() {
    }

    public HistorialChat(String keyChat, Chater me, Chater emisor) {
        this.keyChat = keyChat;
        this.me = me;
        this.emisor = emisor;
    }

    public String getKeyChat() {
        return keyChat;
    }

    public void setKeyChat(String keyChat) {
        this.keyChat = keyChat;
    }

    public Chater getMe() {
        return me;
    }

    public void setMe(Chater me) {
        this.me = me;
    }

    public Chater getEmisor() {
        return emisor;
    }

    public void setEmisor(Chater emisor) {
        this.emisor = emisor;
    }

    public boolean isStar() {
        return star;
    }

    public void setStar(boolean star) {
        this.star = star;
    }

    public boolean isHot() {
        return hot;
    }

    public void setHot(boolean hot) {
        this.hot = hot;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public ChatMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(ChatMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getNoReaded() {
        return noReaded;
    }

    public void setNoReaded(int noReaded) {
        this.noReaded = noReaded;
    }

    public static List<HistorialChat> list (){
        List<HistorialChat> list = new ArrayList<>();
        Chater chater = new Chater(
                "TYPECHATER",
                "KEYDEVICE",
                18,
                "CHICO",
                new Looking(
                        18,
                        48,
                        "CHICA"
                )
        );

        HistorialChat chat = new HistorialChat("KEY",
                    chater,
                chater
                );
        list.add(chat);
        list.add(chat);
        list.add(chat);
        list.add(chat);
        return list;
    }
}
