package apps.steve.fire.randomchat.adapters;

import android.content.Context;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import apps.steve.fire.randomchat.AndroidUtilities;
import apps.steve.fire.randomchat.Constants;
import apps.steve.fire.randomchat.R;
import apps.steve.fire.randomchat.interfaces.OnChatMessageListener;
import apps.steve.fire.randomchat.model.ChatMessage;
import apps.steve.fire.randomchat.widgets.Emoji;
import butterknife.BindView;
import butterknife.ButterKnife;

import static apps.steve.fire.randomchat.model.ChatMessage.BLOCKED;
import static apps.steve.fire.randomchat.model.ChatMessage.PARED;
import static apps.steve.fire.randomchat.model.ChatMessage.PARED_BY_POST;
import static apps.steve.fire.randomchat.model.ChatMessage.UNBLOCKED;
import static apps.steve.fire.randomchat.model.ChatMessage.WAITING;

/**
 * Created by Steve on 23/07/2016.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<ChatMessage> chatMessages;
    private Context context;
    //public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("h:mm a", Locale.getDefault());
    private static String android_id = "ANDROID_ID_UNRESOLVED";

    private final int EMISOR_TEXT = 0, EMISOR_IMAGE = 1, EMISOR_VIDEO = 2, EMISOR_AUDIO = 3,
    RECEPTOR_TEXT = 4, RECEPTOR_IMAGE= 5, RECEPTOR_VIDEO = 6, RECEPTOR_AUDIO = 7, AUTOMATIC_TEXT = 10;

    private OnChatMessageListener listener;

    public ChatAdapter(List<ChatMessage> chatMessages, Context context, OnChatMessageListener listener) {
        Log.d(Constants.TAG, "ChatAdapter");
        this.chatMessages = chatMessages;
        this.context = context;
        android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        this.listener = listener;
    }

    public void addMessage(ChatMessage message){
        this.chatMessages.add(message);
        notifyItemInserted(getItemCount() - 1);
    }

    public void removeMessage(ChatMessage message){
        int index = chatMessages.indexOf(message);
        chatMessages.remove(index);
        notifyItemRemoved(getItemCount() -1 );
    }

    @Override
    public int getItemViewType(int position) {
        Log.d(Constants.TAG, "getItemViewType position: " + position);
        ChatMessage message = chatMessages.get(position);

        if (message.getAndroidID().equals(android_id)){
            switch (message.getMessageType()) {
                case Constants.MESSAGE_TEXT:
                    return EMISOR_TEXT;
                case Constants.MESSAGE_IMAGE:
                    return EMISOR_IMAGE;
                default:
                    return RECEPTOR_TEXT;
            }
        }else{
            if (message.getAndroidID().equals(ChatMessage.AUTOMATIC)){
                return AUTOMATIC_TEXT;
            }
            switch (message.getMessageType()) {
                case Constants.MESSAGE_TEXT:
                    return RECEPTOR_TEXT;
                case Constants.MESSAGE_IMAGE:
                    return RECEPTOR_IMAGE;
                default:
                    return RECEPTOR_TEXT;
            }
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.d(Constants.TAG, "onCreateViewHolder viewType: " + viewType);
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case EMISOR_TEXT:
                View v1 = inflater.inflate(R.layout.chat_user2_item, parent, false);
                viewHolder = new ViewHolderEmisor(v1);
                break;
            case RECEPTOR_TEXT:
                View v2 = inflater.inflate(R.layout.chat_user1_item, parent, false);
                viewHolder = new ViewHolderReceptor(v2);
                break;
            case EMISOR_IMAGE:
                View v3 = inflater.inflate(R.layout.emisor_image, parent, false);
                viewHolder = new ViewHolderEmisorImage(v3);
                break;
            case RECEPTOR_IMAGE:
                View v4 = inflater.inflate(R.layout.receptor_image, parent, false);
                viewHolder = new ViewHolderReceptorImage(v4);
                break;
            case AUTOMATIC_TEXT:
                View v5 = inflater.inflate(R.layout.item_automatic_text, parent, false);
                viewHolder = new AutomaticTextHolder(v5);
                break;
            default:
                View v = inflater.inflate(R.layout.chat_user1_item, parent, false);
                viewHolder = new ViewHolderReceptor(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d(Constants.TAG, "onBindViewHolder holder.getItemViewType: " + holder.getItemViewType());
        switch (holder.getItemViewType()) {
            case EMISOR_TEXT:
                ViewHolderEmisor vh1 = (ViewHolderEmisor) holder;
                vh1.bind(chatMessages.get(position) ,context);
                break;
            case EMISOR_IMAGE:
                ViewHolderEmisorImage vh2 = (ViewHolderEmisorImage) holder;
                vh2.bind(chatMessages.get(position) ,context);
                break;
            case RECEPTOR_TEXT:
                ViewHolderReceptor vh3 = (ViewHolderReceptor) holder;
                vh3.bind(chatMessages.get(position) ,context);
                break;
            case RECEPTOR_IMAGE:
                ViewHolderReceptorImage vh4 = (ViewHolderReceptorImage) holder;
                vh4.bind(chatMessages.get(position), context);
                break;
            case AUTOMATIC_TEXT:
                AutomaticTextHolder vh5 = (AutomaticTextHolder) holder;
                vh5.bind(chatMessages.get(position), context);
                break;
            default:
                ViewHolderEmisor defaultHolder = (ViewHolderEmisor) holder;
                defaultHolder.bind(chatMessages.get(position) ,context);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public class ViewHolderEmisor extends RecyclerView.ViewHolder{

        TextView textViewMessage;
        TextView textViewMessageTime;
        ImageView imageViewMessageStatus;


        public ViewHolderEmisor(View itemView) {
            super(itemView);
            textViewMessage = (TextView) itemView.findViewById(R.id.message_text);
            textViewMessageTime = (TextView) itemView.findViewById(R.id.time_text);
            imageViewMessageStatus = (ImageView) itemView.findViewById(R.id.user_reply_status);

        }

        void bind(ChatMessage message, Context context){
            textViewMessage.setText(Html.fromHtml(Emoji.replaceEmoji(message.getMessageText(),
                    textViewMessage.getPaint().getFontMetricsInt(), AndroidUtilities.dp(16))
                    + " &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;" +
                    "&#160;&#160;&#160;&#160;&#160;&#160;&#160;"));
            //holder2.messageTextView.setText(message.getMessageText());
            textViewMessageTime.setText(SIMPLE_DATE_FORMAT.format(message.getMessageTime()));

            switch (message.getMessageStatus()){
                case Constants.READED:
                    imageViewMessageStatus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_double_check_colored));
                    break;
                case Constants.DELIVERED:
                    imageViewMessageStatus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_double_check));
                    break;
                case Constants.SENT:
                    imageViewMessageStatus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check));//ic_check
                    break;

            }
        }
    }

    public class ViewHolderReceptor extends RecyclerView.ViewHolder{
        TextView textViewMessage;
        TextView textViewTime;

        public ViewHolderReceptor(View v) {
            super(v);

            textViewMessage = (TextView) v.findViewById(R.id.message_text);
            textViewTime = (TextView) v.findViewById(R.id.time_text);
        }

        void bind(ChatMessage message, Context context){

            textViewMessage.setText(Html.fromHtml(Emoji.replaceEmoji(message.getMessageText(),
                    textViewMessage.getPaint().getFontMetricsInt(), AndroidUtilities.dp(16))
                    + " &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;"));
            textViewTime.setText(SIMPLE_DATE_FORMAT.format(message.getMessageTime()));
        }
    }

    public class ViewHolderEmisorImage extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textViewMessageTime;
        ImageView imageViewMessageStatus;
        RelativeLayout parentView;

        public ViewHolderEmisorImage(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_view);
            textViewMessageTime = (TextView) itemView.findViewById(R.id.time_text);
            imageViewMessageStatus = (ImageView) itemView.findViewById(R.id.user_reply_status);
            parentView = (RelativeLayout) itemView.findViewById(R.id.parentView);
        }

        void bind(final ChatMessage message, Context context){
            Glide.with(context)
                    .load(message.getMessageText()) // Uri of the picture
                    .placeholder(R.drawable.ic_whatshot_white_24dp)
                    .into(imageView);

            textViewMessageTime.setText(SIMPLE_DATE_FORMAT.format(message.getMessageTime()));

            switch (message.getMessageStatus()){
                case Constants.READED:
                    imageViewMessageStatus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_double_check_colored));
                    break;
                case Constants.DELIVERED:
                    imageViewMessageStatus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_double_check));
                    break;
                case Constants.SENT:
                    imageViewMessageStatus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check));//ic_check
                    break;

            }

            parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onChatMessageListener(message);
                }
            });
        }
    }

    public class ViewHolderReceptorImage extends RecyclerView.ViewHolder{
        ImageView image;
        TextView textViewTime;
        RelativeLayout parentView;

        public ViewHolderReceptorImage(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image_view);
            textViewTime = (TextView) v.findViewById(R.id.time_text);
            parentView = (RelativeLayout) itemView.findViewById(R.id.parentView);
        }

        void bind(final ChatMessage message, Context context){
            Glide.with(context)
                    .load(message.getMessageText()) // Uri of the picture
                    .placeholder(R.drawable.ic_whatshot_red_24dp)
                    .into(image);
            textViewTime.setText(SIMPLE_DATE_FORMAT.format(message.getMessageTime()));
            parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onChatMessageListener(message);
                }
            });

        }
    }

    public class AutomaticTextHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.message_text) TextView txtMessage;

        public AutomaticTextHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        void bind(ChatMessage message, Context context){
            String automaticMessage = null;
            switch (message.getMessageText()){
                case WAITING:
                    automaticMessage= context.getString(R.string.chat_state_waiting);
                case PARED:
                    automaticMessage= context.getString(R.string.message_automatic_pared);
                    break;
                case BLOCKED:
                    automaticMessage = context.getString(R.string.message_automatic_blocked);
                    break;
                case UNBLOCKED:
                    automaticMessage = context.getString(R.string.message_automatic_unblocked);
                    break;
                case PARED_BY_POST:
                    automaticMessage = context.getString(R.string.chat_state_pared_by_post);
                    break;
                default:
                    break;
            }
            txtMessage.setText(automaticMessage);
        }
    }

}
