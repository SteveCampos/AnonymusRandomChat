package apps.steve.fire.randomchat.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;

import apps.steve.fire.randomchat.R;

/**
 * Created by Steve on 30/09/2016.
 */

public class NativeAdViewHolder extends RecyclerView.ViewHolder {
    private final NativeExpressAdView mNativeAd;

    public NativeAdViewHolder(View itemView) {
        super(itemView);
        mNativeAd = (NativeExpressAdView) itemView.findViewById(R.id.nativeAd);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("7D610238E4AC96FE6016B6B6DF36209A")
                .addTestDevice("6A4C8AA799F4A30921C02DC505824DC0")
                .build();
        mNativeAd.loadAd(adRequest);
    }

    public void bind(AdListener listener){
        mNativeAd.setAdListener(listener);
    }
}
