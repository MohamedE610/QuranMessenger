package com.example.e610.quranmessenger.Services;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ServiceCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.e610.quranmessenger.Main2Activity;
import com.example.e610.quranmessenger.NotificationDismissedReceiver;
import com.example.e610.quranmessenger.Utils.Constants;
import com.example.e610.quranmessenger.Utils.HeadLayer;
import com.example.e610.quranmessenger.R;
import com.example.e610.quranmessenger.SettingsActivity;
import com.example.e610.quranmessenger.Utils.MediaPLayerUtils;
import com.example.e610.quranmessenger.Utils.MySharedPreferences;
import com.example.e610.quranmessenger.viewPagerFragment;
import com.example.e610.quranmessenger.viewPagerFragment1;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


/**************** ExoPlayer ******************/
public class MediaPlayerService extends Service implements ExoPlayer.EventListener{

/*public class MediaPlayerService extends Service {*/


    private SimpleExoPlayer simpleExoPlayer;
    //private SimpleExoPlayerView simpleExoPlayerView;
    private static MediaSessionCompat mediaSessionCompat;
    private PlaybackStateCompat.Builder mStateBuilder;
    private NotificationManager mNotificationManager;



    private final static int FOREGROUND_ID = 999;

    private HeadLayer mHeadLayer;

    private WindowManager mWindowManager;
    private View mFloatingView;
    final String azkar_sabah_sound="http://www.quranmessenger.life/azkar/azkar_sabah/1.mp3";
    final String azkar_masaa_sound ="http://www.quranmessenger.life/azkar/azkar_masaa/1.mp3";

    /**************** ExoPlayer ******************/



    private PendingIntent createOnDismissedIntent(Context context, int notificationId) {
        Intent intent = new Intent(context, NotificationDismissedReceiver.class);
        intent.putExtra("com.example.e610.quranmessenger."+notificationId+"", notificationId);
        intent.setAction("notification_cancelled");
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context.getApplicationContext(),
                        notificationId, intent, 0);
        return pendingIntent;
    }

    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mediaSessionCompat = new MediaSessionCompat(this, "tag");

        // Enable callbacks from MediaButtons and TransportControls.
        mediaSessionCompat.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mediaSessionCompat.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mediaSessionCompat.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mediaSessionCompat.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mediaSessionCompat.setActive(true);

    }


    /*
     * Shows Media Style notification, with actions that depend on the current MediaSession
     * PlaybackState.
     * @param state The PlaybackState of the MediaSession.*/

    NotificationCompat.Builder builder;
    private void showNotification(PlaybackStateCompat state) {
        builder = new NotificationCompat.Builder(this);

        int icon;
        String play_pause;
        if(state.getState() == PlaybackStateCompat.STATE_PLAYING){
            icon = R.drawable.exo_controls_pause;
            play_pause = "pause";
        } else {
            icon = R.drawable.exo_controls_play;
            play_pause = "play";
        }

        Intent closeIntent = new Intent(this, MediaPlayerService.class);
        closeIntent.setAction("cancel");
        PendingIntent closePendingIntent = PendingIntent.getService(this, 0, closeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        android.support.v4.app.NotificationCompat.Action cancelAction = new android.support.v4.app.NotificationCompat.Action(R.drawable.ic_stat_clear, "", closePendingIntent);

        NotificationCompat.Action playPauseAction = new NotificationCompat.Action(
                icon, play_pause,
                MediaButtonReceiver.buildMediaButtonPendingIntent(this,
                        PlaybackStateCompat.ACTION_PLAY_PAUSE));

        NotificationCompat.Action restartAction = new android.support.v4.app.NotificationCompat
                .Action(R.drawable.exo_controls_previous, "restart",
                MediaButtonReceiver.buildMediaButtonPendingIntent
                        (this, PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS));

        PendingIntent settingPendingIntent = PendingIntent.getActivity
                (this, 0, new Intent(this, SettingsActivity.class), 0);

        android.support.v4.app.NotificationCompat.Action settingAction = new android.support.v4.app.NotificationCompat.Action(R.drawable.ic_stat_settings, "", settingPendingIntent);

        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (this, 0, new Intent(this, Main2Activity.class), 0);


        PendingIntent deleteIntent=createOnDismissedIntent(this,99);
        builder.setContentIntent(contentPendingIntent)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Alforqan")
                .setContentText("asd")
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setDeleteIntent(deleteIntent)
                .addAction(settingAction)
                .addAction(playPauseAction)
                .addAction(restartAction)
                .setStyle(new NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSessionCompat.getSessionToken())
                        .setShowActionsInCompactView(0,1)
                        .setShowCancelButton(true)
                        .setCancelButtonIntent(closePendingIntent));

        /*mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(99, builder.build());*/
        startForeground(99,builder.build());
    }


    /*
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.*/

    private void initializePlayer(Uri mediaUri) {
        if (simpleExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            //simpleExoPlayerView.setPlayer(simpleExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            simpleExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(this, "ClassicalMusicQuiz");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    this, userAgent), new DefaultExtractorsFactory(), null, null);
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(true);
        }else
            simpleExoPlayer.setPlayWhenReady(true);
    }


    /*
     * Release ExoPlayer.*/

    private void releasePlayer() {

        if(simpleExoPlayer!=null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }

        if(mNotificationManager!=null) {
            mNotificationManager.cancelAll();
        } else{
            mNotificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            mNotificationManager.cancel(99);
        }

    }
    /**************** ExoPlayer ******************/


    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the player view.
       /* simpleExoPlayerView = (SimpleExoPlayerView) view.findViewById(R.id.player_view);
        simpleExoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                (getResources(), R.drawable.question_mark));*/

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    ;
    int pageNum;
    String sh_name;
    String action="";
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent!=null&& intent.getAction()!=null)
             action=intent.getAction();

        if(action.equals("play")) {
            Bundle bundle = intent.getBundleExtra("pn");
            String url="";
            if(bundle!=null) {
                url = bundle.getString("pn");
                pageNum=bundle.getInt("num");
                sh_name=bundle.getString("sh_name");
            }
                /************* ExoPlayer ***********/
                // Initialize the Media Session.
                initializeMediaSession();
                // Initialize the player.
                initializePlayer(Uri.parse(url));

        }else if(action.equals("cancel")){
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            //stopForeground(true);
            notificationManager.cancel(5476);
            stopSelf();
        }else if(action.equals("azkar")){
            Bundle b=new Bundle();
            b=intent.getBundleExtra("bundle");
            String azkarType=b.getString("azkar");
            String my_url="";
            if(azkarType.equals("am"))
                my_url=azkar_sabah_sound;
            else
                my_url=azkar_masaa_sound;
            /************* ExoPlayer ***********/
            // Initialize the Media Session.
            initializeMediaSession();
            // Initialize the player.
            initializePlayer(Uri.parse(my_url));

        }else if(action.equals("pause")){
            if(simpleExoPlayer!=null)
               simpleExoPlayer.setPlayWhenReady(false);
        }else if(action.equals("playWidget")){
            if(simpleExoPlayer!=null)
               simpleExoPlayer.setPlayWhenReady(true);
        }



        //Notification notification = createNotification(pendingIntent);
        //startForeground(FOREGROUND_ID, notification);

        return START_STICKY;
    }

    public void sendBroadCast(String action , int num){
        Intent intent = new Intent();
        Bundle b=new Bundle();
        b.putInt("num",num);
        intent.putExtra("b",b);
        intent.setAction(action);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        destroyHeadLayer();
        stopForeground(true);
        logServiceEnded();
        Log.d("asdasd","asdasd");
        /**************** ExoPlayer ******************/
        //MySharedPreferences.setUserSetting("LAST_SURAH","-1");

        /*String state=MySharedPreferences.getMediaPlayerState();
        if(state.equals("0"))*/
            MySharedPreferences.setMediaPlayerState("0");
            MySharedPreferences.setUserSetting("LAST_SURAH","-1");

        releasePlayer();
        if(mediaSessionCompat!=null)
          mediaSessionCompat.setActive(false);

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

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Toast.makeText(this,"onTaskRemoved called",Toast.LENGTH_SHORT).show();
        super.onTaskRemoved(rootIntent);
        //do something you want
        //stop service
        this.stopSelf();
    }

    private void logServiceStarted() {
        Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();
    }

    private void logServiceEnded() {
        Toast.makeText(this, "Service ended", Toast.LENGTH_SHORT).show();
    }


    /**************** ExoPlayer ******************/
    // ExoPlayer Event Listeners
    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    /**
     * Method that is called when the ExoPlayer state changes. Used to update the MediaSession
     * PlayBackState to keep in sync, and post the media notification.
     * @param playWhenReady true if ExoPlayer is playing, false if it's paused.
     * @param playbackState int describing the state of ExoPlayer. Can be STATE_READY, STATE_IDLE,
     *                      STATE_BUFFERING, or STATE_ENDED.
     */
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        MySharedPreferences.setUpMySharedPreferences(MediaPlayerService.this,getString(R.string.shared_pref_file_name));

        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    simpleExoPlayer.getCurrentPosition(), 1f);
            Toast.makeText(this,"ExoPlayer.STATE_READY) && playWhenReady",Toast.LENGTH_SHORT).show();
                    sendBroadCast("cancelDialog",pageNum);
            MySharedPreferences.setMediaPlayerState("1");
        } else if((playbackState == ExoPlayer.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    simpleExoPlayer.getCurrentPosition(), 1f);
            Toast.makeText(this,"ExoPlayer.STATE_READY",Toast.LENGTH_SHORT).show();
        } else if((playbackState ==  ExoPlayer.STATE_ENDED)) {
            MySharedPreferences.setMediaPlayerState("0");
            //sendBroadCast();
            /*sendBroadCast("playNextOne",pageNum);
            Toast.makeText(this,"ExoPlayer.STATE_ENDED",Toast.LENGTH_SHORT).show();*/
            if (!action.equals("azkar")){
                MySharedPreferences.setUpMySharedPreferences(this, "extraSetting");
            String isBackground = MySharedPreferences.getUserSetting("isBackground");
            if (isBackground.equals("0")) {
                sendBroadCast("playNextOne", pageNum);
                Toast.makeText(this, "ExoPlayer.STATE_ENDED", Toast.LENGTH_SHORT).show();
            } else {
               /* Intent intent=new Intent(this,MediaPlayerService.class);
                intent.setAction("play");
                Bundle b=new Bundle();
                b.putString("pn", viewPagerFragment1.playSounds(pageNum,sh_name));
                b.putInt("num",pageNum);
                b.putString("sh_name",sh_name);
                intent.putExtra("pn",b);
                startService(intent);*/
                /************* ExoPlayer ***********/
                // Prepare the MediaSource.
                /*String url=viewPagerFragment1.playSounds(++pageNum,sh_name);*/
                String url = MediaPLayerUtils.createUrl(++pageNum, sh_name);

                String userAgent = Util.getUserAgent(this, "ClassicalMusicQuiz");
                MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(url), new DefaultDataSourceFactory(
                        this, userAgent), new DefaultExtractorsFactory(), null, null);
                simpleExoPlayer.prepare(mediaSource);
                simpleExoPlayer.setPlayWhenReady(true);
            }
        }

    }

        mediaSessionCompat.setPlaybackState(mStateBuilder.build());
        showNotification(mStateBuilder.build());

    }

   // MySharedPreferences.setUpMySharedPreferences(MediaPlayerService.this,this.getString(R.string.shared_pref_file_name));
    //String isBackground= MySharedPreferences.getUserSetting("isBackground");
    /*if(isBackground!=null&&isBackground.equals("0")) {
        //stopSelf();
        sendBroadCast("playNextOne", pageNum);
    }else if(isBackground!=null&&isBackground.equals("1")){
        Intent intent=new Intent(MediaPlayerService.this,MediaPlayerService.class);
        intent.setAction("play");
        Bundle b=new Bundle();
        pageNum++;
        b.putString("pn", viewPagerFragment1.playSounds(pageNum,sh_name));
        b.putInt("num",pageNum);
        b.putString("sh_name",sh_name);
        intent.putExtra("pn",b);
        stopSelf();
        startService(intent);
    }*/
    @Override
    public void onPlayerError(ExoPlaybackException error) {
        //String e=error.getMessage();
        sendBroadCast("cancelDialog",pageNum);
        Toast.makeText(MediaPlayerService.this,"حدث خطاء اثناء تحميل الملف الصوتى \n الرجاء التاكد من الشبكه",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPositionDiscontinuity() {

    }

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            //MediaPlayerService.this.startForeground(99,builder.build());
            MySharedPreferences.setMediaPlayerState("1");
            simpleExoPlayer.setPlayWhenReady(true);
        }


        @Override
        public void onPause() {
           // ServiceCompat.stopForeground(MediaPlayerService.this,ServiceCompat.STOP_FOREGROUND_DETACH);
            simpleExoPlayer.setPlayWhenReady(false);
            MySharedPreferences.setMediaPlayerState("0");
            MySharedPreferences.setUserSetting("LAST_SURAH","-1");
        }

        @Override
        public void onSkipToPrevious() {
            simpleExoPlayer.seekTo(0);
        }
    }

    /**
     * Broadcast Receiver registered to receive the MEDIA_BUTTON intent coming from clients.
     */
    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mediaSessionCompat, intent);
        }
    }
    /**************** ExoPlayer ******************/

}
