package com.example.jeff.move4klant;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by Jeff on 21-10-2014.
 */
public class BroadcastReciever extends BroadcastReceiver {
    // makes sure the service is started after a reboot. the service will then continue to scan for bluetooth beacons
    @Override
    public void onReceive(Context context, Intent intent) {

        Calendar cal = Calendar.getInstance();

        Intent i = new Intent(context,Bluetoothscanner.class);
        PendingIntent P = PendingIntent.getService(context, 0, i, 0);

        AlarmManager ALS = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        ALS.setRepeating(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),80 * 1000 ,P);
    }
}


