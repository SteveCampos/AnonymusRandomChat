package apps.steve.fire.randomchat;

/**
 * Created by madhur on 3/1/15.
 */
public class Constants {

    public static final String TAG = "chatbubbles";
    public static final String URL_APP_FIRECHAT = "https://chatsteve.firebaseio.com/random";
    public static final String CHILD_MESSAGES = "messages";
    public static final String CHILD_RANDOMS = "randoms";

    public static final String CHILD_NOTIFICATIONS = "notification";
    public static final String CHILD_USERS = "users";
    public static final String CHILD_TRENDS = "trends";
    public static final String CHILD_USERS_HISTO_CHATS = "histo_chats";
    public static final String CHILD_TRENDS_LIKES = "likes";
    public static final String CHILD_TRENDS_STARS = "stars";
    public static final String CHILD_TRENDS_HOTS = "hots";
    public static final String CHILD_CONNECTION = "connection";
    public static final String CHILD_STATE = "estado";




    public static final String _HERE = "_CREATED_HERE";
    public static final String _PARED = "_PARED_HERE";
    public static final String _NOTIFICATION = "NOTIFICATION_OPENED";

    public static final int SENT = 100;
    public static final int DELIVERED = 101;
    public static final int READED = 102;

    public static final String MESSAGE_TEXT = "text";
    public static final String MESSAGE_IMAGE = "image";
    public static final String MESSAGE_VIDEO = "video";
    public static final String MESSAGE_AUDIO = "audio";

    public static final boolean CHAT_CREATED = true;
    public static final boolean CHAT_PARED = false;

    public static enum Genero{CHICO, CHICA};

    public static final String STATE_ONLINE = "ONLINE";
    public static final String STATE_OFFLINE = "OFFLINE";


    public static final String CHAT_STATE_WRITING = "WRITING";
    public static final String CHAT_STATE_NO_ACTION = "NO_ACTION";
    public static final String CHAT_STATE_PARED = "PARED";
    public static final String CHAT_STATE_WAITING = "WAITING";



    //PATHS

    /*
    public static final String PATH_RANDOM = "/" + Constants.CHILD_RANDOMS + "/";

    public static String getUserPath(String androidID){
        return "/" + Constants.CHILD_USERS + "/" + androidID + "/"+ Constants.CHILD_USERS_HISTO_CHATS + "/";
    }*/
}