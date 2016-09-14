package apps.steve.fire.randomchat.holders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import apps.steve.fire.randomchat.R;
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
    public TextView title;
    @BindView(R.id.text_last_message)
    public TextView message;
    @BindView(R.id.text_time)
    public TextView time;
    @BindView(R.id.text_counter)
    public TextView counter;


    public ChatsHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
