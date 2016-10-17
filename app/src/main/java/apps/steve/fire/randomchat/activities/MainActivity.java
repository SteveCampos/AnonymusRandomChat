package apps.steve.fire.randomchat.activities;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import apps.steve.fire.randomchat.ChatActivity;
import apps.steve.fire.randomchat.Constants;
import apps.steve.fire.randomchat.R;
import apps.steve.fire.randomchat.Utils;
import apps.steve.fire.randomchat.adapters.CountryAutocompleteAdapter;
import apps.steve.fire.randomchat.adapters.MyFragmentAdapter;
import apps.steve.fire.randomchat.adapters.PlaceAutocompleteAdapter;
import apps.steve.fire.randomchat.firebase.FirebaseHelper;
import apps.steve.fire.randomchat.fragments.ChatsFragment;
import apps.steve.fire.randomchat.fragments.SearchFragment;
import apps.steve.fire.randomchat.fragments.TrendFragment;
import apps.steve.fire.randomchat.interfaces.OnChatsListener;
import apps.steve.fire.randomchat.interfaces.OnSearchListener;
import apps.steve.fire.randomchat.model.Connection;
import apps.steve.fire.randomchat.model.Country;
import apps.steve.fire.randomchat.model.HistorialChat;
import apps.steve.fire.randomchat.model.RandomChat;
import apps.steve.fire.randomchat.model.User;
import apps.steve.fire.randomchat.notifications.NotificationFireListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leolin.shortcutbadger.ShortcutBadger;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, NavigationView.OnNavigationItemSelectedListener, OnChatsListener, OnSearchListener, SearchView.OnQueryTextListener, GoogleApiClient.OnConnectionFailedListener, SearchView.OnSuggestionListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.drawer)
    DrawerLayout drawer;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    FirebaseHelper firebaseHelper;
    MyFragmentAdapter adapter;
    int currentItem;
    String androidID;

    private MenuItem itemSearch;
    private SearchView searchView;


    private final static int REQUEST_PLACE_PICKER = 100;

    protected GoogleApiClient mGoogleApiClient;

    private PlaceAutocompleteAdapter mAdapter;


    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));

    private Cursor cursor;

    CountryAutocompleteAdapter countryAutocompleteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Construct a GoogleApiClient for the {@link Places#GEO_DATA_API} using AutoManage
        // functionality, which automatically sets up the API client to handle Activity lifecycle
        // events. If your activity does not extend FragmentActivity, make sure to call connect()
        // and disconnect() explicitly.

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0/* 0 clientID*/, this)
                .addApi(Places.GEO_DATA_API)
                .build();


        // Register a listener that receives callbacks when a suggestion has been selected


        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        AutocompleteFilter.Builder builder = new AutocompleteFilter.Builder();
        builder.setTypeFilter(Place.TYPE_COUNTRY);
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_GREATER_SYDNEY,
                builder.build());

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            currentItem = savedInstanceState.getInt("current_item");
        }

        if (getIntent().hasExtra("current_item")) {
            currentItem = getIntent().getIntExtra("current_item", 0);
        }

        androidID = Utils.getAndroidID(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Snackbar.make(toolbar, "Searching by: " + query, Snackbar.LENGTH_LONG).show();

        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            String uri = intent.getDataString();
            Log.d(TAG, "uri: " + uri);
            Snackbar.make(toolbar, "Suggestion: " + uri, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart");
        super.onRestart();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        ButterKnife.bind(this);
        initViews();
        initFirebase();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        itemSearch = menu.findItem(R.id.action_search);
        searchView = (SearchView) itemSearch.getActionView();
        searchView.setOnQueryTextListener(this);

        searchView.setOnSuggestionListener(this);


        /*
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(this, MainActivity.class)));*/

        // SOME CODE


        cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.HAS_PHONE_NUMBER}, null, null, null);

        // THE DESIRED COLUMNS TO BE BOUND
        String[] columns = new String[]{ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.HAS_PHONE_NUMBER};
        // THE XML DEFINED VIEWS WHICH THE DATA WILL BE BOUND TO
        int[] to = new int[]{R.id.text_title, R.id.text_description};

        // CREATE THE ADAPTER USING THE CURSOR POINTING TO THE DESIRED DATA AS WELL AS THE LAYOUT INFORMATION
        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.item_place, cursor, columns, to, 0);


        //searchView.setSuggestionsAdapter(simpleCursorAdapter);

        SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView
                .findViewById(R.id.search_src_text);

        List<Country> countries = new ArrayList<>();
        countries.add(new Country(getActivity(), Country.AUSTRALIA));




        countryAutocompleteAdapter = new CountryAutocompleteAdapter(getActivity(), countries);

        searchAutoComplete.setAdapter(countryAutocompleteAdapter);
        searchAutoComplete.setOnItemClickListener(this);
        searchAutoComplete.setOnItemSelectedListener(this);


        //searchView.setIconifiedByDefault(false);

        return true;
    }

    private void launchPlacePicker() {
        // Construct an intent for the place picker
        try {
            PlacePicker.IntentBuilder intentBuilder =
                    new PlacePicker.IntentBuilder();

            /*
            LatLngBounds latLngBounds = new LatLngBounds(
                    new LatLng(76.898, 11.3244),
                    new LatLng(77.6754, 11.2323)
            );
            intentBuilder.setLatLngBounds(latLngBounds);
            */
            Intent intent = intentBuilder.build(this);
            // Start the intent by requesting a result,
            // identified by a request code.
            startActivityForResult(intent, REQUEST_PLACE_PICKER);

        } catch (GooglePlayServicesRepairableException e) {
            Log.e(TAG, "GooglePlayServicesRepairableException: " + e);
            // ...
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e(TAG, "GooglePlayServicesNotAvailableException: " + e);
            // ...
        }
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {

        if (requestCode == REQUEST_PLACE_PICKER
                && resultCode == RESULT_OK) {

            // The user has selected a place. Extract the name and address.
            final Place place = PlacePicker.getPlace(this, data);

            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();
            String attributions = PlacePicker.getAttributions(data);
            if (attributions == null) {
                attributions = "";
            }

            Snackbar.make(toolbar, "name: " + name, Snackbar.LENGTH_LONG).show();

            Log.d(TAG, "name: " + name);
            Log.d(TAG, "address: " + address);
            Log.d(TAG, "attributions: " + attributions);

            /*
            mViewName.setText(name);
            mViewAddress.setText(address);
            mViewAttributions.setText(Html.fromHtml(attributions));*/

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initFirebase() {
        Log.d(TAG, "initFirebase");
        firebaseHelper = new FirebaseHelper(getActivity());
        firebaseHelper.initChats(androidID, this);
        firebaseHelper.setOn();
        //IF USER NOT EXIST, CREATE ONE.
        createUser();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        firebaseHelper.removeChatsListener();
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    private void initViews() {
        Log.d(TAG, "initViews");
        // Adding Toolbar to Main screen
        setSupportActionBar(toolbar);
        // Setting ViewPager for each Tabs
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        tabs.setupWithViewPager(viewPager);
        setupTabIcons(tabs);

        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_whatshot_black_24dp);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        //
        viewPager.addOnPageChangeListener(this);

        Log.d(TAG, "navigationView.getHeaderCount(): " + navigationView.getHeaderCount());
        Log.d(TAG, "navigationView.getMenu().size(): " + navigationView.getMenu().size());
        for (int i = 0; i < navigationView.getMenu().size(); i++) {
            Log.d(TAG, "navigationView.getMenu().getItem(" + i + "): " + navigationView.getMenu().getItem(i));
        }
        // Set behavior of Navigation drawer
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void createUser() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {
            //AQUÍ TENGO QUE ASEGURARME QUE CREE EL USUARIO,
            long now = new Date().getTime();
            User user = new User(null, new Connection(Constants.STATE_ONLINE, now), now, 0, null);
            String android_id = Utils.getAndroidID(this);
            Log.d(TAG, "android_id: " + android_id);
            if (TextUtils.isEmpty(android_id)) {
                android_id = Utils.getAndroidID(this);
            }
            startService(new Intent(getActivity(), NotificationFireListener.class));
            firebaseHelper.createUser(android_id, user);
        }
    }

    private AppCompatActivity getActivity() {
        return this;
    }

    private void setupTabIcons(TabLayout tabLayout) {
        TabLayout.Tab tabSearch = tabLayout.getTabAt(0);
        //TabLayout.Tab tabOrders = tabLayout.getTabAt(1);

        /*
        if (tabSearch != null) {
            tabSearch.setIcon(R.drawable.ic_whatshot_black_24dp);
        }*/
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        Log.d(TAG, "setupViewPager");
        adapter = new MyFragmentAdapter(getSupportFragmentManager());

        adapter.addFragment(SearchFragment.newInstance(), getString(R.string.title_activity_search_chat));
        adapter.addFragment(ChatsFragment.newInstance(androidID), getString(R.string.title_activity_historial));
        adapter.addFragment(ChatsFragment.newInstance(androidID), getString(R.string.title_activity_hots));
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentItem);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.d(TAG, "onPageSelected: " + position);
        currentItem = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        Log.d(TAG, "onOptionsItemSelected item.getItemId(): " + item.getItemId());
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            drawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.d(TAG, "onNavigationItemSelected item.getItemId(): " + item.getItemId());
        // Set item in checked state
        item.setChecked(true);
        // TODO: handle navigation
        // Closing drawer on item click
        drawer.closeDrawers();

        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_main:
                launchPlacePicker();
                //Snackbar.make(fab, "R.id.nav_main", Snackbar.LENGTH_LONG).show();
                break;
        }
        return true;
    }

    @OnClick(R.id.fab)
    void onFabClick() {
        Log.d(TAG, "onFabClick");
        startAnim();

        int currentItem = viewPager.getCurrentItem();

        SearchFragment searchFragment = getSearchFragment();
        if (searchFragment != null) {
            searchFragment.startChat();
        }

        firebaseHelper.startChat(this);

        /*
        switch (currentItem){
            //SEARCH FRAGMENT
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
        }
        */
    }

    private SearchFragment getSearchFragment() {


        List<Fragment> fragments = getFragments();
        if (fragments != null) {
            if (fragments.get(0) instanceof SearchFragment) {
                Log.d(TAG, "fragments.get(0) instanceof SearchFragment");
                return (SearchFragment) fragments.get(0);
            } else {
                Log.d(TAG, "fragments.get(0) instanceof SearchFragment: " + null);
                return null;
            }
        } else {
            return null;
        }
    }

    private List<Fragment> getFragments() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        List<Fragment> fragmentsClean = new ArrayList<>();
        if (fragments != null) {
            Log.d(TAG, "getSupportFragmentManager().getFragments().size(): " + fragments.size());
            for (int i = 0; i < fragments.size(); i++) {
                Log.d(TAG, "FRAGMENT: " + i);
                if ((fragments.get(i) instanceof SearchFragment || fragments.get(i) instanceof ChatsFragment)) {
                    fragmentsClean.add(fragments.get(i));
                }
                /*
                if (fragments.get(i)!=  null){
                    Log.d(TAG, "TAG : " + fragments.get(i).getTag());
                }*/
            }
        }
        return fragmentsClean;
    }

    private ChatsFragment getChatsFragment() {

        List<Fragment> fragments = getFragments();
        if (fragments != null) {
            if (fragments.get(1) instanceof ChatsFragment) {
                Log.d(TAG, "fragments.get(1) instanceof ChatsFragment");
                return (ChatsFragment) fragments.get(1);
            } else {
                Log.d(TAG, "fragments.get(1) instanceof ChatsFragment: " + null);
                return null;
            }
        } else {
            return null;
        }

    }

    private ChatsFragment getHotsFragment() {

        List<Fragment> fragments = getFragments();
        if (fragments != null) {
            if (fragments.size() > 2) {
                if (fragments.get(2) instanceof ChatsFragment) {
                    Log.d(TAG, "fragments.get(2) instanceof ChatsFragment");
                    return (ChatsFragment) fragments.get(2);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void onChatChangedListener(boolean success, List<RandomChat> chats) {
        //int currentItem = viewPager.getCurrentItem();
        Log.d(TAG, "onChatChangedListener success: " + success);

        ChatsFragment chatsFragment = getChatsFragment();

        if (success && chatsFragment != null) {
            chatsFragment.setData(chats);
        }
    }

    @Override
    public void onChatsHotListener(boolean success, List<RandomChat> chatList) {
        Log.d(TAG, "onChatsHotListener success: " + success);

        ChatsFragment hotsFragment = getHotsFragment();

        if (success && hotsFragment != null) {
            hotsFragment.setData(chatList);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        outState.putInt("current_position", currentItem);
        super.onSaveInstanceState(outState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState");
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        //viewPager.setCurrentItem(savedInstanceState.getInt("current_position"));
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onCreate");
        firebaseHelper.setOff();
        super.onDestroy();
    }

    void startAnim() {
        Log.d(TAG, "startAnim");
        viewPager.setAlpha((float) 0.3);
        fab.setEnabled(false);
        avi.setVisibility(View.VISIBLE);
        avi.show();
        // or avi.smoothToShow();
    }

    void stopAnim() {
        Log.d(TAG, "stopAnim");
        viewPager.setAlpha((float) 1);
        fab.setEnabled(true);
        avi.setVisibility(View.GONE);
        avi.hide();
        // or avi.smoothToHide();
    }

    @Override
    public void onChatLaunched(int countryId, String key) {
        Log.d(TAG, "onChatLaunched key: " + key);
        firebaseHelper.removeChatsListener();
        SearchFragment searchFragment = getSearchFragment();
        if (searchFragment != null) {
            searchFragment.enabledInputs(true);
        }
        stopAnim();
        launchChatActivity(countryId, key);
    }

    @Override
    public void onFailed(String error) {
        Log.d(TAG, "onFailed error: " + error);
        SearchFragment searchFragment = getSearchFragment();
        if (searchFragment != null) {
            searchFragment.enabledInputs(true);
        }
        Snackbar.make(toolbar, error, Snackbar.LENGTH_LONG).show();
        stopAnim();
    }

    @Override
    public void onNotCountrySelected() {
        SearchFragment searchFragment = getSearchFragment();
        if (searchFragment != null) {
            searchFragment.enabledInputs(true);
        }
        Snackbar.make(toolbar, "onNotCountrySelected", Snackbar.LENGTH_LONG).show();
        stopAnim();
    }

    private void launchChatActivity(int countryId, String key) {
        Log.d(TAG, "launchChatActivity key: " + key);
        // first parameter is the context, second is the class of the activity to launch
        Intent i = new Intent(getActivity(), ChatActivity.class);
        // put "extras" into the bundle for access in the second activity
        i.putExtra("country_id", countryId);
        i.putExtra("key_random", key);
        startActivity(i);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        //Log.d(TAG, "onQueryTextSubmit query: " + query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // newText is text entered by user to SearchView
        Log.d(TAG, "onQueryTextChange newText: " + newText);
        //Snackbar.make(toolbar, "onQueryTextChange: " + newText, Snackbar.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "onConnectionFailed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSuggestionSelect(int position) {
        Log.d(TAG, " onSuggestionSelect position: " + position);
        /*
        cursor.moveToPosition(position);
        String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
        Log.d(TAG, "name: " + name);*/
        return false;
    }

    @Override
    public boolean onSuggestionClick(int position) {
        Log.d(TAG, "onSuggestionClick position: " + position);
        /*cursor.moveToPosition(position);
        String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
        Log.d(TAG, "name: " + name);*/
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Country country = countryAutocompleteAdapter.getItem(i);

        Snackbar.make(toolbar, country.getName(), Snackbar.LENGTH_LONG).show();

        toolbar.setTitle(country.getName());

        firebaseHelper.setCountry(country);

/*        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                .getPlaceById(mGoogleApiClient, placeId);
        placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

        Toast.makeText(getApplicationContext(), "Clicked: " + primaryText,
                Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);*/
    }


    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);

            toolbar.setTitle(place.getName());

            Snackbar.make(toolbar, "Adress: " + place.getAddress(), Snackbar.LENGTH_LONG).show();

            /*
            // Format details of the place for display and show it in a TextView.
            mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
                    place.getId(), place.getAddress(), place.getPhoneNumber(),
                    place.getWebsiteUri()));

            // Display the third party attributions if set.
            final CharSequence thirdPartyAttribution = places.getAttributions();
            if (thirdPartyAttribution == null) {
                mPlaceDetailsAttribution.setVisibility(View.GONE);
            } else {
                mPlaceDetailsAttribution.setVisibility(View.VISIBLE);
                mPlaceDetailsAttribution.setText(Html.fromHtml(thirdPartyAttribution.toString()));
            }*/

            Log.i(TAG, "Place details received: " + place.getName());

            places.release();
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d(TAG, "onItemSelected position: " + i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Log.d(TAG, "onNothingSelected");
    }
}
