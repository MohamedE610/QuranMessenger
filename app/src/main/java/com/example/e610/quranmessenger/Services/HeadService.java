package com.example.e610.quranmessenger.Services;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.e610.quranmessenger.Main2Activity;
import com.example.e610.quranmessenger.Utils.HeadLayer;
import com.example.e610.quranmessenger.R;
import com.example.e610.quranmessenger.SettingsActivity;

/**
 * Foreground service. Creates a head view.
 * The pending intent allows to go back to the settings activity.
 */
public class HeadService extends Service {

    private final static int FOREGROUND_ID = 999;

    private HeadLayer mHeadLayer;

    private WindowManager mWindowManager;
    private View mFloatingView;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    ;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String action=intent.getAction();
        if( action==null || (!action.equals("cancel") && !action.equals("ok"))){
            logServiceStarted();
            //initHeadLayer();
            PendingIntent pendingIntent = createPendingIntent();
            Intent closeIntent = new Intent(this, HeadService.class);
            closeIntent.setAction("cancel");
            PendingIntent closePendingIntent = PendingIntent.getService(this,0, closeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Action cancelAction = new NotificationCompat.Action(R.drawable.ic_stat_clear, "ليس الان", closePendingIntent);
            Intent okIntent = new Intent(this, Main2Activity.class);
            closeIntent.setAction("ok");
            PendingIntent okPendingIntent = PendingIntent.getActivity(this,0, okIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Action okAction = new NotificationCompat.Action(R.drawable.ic_stat_check, "متابعه الورد", okPendingIntent);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.wrdk)
                            .setContentTitle("وردك اليومى الان")
                            .setContentIntent(pendingIntent)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .addAction(okAction)
                            .addAction(cancelAction);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(5566, mBuilder.build());

        }else if(action!=null&&action.equals("cancel")){
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(5566);
            stopSelf();
        }

        //Notification notification = createNotification(pendingIntent);
        //startForeground(FOREGROUND_ID, notification);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        destroyHeadLayer();
        stopForeground(true);

        logServiceEnded();
    }

    private void initHeadLayer() {
        mHeadLayer = new HeadLayer(this);

    }

    private void destroyHeadLayer() {
        if(mHeadLayer!=null){
            mHeadLayer.destroy();
            mHeadLayer = null;
        }
    }

    private PendingIntent createPendingIntent() {
        Intent intent = new Intent(this, SettingsActivity.class);
        return PendingIntent.getActivity(this,8976, intent, 0);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private Notification createNotification(PendingIntent intent) {
        return new Notification.Builder(this)
                .setContentTitle(getText(R.string.notificationTitle))
                .setContentText(getText(R.string.notificationText))
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(intent)
                .build();
    }

    private void logServiceStarted() {
        Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();
    }

    private void logServiceEnded() {
        Toast.makeText(this, "Service ended", Toast.LENGTH_SHORT).show();
    }
}
