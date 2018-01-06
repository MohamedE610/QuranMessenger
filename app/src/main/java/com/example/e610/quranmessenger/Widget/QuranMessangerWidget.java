package com.example.e610.quranmessenger.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.e610.quranmessenger.R;
import com.example.e610.quranmessenger.Services.MediaPlayerService;
import com.example.e610.quranmessenger.Utils.ServiceUtils;

/**
 * Implementation of App Widget functionality.
 */
public class QuranMessangerWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        //CharSequence widgetText = context.getString(R.string.appwidget_text);
        CharSequence widgetText ="الفرقان";
        // Construct the RemoteViews object
        String packageName=context.getPackageName();
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.quran_messanger_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);
        views.setImageViewResource(R.id.img_widget,R.drawable.widget_play);
        views.setImageViewResource(R.id.img_widget_restart,R.drawable.stop_on);

        //to play;
        Intent mediaIntent=new Intent();
        mediaIntent.setAction("widget_play");
        mediaIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        PendingIntent mediaPendingIntent=PendingIntent.getBroadcast(context,12233,mediaIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.img_widget,mediaPendingIntent);

        Intent restartIntent=new Intent();
        restartIntent.setAction("widget_restart");
        restartIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        PendingIntent restartPendingIntent=PendingIntent.getBroadcast(context,223344,restartIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.img_widget_restart,restartPendingIntent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    boolean isRestarted=false;
    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int appWidgetId = 0;

        String action="";
        action=intent.getAction();
        if(action!=null&&action.equals("widget_play")){
            appWidgetId = intent.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.quran_messanger_widget);
            views.setImageViewResource(R.id.img_widget,R.drawable.widget_pause);

            //to pause;
            Intent mediaIntent=new Intent();
            mediaIntent.setAction("widget_pause");
            mediaIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
            PendingIntent mediaPendingIntent=PendingIntent.getBroadcast(context,334455,mediaIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.img_widget,mediaPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);

            ServiceUtils.startMediaService(context,"1","hosary");

        }else if(action!=null&&action.equals("widget_pause")){
            appWidgetId = intent.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.quran_messanger_widget);
            views.setImageViewResource(R.id.img_widget,R.drawable.widget_play);

            //to play;
            Intent mediaIntent=new Intent();
            mediaIntent.setAction("widget_play");
            mediaIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
            PendingIntent mediaPendingIntent=PendingIntent.getBroadcast(context,12233,mediaIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.img_widget,mediaPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);

            Intent pauseIntent=new Intent(context,MediaPlayerService.class);
            pauseIntent.setAction("pause");
            context.startService(pauseIntent);

        }else if(action!=null&&action.equals("widget_restart")){
            appWidgetId = intent.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.quran_messanger_widget);
            views.setImageViewResource(R.id.img_widget,R.drawable.widget_play);

            appWidgetManager.updateAppWidget(appWidgetId, views);

            ServiceUtils.stopMediaService(context);
        }

        super.onReceive(context, intent);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

