package apps.steve.fire.randomchat.holders;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Date;

import apps.steve.fire.randomchat.Constants;
import apps.steve.fire.randomchat.R;
import apps.steve.fire.randomchat.Utils;
import apps.steve.fire.randomchat.interfaces.OnChatItemClickListener;
import apps.steve.fire.randomchat.model.Country;
import apps.steve.fire.randomchat.model.Emisor;
import apps.steve.fire.randomchat.model.RandomChat;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Steve on 19/08/2016.
 */

public class ChatsHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.card_historial)
    public CardView cardView;
    @BindView(R.id.imageview_avatar)
    public ImageView imageView;
    @BindView(R.id.text_title)
    public TextView textTitle;
    @BindView(R.id.text_last_message)
    public TextView lastMessage;
    @BindView(R.id.text_time)
    public TextView time;
    @BindView(R.id.text_counter)
    public TextView counter;


    public ChatsHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(final RandomChat item, String androidID, Context mContext, final OnChatItemClickListener listener){


        time.setText(Utils.calculateLastConnection(new Date(Utils.abs(item.getLastMessage().getMessageTime())), new Date(), mContext));


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
                title = mContext.getString(R.string.girl);
                break;
            case CHICO:
                idAvatar = R.drawable.boy_1;
                title = mContext.getString(R.string.boy);
                break;
        }

        if (!TextUtils.isEmpty(item.getHimName())){
            title = item.getHimName();
        }


        message = item.getLastMessage().getMessageText();

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

            lastMessage.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        }




        if (item.isHot()){
            Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_whatshot_red_24dp);
            time.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        }


        if (interlocutor == null){
            counterMessage = mContext.getString(R.string.chat_state_waiting);
            counterColor = ContextCompat.getColor(mContext, R.color.vimeo_blue);
        }else{
            if (item.getNoReaded() > 0) {
                counterMessage = ""+item.getNoReaded();
                counter.setBackgroundResource(R.drawable.badge_circle);
                time.setTextColor(ContextCompat.getColor(mContext, R.color.vimeo_blue));
            }
        }



        Glide.with(mContext)
                .load(idAvatar)
                .placeholder(idAvatar)
                .into(imageView);

        textTitle.setText(title);
        Country country = new Country(mContext, item.getCountry().getCountryID());
        Drawable drawable = ContextCompat.getDrawable(mContext, country.getDrawableID());
        textTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);

        lastMessage.setText(message);

        counter.setText(counterMessage);
        counter.setTextColor(counterColor);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onChatItemClicked(item, v, true);
            }
        });

    }
}
