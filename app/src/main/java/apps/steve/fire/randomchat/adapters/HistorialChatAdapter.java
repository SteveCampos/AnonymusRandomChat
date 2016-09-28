package apps.steve.fire.randomchat.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.Date;
import java.util.List;

import apps.steve.fire.randomchat.Constants;
import apps.steve.fire.randomchat.Historial;
import apps.steve.fire.randomchat.R;
import apps.steve.fire.randomchat.Utils;
import apps.steve.fire.randomchat.holders.ChatsHolder;
import apps.steve.fire.randomchat.model.Emisor;
import apps.steve.fire.randomchat.model.HistorialChat;
import apps.steve.fire.randomchat.model.RandomChat;

import static apps.steve.fire.randomchat.Constants.Genero.CHICO;

/**
 * Created by Steve on 14/07/2016.
 */

public class HistorialChatAdapter extends RecyclerView.Adapter<ChatsHolder> {
    //https://cinegaygratis.blogspot.pe/2015/07/un-chant-d-amour.html

    private static final String TAG = HistorialChatAdapter.class.getSimpleName();

    public interface OnStartChatListener {
        void onStartChat(RandomChat item, View view, boolean liked);
    }

    private final OnStartChatListener listener;
    // Store a member variable for the contacts
    private List<RandomChat> mHistorial;
    private Context mContext;
    String androidID;

    public HistorialChatAdapter(List<RandomChat> mHistorial, Context mContext, String androidID, OnStartChatListener listener) {
        this.mHistorial = mHistorial;
        this.mContext = mContext;
        this.listener = listener;
        this.androidID = androidID;
    }


    // Usually involves inflating a layout from XML and returning the holder
    //
    @Override
    public ChatsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_historial_chat, parent, false);
        // Return a new holder instance
        return new ChatsHolder(contactView);
    }

    public void setData(List<RandomChat> mHistorial) {
        this.mHistorial = mHistorial;
        notifyDataSetChanged();
    }


    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ChatsHolder holder, int position) {

        final RandomChat item = mHistorial.get(position);

        holder.time.setText(Utils.calculateLastConnection(new Date(Utils.abs(item.getLastMessage().getMessageTime())), new Date(), mContext));


        Emisor me = item.getEmisor();
        Emisor interlocutor = item.getReceptor();
        if (!item.getEmisor().getKeyDevice().equals(androidID)){
            me = item.getReceptor();
            interlocutor = item.getEmisor();
        }

        int idAvatar = R.drawable.boy_1;
        String title = "";
        String message = "";
        String counterMessage = "";
        int counterColor = ContextCompat.getColor(mContext, R.color.white);



        String interlocutorGenero = me.getLooking().getGenero();

        //AVATAR, AND GENDER

        switch (Constants.Genero.valueOf(interlocutorGenero)) {
            case CHICA:
                idAvatar = R.drawable.girl_2;
                title = mContext.getString(R.string.boy);
                break;
            case CHICO:
                idAvatar = R.drawable.boy_1;
                title = mContext.getString(R.string.girl);
                break;
        }


        message = item.getLastMessage().getMessageText();


        /*
        if (interlocutor == null){

            holder.counter.setText("ESPERANDO...");
            holder.counter.setVisibility(View.VISIBLE);
            holder.counter.setTextColor(ContextCompat.getColor(mContext, R.color.vimeo_blue));
            holder.time.setTextColor(ContextCompat.getColor(mContext, R.color.vimeo_blue));
        }else{
            interlocutorGenero = interlocutor.getGenero();


        }*/


        if (item.getLastMessage().getAndroidID().equals(me.getKeyDevice())) {

            int drawableID =  R.drawable.ic_double_check;

            switch (item.getLastMessage().getMessageStatus()) {
                case Constants.READED:
                    drawableID = R.drawable.ic_double_check_colored;
                    break;
                case Constants.DELIVERED:
                    drawableID = R.drawable.ic_double_check;
                    break;
                case Constants.SENT:
                    drawableID = R.drawable.ic_check;
                    break;
            }

            Drawable drawable = ContextCompat.getDrawable(mContext, drawableID);

            holder.message.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);

            if (item.getNoReaded() > 0) {
                counterMessage = ""+item.getNoReaded();
                holder.counter.setBackgroundResource(R.drawable.badge_circle);
                holder.time.setTextColor(ContextCompat.getColor(mContext, R.color.vimeo_blue));
            }
        }


        if (interlocutor == null){
            counterMessage = mContext.getString(R.string.chat_state_waiting);
            counterColor = ContextCompat.getColor(mContext, R.color.vimeo_blue);
        }



        Glide.with(mContext)
                .load(idAvatar)
                .placeholder(idAvatar)
                .into(holder.imageView);

        holder.title.setText(interlocutorGenero);

        holder.message.setText(message);

        holder.counter.setText(counterMessage);
        holder.counter.setTextColor(counterColor);





        /*
        if (!me.getKeyDevice().equals(androidID)) {
            holder.title.setText("!item.getMe().getKeyDevice().equals(androidID)");
        }*/

        Log.d(TAG, "androidID: " + androidID);
        Log.d(TAG, "item.getLastMessage().getAndroidID(): " + item.getLastMessage().getAndroidID());


        Log.d(TAG, "item.getLastMessage().getAndroidID().equals(androidID): " + item.getLastMessage().getAndroidID().equals(androidID));


        /*buttonLike.setLiked(item.isLike());
        buttonHot.setLiked(item.isHot());
        buttonStar.setLiked(item.isStar());*/


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onStartChat(item, v, true);
            }
        });
        /*
        buttonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onStartChat(item, v, true);
            }
        });
        buttonStar.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                listener.onStartChat(item, likeButton, true);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                listener.onStartChat(item, likeButton, false);
            }
        });
        buttonHot.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                listener.onStartChat(item, likeButton, true);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                listener.onStartChat(item, likeButton, false);
            }
        });
        buttonLike.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                listener.onStartChat(item, likeButton, true);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                listener.onStartChat(item, likeButton, false);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mHistorial != null ? mHistorial.size() : 0;
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

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
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

        }

        public void bind(final HistorialChat item, Context mContext, final OnStartChatListener listener){

            textAgeRange.setText("Edad: "+item.getEmisor().getEdad());
            textGroupName.setText(item.getEmisor().getGenero());
            //textLanguage.setText(R.string.language_spanish);

            int idAvatar = R.drawable.boy_1;
            switch (Constants.Genero.valueOf(item.getEmisor().getGenero())){
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

            buttonLike.setLiked(item.isLike());
            buttonHot.setLiked(item.isHot());
            buttonStar.setLiked(item.isStar());


            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onStartChat(item, v, true);
                }
            });
            buttonChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onStartChat(item, v, true);
                }
            });
            buttonStar.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    listener.onStartChat(item, likeButton, true);
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    listener.onStartChat(item, likeButton, false);
                }
            });
            buttonHot.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    listener.onStartChat(item, likeButton, true);
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    listener.onStartChat(item, likeButton, false);
                }
            });
            buttonLike.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    listener.onStartChat(item, likeButton, true);
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    listener.onStartChat(item, likeButton, false);
                }
            });

        }

    }*/
}
