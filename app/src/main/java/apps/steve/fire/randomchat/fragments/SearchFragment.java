package apps.steve.fire.randomchat.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.rangebar.RangeBar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.angmarch.circledpicker.CircledPicker;

import java.util.ArrayList;
import java.util.Arrays;

import apps.steve.fire.randomchat.ChatActivity;
import apps.steve.fire.randomchat.Constants;
import apps.steve.fire.randomchat.Historial;
import apps.steve.fire.randomchat.R;
import apps.steve.fire.randomchat.Utils;
import apps.steve.fire.randomchat.activities.MainActivity;
import apps.steve.fire.randomchat.firebase.FirebaseHelper;
import apps.steve.fire.randomchat.interfaces.OnSearchListener;
import apps.steve.fire.randomchat.model.Chater;
import apps.steve.fire.randomchat.model.Emisor;
import apps.steve.fire.randomchat.model.GeneroSpinnerAdapter;
import apps.steve.fire.randomchat.model.HistorialChat;
import apps.steve.fire.randomchat.model.Looking;
import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;

import static apps.steve.fire.randomchat.Utils.calculateAgeGroup;
import static apps.steve.fire.randomchat.Utils.getAndroidID;
import static apps.steve.fire.randomchat.Utils.isBetween;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements RangeBar.OnRangeBarChangeListener, CircledPicker.OnSliderMoveListener{


    //Field and method binding for Android view

    private OnSearchListener listener;

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

    @BindArray(R.array.genero)
    String[] generos;

    @BindString(R.string.article_emisor)
    String emisorArticle;

    @BindString(R.string.article_boy)
    String boyArticle;
    @BindString(R.string.article_girl)
    String girlArticle;

    @BindString(R.string.boy_age_group_1)
    String boyAgeGroup1;
    @BindString(R.string.boy_age_group_2)
    String boyAgeGroup2;
    @BindString(R.string.boy_age_group_3)
    String boyAgeGroup3;
    @BindString(R.string.boy_age_group_4)
    String boyAgeGroup4;

    @BindString(R.string.girl_age_group_1)
    String girlAgeGroup1;
    @BindString(R.string.girl_age_group_2)
    String girlAgeGroup2;
    @BindString(R.string.girl_age_group_3)
    String girlAgeGroup3;
    @BindString(R.string.girl_age_group_4)
    String girlAgeGroup4;


    public static final String TAG = SearchFragment.class.getSimpleName();
    //Firebase refRandom;
    private String android_id = "";
    //Query queryRef;
    //ValueEventListener randomChatListener;
    private int countFabClicked = 0;

    //EMISOR ATTR
    private int emisorEdad;

    //RECEPTOR ATTR
    private int receptorEdadMin;
    private int receptorEdadMax;


    Constants.Genero genero;
    Constants.Genero generoReceptor;

    ArrayList<String> listGroupAge = new ArrayList<>();

    //DATABASE REFERENCES
    //DatabaseReference refRandoms;
    //FirebaseDatabase database;


    private Emisor emisorPared;
    private Emisor receptorPared;
    //FirebaseHelper firebaseHelper;

    @BindView(R.id.layout)
    LinearLayoutCompat linearLayoutCompat;

    private View view;

    private boolean isViewsCreated = false;
    private boolean isSpinnerEmisorFirsTime = true;
    private boolean isSpinnerReceptorFirsTime = true;

    public
    SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(){
        //SearchFragment searchFragment = new SearchFragment();
        return new SearchFragment();
    }

    public SearchFragment getInstance(){
        return newInstance();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView");
        view  =  inflater.inflate(R.layout.content_search_chat, container, false);
        setRetainInstance(true);
        //ButterKnife.bind(this, view);
        //init();
        return view;
    }

    @Override
    public void onResume() {
        ButterKnife.bind(this, view);
        Log.d(TAG, "onResume");
        init();
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");
        isViewsCreated = true;
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView");
        isViewsCreated = false;
        super.onDestroyView();
    }

    public void startChat() {
        if (!isViewsCreated){
            return;
        }

        Snackbar.make(linearLayoutCompat, R.string.app_name, Snackbar.LENGTH_LONG).show();
        enabledInputs(false);
        Utils.saveEmisor(getActivity(), new Emisor(android_id, emisorEdad, genero.name(), new Looking(receptorEdadMin, receptorEdadMax, generoReceptor.name())));
        //firebaseHelper.startChat(this);
    }



    private void init() {

        Log.d(TAG, "init()");

        GeneroSpinnerAdapter spinnerAdapter = new GeneroSpinnerAdapter(getActivity());
        spinnerAdapter.addItems(Arrays.asList(generos));
        spinnerGenero.setAdapter(spinnerAdapter);
        spinnerGeneroReceptor.setAdapter(spinnerAdapter);
        //firebaseHelper = new FirebaseHelper(getActivity());


        // FIREBASE INIT

        /*database = FirebaseDatabase.getInstance();
        refRandoms = database.getReference(Constants.CHILD_RANDOMS);*/

        //SET DEFAULT VALUES

        android_id = getAndroidID(getActivity());
        //genero = Constants.Genero.CHICA;
        //generoReceptor = Constants.Genero.CHICA;



        circledPickerEdad.setValue(18);

        receptorEdadMin = 18;
        receptorEdadMax = 48;
        rangeBarReceptor.setRangePinsByValue(receptorEdadMin, receptorEdadMax);
        rangeBarReceptor.setOnRangeBarChangeListener(this);
        circledPickerEdad.setOnMoveListener(this);
        enabledInputs(true);


        //PREFERENCES

        Log.d(TAG, "SETTING PREFERENCES ...");

        Emisor prefEmisor = Utils.getEmisor(getActivity());
        circledPickerEdad.setValue(prefEmisor.getEdad());

        genero = Constants.Genero.valueOf(prefEmisor.getGenero());
        emisorEdad = prefEmisor.getEdad();
        generoReceptor = Constants.Genero.valueOf(prefEmisor.getLooking().getGenero());
        switch (genero){
            case CHICO:
                spinnerGenero.setSelection(0);
                break;
            case CHICA:
                spinnerGenero.setSelection(1);
                break;
        }
        switch (generoReceptor){
            case CHICO:
                spinnerGeneroReceptor.setSelection(0);
                break;
            case CHICA:
                spinnerGeneroReceptor.setSelection(1);
                break;
        }

        //spinnerGenero.setSelection(((GeneroSpinnerAdapter)spinnerGenero.getAdapter()).getPosition(prefEmisor.getGenero()));
        //spinnerGeneroReceptor.setSelection(((GeneroSpinnerAdapter)spinnerGenero.getAdapter()).getPosition(prefEmisor.getLooking().getGenero()));
        Log.d(TAG, "((GeneroSpinnerAdapter)spinnerGenero.getAdapter()).getPosition(prefEmisor.getLooking().getGenero()): " + ((GeneroSpinnerAdapter)spinnerGenero.getAdapter()).getPosition(prefEmisor.getLooking().getGenero()));
        receptorEdadMin = prefEmisor.getLooking().getEdadMin();
        receptorEdadMax = prefEmisor.getLooking().getEdadMax();
        updateUIEmisor();
        updateUIReceptor();
    }

    @Override
    public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {


        if (!TextUtils.isEmpty(leftPinValue) && !TextUtils.isEmpty(rightPinValue)) {
            receptorEdadMin = Integer.parseInt(leftPinValue);
            receptorEdadMax = Integer.parseInt(rightPinValue);


            if (receptorEdadMin <18){
                rangeBarReceptor.setRangePinsByValue(18, receptorEdadMax);
                return;
            }
            if (receptorEdadMax > 60){
                rangeBarReceptor.setRangePinsByValue(receptorEdadMin, 60);
                return;
            }
        }
        //UPDATE UI RECEPTOR HERE!
        updateUIReceptor();
    }

    @Override
    public void onSliderMove(int number) {
        emisorEdad = number >= 18 ? number : 18;
        Log.d(TAG, "" + number);
        //UPDATE UI EMISOR HERE!
        updateUIEmisor();
    }


    @OnItemSelected({R.id.text_range_age})
    void onItemSelected(Spinner spinner, int position) {
        Log.d(TAG, "onItemSelected position: "+ position);
        /*if (isSpinnerEmisorFirsTime){
            isSpinnerEmisorFirsTime = false;
            return;
        }*/
        switch (position) {
            case 0:
                genero = Constants.Genero.CHICO;
                break;
            case 1:
                genero = Constants.Genero.CHICA;
                break;
            default:
                break;
        }
        //UPDATE UI EMISOR HERE!
        updateUIEmisor();
    }


    @OnItemSelected({R.id.spinner_genero_receptor})
    void onItemSelectedReceptor(Spinner spinner, int position) {
  /*      if (isSpinnerReceptorFirsTime){
            isSpinnerReceptorFirsTime = false;
            return;
        }
*/
        Log.d(TAG, "onItemSelectedReceptor position: "+ position);
        switch (position) {
            case 0:
                generoReceptor = Constants.Genero.CHICO;
                break;
            case 1:
                generoReceptor = Constants.Genero.CHICA;
                break;
            default:
                break;
        }
        //UPDATE UI RECEPTOR HERE!
        updateUIReceptor();

    }


    private void updateUIEmisor() {

        //DEFAULT VALUES
        String ageGroup = getString(R.string.boy_age_group_1);
        int imageAvatar = R.drawable.boy_1;

        switch (genero) {
            case CHICO:
                if (isBetween(emisorEdad, 0, 24)) {
                    ageGroup = getString(R.string.boy_age_group_1);
                    imageAvatar = R.drawable.boy_1;
                }
                if (isBetween(emisorEdad, 25, 35)) {
                    ageGroup = getString(R.string.boy_age_group_2);
                    imageAvatar = R.drawable.man;
                }
                if (isBetween(emisorEdad, 36, 45)) {
                    ageGroup = getString(R.string.boy_age_group_3);
                    imageAvatar = R.drawable.man_1;
                }
                if (isBetween(emisorEdad, 46, 60)) {
                    ageGroup = getString(R.string.boy_age_group_4);
                    imageAvatar = R.drawable.man_20;
                }

                imageAvatar = R.drawable.boy_1;
                break;
            case CHICA:

                if (isBetween(emisorEdad, 0, 24)) {
                    ageGroup = getString(R.string.girl_age_group_1);
                    imageAvatar = R.drawable.girl_2;
                }
                if (isBetween(emisorEdad, 25, 35)) {
                    ageGroup = getString(R.string.girl_age_group_2);
                    imageAvatar = R.drawable.girl_3;
                }
                if (isBetween(emisorEdad, 36, 45)) {
                    ageGroup = getString(R.string.girl_age_group_3);
                    imageAvatar = R.drawable.user_woman_20;
                }
                if (isBetween(emisorEdad, 46, 60)) {
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

    private void updateUIReceptor() {


        listGroupAge.clear();

        //UPDATE UI TEXT RANGE
        if (receptorEdadMin != 0 && receptorEdadMax != 0) {
            textViewAgeRangeReceptor.setText(receptorEdadMin + " - " + receptorEdadMax);
        }

        //UPDATE AVATAR, AGE GROUP, AND ARTICLE RECEPTOR
        //DEFAULT VALUES

        int imageAvatar = R.drawable.user_man_12;

        String[][] groups = {
                {boyAgeGroup1, boyAgeGroup2, boyAgeGroup3, boyAgeGroup4},
                {girlAgeGroup1, girlAgeGroup2, girlAgeGroup3, girlAgeGroup4}
        };
        String ageGroups = calculateAgeGroup(groups, generoReceptor, receptorEdadMin, receptorEdadMax);

        switch (generoReceptor) {
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

    public void enabledInputs(boolean enabled) {
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




    /*
    @Override
    public void onChatLaunched(String key) {
        firebaseHelper.removeListenerRead();
        enabledInputs(true);
        listener.onChatLaunched(key);
    }

    @Override
    public void onFailed(String error) {
        firebaseHelper.removeListenerRead();
        enabledInputs(true);
        listener.onFailed(error);
    }*/

    // This event fires 1st, before creation of fragment or any views
    // The onAttach method is called when the Fragment instance is associated with an Activity.
    // This does not mean the Activity is fully initialized.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSearchListener) {
            listener = (OnSearchListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement OnSearchListener");
        }
    }

    // This method is called when the fragment is no longer connected to the Activity
    // Any references saved in onAttach should be nulled out here to prevent memory leaks.
    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }
}
