package apps.steve.fire.randomchat.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.Date;
import java.util.List;

import apps.steve.fire.randomchat.ChatActivity;
import apps.steve.fire.randomchat.Constants;
import apps.steve.fire.randomchat.R;
import apps.steve.fire.randomchat.Utils;
import apps.steve.fire.randomchat.adapters.MyFragmentAdapter;
import apps.steve.fire.randomchat.firebase.FirebaseHelper;
import apps.steve.fire.randomchat.fragments.ChatsFragment;
import apps.steve.fire.randomchat.fragments.SearchFragment;
import apps.steve.fire.randomchat.fragments.TrendFragment;
import apps.steve.fire.randomchat.interfaces.OnChatsListener;
import apps.steve.fire.randomchat.interfaces.OnSearchListener;
import apps.steve.fire.randomchat.model.Connection;
import apps.steve.fire.randomchat.model.HistorialChat;
import apps.steve.fire.randomchat.model.RandomChat;
import apps.steve.fire.randomchat.model.User;
import apps.steve.fire.randomchat.notifications.NotificationFireListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leolin.shortcutbadger.ShortcutBadger;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, NavigationView.OnNavigationItemSelectedListener, OnChatsListener, OnSearchListener {

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    }

    @Override
    protected void onResume() {
        ButterKnife.bind(this);
        initViews();
        super.onResume();
    }

    @Override
    protected void onPause() {
        firebaseHelper.removeChatsListener();
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void initViews() {

        firebaseHelper = new FirebaseHelper(getActivity());

        androidID = Utils.getAndroidID(this);
        firebaseHelper.initChats(androidID, this);
        firebaseHelper.setOn();


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

        //IF USER NOT EXIST, CREATE ONE.
        createUser();
    }

    private void createUser() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!prefs.getBoolean("firstTime", false)) {
            //AQUÍ TENGO QUE ASEGURARME QUE CREE EL USUARIO,
            long now = new Date().getTime();
            User user = new User(null, new Connection(Constants.STATE_ONLINE, now), now, 0, null);
            String android_id = Utils.getAndroidID(this);
            Log.d(TAG, "android_id: " + android_id);
            if (TextUtils.isEmpty(android_id)){
                android_id = Utils.getAndroidID(this);
            }
            startService(new Intent(getActivity(), NotificationFireListener.class));
            firebaseHelper.createUser(android_id, user);
        }
    }

    private AppCompatActivity getActivity(){
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
        Log.d(TAG, "onPageSelected: "+ position);
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
        // Set item in checked state
        item.setChecked(true);
        // TODO: handle navigation
        // Closing drawer on item click
        drawer.closeDrawers();

        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_main:
                //Snackbar.make(fab, "R.id.nav_main", Snackbar.LENGTH_LONG).show();
                break;
        }
        return true;
    }

    @OnClick(R.id.fab)
    void onFabClick(){
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

    private SearchFragment getSearchFragment(){


        List<Fragment> fragments = getFragments();
        if (fragments != null) {
            if (fragments.get(0) instanceof SearchFragment) {
                Log.d(TAG, "fragments.get(0) instanceof SearchFragment");
                return (SearchFragment) fragments.get(0);
            } else {
                Log.d(TAG, "fragments.get(0) instanceof SearchFragment: " + null);
                return null;
            }
        }else {
            return null;
        }
    }

    private List<Fragment>  getFragments(){
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null){
            Log.d(TAG, "getSupportFragmentManager().getFragments().size(): "+ fragments.size());
            for (int i= 0; i< fragments.size(); i++){

                Log.d(TAG, "FRAGMENT: " + i );
                if (fragments.get(i)!=  null){
                    Log.d(TAG, "TAG : " + fragments.get(i).getTag());
                }
            }
        }
        return fragments;
    }

    private ChatsFragment getChatsFragment(){

        List<Fragment> fragments = getFragments();
        if (fragments != null){
            if (fragments.get(1) instanceof ChatsFragment){
                Log.d(TAG, "fragments.get(1) instanceof ChatsFragment");
                return (ChatsFragment) fragments.get(1);
            }else
            {
                Log.d(TAG, "fragments.get(1) instanceof ChatsFragment: " + null);
                return null;
            }
        }else{
            return null;
        }

    }

    private ChatsFragment getHotsFragment(){

        List<Fragment> fragments = getFragments();
        if (fragments != null) {
            if (fragments.size() > 2) {
                if (fragments.get(2) instanceof ChatsFragment) {
                    Log.d(TAG, "fragments.get(2) instanceof ChatsFragment");
                    return (ChatsFragment) fragments.get(2);
                } else if (fragments.get(3) instanceof ChatsFragment){
                    return  (ChatsFragment) fragments.get(3);
                }else{
                    return null;
                }
            } else {
                return null;
            }
        }else{
            return null;
        }
    }

    @Override
    public void onChatChangedListener(boolean success, List<RandomChat> chats) {
        //int currentItem = viewPager.getCurrentItem();
        Log.d(TAG, "onChatChangedListener success: " + success);

        ChatsFragment chatsFragment = getChatsFragment();

        if (success && chatsFragment != null){
            chatsFragment.setData(chats);
        }
    }

    @Override
    public void onChatsHotListener(boolean success, List<RandomChat> chatList) {
        Log.d(TAG, "onChatChangedListener success: " + success);

        ChatsFragment hotsFragment = getHotsFragment();

        if (success && hotsFragment != null){
            hotsFragment.setData(chatList);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("current_position", currentItem);
        super.onSaveInstanceState(outState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        //viewPager.setCurrentItem(savedInstanceState.getInt("current_position"));
    }

    @Override
    protected void onDestroy() {
        firebaseHelper.setOff();
        super.onDestroy();
    }

    void startAnim(){

        viewPager.setAlpha((float)0.3);
        fab.setEnabled(false);
        avi.setVisibility(View.VISIBLE);
        avi.show();
        // or avi.smoothToShow();
    }

    void stopAnim(){

        viewPager.setAlpha((float)1);
        fab.setEnabled(true);
        avi.setVisibility(View.GONE);
        avi.hide();
        // or avi.smoothToHide();
    }

    @Override
    public void onChatLaunched(String key) {
        firebaseHelper.removeChatsListener();
        SearchFragment searchFragment = getSearchFragment();
        if (searchFragment != null) {
            searchFragment.enabledInputs(true);
        }
        stopAnim();
        launchChatActivity(key);
    }

    @Override
    public void onFailed(String error) {
        SearchFragment searchFragment = getSearchFragment();
        if (searchFragment != null) {
            searchFragment.enabledInputs(true);
        }
        Snackbar.make(toolbar, error, Snackbar.LENGTH_LONG).show();
        stopAnim();
    }

    private void launchChatActivity(String key) {
        // first parameter is the context, second is the class of the activity to launch
        Intent i = new Intent(getActivity(), ChatActivity.class);
        // put "extras" into the bundle for access in the second activity
        i.putExtra("key_random", key);
        startActivity(i);
    }
}
