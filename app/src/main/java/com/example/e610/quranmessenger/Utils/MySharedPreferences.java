package com.example.e610.quranmessenger.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class MySharedPreferences {

    static Context context;
    static String FileName;
    static SharedPreferences sharedPref;
    static SharedPreferences.Editor editor;

    public static void setUpMySharedPreferences(Context context_,String FileName_){
         context=context_;
        FileName=FileName_;
        sharedPref = context.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        editor=sharedPref.edit();
    }

    public static void setUserSetting(String key,String value){
        editor.putString(key,value);
        editor.commit();
    }

    public static String getUserSetting(String key){

        String UserSetting=sharedPref.getString(key,"hosary");

        return UserSetting;
    }
    //"widgetRecipe"
    public static void saveData(String data){
        editor.putString("NOA",data);
        editor.commit();
    }

    public static String getData(){
        String data = sharedPref.getString("NOA","1");
        return data ;
    }

    public static boolean IsFirstTime(){
        String check=sharedPref.getString("FirstTime","");

        if(check.equals("yes"))
            return false;
         return true;
    }

    public static void FirstTime(){
        editor.putString("FirstTime","yes");
        editor.commit();
    }

    public void Clear(){
        editor.clear();
        editor.commit();
    }


    public static void setMediaPlayerState(String data){
        editor.putString("MediaPlayerState",data);
        editor.commit();
    }

    public static String getMediaPlayerState(){
        String data = sharedPref.getString("MediaPlayerState","-1");
        return data ;
    }

}
