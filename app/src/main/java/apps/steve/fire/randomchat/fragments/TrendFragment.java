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

import apps.steve.fire.randomchat.R;
import apps.steve.fire.randomchat.Trend;
import apps.steve.fire.randomchat.adapters.TrendAdapter;
import apps.steve.fire.randomchat.model.UserTrend;
import apps.steve.fire.randomchat.notifications.NotificationFireListener;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrendFragment extends Fragment implements TrendAdapter.OnUserTrendListener {

    @BindView(R.id.my_recycler_view)
    RecyclerView recyclerView;

    private TrendAdapter adapter;

    public TrendFragment() {
        // Required empty public constructor
    }

    public static TrendFragment newInstance(){
        return new TrendFragment();
    }

    public TrendFragment getInstance(){
        return newInstance();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        ButterKnife.bind(this, recyclerView);
        setRecyclerView();
        return recyclerView;
    }

    private void setRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new TrendAdapter(UserTrend.list(), getActivity(), this
        );
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onUserTrendListener(UserTrend item, View view, boolean liked) {
        getActivity().startService(new Intent(getActivity(), NotificationFireListener.class));
        Snackbar.make(recyclerView, item.getGenero(), Snackbar.LENGTH_LONG).show();
    }
}
