package com.example.e610.quranmessenger.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by E610 on 2/3/2018.
 */
public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context," Boot Completed",Toast.LENGTH_LONG).show();

    }

}