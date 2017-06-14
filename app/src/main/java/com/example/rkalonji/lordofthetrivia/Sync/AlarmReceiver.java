package com.example.rkalonji.lordofthetrivia.Sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.rkalonji.lordofthetrivia.AlarmService;

/**
 * Created by Rkalonji on 06/14/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "com.example.rkalonji.lordofthetrivia.alarm";

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, AlarmService.class);
        i.putExtra("foo", "bar");
        context.startService(i);
    }
}
