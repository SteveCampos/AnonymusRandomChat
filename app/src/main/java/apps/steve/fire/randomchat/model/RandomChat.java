package apps.steve.fire.randomchat.model;

import android.text.TextUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Steve on 19/05/2016.
 */
@JsonIgnoreProperties({"messages"})
public class RandomChat {



    public static final String WAITING = "WAITING";
    public static final String PARED = "PARED";
    public static final String DESERTED_ONE = "DESERTED_ONE";
    public static final String DESERTED = "DESERTED";

    public static final String WRITING = "Escribiendo...";
    public static final String EN_LINEA = "En Línea";


    private Emisor emisor;
    private Emisor receptor;
    private String estado;
    private String search;
    //private int userNumber;
    private String action;
    private long time;
    private Message messages;


    private String keyChat;
    private boolean hot;
    private ChatMessage lastMessage;
    private int noReaded;

    private String himName;
    private Country country;

    public RandomChat() {

    }

    public Emisor getEmisor() {
        return emisor;
    }

    public void setEmisor(Emisor emisor) {
        this.emisor = emisor;
    }

    public Emisor getReceptor() {
        return receptor;
    }

    public void setReceptor(Emisor receptor) {
        this.receptor = receptor;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Message getMessages() {
        return messages;
    }

    public void setMessages(Message messages) {
        this.messages = messages;
    }

    public String getKeyChat() {
        return keyChat;
    }

    public void setKeyChat(String keyChat) {
        this.keyChat = keyChat;
    }

    public boolean isHot() {
        return hot;
    }

    public void setHot(boolean hot) {
        this.hot = hot;
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

    public String getHimName() {
        return himName;
    }

    public void setHimName(String himName) {
        this.himName = himName;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("emisor", emisor.toMap());
        if (!TextUtils.isEmpty(receptor.getKeyDevice())){
            result.put("receptor", receptor.toMap());
        }
        result.put("estado", estado);
        result.put("search", search);
        result.put("action", action);
        result.put("time", time);
        //result.put("messages"+"/" + keyChat, ChatMessage.getParedMessage().toMap());
        result.put("country", country.toMap());
        return result;
    }

    @Exclude
    public Map<String, Object> toHistoMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("emisor", emisor.toMap());
        if (!TextUtils.isEmpty(receptor.getKeyDevice())){
            result.put("receptor", receptor.toMap());
        }
        result.put("estado", estado);
        result.put("search", search);
        result.put("action", action);
        result.put("time", time);
        //result.put("messages", messages);
        result.put("country", country.toMap());
        result.put("keyChat", keyChat);
        result.put("hot", hot);
        result.put("lastMessage", lastMessage.toMap());
        result.put("noReaded", noReaded);
        return result;
    }



}
