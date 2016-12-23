package apps.steve.fire.randomchat.holders;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
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
import apps.steve.fire.randomchat.interfaces.OnPostListener;
import apps.steve.fire.randomchat.model.ChatMessage;
import apps.steve.fire.randomchat.model.Country;
import apps.steve.fire.randomchat.model.Emisor;
import apps.steve.fire.randomchat.model.RandomChat;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Steve on 19/12/2016.
 */

public class PostHolder extends RecyclerView.ViewHolder {


    @BindView(R.id.cardview)
    public CardView cardView;
    @BindView(R.id.imageview_avatar)
    public ImageView imageView;
    @BindView(R.id.text_title)
    public TextView textTitle;
    @BindView(R.id.text_content)
    public TextView textContent;
    @BindView(R.id.text_time)
    public TextView time;

    @BindView(R.id.btn_chat)
    public AppCompatButton btnChat;


    public PostHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }


    public void bind(final RandomChat item, String androidID, Context mContext, final OnPostListener listener){

        boolean isMe = false;

        time.setText(Utils.calculateLastConnection(new Date(Utils.abs(item.getLastMessage().getMessageTime())), new Date(), mContext));

        Emisor emisor = item.getEmisor();
        //Emisor interlocutor = item.getReceptor();
        if (emisor.getKeyDevice().equals(androidID)){
            //me = item.getReceptor();
            //interlocutor = item.getEmisor();
            //btnChat.setEnabled(false);
            btnChat.setAlpha((float) 0.8);
            isMe = true;
        }

        int idAvatar = R.drawable.boy_1;
        String gender = "";
        String content = "";
        String counterMessage = "";
        int counterColor = ContextCompat.getColor(mContext, R.color.white);



        String interlocutorGenero = emisor.getLooking().getGenero();

        //AVATAR, AND GENDER

        switch (Constants.Genero.valueOf(interlocutorGenero)) {
            case CHICA:
                idAvatar = R.drawable.girl_2;
                gender = mContext.getString(R.string.girl);
                break;
            case CHICO:
                idAvatar = R.drawable.boy_1;
                gender = mContext.getString(R.string.boy);
                break;
        }

        if (!TextUtils.isEmpty(item.getHimName())){
            gender = item.getHimName();
        }


        content = item.getLastMessage().getMessageText();
        /*

        if (item.getLastMessage().getAndroidID().equals(ChatMessage.AUTOMATIC)){
            switch (message){
                case ChatMessage.WAITING:
                    message = mContext.getString(R.string.chat_state_no_message);
                    break;
                case ChatMessage.PARED:
                    message = mContext.getString(R.string.message_automatic_pared);
                    break;
                case ChatMessage.BLOCKED:
                    message = mContext.getString(R.string.message_automatic_blocked);
                    break;
                case ChatMessage.UNBLOCKED:
                    message = mContext.getString(R.string.message_automatic_unblocked);
                    break;
                default:
                    break;
            }
        }

        if (item.getLastMessage().getAndroidID().equals(me.getKeyDevice())) {

            int drawableID =  R.drawable.ic_check;

            switch (item.getLastMessage().getMessageStatus()) {
                case Constants.READED:
                    drawableID = R.drawable.ic_double_check_colored;
                    break;
                case Constants.DELIVERED:
                    drawableID = R.drawable.ic_double_check;
                    break;
                case Constants.SENT:
                    drawableID = R.drawable.ic_check;//ic_check
                    break;
            }

            Drawable drawable = ContextCompat.getDrawable(mContext, drawableID);

            lastMessage.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        }else{
            lastMessage.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }






        if (interlocutor == null){
            counterMessage = mContext.getString(R.string.chat_state_waiting);
            counterColor = ContextCompat.getColor(mContext, R.color.vimeo_blue);
        }else{
            if (item.getNoReaded() > 0) {
                counterMessage = ""+item.getNoReaded();
                counter.setBackgroundResource(R.drawable.badge_circle);
                time.setTextColor(ContextCompat.getColor(mContext, R.color.vimeo_blue));
            }else{
                counter.setBackgroundResource(R.drawable.ic_whatshot_white_24dp);//Fondo blanco
                time.setTextColor(ContextCompat.getColor(mContext, R.color.grey_600));
            }
        }
        */


        if (item.isHot()){
            Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_flash);
            time.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        }else{
            Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_flash_white);
            time.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        }


        Glide.with(mContext)
                .load(idAvatar)
                .placeholder(idAvatar)
                .into(imageView);

        textTitle.setText(gender + " " + emisor.getEdad());
        /*Country country = new Country(mContext, item.getCountry().getCountryID());
        Drawable drawable = ContextCompat.getDrawable(mContext, country.getDrawableID());
        textTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);*/

        textContent.setText(content);

        //counter.setText(counterMessage);
        //counter.setTextColor(counterColor);

        final boolean finalIsMe = isMe;
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!finalIsMe){
                    listener.onPostItemClicked(item, cardView, true);
                }else{
                    Snackbar.make(cardView, R.string.chat_init_error_yourself, Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }
}
