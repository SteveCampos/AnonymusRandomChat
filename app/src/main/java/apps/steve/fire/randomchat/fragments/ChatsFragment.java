package apps.steve.fire.randomchat.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import apps.steve.fire.randomchat.ChatActivity;
import apps.steve.fire.randomchat.Constants;
import apps.steve.fire.randomchat.Historial;
import apps.steve.fire.randomchat.R;
import apps.steve.fire.randomchat.Utils;
import apps.steve.fire.randomchat.adapters.HistorialChatAdapter;
import apps.steve.fire.randomchat.firebase.FirebaseChats;
import apps.steve.fire.randomchat.interfaces.OnChatItemClickListener;
import apps.steve.fire.randomchat.interfaces.OnChatsListener;
import apps.steve.fire.randomchat.model.Emisor;
import apps.steve.fire.randomchat.model.HistorialChat;
import apps.steve.fire.randomchat.model.RandomChat;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment implements OnChatItemClickListener {


    public final static int TYPE_ALL = 1123;
    public final static int TYPE_HOTS = 5813;

    private OnChatsListener listener;

    @BindView(R.id.my_recycler_view)
    RecyclerView recyclerView;

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
        recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        ButterKnife.bind(this, recyclerView);
        setRecyclerView(getArguments().getString("ARG_ANDROID_ID"), getArguments().getInt("ARG_TYPE_CHATS", TYPE_ALL));
        setRetainInstance(true);
        return recyclerView;
    }

    private void setRecyclerView(String androidID, int type){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new HistorialChatAdapter(null, getActivity(), androidID , this);
        recyclerView.setAdapter(adapter);
        listener.onFragmentReady(type);
    }

    public void setData(List<RandomChat> list){
        if (adapter!=null){
            adapter.setData(list);
        }
    }


    private void launchChatActivity(RandomChat item, String start_type) {
        // first parameter is the context, second is the class of the activity to launch
        Intent i = new Intent(getActivity(), ChatActivity.class);
        // put "extras" into the bundle for access in the second activity
        i.putExtra("key_random", item.getKeyChat());
        i.putExtra("country_id", item.getCountry().getCountryID());
        /*i.putExtra("start_type", start_type);
        i.putExtra("android_id", historialChat.getMe().getKeyDevice());
        i.putExtra("android_id_receptor", historialChat.getEmisor().getKeyDevice());*/
        // brings up the second activity
        startActivity(i);
    }

    @Override
    public void onChatItemClicked(RandomChat item, View view, boolean liked) {
        updateShortCut(item);
        launchChatActivity(item, Constants._HERE);
    }

    private void updateShortCut(RandomChat item){
        int x = item.getNoReaded();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = preferences.edit();
        int noReaded = preferences.getInt(Constants.PREF_NOTI_COUNT, 0);
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
        ShortcutBadger.applyCount(getContext(), noReaded);

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
}
