package com.example.e610.quranmessenger.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.e610.quranmessenger.Services.AzanService;
import com.example.e610.quranmessenger.Services.MediaPlayerService;

/**
 * Created by E610 on 12/31/2017.
 */
public class ServiceUtils {

    public static void startMediaService(Context context, String pageNum, String shekhName){
        stopMediaService(context);
        Intent intent = new Intent(context, MediaPlayerService.class);
        intent.setAction("play");
        Bundle b = new Bundle();
        b.putString("pn", MediaPLayerUtils.createUrl(Integer.valueOf(pageNum), shekhName));
        b.putInt("num", Integer.valueOf(pageNum));
        b.putString("sh_name",shekhName);
        intent.putExtra("pn", b);
        context.startService(intent);
    }

    public static void stopMediaService(Context context){
        context.stopService(new Intent(context,MediaPlayerService.class));
    }

    public static void pauseMediaService(Context context){
        Intent intent=new Intent(context,MediaPlayerService.class);
        intent.setAction("pause");
        context.startService(intent);
    }

    public static void resumeMediaService(Context context){
        Intent intent=new Intent(context,MediaPlayerService.class);
        intent.setAction("resume");
        context.startService(intent);
    }

    public static void pauseAzanService(Context context){
        Intent intent=new Intent(context,AzanService.class);
        intent.setAction("pause");
        context.startService(intent);
    }

    public static void resumeAzanService(Context context){
        Intent intent=new Intent(context,AzanService.class);
        intent.setAction("resume");
        context.startService(intent);
    }

    public static void stopAlarmManagerService(){

    }

    public static void startAlarmManagerService(){

    }

}
