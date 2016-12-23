package apps.steve.fire.randomchat.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import apps.steve.fire.randomchat.R;
import apps.steve.fire.randomchat.adapters.HistorialChatAdapter;
import apps.steve.fire.randomchat.adapters.PostAdapter;
import apps.steve.fire.randomchat.interfaces.OnPostListener;
import apps.steve.fire.randomchat.model.HistorialChat;
import apps.steve.fire.randomchat.model.RandomChat;
import butterknife.BindView;
import butterknife.ButterKnife;


public class PostsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnPostListener {


    private static final String TAG = PostsFragment.class.getSimpleName();
    @BindView(R.id.my_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_to_refresh_layout)
    SwipeRefreshLayout refreshLayout;

    private PostAdapter adapter;
    //private List<HistorialChat> historialChats = new ArrayList<>();
    private OnPostListener listener;
    private boolean isViewCreated = false;

    public PostsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param androidID Parameter 1.
     * @return A new instance of fragment PostsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostsFragment newInstance(String androidID){
        PostsFragment fragment = new PostsFragment();
        Bundle args = new Bundle();
        args.putString("ARG_ANDROID_ID", androidID);
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
        setRecyclerView(getArguments().getString("ARG_ANDROID_ID"));
        setRetainInstance(true);
        return refreshLayout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        isViewCreated = true;
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        isViewCreated = false;
        super.onDestroyView();
    }

    private void setRecyclerView(String androidID){

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.light_blue_400),
                ContextCompat.getColor(getActivity(), R.color.accent),
                ContextCompat.getColor(getActivity(), R.color.light_blue_700));


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new PostAdapter(null, getActivity(), androidID , this);
        adapter.setLinearLayoutManager(linearLayoutManager);
        adapter.setRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
    }

    public void setData(List<RandomChat> list){
        if (!isViewCreated){
            return;
        }
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



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPostListener) {
            listener = (OnPostListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

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

    @Override
    public void onPostItemClicked(RandomChat item, View v, boolean bool) {
        listener.onPostItemClicked(item, v, bool);
    }

    @Override
    public void onLoadMore() {
        listener.onLoadMore();
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void onPostCreated() {

    }

    @Override
    public void onFailed(String error) {

    }

    @Override
    public void onChatLaunched(int countryId, String key) {

    }

}
