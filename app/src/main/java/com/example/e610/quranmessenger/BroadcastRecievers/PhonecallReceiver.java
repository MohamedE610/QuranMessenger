package com.example.e610.quranmessenger.BroadcastRecievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.example.e610.quranmessenger.Utils.ServiceUtils;

/**
 * Created by E610 on 2/5/2018.
 */
public  class PhonecallReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        //We listen to two intents.  The new outgoing call only tells us of an outgoing call.  We use it to get the number.
        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {

            ServiceUtils.pauseMediaService(context);
            ServiceUtils.pauseAzanService(context);

        } else {
            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            int state = 0;

            //Went to idle-  this is the end of a call.  What type depends on previous state(s)
            if (stateStr != null && stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                state = TelephonyManager.CALL_STATE_IDLE;
                ServiceUtils.resumeMediaService(context);
                ServiceUtils.resumeAzanService(context);


            } else if (stateStr != null && stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                state = TelephonyManager.CALL_STATE_OFFHOOK;

            } else if (stateStr != null && stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                state = TelephonyManager.CALL_STATE_RINGING;
                ServiceUtils.pauseMediaService(context);
                ServiceUtils.pauseAzanService(context);

            }

        }
    }

}