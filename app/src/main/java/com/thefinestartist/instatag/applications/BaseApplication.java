package com.thefinestartist.instatag.applications;


import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.aviary.android.feather.sdk.IAviaryClientCredentials;

/**
 * Created by TheFinestArtist on 5/5/15.
 */
public class BaseApplication extends MultiDexApplication implements IAviaryClientCredentials {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public String getBillingKey() {
        return "";
    }

    @Override
    public String getClientID() {
        return "44f2f67e1cc14890a63cf32259ea0837";
    }

    @Override
    public String getClientSecret() {
        return "9236f457-82b4-4ca1-b80a-ac34b2c0ca0a";
    }
}
