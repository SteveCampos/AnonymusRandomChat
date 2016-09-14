package apps.steve.fire.randomchat.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.List;

import apps.steve.fire.randomchat.Constants;
import apps.steve.fire.randomchat.R;
import apps.steve.fire.randomchat.Trend;
import apps.steve.fire.randomchat.holders.TrendUserHolder;
import apps.steve.fire.randomchat.model.HistorialChat;
import apps.steve.fire.randomchat.model.UserTrend;

/**
 * Created by Steve on 17/07/2016.
 */

public class TrendAdapter extends RecyclerView.Adapter<TrendUserHolder> {



    public interface OnUserTrendListener{
        void onUserTrendListener(UserTrend item, View view, boolean liked);
    }

    private final OnUserTrendListener listener;
    // Store a member variable for the contacts
    private List<UserTrend> mListTrends;
    private Context mContext;

    public TrendAdapter(List<UserTrend> mListTrends, Context mContext, OnUserTrendListener listener) {
        this.listener = listener;
        this.mListTrends = mListTrends;
        this.mContext = mContext;
    }


    @Override
    public TrendUserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_user_trend, parent, false);
        // Return a new holder instance
        return new TrendUserHolder(contactView);
    }

    @Override
    public void onBindViewHolder(TrendUserHolder holder, int position) {
        final UserTrend userTrend = mListTrends.get(position);
        //holder.bind(mListTrends.get(position), mContext, listener);
        /*starsCount.setText(""+userTrend.getListUsersStars().size());
        hotsCount.setText(""+userTrend.getListUsersHots().size());
        likesCount.setText(""+userTrend.getListUsersLikes().size());*/

        holder.title.setText("18 Young Girl");
        /*
        holder.hots.setText();
        holder.state.setText();*/


        int idAvatar = R.drawable.boy_1;
        switch (Constants.Genero.valueOf(userTrend.getGenero())){
            case CHICA:
                idAvatar = R.drawable.girl_2;
                break;
            case CHICO:
                idAvatar = R.drawable.boy_1;
                break;
        }

        /*
        Glide.with(mContext)
                .load(idAvatar)
                .into(holder.avatar);
        */
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUserTrendListener(userTrend, v, true);
            }
        });
        holder.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onUserTrendListener(userTrend, view, true);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mListTrends.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    /*
    static class ViewHolder extends RecyclerView.ViewHolder{

        private static final String TAG = "HistorialHolder";
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        CardView cardView;
        ImageView imgAvatar;
        TextView textAgeRange;
        TextView textGroupName;
        TextView textLanguage;
        AppCompatButton buttonChat;
        LikeButton buttonStar;
        LikeButton buttonLike;
        LikeButton buttonHot;
        TextView starsCount;
        TextView hotsCount;
        TextView likesCount;


        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_historial);
            textAgeRange = (TextView) itemView.findViewById(R.id.text_range_age);
            textGroupName = (TextView) itemView.findViewById(R.id.text_groupname);
            textLanguage = (TextView) itemView.findViewById(R.id.text_language);
            imgAvatar = (ImageView) itemView.findViewById(R.id.imageview_avatar);
            buttonChat = (AppCompatButton) itemView.findViewById(R.id.btn_chat);
            buttonStar = (LikeButton) itemView.findViewById(R.id.star_button);
            buttonLike = (LikeButton) itemView.findViewById(R.id.like_button);
            buttonHot = (LikeButton) itemView.findViewById(R.id.hot_button);
            starsCount = (TextView) itemView.findViewById(R.id.text_stars_count);
            hotsCount = (TextView) itemView.findViewById(R.id.text_hots_count);
            likesCount = (TextView) itemView.findViewById(R.id.text_likes_count);

        }

        public void bind(final UserTrend userTrend, Context mContext, final OnUserTrendListener listener){

            starsCount.setText(""+userTrend.getListUsersStars().size());
            hotsCount.setText(""+userTrend.getListUsersHots().size());
            likesCount.setText(""+userTrend.getListUsersLikes().size());

            textAgeRange.setText(""+userTrend.getEdad());
            textGroupName.setText(""+userTrend.getGenero());


            int idAvatar = R.drawable.boy_1;
            switch (Constants.Genero.valueOf(userTrend.getGenero())){
                case CHICA:
                    idAvatar = R.drawable.girl_2;
                    break;
                case CHICO:
                    idAvatar = R.drawable.boy_1;
                    break;
            }

            Glide.with(mContext)
                    .load(idAvatar)
                    .into(imgAvatar);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onUserTrendListener(userTrend, v, true);
                }
            });

            buttonChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onUserTrendListener(userTrend, v, true);
                }
            });
            buttonStar.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    listener.onUserTrendListener(userTrend, likeButton, true);
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    listener.onUserTrendListener(userTrend, likeButton, false);
                }
            });
            buttonHot.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    listener.onUserTrendListener(userTrend, likeButton, true);
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    listener.onUserTrendListener(userTrend, likeButton, false);
                }
            });
            buttonLike.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    listener.onUserTrendListener(userTrend, likeButton, true);
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    listener.onUserTrendListener(userTrend, likeButton, false);
                }
            });
        }
    }*/
}
