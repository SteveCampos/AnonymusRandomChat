package apps.steve.fire.randomchat.fragments;


import android.content.Intent;
import android.os.Bundle;
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


    @BindView(R.id.my_recycler_view)
    RecyclerView recyclerView;

    private HistorialChatAdapter adapter;
    private List<HistorialChat> historialChats = new ArrayList<>();

    public ChatsFragment() {
        // Required empty public constructor
    }

    public static ChatsFragment newInstance(String androidID){
        ChatsFragment fragment = new ChatsFragment();
        Bundle args = new Bundle();
        args.putString("androidID", androidID);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        ButterKnife.bind(this, recyclerView);
        setRecyclerView(getArguments().getString("androidID"));
        setRetainInstance(true);
        return recyclerView;
    }

    private void setRecyclerView(String androidID){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new HistorialChatAdapter(null, getActivity(), androidID , this);
        recyclerView.setAdapter(adapter);
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
        updateShortCut(item.getNoReaded());
        launchChatActivity(item, Constants._HERE);
    }
    private void updateShortCut(int x){
        int noReaded = Utils.getNoReaded(getContext());
        noReaded = noReaded - x;
        ShortcutBadger.applyCount(getContext(), noReaded > 0 ? noReaded: 0);
    }
}
