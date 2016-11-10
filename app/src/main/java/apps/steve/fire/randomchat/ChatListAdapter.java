package apps.steve.fire.randomchat;

import android.content.Context;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import apps.steve.fire.randomchat.model.ChatMessage;
import apps.steve.fire.randomchat.widgets.Emoji;

/**
 * Created by madhur on 17/01/15.
 */
public class ChatListAdapter extends BaseAdapter {

    private ArrayList<ChatMessage> chatMessages;
    private Context context;
    //public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("h:mm a", Locale.US);
    private static String android_id = "ANDROID_ID_UNRESOLVED";

    public ChatListAdapter(ArrayList<ChatMessage> chatMessages, Context context) {
        this.chatMessages = chatMessages;
        this.context = context;
        android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

    }


    @Override
    public int getCount() {
        return chatMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return chatMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;
        ChatMessage message = chatMessages.get(position);
        ViewHolder1 holder1;
        ViewHolder2 holder2;

        Log.d(Constants.TAG, "MESSAGE.GETANDROID : " + message.getAndroidID());
        Log.d(Constants.TAG, "ANDROID ID NOW : "+ android_id);
        if (!message.getAndroidID().equals(android_id)) {
            if (convertView == null) {
                v = LayoutInflater.from(context).inflate(R.layout.chat_user1_item, null, false);
                holder1 = new ViewHolder1();


                holder1.messageTextView = (TextView) v.findViewById(R.id.message_text);
                holder1.timeTextView = (TextView) v.findViewById(R.id.time_text);

                v.setTag(holder1);
            } else {
                v = convertView;
                holder1 = (ViewHolder1) v.getTag();

            }

            holder1.messageTextView.setText(Html.fromHtml(Emoji.replaceEmoji(message.getMessageText(),
                    holder1.messageTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(16))
                    + " &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;"));
            holder1.timeTextView.setText(SIMPLE_DATE_FORMAT.format(message.getMessageTime()));


        } else if (message.getAndroidID().equals(android_id)) {

            if (convertView == null) {
                v = LayoutInflater.from(context).inflate(R.layout.chat_user2_item, null, false);

                holder2 = new ViewHolder2();


                holder2.messageTextView = (TextView) v.findViewById(R.id.message_text);
                holder2.timeTextView = (TextView) v.findViewById(R.id.time_text);
                holder2.messageStatus = (ImageView) v.findViewById(R.id.user_reply_status);
                v.setTag(holder2);

            } else {
                v = convertView;
                holder2 = (ViewHolder2) v.getTag();

            }

            holder2.messageTextView.setText(Html.fromHtml(Emoji.replaceEmoji(message.getMessageText(),
                    holder2.messageTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(16))
                    + " &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;" +
                    "&#160;&#160;&#160;&#160;&#160;&#160;&#160;"));
            //holder2.messageTextView.setText(message.getMessageText());
            holder2.timeTextView.setText(SIMPLE_DATE_FORMAT.format(message.getMessageTime()));

            if (message.getMessageStatus() == Constants.DELIVERED) {
                holder2.messageStatus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_double_checking));
            } else if (message.getMessageStatus() == Constants.SENT) {
                holder2.messageStatus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check));//ic_check
            }



        }


        return v;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = chatMessages.get(position);
        if (message.getAndroidID().equals(android_id)){
            return 1;
        }else{
            return 0;
        }
    }

    private class ViewHolder1 {
        public TextView messageTextView;
        public TextView timeTextView;


    }

    private class ViewHolder2 {
        public ImageView messageStatus;
        public TextView messageTextView;
        public TextView timeTextView;

    }
}
