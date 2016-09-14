package apps.steve.fire.randomchat.holders;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import apps.steve.fire.randomchat.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Steve on 22/08/2016.
 */

public class TrendUserHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.cardview) public CardView cardView;
    @BindView(R.id.imageview_avatar) public ImageView avatar;
    @BindView(R.id.text_title) public TextView title;
    @BindView(R.id.text_hots) public TextView hots;
    @BindView(R.id.text_state) public TextView state;
    @BindView(R.id.btn_chat) public AppCompatButton btnChat;

    public TrendUserHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
