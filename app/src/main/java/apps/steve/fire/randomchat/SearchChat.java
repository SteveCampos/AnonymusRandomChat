package apps.steve.fire.randomchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.appyvet.rangebar.RangeBar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.angmarch.circledpicker.CircledPicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import apps.steve.fire.randomchat.model.Connection;
import apps.steve.fire.randomchat.model.GeneroSpinnerAdapter;
import apps.steve.fire.randomchat.model.Looking;
import apps.steve.fire.randomchat.model.User;
import apps.steve.fire.randomchat.notifications.NotificationFireListener;
import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import apps.steve.fire.randomchat.model.Emisor;
import apps.steve.fire.randomchat.model.RandomChat;
import static apps.steve.fire.randomchat.Constants.Genero;
import static apps.steve.fire.randomchat.Utils.calculateAgeGroup;
import static apps.steve.fire.randomchat.Utils.getAndroidID;
import static apps.steve.fire.randomchat.Utils.isBetween;

import butterknife.OnItemSelected;

public class SearchChat extends AppCompatActivity implements RangeBar.OnRangeBarChangeListener, CircledPicker.OnSliderMoveListener {

    public static final String TAG = "FireChatSteve";
    //Firebase refRandom;
    private String android_id = "";
    //Query queryRef;
    //ValueEventListener randomChatListener;
    private int countFabClicked = 0;

    //EMISOR ATTR
    private int emisorEdad;
    private String emisorGenero;

    //RECEPTOR ATTR
    private int receptorEdadMin;
    private int receptorEdadMax;
    private String receptorGenero;

    //Field and method binding for Android view

    //Emisor
    @BindView(R.id.imageview_avatar)
    ImageView imageViewAvatar;

    @BindView(R.id.text_range_age)
    Spinner spinnerGenero;

    @BindView(R.id.circled_picker_edad)
    CircledPicker circledPickerEdad;

    @BindView(R.id.textview_age_group)
    TextView textViewAgeGroup;

    @BindView(R.id.text_groupname)
    TextView textViewGeneroArticulo;

    //Receptor
    @BindView(R.id.imageview_receptor)
    ImageView imageViewReceptor;

    @BindView(R.id.spinner_genero_receptor)
    Spinner spinnerGeneroReceptor;

    @BindView(R.id.textview_genero_articulo_receptor)
    TextView textViewGeneroArticuloReceptor;

    @BindView(R.id.text_age_group)
    TextView textViewAgeGroupReceptor;

    @BindView(R.id.text_age_range)
    TextView textViewAgeRangeReceptor;

    @BindView(R.id.range_bar_edad_receptor)
    RangeBar rangeBarReceptor;

    //UI General
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindArray(R.array.genero) String[] generos;

    @BindString(R.string.article_emisor) String emisorArticle;

    @BindString(R.string.article_boy) String boyArticle;
    @BindString(R.string.article_girl) String girlArticle;

    @BindString(R.string.boy_age_group_1) String boyAgeGroup1;
    @BindString(R.string.boy_age_group_2) String boyAgeGroup2;
    @BindString(R.string.boy_age_group_3) String boyAgeGroup3;
    @BindString(R.string.boy_age_group_4) String boyAgeGroup4;

    @BindString(R.string.girl_age_group_1) String girlAgeGroup1;
    @BindString(R.string.girl_age_group_2) String girlAgeGroup2;
    @BindString(R.string.girl_age_group_3) String girlAgeGroup3;
    @BindString(R.string.girl_age_group_4) String girlAgeGroup4;

    //private enum Genero{CHICO, CHICA};

    Genero genero;
    Genero generoReceptor;

    ArrayList<String> listGroupAge = new ArrayList<>();

    //DATABASE REFERENCES
    DatabaseReference refRandoms;
    FirebaseDatabase database;


    private Emisor emisorPared;
    private Emisor receptorPared;

    ///private BubblesManager bubblesManager;

    @Override
    protected void onDestroy() {
        super.onDestroy();

       /* if (bubblesManager !=null){
            bubblesManager.recycle();
        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_chat);
        init();
    }

    private void init(){

        ButterKnife.bind(this);

        //initializeBubblesManager();

        // FIREBASE INIT

        database = FirebaseDatabase.getInstance();
        refRandoms = database.getReference(Constants.CHILD_RANDOMS);

        setSupportActionBar(toolbar);

        //SET DEFAULT VALUES

        android_id = getAndroidID(getActivity());
        genero = Genero.CHICO;
        generoReceptor = Genero.CHICO;



        circledPickerEdad.setValue(18);


        GeneroSpinnerAdapter spinnerAdapter = new GeneroSpinnerAdapter(this);
        spinnerAdapter.addItems(Arrays.asList(generos));

        spinnerGenero.setAdapter(spinnerAdapter);
        spinnerGeneroReceptor.setAdapter(spinnerAdapter);

        receptorEdadMin = 18;
        receptorEdadMax = 30;
        rangeBarReceptor.setRangePinsByValue(receptorEdadMin, receptorEdadMax);
        rangeBarReceptor.setOnRangeBarChangeListener(this);
        circledPickerEdad.setOnMoveListener(this);


        enableInputs();

        //IF USER NOT EXISTS, CREATE.
        createUser();
    }
/*sssss
    private void addNewBubble() {
        BubbleLayout bubbleView = (BubbleLayout)LayoutInflater.from(SearchChat.this).inflate(R.layout.bubble_layout, null);
        bubbleView.setOnBubbleRemoveListener(new BubbleLayout.OnBubbleRemoveListener() {
            @Override
            public void onBubbleRemoved(BubbleLayout bubble) { }
        });
        bubbleView.setOnBubbleClickListener(new BubbleLayout.OnBubbleClickListener() {

            @Override
            public void onBubbleClick(BubbleLayout bubble) {
                Toast.makeText(getApplicationContext(), "Clicked !",
                        Toast.LENGTH_SHORT).show();
            }
        });
        bubbleView.setShouldStickToWall(true);
        bubblesManager.addBubble(bubbleView, 60, 20);
    }


    private void initializeBubblesManager() {
        bubblesManager = new BubblesManager.Builder(this)
                .setTrashLayout(R.layout.bubble_trash_layout)
                .build();
        bubblesManager.initialize();
    }
    */

    @OnClick(R.id.fab)
    public void fab(){
        //HERE DISABLE INPUTS
        disableInputs();
        Log.d(TAG, "FAB CLICKED");
        //addNewBubble();
        readNodeRandoms();

        //startService(new Intent(getActivity(), NotificationFireListener.class));

        Snackbar.make(fab, "Searching chat ...", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        //* ADD ACTION TO CANCEL SEARCH.
    }

    @Override
    protected void onResume() {
        enableInputs();
        super.onResume();
    }

    private void enabledInputs(boolean enabled){
        //EMISOR
        spinnerGenero.setEnabled(enabled);
        circledPickerEdad.setEnabled(enabled);
        circledPickerEdad.setActivated(enabled);
        circledPickerEdad.setClickable(enabled);

        //RECEPTOR
        spinnerGeneroReceptor.setEnabled(enabled);
        rangeBarReceptor.setEnabled(enabled);
        rangeBarReceptor.setActivated(enabled);
    }

    private void enableInputs(){
        enabledInputs(true);
    }

    private void disableInputs(){
        enabledInputs(false);
    }

    public void launchChatActivity(String key, String start_type) {
        // first parameter is the context, second is the class of the activity to launch
        Intent i = new Intent(getActivity(), ChatActivity.class);
        // put "extras" into the bundle for access in the second activity
        i.putExtra("key_random", key);
        i.putExtra("start_type", start_type);
        i.putExtra("me", receptorPared);
        i.putExtra("emisor", emisorPared);
        // brings up the second activity
        startActivity(i);
    }

    private void createNewChat() {

        Log.d(TAG, "createNewChat()");
        RandomChat chatRandom = new RandomChat();
        Emisor emisor = new Emisor();
        emisor.setKeyDevice(android_id);
        emisor.setEdad(emisorEdad);
        emisor.setGenero(genero.name());
        emisor.setLooking(new Looking(receptorEdadMin, receptorEdadMax, generoReceptor.name()));

        //EL RECEPTOR ESTARÁ VACÍO
        Emisor receptor = new Emisor();

        chatRandom.setEmisor(emisor);
        chatRandom.setReceptor(receptor);
        chatRandom.setEstado(RandomChat.WAITING);
        chatRandom.setTime(new Date().getTime());

        DatabaseReference newNodePush = refRandoms.push();
        String keyChat = "";

        keyChat = newNodePush.getKey();
        Log.d(TAG, "KEY CHAT : " + keyChat);

        final String finalKeyChat = keyChat;
        newNodePush.setValue(chatRandom, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null){
                    Log.d(TAG, "CREATE NEW CHAT ERROR : " + databaseError.getMessage());
                }else{
                    Log.d(TAG, "CREATE NEW CHAT NODE SUCCESS : " + true);
                    launchChatActivity(finalKeyChat, Constants._HERE);
                }
            }
        });

    }

    private String readNodeRandoms(){
        final String[] key = {"ERROR_KEY"};
        com.google.firebase.database.Query queryRandoms = refRandoms.orderByChild("estado").equalTo(RandomChat.WAITING);


        receptorPared = new Emisor();
        receptorPared.setEdad(emisorEdad);
        receptorPared.setGenero(genero.name());
        receptorPared.setKeyDevice(android_id);
        receptorPared.setLooking(new Looking(receptorEdadMin, receptorEdadMax, generoReceptor.name()));


        queryRandoms.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                Log.d(TAG, "There are " + dataSnapshot.getChildrenCount() + " RandomChats");
                Log.d(TAG, "SNAPTSHOT : " + dataSnapshot.getValue());
                boolean encontrado = false;

                if (dataSnapshot.getChildrenCount() > 0) {
                    for (com.google.firebase.database.DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Log.d(TAG, "GETKEY() : " + postSnapshot.getKey());

                        RandomChat chatRandom = postSnapshot.getValue(RandomChat.class);

                        Log.d(TAG, "EMISOR KEYDEVICE : " + chatRandom.getEmisor().getKeyDevice());
                        Log.d(TAG, "EMISOR GENERO: "  + chatRandom.getEmisor().getGenero());
                        Log.d(TAG, "EMISOR  EDAD : "  + chatRandom.getEmisor().getEdad());
                        Log.d(TAG, "EMISOR LOOKING GENERO : " + chatRandom.getEmisor().getLooking().getGenero());
                        Log.d(TAG, "EMISOR LOOKING EDAD MIN : " + chatRandom.getEmisor().getLooking().getEdadMin());
                        Log.d(TAG, "EMISOR LOOKING EDAD MAX : " + chatRandom.getEmisor().getLooking().getEdadMax());
                        Log.d(TAG, "CHAT ESTADO : " + chatRandom.getEstado());
                        Log.d(TAG, "CHAT TIME CREATION: " + chatRandom.getTime());
                        //Log.d(TAG, "USERS NUMBER : " + chatRandom.getUserNumber());


                        //COMPARAR SI EL ANDROID ID DEVICE, NO ES EL MISMO QUE EL ID DEVICE DE ESTE DISPOSITIVO.
                        if (!chatRandom.getEmisor().getKeyDevice().equals(android_id) && chatRandom.getEmisor().getLooking().getGenero().equals(genero.name()))
                        {
                            Log.d(TAG, "SOY EL SEXO QUE ESTÁ BUSCANDO : " + true);
                            if (isBetween(emisorEdad, chatRandom.getEmisor().getLooking().getEdadMin(),chatRandom.getEmisor().getLooking().getEdadMax()))
                            {
                                Log.d(TAG, "ESTOY ENTRE LA EDAD QUE BUSCA : " + true);
                                Log.d(TAG, "DEFINITIVAMENTE, SOY LO QUE BUSCA : " + true);

                                Log.d(TAG, "AHORA VAMOS A VER SI ES LO QUE BUSCO : XD");

                                if (generoReceptor.name().equals(chatRandom.getEmisor().getGenero()))
                                {
                                    Log.d(TAG, "ES EL GÉNERO QUE BUSCO : " + true);

                                    if (isBetween(chatRandom.getEmisor().getEdad(), receptorEdadMin, receptorEdadMax))
                                    {
                                        Log.d(TAG, "ES LA EDAD QUE BUSCO PUTITOS  : " + true);
                                        Log.d(TAG, "AQUÍ HAY UN MATCH PERRAS");
                                        encontrado = true;
                                        emisorPared = chatRandom.getEmisor();
                                        key[0] = postSnapshot.getKey();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

                //AQUÍ CREAR UN NUEVO CHAT SI NO SE ENCONTRÓ
                //O VALIDAR SI EL CHAT SIGUE ESPERANDO XDDDD.
                if (!encontrado)
                {
                    createNewChat();
                }else{
                    pared(key[0]);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.d(TAG, "DatabaseError() : " + databaseError.getMessage());
                enableInputs();
                Snackbar.make(fab, "ERROR: "+ databaseError.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
        countFabClicked++;
        return key[0];
    }

    private void createUser(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if(!prefs.getBoolean("firstTime", false)) {
            //AQUÍ TENGO QUE ASEGURARME QUE CREE EL USUARIO,
            long now = new Date().getTime();
            User user = new User(null, new Connection(Constants.STATE_ONLINE, now),  now, 0, null);

            if (database == null){
                database = FirebaseDatabase.getInstance();
            }
            if (TextUtils.isEmpty(android_id)){
                android_id = getAndroidID(getActivity());
            }

            database.getReference(Constants.CHILD_USERS).
                    child(android_id).
                    setValue(user, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if (databaseError == null){
                                Log.d(TAG, "User :"+ android_id +" created SUCCESS");
                                //ON SUCCESS
                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putBoolean("firstTime", true);
                                editor.apply();

                            }else{
                                //ESCRIBIR EN UN PUTO LOG, QUE ALGO FUE MAL
                                Log.d(TAG, "CREATE USER ERROR: " + databaseError);
                            }
                        }
                    });
        }
    }

    private void pared(final String key) {
        Log.d(TAG, "PARED(), KEY:  " + key );
        DatabaseReference estadoKey = refRandoms.child(key).child("estado");
        estadoKey.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "DataSnapshot : "+ dataSnapshot.getValue());
                if(dataSnapshot.getValue().equals(RandomChat.WAITING))
                {
                    updateEstadoNode(key);
                }else
                {
                    //YA NO ESTÁ ESPERANDO ALGÚN PUTITO SE EMPAREJÓ JUSTO ANTES QUE YO. CARAJO
                    Log.d(TAG, "ALGÚN PUTITO, SE EMPAREJÓ MILISEGUNDOS ANTES QUE YO : " + true);
                    //SI ESTO PASA, ENTONCES VUELVO A LEER TODOS LOS REGISTROS
                    readNodeRandoms();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "DatabaseError : " + databaseError);
            }
        });

    }

    private void updateEstadoNode(final String key)
    {
        //SIGUE ESPERANDO EMPAREJAR YA!!!!!!
        DatabaseReference finded_chat = refRandoms.child(key);
        Map<String, Object> estado = new HashMap<String, Object>();
        estado.put("estado", RandomChat.PARED);
        finded_chat.updateChildren(estado, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Log.d(TAG, "DatabaseReference : " + databaseReference);
                if (databaseError != null){
                    //HUBO UN PUTO ERROR
                    Log.d(TAG, "updateEstadoNode , databaseError : "+ databaseError.getMessage());
                    readNodeRandoms();
                }else{
                    //SE EMPAREJÓ CARAJO
                    //INICIAR LA OTRA ACTIVIDAD CONCHESUVIDA.
                    launchChatActivity(key, Constants._PARED);
                }
            }
        });
    }



    private AppCompatActivity getActivity(){
        return this;
    }


    @Override
    public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
        if (!TextUtils.isEmpty(leftPinValue)&&!TextUtils.isEmpty(rightPinValue))
        {
            receptorEdadMin = Integer.parseInt(leftPinValue);
            receptorEdadMax = Integer.parseInt(rightPinValue);
        }
        //UPDATE UI RECEPTOR HERE!
        updateUIReceptor();
    }

    @OnItemSelected({R.id.text_range_age})
    void onItemSelected(Spinner spinner, int position) {
        switch (position){
            case 0:
                genero = Genero.CHICO;
                break;
            case 1:
                genero = Genero.CHICA;
                break;
            default:
                break;
        }
        //UPDATE UI EMISOR HERE!
        updateUIEmisor();
        /*Log.d(TAG, "POSITION : " + position);
        Log.d(TAG, "String : " + spinner.getItemIdAtPosition(position));
        Snackbar.make(fab,"POS: " + position + ", ITEM" + generos[position], Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();*/
    }

    @OnItemSelected({R.id.spinner_genero_receptor})
    void onItemSelectedReceptor(Spinner spinner, int position){
        switch (position){
            case 0:
                generoReceptor = Genero.CHICO;
                break;
            case 1:
                generoReceptor = Genero.CHICA;
                break;
            default:
                break;
        }
        //UPDATE UI RECEPTOR HERE!
        updateUIReceptor();

    }

    private void updateUIEmisor(){

        //DEFAULT VALUES
        String ageGroup = getString(R.string.boy_age_group_1);
        int imageAvatar = R.drawable.boy_1;

        switch (genero){
            case CHICO:
                if (isBetween(emisorEdad, 0, 24))
                {
                    ageGroup = getString(R.string.boy_age_group_1);
                    imageAvatar = R.drawable.boy_1;
                }
                if (isBetween(emisorEdad, 25, 35))
                {
                    ageGroup = getString(R.string.boy_age_group_2);
                    imageAvatar = R.drawable.man;
                }
                if (isBetween(emisorEdad, 36, 45))
                {
                    ageGroup = getString(R.string.boy_age_group_3);
                    imageAvatar = R.drawable.man_1;
                }
                if (isBetween(emisorEdad, 46, 60))
                {
                    ageGroup = getString(R.string.boy_age_group_4);
                    imageAvatar = R.drawable.man_20;
                }

                imageAvatar = R.drawable.boy_1;
                break;
            case CHICA:

                if (isBetween(emisorEdad, 0, 24))
                {
                    ageGroup = getString(R.string.girl_age_group_1);
                    imageAvatar = R.drawable.girl_2;
                }
                if (isBetween(emisorEdad, 25, 35))
                {
                    ageGroup = getString(R.string.girl_age_group_2);
                    imageAvatar = R.drawable.girl_3;
                }
                if (isBetween(emisorEdad, 36, 45))
                {
                    ageGroup = getString(R.string.girl_age_group_3);
                    imageAvatar = R.drawable.user_woman_20;
                }
                if (isBetween(emisorEdad, 46, 60))
                {
                    ageGroup = getString(R.string.girl_age_group_4);
                    imageAvatar = R.drawable.user_woman_24;
                }

//
                imageAvatar = R.drawable.girl_2;
                break;
            default:
                Log.d(TAG, "WHAT THE HELL GENDER IS THIS?");
        }

        textViewAgeGroup.setText(ageGroup);
        imageViewAvatar.setImageDrawable(ContextCompat.getDrawable(getActivity(), imageAvatar));

    }

    private void updateUIReceptor(){


        listGroupAge.clear();

        //UPDATE UI TEXT RANGE
        if (receptorEdadMin != 0 && receptorEdadMax != 0)
        {
            textViewAgeRangeReceptor.setText(receptorEdadMin + " - " + receptorEdadMax);
        }

        //UPDATE AVATAR, AGE GROUP, AND ARTICLE RECEPTOR
        //DEFAULT VALUES

        int imageAvatar = R.drawable.user_man_12;

        String [][] groups = {
                {boyAgeGroup1, boyAgeGroup2, boyAgeGroup3, boyAgeGroup4},
                {girlAgeGroup1, girlAgeGroup2, girlAgeGroup3, girlAgeGroup4}
        };
        String ageGroups = calculateAgeGroup(groups, generoReceptor, receptorEdadMin, receptorEdadMax);

        switch (generoReceptor){
            case CHICO:
                imageAvatar = R.drawable.user_man_12;
                textViewGeneroArticuloReceptor.setText(boyArticle);
                break;
            case CHICA:
                imageAvatar = R.drawable.user_woman_12;
                textViewGeneroArticuloReceptor.setText(girlArticle);
                break;
            default:
                Log.d(TAG, "WHAT THE HELL GENDER IS THIS?");
        }

        textViewAgeGroupReceptor.setText(ageGroups);
        imageViewReceptor.setImageDrawable(ContextCompat.getDrawable(getActivity(), imageAvatar));

    }




    @Override
    public void onSliderMove(int number) {
        emisorEdad = number;
        Log.d(TAG, ""+number);
        //UPDATE UI EMISOR HERE!
        updateUIEmisor();
    }

}
