package com.example.e610.quranmessenger.Fragments;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.example.e610.quranmessenger.Models.PrayerTimes.PrayerTimes;
import com.example.e610.quranmessenger.R;
import com.example.e610.quranmessenger.Services.AzanService;
import com.example.e610.quranmessenger.Utils.FetchAzanData;
import com.example.e610.quranmessenger.Utils.MySharedPreferences;
import com.example.e610.quranmessenger.Utils.NetworkResponse;
import com.example.e610.quranmessenger.Utils.NetworkState;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.HashMap;

public class SettingsAzanFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Activity ctx;

    public interface IDataRecieved {
        void onSuccess(String[] times);
    }

    IDataRecieved dataRecieved;

    public SettingsAzanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //dataRecieved=(IDataRecieved)activity;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.azan_setting);
        azanMethod();
        // Inflate the layout for this fragment
    }

    private void azanMethod() {
        MySharedPreferences.setUpMySharedPreferences(getActivity(), "extraSetting");
        if (MySharedPreferences.getUserSetting("Fajr").contains(":")) {
            String s = " الفجر : " + MySharedPreferences.getUserSetting("Fajr") + "\n"
                    + " الظهر : " + MySharedPreferences.getUserSetting("Dhuhr") + "\n"
                    + " العصر : " + MySharedPreferences.getUserSetting("Asr") + "\n"
                    + " المغرب : " + MySharedPreferences.getUserSetting("Maghrib") + "\n"
                    + " العشاء " + MySharedPreferences.getUserSetting("Isha") + "\n";

            /*dataRecieved.onSuccess(new String[]{MySharedPreferences.getUserSetting("Fajr"),MySharedPreferences.getUserSetting("Dhuhr")
                    ,MySharedPreferences.getUserSetting("Asr"),MySharedPreferences.getUserSetting("Maghrib"),MySharedPreferences.getUserSetti
                    ("Isha")});*/

            Preference preference = findPreference("azan");
            //preference.setSummary(s);
           /* MySharedPreferences.getUserSetting("Fajr");
            MySharedPreferences.getUserSetting("Dhuhr");
            MySharedPreferences.getUserSetting("Asr");
            MySharedPreferences.getUserSetting("Maghrib");
            MySharedPreferences.getUserSetting("Isha");*/
        } else if (NetworkState.ConnectionAvailable(getActivity())) {
            FetchAzanData fetchAzanData = new FetchAzanData(getActivity());
            fetchAzanData.setNetworkResponse(new NetworkResponse() {
                @Override
                public void OnSuccess(String JsonData) {
                    try {
                        Gson gson = new Gson();
                        PrayerTimes prayerTimes = gson.fromJson(JsonData, PrayerTimes.class);
                        String s = " الفجر : " + prayerTimes.getData().get(0).getTimings().Fajr + "\n"
                                + " الظهر : " + prayerTimes.getData().get(0).getTimings().Dhuhr + "\n"
                                + " العصر : " + prayerTimes.getData().get(0).getTimings().Asr + "\n"
                                + " المغرب : " + prayerTimes.getData().get(0).getTimings().Maghrib + "\n"
                                + " االعشاء :" + prayerTimes.getData().get(0).getTimings().Isha + "\n";

                    /*dataRecieved.onSuccess(new String[]{prayerTimes.getData().getTimings().Fajr,
                            prayerTimes.getData().getTimings().Dhuhr,prayerTimes.getData().getTimings().Asr,
                            prayerTimes.getData().getTimings().Maghrib,prayerTimes.getData().getTimings().Isha });*/

                        Preference preference = findPreference("azan");
                        //preference.setSummary(s);

                        Calendar calendar = Calendar.getInstance();
                        int i = calendar.get(Calendar.DAY_OF_MONTH);

                        String[] times = new String[6];
                        String[] ss = new String[2];

                        ss = prayerTimes.getData().get(i).getTimings().Fajr.split(" ");
                        // times[0] = prayerTimes.getData().get(0).getTimings().Fajr;
                        times[0] = ss[0];

                        ss = prayerTimes.getData().get(i).getTimings().Dhuhr.split(" ");
                        //times[1] = prayerTimes.getData().get(0).getTimings().Dhuhr;
                        times[1] = ss[0];

                        //times[2] = prayerTimes.getData().get(0).getTimings().Asr;
                        ss = prayerTimes.getData().get(i).getTimings().Asr.split(" ");
                        times[2] = ss[0];

                        //times[3] = prayerTimes.getData().get(0).getTimings().Maghrib;
                        ss = prayerTimes.getData().get(i).getTimings().Maghrib.split(" ");
                        times[3] = ss[0];

                        //times[4] = prayerTimes.getData().get(0).getTimings().Isha;
                        ss = prayerTimes.getData().get(i).getTimings().Isha.split(" ");
                        times[4] = ss[0];

                        //times[5] = prayerTimes.getData().get(0).getTimings().Sunrise;
                        ss = prayerTimes.getData().get(i).getTimings().Sunrise.split(" ");
                        times[5] = ss[0];
                    /*String[] times = new String[5];
                    times[0] = prayerTimes.getData().get(0).getTimings().Fajr;
                    times[1] = prayerTimes.getData().get(0).getTimings().Dhuhr;
                    times[2] = prayerTimes.getData().get(0).getTimings().Asr;
                    times[3] = prayerTimes.getData().get(0).getTimings().Maghrib;
                    times[4] = prayerTimes.getData().get(0).getTimings().Isha;*/
                        //dataRecieved.onSuccess(times);

                        MySharedPreferences.setUpMySharedPreferences(getActivity(), "extraSetting");
                        MySharedPreferences.setAzanState("1");
                        MySharedPreferences.setUserSetting("Fajr", times[0]);
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
                    } catch (Exception e) {
                        String errorMsg="لقد حدث خطاء... الرجاء قم بتشغل ال GPS";
                        Toast.makeText(getActivity(),errorMsg,Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void OnFailure(boolean Failure) {

                }
            });
        } else
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();

        String name = MySharedPreferences.getUserSetting("azan_voice");
        String s = "";
        if (name.equals("azan0"))
            s = "الصوت الاول";
        else if (name.equals("azan1"))
            s = "الصوت الثانى";
        Preference preference = findPreference("azan_voice");
        preference.setSummary(s);
    }


    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        MySharedPreferences.setUpMySharedPreferences(getActivity(), "extraSetting");
        if (key.equals("azan")) {
            ctx = getActivity();
            boolean azan = sharedPreferences.getBoolean("azan", false);

            if (azan) {
                if (NetworkState.ConnectionAvailable(ctx)) {
                    FetchAzanData fetchAzanData = new FetchAzanData(getActivity());
                    fetchAzanData.setNetworkResponse(new NetworkResponse() {
                        @Override
                        public void OnSuccess(String JsonData) {
                            try {

                                Gson gson = new Gson();
                                PrayerTimes prayerTimes = gson.fromJson(JsonData, PrayerTimes.class);
                                String s = " الفجر : " + prayerTimes.getData().get(0).getTimings().Fajr + "\n"
                                        + " الظهر : " + prayerTimes.getData().get(0).getTimings().Dhuhr + "\n"
                                        + " العصر : " + prayerTimes.getData().get(0).getTimings().Asr + "\n"
                                        + " المغرب : " + prayerTimes.getData().get(0).getTimings().Maghrib + "\n"
                                        + " االعشاء :" + prayerTimes.getData().get(0).getTimings().Isha + "\n";
                                Preference preference = findPreference("azan");
                                //preference.setSummary(s);

                                Calendar calendar = Calendar.getInstance();
                                int i = calendar.get(Calendar.DAY_OF_MONTH);

                                String[] times = new String[6];
                                String[] ss = new String[2];

                                ss = prayerTimes.getData().get(i).getTimings().Fajr.split(" ");
                                // times[0] = prayerTimes.getData().get(0).getTimings().Fajr;
                                times[0] = ss[0];

                                ss = prayerTimes.getData().get(i).getTimings().Dhuhr.split(" ");
                                //times[1] = prayerTimes.getData().get(0).getTimings().Dhuhr;
                                times[1] = ss[0];

                                //times[2] = prayerTimes.getData().get(0).getTimings().Asr;
                                ss = prayerTimes.getData().get(i).getTimings().Asr.split(" ");
                                times[2] = ss[0];

                                //times[3] = prayerTimes.getData().get(0).getTimings().Maghrib;
                                ss = prayerTimes.getData().get(i).getTimings().Maghrib.split(" ");
                                times[3] = ss[0];

                                //times[4] = prayerTimes.getData().get(0).getTimings().Isha;
                                ss = prayerTimes.getData().get(i).getTimings().Isha.split(" ");
                                times[4] = ss[0];

                                //times[5] = prayerTimes.getData().get(0).getTimings().Sunrise;
                                ss = prayerTimes.getData().get(i).getTimings().Sunrise.split(" ");
                                times[5] = ss[0];
                    /*String[] times = new String[5];
                    times[0] = prayerTimes.getData().get(0).getTimings().Fajr;
                    times[1] = prayerTimes.getData().get(0).getTimings().Dhuhr;
                    times[2] = prayerTimes.getData().get(0).getTimings().Asr;
                    times[3] = prayerTimes.getData().get(0).getTimings().Maghrib;
                    times[4] = prayerTimes.getData().get(0).getTimings().Isha;*/
                                //dataRecieved.onSuccess(times);

                                MySharedPreferences.setUpMySharedPreferences(getActivity(), "extraSetting");
                                MySharedPreferences.setAzanState("1");
                                MySharedPreferences.setUserSetting("Fajr", times[0]);
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
                                for (int ii = 0; ii < times.length-1; ii++) {
                                    String[] str = times[ii].split(":");
                                    startAzanService(Integer.valueOf(str[0]), Integer.valueOf(str[1]), ii + 8000);
                                }
                            } catch (Exception e) {
                                String errorMsg="لقد حدث خطاء... الرجاء قم بتشغل ال GPS";
                                Toast.makeText(getActivity(),errorMsg,Toast.LENGTH_LONG).show();
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
                    Preference preference = findPreference("azan");
                    //preference.setSummary(s);
                    String[] times = new String[5];
                    times[0] = MySharedPreferences.getUserSetting("Fajr");
                    times[1] = MySharedPreferences.getUserSetting("Dhuhr");
                    times[2] = MySharedPreferences.getUserSetting("Asr");
                    times[3] = MySharedPreferences.getUserSetting("Maghrib");
                    times[4] = MySharedPreferences.getUserSetting("Isha");

                    //dataRecieved.onSuccess(times);

                    for (int i = 0; i < times.length; i++) {
                        String[] str = times[i].split(":");
                        startAzanService(Integer.valueOf(str[0]), Integer.valueOf(str[1]), i + 8000);
                    }
                    /* MySharedPreferences.getUserSetting("Fajr");
                       MySharedPreferences.getUserSetting("Dhuhr");
                       MySharedPreferences.getUserSetting("Asr");
                       MySharedPreferences.getUserSetting("Maghrib");
                       MySharedPreferences.getUserSetting("Isha");*/
                } else
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
            } else {
                MySharedPreferences.setUpMySharedPreferences(getActivity(), getActivity().getResources().getString(R.string.shared_pref_file_name));
                MySharedPreferences.setAzanState("-1");
                Preference preference = findPreference("azan");
                //preference.setSummary("");
                canselAzanAlarms();
            }
        } else if (key.equals("azan_voice")) {
            String s = "";
            String name = sharedPreferences.getString("azan_voice", "");
            MySharedPreferences.setUserSetting("azan_voice", name);
            if (name.equals("azan0"))
                s = "الصوت الاول";
            else if (name.equals("azan1"))
                s = "الصوت الثانى";
            Preference preference = findPreference("azan_voice");
            preference.setSummary(s);
            //write a rest of code here

        }
    }


    HashMap<Integer, PendingIntent> pendingIntentAzanList = new HashMap<>();
    AlarmManager alarmManager;

    private void startAzanService(int h, int m, int id) {
        Intent intent = new Intent(getActivity(), AzanService.class);
        PendingIntent pendingIntent = PendingIntent.getService(getActivity(), id, intent, 0);
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

        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, _alarm, AlarmManager.INTERVAL_DAY, pendingIntent);
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,_alarm,2*60*1000,pendingIntent);
        /*Context context = getActivity();
        context.startService(new Intent(context, HeadService.class));*/
    }

    private void canselAzanAlarms() {
        getActivity().stopService(new Intent(getActivity(), AzanService.class));
        if (pendingIntentAzanList != null && pendingIntentAzanList.size() == 5) {
            if (alarmManager != null) {
                for (int i = 0; i < pendingIntentAzanList.size(); i++) {
                    alarmManager.cancel(pendingIntentAzanList.get(i + 8000));
                }
                //pendingIntentList.clear();
            }
        } else if (pendingIntentAzanList == null || pendingIntentAzanList.size() != 5) {
            pendingIntentAzanList = new HashMap<>();
            for (int i = 0; i < 5; i++) {
                Intent intent = new Intent(getActivity(), AzanService.class);
                PendingIntent pendingIntent = PendingIntent.getService(getActivity(), i + 8000, intent, 0);
                pendingIntentAzanList.put(i + 8000, pendingIntent);
            }
            if (alarmManager != null) {
                for (int i = 0; i < pendingIntentAzanList.size(); i++) {
                    alarmManager.cancel(pendingIntentAzanList.get(i + 8000));
                }
                //pendingIntentList.clear();
            }
        }
    }
}