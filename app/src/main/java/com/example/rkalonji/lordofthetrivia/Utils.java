package com.example.rkalonji.lordofthetrivia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import static android.R.attr.y;

/**
 * Created by rkalonji on 05/31/2017.
 */

public class Utils {
    public void loadAddBanner (View v, int resourceId) {
        AdView mAdView = (AdView) v.findViewById(resourceId);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
    }

    public SQLiteDatabase returnWritableDatabase (Context context) {
        TriviasProvider.DatabaseHelper x = new TriviasProvider.DatabaseHelper(context);
        return x.getWritableDatabase();
    }
}
