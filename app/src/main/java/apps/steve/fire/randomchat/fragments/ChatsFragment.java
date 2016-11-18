package apps.steve.fire.randomchat.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import apps.steve.fire.randomchat.ChatActivity;
import apps.steve.fire.randomchat.Constants;
import apps.steve.fire.randomchat.R;
import apps.steve.fire.randomchat.adapters.HistorialChatAdapter;
import apps.steve.fire.randomchat.interfaces.OnChatItemClickListener;
import apps.steve.fire.randomchat.interfaces.OnChatsListener;
import apps.steve.fire.randomchat.model.HistorialChat;
import apps.steve.fire.randomchat.model.RandomChat;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment implements OnChatItemClickListener, SwipeRefreshLayout.OnRefreshListener {


    public final static int TYPE_ALL = 1123;
    public final static int TYPE_HOTS = 5813;
    private static final String TAG = ChatsFragment.class.getSimpleName();

    private OnChatsListener listener;

    @BindView(R.id.my_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_to_refresh_layout)
    SwipeRefreshLayout refreshLayout;

    private HistorialChatAdapter adapter;
    private List<HistorialChat> historialChats = new ArrayList<>();

    public ChatsFragment() {
        // Required empty public constructor
    }

    public static ChatsFragment newInstance(String androidID, int type){
        ChatsFragment fragment = new ChatsFragment();
        Bundle args = new Bundle();
        args.putString("ARG_ANDROID_ID", androidID);
        args.putInt("ARG_TYPE_CHATS", type);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        refreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.recycler_view, container, false);
        ButterKnife.bind(this, refreshLayout);
        //recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        setRecyclerView(getArguments().getString("ARG_ANDROID_ID"), getArguments().getInt("ARG_TYPE_CHATS", TYPE_ALL));
        setRetainInstance(true);
        return refreshLayout;
    }

    private void setRecyclerView(String androidID, int type){

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.light_blue_400),
                ContextCompat.getColor(getActivity(), R.color.accent),
                ContextCompat.getColor(getActivity(), R.color.light_blue_700));


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new HistorialChatAdapter(null, getActivity(), androidID , this);
        adapter.setLinearLayoutManager(linearLayoutManager);
        adapter.setRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
    }

    public void setData(List<RandomChat> list){
        refreshLayout.setRefreshing(false);
        if (adapter!=null && list != null){
            if (list.size() > 4){
                if (list.get(1) != null){
                    list.add(1, null);
                }
            }
            adapter.setData(list);
        }
    }


    private void launchChatActivity(RandomChat item, String start_type) {
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
    public void onChatItemClicked(RandomChat item, View view, boolean liked) {
        //updateShortCut(item);
        if (listener!=null){
            listener.onChatItemClicked(item);
        }
        //launchChatActivity(item, Constants._HERE);
    }

    @Override
    public void onLoadMore() {
        listener.onLoadMore();
        refreshLayout.setRefreshing(true);
    }

    private void updateShortCut(RandomChat item){
        int x = item.getNoReaded();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = preferences.edit();


        int noReaded = preferences.getInt(Constants.PREF_NOTI_COUNT, 0);
        String notiKeys = preferences.getString(Constants.PREF_NOTI_KEYS, "");

        Log.d(TAG, "preferences.getInt(Constants.PREF_NOTI_COUNT, 0): " + noReaded );
        Log.d(TAG, "item.getNoReaded(): " + x);
        Log.d(TAG, " count - noReaded: " + (noReaded -x ));
        Log.d(TAG, "preferences.getString(Constants.PREF_NOTI_KEYS, ): " + notiKeys );

        String notiKeysRemoved = notiKeys.replace(item.getKeyChat()+".", "");
        Log.d(TAG, "notiKeysRemoved " + notiKeysRemoved );

        noReaded = noReaded > x ? (noReaded - x) : 0;
        Log.d(TAG, "RESULTADO RESTA : "  + noReaded);


        editor.putString(item.getKeyChat(), ""); //Notifications messages to keyChat Readed!
        editor.putString(Constants.PREF_NOTI_KEYS, notiKeysRemoved);
        editor.putInt(Constants.PREF_NOTI_COUNT, noReaded);
        editor.apply();


        ShortcutBadger.applyCount(getContext(), noReaded);
        /*
        String lastUserIdNoti = preferences.getString(Constants.PREF_NOTI_USER_ID_LAST, "");
        noReaded = noReaded > x ? (noReaded - x) : 0;
        editor.putInt(Constants.PREF_NOTI_COUNT, noReaded);

        if (item.getEmisor() ==null || item.getReceptor() ==null){
            return;
        }

        if (item.getEmisor().getKeyDevice().equals(lastUserIdNoti) || item.getReceptor().getKeyDevice().equals(lastUserIdNoti)){
            editor.putString(Constants.PREF_NOTI_MESSAGES, "");
        }
        editor.apply();
        ShortcutBadger.applyCount(getContext(), noReaded);*/
    }

    // This event fires 1st, before creation of fragment or any views
    // The onAttach method is called when the Fragment instance is associated with an Activity.
    // This does not mean the Activity is fully initialized.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnChatsListener) {
            listener = (OnChatsListener) context;
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

    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh");
        refreshLayout.setRefreshing(false);
    }
}
