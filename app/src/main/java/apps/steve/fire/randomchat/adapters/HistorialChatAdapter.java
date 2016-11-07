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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import apps.steve.fire.randomchat.Constants;
import apps.steve.fire.randomchat.Historial;
import apps.steve.fire.randomchat.R;
import apps.steve.fire.randomchat.Utils;
import apps.steve.fire.randomchat.holders.ChatsHolder;
import apps.steve.fire.randomchat.holders.NativeAdViewHolder;
import apps.steve.fire.randomchat.interfaces.OnChatItemClickListener;
import apps.steve.fire.randomchat.model.Emisor;
import apps.steve.fire.randomchat.model.HistorialChat;
import apps.steve.fire.randomchat.model.RandomChat;

import static apps.steve.fire.randomchat.Constants.Genero.CHICO;

/**
 * Created by Steve on 14/07/2016.
 */

public class HistorialChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = HistorialChatAdapter.class.getSimpleName();

    private static final int CHAT_VIEW_TYPE = 1;
    private static final int NATIVE_AD_VIEW_TYPE = 2;

    private final OnChatItemClickListener listener;
    // Store a member variable for the contacts
    private List<RandomChat> mHistorial = new ArrayList<>();
    private Context mContext;
    String androidID;

    public HistorialChatAdapter(List<RandomChat> mHistorial, Context mContext, String androidID, OnChatItemClickListener listener) {
        this.mHistorial = mHistorial;
        this.mContext = mContext;
        this.listener = listener;
        this.androidID = androidID;
    }


    // Usually involves inflating a layout from XML and returning the holder
    //
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Return a new holder instance
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
            case CHAT_VIEW_TYPE:
                View v1 = inflater.inflate(R.layout.item_historial_chat, parent, false);
                viewHolder = new ChatsHolder(v1);
                break;
            case NATIVE_AD_VIEW_TYPE:
                View v2 = inflater.inflate(R.layout.list_item_native_ad, parent, false);
                viewHolder = new NativeAdViewHolder(v2);
                break;
        }
        return viewHolder;
    }

    public void setData(List<RandomChat> list) {
        this.mHistorial = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (mHistorial.get(position) == null) {
            return NATIVE_AD_VIEW_TYPE;
        }

        switch (mHistorial.get(position).getLastMessage().getMessageType()) {
            case Constants.MESSAGE_TEXT:
                break;
            default:
                break;
        }
        return CHAT_VIEW_TYPE;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final RandomChat item = mHistorial.get(position);

        switch (holder.getItemViewType()) {
            case CHAT_VIEW_TYPE:
                ChatsHolder vh1 = (ChatsHolder) holder;
                vh1.bind(item, androidID, mContext, new OnChatItemClickListener() {
                    @Override
                    public void onChatItemClicked(RandomChat item, View view, boolean liked) {
                        listener.onChatItemClicked(item, view, liked);
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




        /*
        if (!me.getKeyDevice().equals(androidID)) {
            holder.title.setText("!item.getMe().getKeyDevice().equals(androidID)");
        }*/

        /*
        Log.d(TAG, "androidID: " + androidID);
        Log.d(TAG, "item.getLastMessage().getAndroidID(): " + item.getLastMessage().getAndroidID());


        Log.d(TAG, "item.getLastMessage().getAndroidID().equals(androidID): " + item.getLastMessage().getAndroidID().equals(androidID));*/


        /*buttonLike.setLiked(item.isLike());
        buttonHot.setLiked(item.isHot());
        buttonStar.setLiked(item.isStar());*/




        /*
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onStartChat(item, v, true);
            }
        });
        */

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
