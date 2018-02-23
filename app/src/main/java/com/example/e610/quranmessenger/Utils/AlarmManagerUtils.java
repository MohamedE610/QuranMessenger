package com.example.e610.quranmessenger.Utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.e610.quranmessenger.R;
import com.example.e610.quranmessenger.Services.AzanService;
import com.example.e610.quranmessenger.Services.AzkarService;
import com.example.e610.quranmessenger.Services.HeadService;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by E610 on 2/3/2018.
 */
public class AlarmManagerUtils {
    static Context context;
    public AlarmManagerUtils(Context ctx){
        context=ctx;
    }



    /************************ al werd alyomi ********************/
    HashMap<Integer, PendingIntent> pendingIntentList = new HashMap<>();
    AlarmManager alarmManager;

    public void startHeadService(int h, int m, int id) {
        Intent intent = new Intent(context, HeadService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, id, intent, 0);
        if (pendingIntentList != null && !pendingIntentList.containsKey(id))
            pendingIntentList.put(id, pendingIntent);
        long _alarm = 0;
        Calendar now = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        //calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, h);
        calendar.set(Calendar.MINUTE, m);
        long asd = AlarmManager.INTERVAL_DAY;
        if (calendar.getTimeInMillis() <= now.getTimeInMillis())
            _alarm = calendar.getTimeInMillis() + (AlarmManager.INTERVAL_DAY + 1);
        else
            _alarm = calendar.getTimeInMillis();

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, _alarm, AlarmManager.INTERVAL_DAY, pendingIntent);
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,_alarm,2*60*1000,pendingIntent);
        /*Context context = getActivity();
        context.startService(new Intent(context, HeadService.class));*/
    }

    public void stopHeadService() {
        context.stopService(new Intent(context, HeadService.class));
        canselAlarms();
    }

    public void canselAlarms() {
        MySharedPreferences.setUpMySharedPreferences(context, context.getString(R.string.shared_pref_file_name));
        String alarmNumbers=MySharedPreferences.getData();
        int alarmNum=Integer.valueOf(alarmNumbers);
        if(pendingIntentList!=null&&pendingIntentList.size()==alarmNum){
            if (alarmManager != null) {
                for (int i = 0; i < pendingIntentList.size(); i++) {
                    alarmManager.cancel(pendingIntentList.get(i+6000));
                }
                //pendingIntentList.clear();
            }
        }else if(pendingIntentList==null||pendingIntentList.size()!=alarmNum){
            pendingIntentList=new HashMap<>();
            for (int i = 0; i <alarmNum ; i++) {
                Intent intent = new Intent(context, HeadService.class);
                PendingIntent pendingIntent = PendingIntent.getService(context, 6000+i, intent, 0);
                pendingIntentList.put(i+6000,pendingIntent);
            }

            if (alarmManager != null) {
                for (int i = 0; i < pendingIntentList.size(); i++) {
                    alarmManager.cancel(pendingIntentList.get(i+6000));
                }
                //pendingIntentList.clear();
            }

        }
    }


    /****************************** azkar *********************************/
// 0 -> am
// 1 -> pm
 HashMap<Integer, PendingIntent> pendingIntentAzkarList = new HashMap<>();
     AlarmManager azkarAlarmManager;
    public  void startAzkarService(int h, int m, int id , int azkar_type) {
        Intent intent = new Intent(context, AzkarService.class);
        intent.setAction("azkar");
        intent.putExtra("azkar_type",azkar_type);
        PendingIntent pendingIntent = PendingIntent.getService(context, id, intent, 0);
        if (pendingIntentAzkarList != null && !pendingIntentAzkarList.containsKey(id))
            pendingIntentAzkarList.put(id, pendingIntent);
        long _alarm = 0;
        Calendar now = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        //calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, h);
        calendar.set(Calendar.MINUTE, m);
        long asd = AlarmManager.INTERVAL_DAY;
        if (calendar.getTimeInMillis() <= now.getTimeInMillis())
            _alarm = calendar.getTimeInMillis() + (AlarmManager.INTERVAL_DAY + 1);
        else
            _alarm = calendar.getTimeInMillis();

        azkarAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        //azkarAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, _alarm, AlarmManager.INTERVAL_DAY, pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            azkarAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, _alarm, pendingIntent);
        }else{
            azkarAlarmManager.set(AlarmManager.RTC_WAKEUP, _alarm, pendingIntent);
        }
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,_alarm,2*60*1000,pendingIntent);
        /*Context context = getActivity();
        context.startService(new Intent(context, HeadService.class));*/
    }

    public void stopAzkarService() {
        context.stopService(new Intent(context, HeadService.class));
        canselAzkarAlarms();
    }

    public void canselAzkarAlarms() {

        if(pendingIntentAzkarList!=null&&pendingIntentAzkarList.size()==2){
            if (azkarAlarmManager != null) {
                for (int i = 0; i < pendingIntentAzkarList.size(); i++) {
                    if(i==0)
                        azkarAlarmManager.cancel(pendingIntentAzkarList.get(9911));
                    else
                        azkarAlarmManager.cancel(pendingIntentAzkarList.get(1199));
                }
                //pendingIntentList.clear();
            }
        }else if(pendingIntentAzkarList==null||pendingIntentAzkarList.size()!=2){
            pendingIntentAzkarList=new HashMap<>();
            for (int i = 0; i <2 ; i++) {
                if(i==0) {
                    Intent intent = new Intent(context, HeadService.class);
                    PendingIntent pendingIntent = PendingIntent.getService(context,9911, intent, 0);
                    pendingIntentAzkarList.put(9911, pendingIntent);
                }else{
                    Intent intent = new Intent(context, HeadService.class);
                    PendingIntent pendingIntent = PendingIntent.getService(context, 1199, intent, 0);
                    pendingIntentAzkarList.put(1199, pendingIntent);
                }
            }

            if (azkarAlarmManager != null) {
                for (int i = 0; i < pendingIntentAzkarList.size(); i++) {
                    if(i==0)
                        azkarAlarmManager.cancel(pendingIntentAzkarList.get(9911));
                    else
                        azkarAlarmManager.cancel(pendingIntentAzkarList.get(1199));
                }
                //pendingIntentList.clear();
            }

        }
    }






    /************************ azan ***********************/
    HashMap<Integer, PendingIntent> pendingIntentAzanList = new HashMap<>();
    AlarmManager azanAlarmManager;

    public void startAzanService(int h, int m, int id) {
        Intent intent = new Intent(context, AzanService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, id, intent, 0);
        if (pendingIntentAzanList != null && !pendingIntentAzanList.containsKey(id))
            pendingIntentAzanList.put(id, pendingIntent);
        long _alarm = 0;
        Calendar now = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        //calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, h);
        calendar.set(Calendar.MINUTE, m);
        long asd = AlarmManager.INTERVAL_DAY;
        if (calendar.getTimeInMillis() <= now.getTimeInMillis())
            _alarm = calendar.getTimeInMillis() + (AlarmManager.INTERVAL_DAY + 1);
        else
            _alarm = calendar.getTimeInMillis();

        azanAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        azanAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, _alarm, AlarmManager.INTERVAL_DAY, pendingIntent);
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,_alarm,2*60*1000,pendingIntent);
        /*Context context = getActivity();
        context.startService(new Intent(context, HeadService.class));*/
    }

    public void canselAzanAlarms() {
        context.stopService(new Intent(context, AzanService.class));
        if (pendingIntentAzanList != null && pendingIntentAzanList.size() == 5) {
            if (azanAlarmManager != null) {
                for (int i = 0; i < pendingIntentAzanList.size(); i++) {
                    azanAlarmManager.cancel(pendingIntentAzanList.get(i + 8000));
                }
                //pendingIntentList.clear();
            }
        } else if (pendingIntentAzanList == null || pendingIntentAzanList.size() != 5) {
            pendingIntentAzanList = new HashMap<>();
            for (int i = 0; i < 5; i++) {
                Intent intent = new Intent(context, AzanService.class);
                PendingIntent pendingIntent = PendingIntent.getService(context, i + 8000, intent, 0);
                pendingIntentAzanList.put(i + 8000, pendingIntent);
            }
            if (azanAlarmManager != null) {
                for (int i = 0; i < pendingIntentAzanList.size(); i++) {
                    azanAlarmManager.cancel(pendingIntentAzanList.get(i + 8000));
                }
                //pendingIntentList.clear();
            }
        }
    }
}
