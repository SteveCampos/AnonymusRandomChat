package apps.steve.fire.randomchat.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdListener;

import java.util.ArrayList;
import java.util.List;

import apps.steve.fire.randomchat.Constants;
import apps.steve.fire.randomchat.R;
import apps.steve.fire.randomchat.holders.ChatsHolder;
import apps.steve.fire.randomchat.holders.NativeAdViewHolder;
import apps.steve.fire.randomchat.holders.PostHolder;
import apps.steve.fire.randomchat.interfaces.OnChatItemClickListener;
import apps.steve.fire.randomchat.interfaces.OnPostListener;
import apps.steve.fire.randomchat.model.RandomChat;

/**
 * Created by Steve on 19/12/2016.
 */

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = HistorialChatAdapter.class.getSimpleName();

    private static final int POST_VIEW_TYPE = 1;
    private static final int NATIVE_AD_VIEW_TYPE = 2;

    private OnPostListener listener;
    // Store a member variable for the contacts
    private List<RandomChat> mPosts = new ArrayList<>();
    private Context mContext;
    String androidID;


    //ONENDLESSSCROLL LISTENER
    private LinearLayoutManager mLinearLayoutManager;
    private boolean isMoreLoading = false;
    private int visibleThreshold = 1;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.


    public PostAdapter(List<RandomChat> mPosts, Context mContext, String androidID, OnPostListener listener) {
        this.mPosts = mPosts;
        this.mContext = mContext;
        this.listener = listener;
        this.androidID = androidID;
    }

    public void setLinearLayoutManager(LinearLayoutManager linearLayoutManager){
        this.mLinearLayoutManager=linearLayoutManager;
    }

    public void setRecyclerView(RecyclerView mView){
        mView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = mLinearLayoutManager.getItemCount();
                firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
                Log.d(TAG, "visibleItemCount: " + visibleItemCount);
                Log.d(TAG, "totalItemCount: " + totalItemCount);
                Log.d(TAG, "firstVisibleItem: " + firstVisibleItem);
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }

                if (totalItemCount < (firstVisibleItem + visibleItemCount)) {
                    //if (!isMoreLoading && (totalItemCount - visibleItemCount)<= (firstVisibleItem + visibleThreshold)) {
                    //if (firstVisibleItem + visibleItemCount <=20) {
                    if (listener != null) {
                        listener.onLoadMore();
                    }
                    isMoreLoading = true;
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Return a new holder instance
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case POST_VIEW_TYPE:
                View v1 = inflater.inflate(R.layout.item_post, parent, false);
                viewHolder = new PostHolder(v1);
                break;
            case NATIVE_AD_VIEW_TYPE:
                View v2 = inflater.inflate(R.layout.list_item_native_ad, parent, false);
                viewHolder = new NativeAdViewHolder(v2);
                break;
        }
        return viewHolder;
    }
    public void setData(List<RandomChat> posts) {
        this.mPosts = posts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (mPosts.get(position) == null) {
            return NATIVE_AD_VIEW_TYPE;
        }

        switch (mPosts.get(position).getLastMessage().getMessageType()) {
            case Constants.MESSAGE_TEXT:
                break;
            default:
                break;
        }
        return POST_VIEW_TYPE;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final RandomChat item = mPosts.get(position);

        switch (holder.getItemViewType()) {
            case POST_VIEW_TYPE:
                PostHolder vh1 = (PostHolder) holder;
                vh1.bind(item, androidID, mContext, new OnPostListener() {
                    @Override
                    public void onPostItemClicked(RandomChat item, View view, boolean bool) {
                        listener.onPostItemClicked(item, view, bool);
                    }

                    @Override
                    public void onLoadMore() {
                        listener.onLoadMore();
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
                });
                break;
            case NATIVE_AD_VIEW_TYPE:
                NativeAdViewHolder vh2 = (NativeAdViewHolder) holder;
                vh2.bind(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        //Toast.makeText(mContext, "onAdClosed", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        //Toast.makeText(mContext, "onAdFailedToLoad", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onAdLeftApplication() {
                        super.onAdLeftApplication();
                        //Toast.makeText(mContext, "onAdLeftApplication", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onAdOpened() {
                        super.onAdOpened();
                        //Toast.makeText(mContext, "onAdOpened", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        //Toast.makeText(mContext, "onAdLoaded", Toast.LENGTH_LONG).show();
                    }
                });
                break;
        }

    }

    @Override
    public int getItemCount() {
        return mPosts != null ? mPosts.size() : 0;
    }
}
