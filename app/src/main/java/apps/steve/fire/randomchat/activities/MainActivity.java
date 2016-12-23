package apps.steve.fire.randomchat.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import apps.steve.fire.randomchat.BuildConfig;
import apps.steve.fire.randomchat.ChatActivity;
import apps.steve.fire.randomchat.Constants;
import apps.steve.fire.randomchat.R;
import apps.steve.fire.randomchat.UploadImge;
import apps.steve.fire.randomchat.Utils;
import apps.steve.fire.randomchat.adapters.CountryAutocompleteAdapter;
import apps.steve.fire.randomchat.adapters.MyFragmentAdapter;
import apps.steve.fire.randomchat.firebase.FirebaseHelper;
import apps.steve.fire.randomchat.fragments.ChatsFragment;
import apps.steve.fire.randomchat.fragments.PostsFragment;
import apps.steve.fire.randomchat.fragments.SearchFragment;
import apps.steve.fire.randomchat.interfaces.OnChatsListener;
import apps.steve.fire.randomchat.interfaces.OnPostListener;
import apps.steve.fire.randomchat.interfaces.OnSearchListener;
import apps.steve.fire.randomchat.model.ChatMessage;
import apps.steve.fire.randomchat.model.Connection;
import apps.steve.fire.randomchat.model.Country;
import apps.steve.fire.randomchat.model.RandomChat;
import apps.steve.fire.randomchat.model.User;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import saschpe.versioninfo.widget.VersionInfoDialogFragment;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, NavigationView.OnNavigationItemSelectedListener, OnChatsListener, OnSearchListener, AdapterView.OnItemClickListener, OnPostListener {

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


    CountryAutocompleteAdapter countryAutocompleteAdapter;

    private List<RandomChat> chats = new ArrayList<>();
    private List<RandomChat> chatsHot = new ArrayList<>();
    private List<RandomChat> posts = new ArrayList<>();


    private boolean launchedChat = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        launchedChat= false;
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
        //searchView.setOnQueryTextListener(this);

        //searchView.setOnSuggestionListener(this);

        //searchView.setSuggestionsAdapter(simpleCursorAdapter);

        SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView
                .findViewById(R.id.search_src_text);


        countryAutocompleteAdapter = new CountryAutocompleteAdapter(getActivity(), Country.getAll(getActivity()));
        searchAutoComplete.setAdapter(countryAutocompleteAdapter);
        searchAutoComplete.setOnItemClickListener(this);
        //searchAutoComplete.setOnItemSelectedListener(this);
        //searchView.setIconifiedByDefault(false);
        return true;
    }

    private void initFirebase() {
        Log.d(TAG, "initFirebase");
        firebaseHelper = new FirebaseHelper(getActivity());
        firebaseHelper.initChats(androidID, this);
        firebaseHelper.setOn();
        setCountry(getPrefCountry());
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
        if (!launchedChat){
            firebaseHelper.setOff();
        }
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

        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_whatshot_white_24dp);
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
            //startService(new Intent(getActivity(), NotificationFireListener.class));
            firebaseHelper.createUser(android_id, user);
        }
    }

    private AppCompatActivity getActivity() {
        return this;
    }

    /*
    private void setupTabIcons(TabLayout tabLayout) {
        TabLayout.Tab tabSearch = tabLayout.getTabAt(0);
        //TabLayout.Tab tabOrders = tabLayout.getTabAt(1);


        if (tabSearch != null) {
            tabSearch.setIcon(R.drawable.ic_whatshot_black_24dp);
        }
    }*/

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        Log.d(TAG, "setupViewPager");
        adapter = new MyFragmentAdapter(getSupportFragmentManager());

        adapter.addFragment(SearchFragment.newInstance(), getString(R.string.title_activity_search_chat));
        adapter.addFragment(ChatsFragment.newInstance(androidID, ChatsFragment.TYPE_ALL), getString(R.string.title_activity_historial));
        adapter.addFragment(ChatsFragment.newInstance(androidID, ChatsFragment.TYPE_HOTS), getString(R.string.title_activity_hots));
        adapter.addFragment(PostsFragment.newInstance(androidID), getString(R.string.title_activity_posts));
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
        switch (currentItem) {
            case 0:
                break;
            case 1:
                ChatsFragment fragment = getChatsFragment();
                if (fragment != null && chats != null){
                    fragment.setData(chats);
                }
                break;
            case 2:
                ChatsFragment hotFragment = getHotsFragment();
                if (hotFragment != null && chatsHot != null){
                    getHotsFragment().setData(chatsHot);
                }
                break;
            case 3:
                Log.d(TAG, "Post");
                PostsFragment postFragment = getPostsFragment();
                if (postFragment != null && posts != null){
                    getPostsFragment().setData(posts);
                }
                break;
        }
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
        if (id == android.R.id.home) {
            drawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    // TODO: handle navigation
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.d(TAG, "onNavigationItemSelected item.getItemId(): " + item.getItemId());
        // Set item in checked state
        item.setChecked(true);
        // Closing drawer on item click
        drawer.closeDrawers();

        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_main:
                break;
            case R.id.nav_credits:
                showCredits();
                break;
            /*case R.id.nav_upload_image:
                startActivity(new Intent(this, UploadImge.class));
                break;*/
        }
        return true;
    }

    public void showCredits(){
        VersionInfoDialogFragment
                .newInstance(
                        getString(R.string.app_name),
                        BuildConfig.VERSION_NAME,
                        getString(R.string.app_credits_content),
                        R.mipmap.ic_launcher)
                .show(getFragmentManager(), "version_info");
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

        if(currentItem == 0 || currentItem == 1 || currentItem == 2 ){
            firebaseHelper.startChat(this);
        }



        switch (currentItem){
            //SEARCH FRAGMENT
            /*
            case 0:
                firebaseHelper.startChat(this);
                break;
            case 1:
                firebaseHelper.startChat(this);
                break;
            case 2:
                firebaseHelper.startChat(this);
                break;*/
            case 3:
                showDialogNewPost();
                Log.d(TAG, "Posts");
                break;
        }

    }

    private void showDialogNewPost(){
        new MaterialDialog.Builder(this)
                .title(R.string.dialog_title_create_post)
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE)
                .input(R.string.dialog_input_create_post, R.string.dialog_post_prefill, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        // Do something
                        Snackbar.make(toolbar, R.string.action_creating_post, Snackbar.LENGTH_LONG).show();
                        firebaseHelper.createNewPost(
                                Utils.getEmisor(getActivity()),
                                new ChatMessage(
                                        input.toString(),
                                        androidID,
                                        Constants.SENT,
                                        Constants.MESSAGE_TEXT,
                                        -new Date().getTime(),
                                        "2325"
                                ),
                                MainActivity.this);
                        dialog.dismiss();
                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                stopAnim();
            }
        }).show();

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
                if ((fragments.get(i) instanceof SearchFragment || fragments.get(i) instanceof ChatsFragment || fragments.get(i) instanceof PostsFragment)) {
                    fragmentsClean.add(fragments.get(i));
                }

                if (fragments.get(i) != null) {
                    Log.d(TAG, "TAG : " + fragments.get(i).getTag());
                }
            }
        }
        return fragmentsClean;
    }

    private ChatsFragment getChatsFragment() {

        List<Fragment> fragments = getFragments();
        if (fragments != null) {
            if (fragments.size() < 2) {
                return null;
            }
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

    private PostsFragment getPostsFragment() {
        List<Fragment> fragments = getFragments();
        if (fragments != null) {

            for (Fragment f: fragments) {
                if (f instanceof PostsFragment){
                    return (PostsFragment) f;
                }
            }
        } else {
            return null;
        }

        return null;
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
        if (chats == null) {
            return;
        }

        this.chats = chats;

        ChatsFragment chatsFragment = getChatsFragment();

        if (success && chatsFragment != null) {
            chatsFragment.setData(chats);
        }
    }

    @Override
    public void onChatsHotListener(boolean success, List<RandomChat> chatList) {
        Log.d(TAG, "onChatsHotListener success: " + success);

        if (chatList == null) {
            return;
        }

        this.chatsHot = chatList;

        ChatsFragment hotsFragment = getHotsFragment();
        if (success && hotsFragment != null) {
            hotsFragment.setData(chatList);
        }
    }

    @Override
    public void onPostItemClicked(RandomChat item, View v, boolean bool) {
        Log.d(TAG, "onPostItemClicked");
        startAnim();
        firebaseHelper.paredByPost(item, Utils.getEmisor(getActivity()), this);
    }

    @Override
    public void onLoadMore() {
        switch (currentItem) {
            case 0:
                //send List<> All
                Log.d(TAG, "currentItem: " + 0);
                break;
            case 1:
                firebaseHelper.incrementLimit(20);
                //send List<> Hots
                Log.d(TAG, "currentItem: " + 1);
                break;
            case 2:
                firebaseHelper.incrementHotsLimit(20);
                break;
            case 3:
                firebaseHelper.incrementPostLimit(20);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPostCreated() {
        stopAnim();
        Snackbar.make(toolbar, R.string.post_created, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onChatItemClicked(RandomChat item) {
        launchChatActivity(item, Constants._HERE);
    }

    @Override
    public void onPostsListener(boolean success, List<RandomChat> posts) {
        Log.d(TAG, "onChatsHotListener success: " + success);

        if (posts == null) {
            return;
        }

        this.posts = posts;

        PostsFragment postFragment = getPostsFragment();
        if (success && postFragment != null) {
            postFragment.setData(posts);
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
        launchedChat = true;
        Log.d(TAG, "launchChatActivity key: " + key);
        // first parameter is the context, second is the class of the activity to launch
        Intent i = new Intent(getActivity(), ChatActivity.class);
        // put "extras" into the bundle for access in the second activity
        i.putExtra("country_id", countryId);
        i.putExtra("key_random", key);
        startActivity(i);
    }
    private void launchChatActivity(RandomChat item, String start_type) {
        launchedChat = true;
        // first parameter is the context, second is the class of the activity to launch
        Intent i = new Intent(getActivity(), ChatActivity.class);
        // put "extras" into the bundle for access in the second activity
        i.putExtra("key_random", item.getKeyChat());
        i.putExtra("country_id", item.getCountry().getCountryID());
        i.putExtra("extra_unread", item.getNoReaded());
        /*i.putExtra("start_type", start_type);
        i.putExtra("android_id", historialChat.getMe().getKeyDevice());
        i.putExtra("android_id_receptor", historialChat.getEmisor().getKeyDevice());*/
        // brings up the second activity
        startActivity(i);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Country country = countryAutocompleteAdapter.getItem(i);
        Snackbar.make(toolbar, country.getName(), Snackbar.LENGTH_LONG).show();
        //searchView.clearFocus();
        //searchView.onActionViewCollapsed();
        /*if (!searchView.isIconified()) {
            searchView.setIconified(true);
        }*/
        invalidateOptionsMenu();
        setCountry(country);
    }

    private void setCountry(Country country) {
        toolbar.setTitle(country.getName());
        firebaseHelper.setCountry(country);
        saveCountryId(country.getCountryID());
    }

    private void saveCountryId(int id) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("PREF_COUNTRY_ID", id);
        editor.apply();
    }

    private Country getPrefCountry() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int id = preferences.getInt("PREF_COUNTRY_ID", Country.PERU);
        return new Country(getActivity(), id);
    }
}
