package com.example.e610.quranmessenger.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.Preference;
import android.widget.Toast;

import com.example.e610.quranmessenger.Models.PrayerTimes.PrayerTimes;
import com.example.e610.quranmessenger.R;
import com.google.gson.Gson;

import java.util.Calendar;

/**
 * Created by E610 on 2/3/2018.
 */
public class BootCompleteReceiver extends BroadcastReceiver {

    Context context;
    AlarmManagerUtils alarmManagerUtils;
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context," Boot Completed",Toast.LENGTH_LONG).show();

        String azanStatus, azkarAmStatus, azkarPmStatus, alarmStatus;
        this.context=context;
        alarmManagerUtils=new AlarmManagerUtils(context);
        MySharedPreferences.setUpMySharedPreferences(context,context.getResources().getString(R.string.shared_pref_file_name));
        azanStatus=MySharedPreferences.getAzanState();
        azkarAmStatus=MySharedPreferences.getAzkarAmState();
        azkarPmStatus=MySharedPreferences.getAzkarPmState();
        alarmStatus=MySharedPreferences.getAlarmState();


        try {
            if (azanStatus.equals("1")) {

                azanMethod();
                Toast.makeText(context, "Azan Done", Toast.LENGTH_LONG).show();

            }

            if (azkarAmStatus.equals("1")) {

                azkarAmMethod();
                Toast.makeText(context, "AzkarAm Done", Toast.LENGTH_LONG).show();

            }

            if (azkarPmStatus.equals("1")) {

                azkarPmMethod();
                Toast.makeText(context, "AzkarPm Done", Toast.LENGTH_LONG).show();

            }

            if (alarmStatus.equals("1")) {

                alarmMethod();
                Toast.makeText(context, "Alarm Done", Toast.LENGTH_LONG).show();

            }


        }catch (Exception e){
            Toast.makeText(context, "Exception !", Toast.LENGTH_LONG).show();
        }


    }


    private void azanMethod(){
        if (NetworkState.ConnectionAvailable(context)) {
            FetchAzanData fetchAzanData = new FetchAzanData(context);
            fetchAzanData.setNetworkResponse(new NetworkResponse() {
                @Override
                public void OnSuccess(String JsonData) {
                    Gson gson = new Gson();
                    PrayerTimes prayerTimes = gson.fromJson(JsonData, PrayerTimes.class);
                    String s = " الفجر : " + prayerTimes.getData().get(0).getTimings().Fajr + "\n"
                            + " الظهر : " + prayerTimes.getData().get(0).getTimings().Dhuhr + "\n"
                            + " العصر : " + prayerTimes.getData().get(0).getTimings().Asr + "\n"
                            + " المغرب : " + prayerTimes.getData().get(0).getTimings().Maghrib + "\n"
                            + " االعشاء :" + prayerTimes.getData().get(0).getTimings().Isha + "\n";


                    Calendar calendar=Calendar.getInstance();
                    int i= calendar.get(Calendar.DAY_OF_MONTH);

                    String[] times = new String[6];
                    String[] ss=new String[2];

                    ss=prayerTimes.getData().get(i).getTimings().Fajr.split(" ");
                    // times[0] = prayerTimes.getData().get(0).getTimings().Fajr;
                    times[0] =ss[0];

                    ss=prayerTimes.getData().get(i).getTimings().Dhuhr.split(" ");
                    //times[1] = prayerTimes.getData().get(0).getTimings().Dhuhr;
                    times[1] =ss[0];

                    //times[2] = prayerTimes.getData().get(0).getTimings().Asr;
                    ss=prayerTimes.getData().get(i).getTimings().Asr.split(" ");
                    times[2] =ss[0];

                    //times[3] = prayerTimes.getData().get(0).getTimings().Maghrib;
                    ss=prayerTimes.getData().get(i).getTimings().Maghrib.split(" ");
                    times[3] =ss[0];

                    //times[4] = prayerTimes.getData().get(0).getTimings().Isha;
                    ss=prayerTimes.getData().get(i).getTimings().Isha.split(" ");
                    times[4] =ss[0];

                    //times[5] = prayerTimes.getData().get(0).getTimings().Sunrise;
                    ss=prayerTimes.getData().get(i).getTimings().Sunrise.split(" ");
                    times[5] =ss[0];
                    /*String[] times = new String[5];
                    times[0] = prayerTimes.getData().get(0).getTimings().Fajr;
                    times[1] = prayerTimes.getData().get(0).getTimings().Dhuhr;
                    times[2] = prayerTimes.getData().get(0).getTimings().Asr;
                    times[3] = prayerTimes.getData().get(0).getTimings().Maghrib;
                    times[4] = prayerTimes.getData().get(0).getTimings().Isha;*/
                    //dataRecieved.onSuccess(times);

                    MySharedPreferences.setUpMySharedPreferences(context, "extraSetting");
                    MySharedPreferences.setAzanState("1");
                    MySharedPreferences.setUserSetting("Fajr",times[0]);
                    MySharedPreferences.setUserSetting("Dhuhr", times[1]);
                    MySharedPreferences.setUserSetting("Asr", times[2]);
                    MySharedPreferences.setUserSetting("Maghrib", times[3]);
                    MySharedPreferences.setUserSetting("Isha", times[4]);
                    /*MySharedPreferences.setUpMySharedPreferences(context, "extraSetting");
                    MySharedPreferences.setAzanState("1");
                    MySharedPreferences.setUserSetting("Fajr", prayerTimes.getData().get(0).getTimings().Fajr);
                    MySharedPreferences.setUserSetting("Dhuhr", prayerTimes.getData().get(0).getTimings().Dhuhr);
                    MySharedPreferences.setUserSetting("Asr", prayerTimes.getData().get(0).getTimings().Asr);
                    MySharedPreferences.setUserSetting("Maghrib", prayerTimes.getData().get(0).getTimings().Maghrib);
                    MySharedPreferences.setUserSetting("Isha", prayerTimes.getData().get(0).getTimings().Isha);*/

                    //times[4]="19:30";
                    for (int ii = 0; ii < times.length; ii++) {
                        String[] str = times[ii].split(":");
                        alarmManagerUtils.startAzanService(Integer.valueOf(str[0]), Integer.valueOf(str[1]), ii + 8000);
                    }
                }

                @Override
                public void OnFailure(boolean Failure) {

                }
            });
            fetchAzanData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } else if (MySharedPreferences.getUserSetting("Fajr").contains(":")) {
            String s = " الفجر : " + MySharedPreferences.getUserSetting("Fajr") + "\n"
                    + " الظهر : " + MySharedPreferences.getUserSetting("Dhuhr") + "\n"
                    + " العصر : " + MySharedPreferences.getUserSetting("Asr") + "\n"
                    + " المغرب : " + MySharedPreferences.getUserSetting("Maghrib") + "\n"
                    + " االعشاء :" + MySharedPreferences.getUserSetting("Isha") + "\n";
            String[] times = new String[5];
            times[0] = MySharedPreferences.getUserSetting("Fajr");
            times[1] = MySharedPreferences.getUserSetting("Dhuhr");
            times[2] = MySharedPreferences.getUserSetting("Asr");
            times[3] = MySharedPreferences.getUserSetting("Maghrib");
            times[4] = MySharedPreferences.getUserSetting("Isha");

            //dataRecieved.onSuccess(times);

            for (int i = 0; i < times.length; i++) {
                String[] str = times[i].split(":");
                alarmManagerUtils.startAzanService(Integer.valueOf(str[0]), Integer.valueOf(str[1]), i + 8000);
            }
                    /* MySharedPreferences.getUserSetting("Fajr");
                       MySharedPreferences.getUserSetting("Dhuhr");
                       MySharedPreferences.getUserSetting("Asr");
                       MySharedPreferences.getUserSetting("Maghrib");
                       MySharedPreferences.getUserSetting("Isha");*/
        }
    }

    private void azkarAmMethod(){
        String azkar_am=MySharedPreferences.getUserSetting("am_alarm");
        String[] strs = azkar_am.split(":");
        int h = Integer.valueOf(strs[0]);
        int m = Integer.valueOf(strs[1]);
        alarmManagerUtils.startAzkarService(h, m, 9911,0);
    }

    private void azkarPmMethod(){
        String azkar_pm = MySharedPreferences.getUserSetting("pm_alarm");
        String[] strs = azkar_pm.split(":");
        int h = Integer.valueOf(strs[0]);
        int m = Integer.valueOf(strs[1]);
        alarmManagerUtils.startAzkarService(h, m, 1199, 1);
    }

    private void alarmMethod(){
        String alarmNum = MySharedPreferences.getData();
        String alarmName ="alarm";
        int NumOfAlarm = Integer.valueOf(alarmNum);
        for (int i = 0; i < NumOfAlarm; i++) {
            String s = MySharedPreferences.getUserSetting(alarmName + i);
            if (!s.equals("0:0")) {
                String[] strs = s.split(":");
                int h = Integer.valueOf(strs[0]);
                int m = Integer.valueOf(strs[1]);
                alarmManagerUtils.startHeadService(h, m, i);
            }
        }
    }


}