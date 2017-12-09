package com.example.e610.quranmessenger.Services;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.e610.quranmessenger.PrayerTimesActivity;
import com.example.e610.quranmessenger.R;
import com.example.e610.quranmessenger.SettingsActivity;

/**
 * Foreground service. Creates a head view.
 * The pending intent allows to go back to the settings activity.
 */
public class AzanService extends Service {

    private final static int FOREGROUND_ID = 999;


    MediaPlayer mPlayer;
    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer = MediaPlayer.create(AzanService.this, R.raw.azan1);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String action=intent.getAction();
        if( action==null || (!action.equals("cancel") && !action.equals("ok"))){
            Toast.makeText(AzanService.this, "ألأذان", Toast.LENGTH_LONG).show();
            mPlayer.start();
       /* mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        mPlayer.prepareAsync();*/

            Intent closeIntent = new Intent(this, AzanService.class);
            closeIntent.setAction("cancel");
            PendingIntent closePendingIntent = PendingIntent.getService(this, 0, closeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Action cancelAction = new NotificationCompat.Action(R.drawable.ic_stat_clear, "ايقاف", closePendingIntent);
            Intent okIntent = new Intent(this, PrayerTimesActivity.class);
            closeIntent.setAction("ok");
            PendingIntent okPendingIntent = PendingIntent.getActivity(this, 0, okIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Action okAction = new NotificationCompat.Action(R.drawable.ic_stat_check, "الاعدادات", okPendingIntent);

            logServiceStarted();
            PendingIntent pendingIntent = createPendingIntent();
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.wrdk)
                            .setContentTitle("ألأذان")
                            .setContentText("و الان موعد الصلاه")
                            .setContentIntent(pendingIntent)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .addAction(okAction)
                            .addAction(cancelAction);
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(5476, mBuilder.build());

        /*Notification notification = createNotification(pendingIntent);
        startForeground(FOREGROUND_ID, notification);*/
        }else if(action!=null&&action.equals("cancel")){
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(5476);
            stopSelf();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        logServiceEnded();
        if(mPlayer!=null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }


    private PendingIntent createPendingIntent() {
        Intent intent = new Intent(this, PrayerTimesActivity.class);
        return PendingIntent.getActivity(this, 1265, intent, 0);
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
