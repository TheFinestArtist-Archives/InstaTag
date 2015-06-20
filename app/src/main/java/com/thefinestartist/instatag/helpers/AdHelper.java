package com.thefinestartist.instatag.helpers;

import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.thefinestartist.instatag.R;

/**
 * Created by TheFinestArtist on 5/4/15.
 */
public class AdHelper {

    public static void loadBannerAd(AdView adView) {
        try {
            adView.loadAd(new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice("BE5D7D1E701EF21AB93369A353CAA3ED")
                    .addTestDevice("921DF5A672991967B9FFE364D0FF8498")
                    .addTestDevice("A642C45F5DD4C0E09AA896DDABD36789")
                    .addTestDevice("5270E2092AA1F46AC51964363699AB9E")
                    .build());
        } catch (Exception e) {
//            Crashlytics.logException(e);
        }
    }

    public static void popUpAd(Context context) {
        final InterstitialAd interstitial = new InterstitialAd(context);
        interstitial.setAdUnitId(context.getString(R.string.interstitial_ad_unit_id));

        AdRequest adRequest = new AdRequest.Builder().build();
        interstitial.loadAd(adRequest);
        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                interstitial.show();
            }
        });
    }
}
