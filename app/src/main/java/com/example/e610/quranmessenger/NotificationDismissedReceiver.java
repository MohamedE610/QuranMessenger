package com.example.e610.quranmessenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.e610.quranmessenger.Services.MediaPlayerService;

/**
 * Created by E610 on 12/15/2017.
 */
public class NotificationDismissedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getExtras().getInt("com.example.e610.quranmessenger.99");
        if(notificationId==99){
            context.stopService(new Intent(context,MediaPlayerService.class));
        }
      /* Your code to handle the event here */
    }
}