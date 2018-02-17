package com.example.e610.quranmessenger.BroadcastRecievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.e610.quranmessenger.Services.AzanService;
import com.example.e610.quranmessenger.Services.MediaPlayerService;

/**
 * Created by E610 on 12/15/2017.
 */
public class NotificationDismissedReceiver extends BroadcastReceiver {

    int azanNotificationId=5476;
    int mediaNotificationId=99;
    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getExtras().getInt("com.example.e610.quranmessenger."+mediaNotificationId);
        if(notificationId==mediaNotificationId){
            context.stopService(new Intent(context,MediaPlayerService.class));
            return;
        }

        notificationId = intent.getExtras().getInt("com.example.e610.quranmessenger."+azanNotificationId);
        if(notificationId==azanNotificationId){
            context.stopService(new Intent(context,AzanService.class));
        }
      /* Your code to handle the event here */
    }
}