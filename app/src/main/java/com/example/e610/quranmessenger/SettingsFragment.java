package com.example.e610.quranmessenger;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.example.e610.quranmessenger.Models.PrayerTimes.PrayerTimes;
import com.example.e610.quranmessenger.Services.AzanService;
import com.example.e610.quranmessenger.Services.HeadService;
import com.example.e610.quranmessenger.Utils.FetchAzanData;
import com.example.e610.quranmessenger.Utils.MySharedPreferences;
import com.example.e610.quranmessenger.Utils.NetworkResponse;
import com.example.e610.quranmessenger.Utils.NetworkState;
import com.example.e610.quranmessenger.Utils.PermissionChecker;
import com.example.e610.quranmessenger.Utils.TimePreference;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Standard settings screen.
 * It allows to enable or disable the head service.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final static String SERVICE_ENABLED_KEY = "serviceEnabledKey";
    final String alarmName = "alarm";
    Context ctx;
    HashMap<Integer, PendingIntent> pendingIntentList = new HashMap<>();
    AlarmManager alarmManager;
    HashMap<Integer, PendingIntent> pendingIntentAzanList = new HashMap<>();
    private PermissionChecker mPermissionChecker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        addPreferencesFromResource(R.xml.settings);
        enableHeadServiceCheckbox(false);
        mPermissionChecker = new PermissionChecker(getActivity());
        if (!mPermissionChecker.isRequiredPermissionGranted()) {
            enableHeadServiceCheckbox(false);
            Intent intent = mPermissionChecker.createRequiredPermissionIntent();
            startActivityForResult(intent, PermissionChecker.REQUIRED_PERMISSION_REQUEST_CODE);
        } else {
            enableHeadServiceCheckbox(true);
        }

        MySharedPreferences.setUpMySharedPreferences(getActivity(), "extraSetting");
        String alarmNum = MySharedPreferences.getData();
        int NumOfAlarm = Integer.valueOf(alarmNum);
        for (int i = 0; i < NumOfAlarm; i++) {
            String alarm = alarmName + i + "";

            TimePreference timePreference = new TimePreference(getActivity(), null);
            timePreference.setKey(alarm);
            timePreference.setTitle(getString(R.string.alarm_time) + " " + i);
            String strValue=MySharedPreferences.getUserSetting(alarm);
            timePreference.setDefaultValue("12:44");
            timePreference.setSummary(strValue);
            getPreferenceScreen().addPreference(timePreference);
            timePreference.setDependency(SERVICE_ENABLED_KEY);
        }


          if (MySharedPreferences.getUserSetting("Fajr").contains(":")) {
            String s = " الفجر : " + MySharedPreferences.getUserSetting("Fajr") + "\n"
                    + " الظهر : " + MySharedPreferences.getUserSetting("Dhuhr") + "\n"
                    + " العصر : " + MySharedPreferences.getUserSetting("Asr") + "\n"
                    + " المغرب : " + MySharedPreferences.getUserSetting("Maghrib") + "\n"
                    + " العشاء " + MySharedPreferences.getUserSetting("Isha") + "\n";
            Preference preference = findPreference("azan");
            preference.setSummary(s);
           /* MySharedPreferences.getUserSetting("Fajr");
            MySharedPreferences.getUserSetting("Dhuhr");
            MySharedPreferences.getUserSetting("Asr");
            MySharedPreferences.getUserSetting("Maghrib");
            MySharedPreferences.getUserSetting("Isha");*/
        }else if (NetworkState.ConnectionAvailable(getActivity())) {
            FetchAzanData fetchAzanData = new FetchAzanData();
            fetchAzanData.setNetworkResponse(new NetworkResponse() {
                @Override
                public void OnSuccess(String JsonData) {
                    Gson gson = new Gson();
                    PrayerTimes prayerTimes = gson.fromJson(JsonData, PrayerTimes.class);
                    String s = " الفجر : " + prayerTimes.getData().getTimings().Fajr + "\n"
                            + " الظهر : " + prayerTimes.getData().getTimings().Dhuhr + "\n"
                            + " العصر : " + prayerTimes.getData().getTimings().Asr + "\n"
                            + " المغرب : " + prayerTimes.getData().getTimings().Maghrib + "\n"
                            + " االعشاء :" + prayerTimes.getData().getTimings().Isha + "\n";
                    Preference preference = findPreference("azan");
                    preference.setSummary(s);
                    MySharedPreferences.setUpMySharedPreferences(getActivity(), "extraSetting");
                    MySharedPreferences.setUserSetting("Fajr", prayerTimes.getData().getTimings().Fajr);
                    MySharedPreferences.setUserSetting("Dhuhr", prayerTimes.getData().getTimings().Dhuhr);
                    MySharedPreferences.setUserSetting("Asr", prayerTimes.getData().getTimings().Asr);
                    MySharedPreferences.setUserSetting("Maghrib", prayerTimes.getData().getTimings().Maghrib);
                    MySharedPreferences.setUserSetting("Isha", prayerTimes.getData().getTimings().Isha);
                }

                @Override
                public void OnFailure(boolean Failure) {

                }
            });
        }  else
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();

        Preference preference = findPreference("shekh");
        String shekhName=MySharedPreferences.getUserSetting("shekhName");
        preference.setSummary(shekhName);

        String aNum = MySharedPreferences.getData();
        Preference Pref = findPreference("alarm_numbers");
        Pref.setSummary(aNum);

    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PermissionChecker.REQUIRED_PERMISSION_REQUEST_CODE) {
            if (!mPermissionChecker.isRequiredPermissionGranted()) {
                Toast.makeText(getActivity(), "Required permission is not granted. Please restart the app and grant required permission.", Toast.LENGTH_LONG).show();
            } else {
                enableHeadServiceCheckbox(true);
            }
        }
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

    //boolean enabled;
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
       /* String s=sharedPreferences.getString("alarm_time","0:0");
        String [] strs=s.split(":");
        int h=Integer.valueOf(strs[0]);
        int m=Integer.valueOf(strs[1]);*/
        //Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
        boolean enabled = sharedPreferences.getBoolean(SERVICE_ENABLED_KEY, false);

/*public setAlarm(String time, Context context){
        String[] strTime;

        strTime = time.split(":");

        int hour, min, sec;
        //set when to alarm
        hour = Integer.valueOf(strTime[0]);
        min = Integer.valueOf(strTime[1]);
        sec = 0;

        long _alarm = 0;
        Calendar now = Calendar.getInstance();
        Calendar alarm = Calendar.getInstance();
        alarm.set(Calendar.HOUR_OF_DAY, hour);
        alarm.set(Calendar.MINUTE, min);
        alarm.set(Calendar.SECOND, sec);

        if(alarm.getTimeInMillis() <= now.getTimeInMillis())
            _alarm = alarm.getTimeInMillis() + (AlarmManager.INTERVAL_DAY+1);
        else
            _alarm = alarm.getTimeInMillis();

        //Create a new PendingIntent and add it to the AlarmManager
        Intent intent = new Intent(context, AlarmReceiverActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 19248, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, _alarm, AlarmManager.INTERVAL_DAY, pendingIntent);
    }*/

        if (SERVICE_ENABLED_KEY.equals(key)) {
            if (enabled) {
                String alarmNum = sharedPreferences.getString("alarm_numbers", "0");
                int NumOfAlarm = Integer.valueOf(alarmNum);
                for (int i = 0; i < NumOfAlarm; i++) {
                    String s = sharedPreferences.getString(alarmName + i + "", "0:0");
                    if (!s.equals("0:0")) {
                        String[] strs = s.split(":");
                        int h = Integer.valueOf(strs[0]);
                        int m = Integer.valueOf(strs[1]);
                        startHeadService(h, m, i);
                    }
                }
            } else {
                stopHeadService();
            }
        } else if (key.equals("alarm_numbers")) {
            MySharedPreferences.setUpMySharedPreferences(getActivity(), "extraSetting");
            String alarmNum = MySharedPreferences.getData();
            String aNum=sharedPreferences.getString(key,"");
            Preference Pref = findPreference(key);
            Pref.setSummary(aNum);

            int NumOfAlarm = Integer.valueOf(alarmNum);
            for (int i = 0; i < NumOfAlarm; i++) {
                String pAlarm = alarmName + i + "";
                Preference timePref = findPreference(pAlarm);
                if (timePref != null)
                    getPreferenceScreen().removePreference(timePref);
            }

            canselAlarms();

            alarmNum = sharedPreferences.getString("alarm_numbers", "0");
            MySharedPreferences.saveData(alarmNum);
            NumOfAlarm = Integer.valueOf(alarmNum);
            for (int i = 0; i < NumOfAlarm; i++) {
                String alarm = alarmName + i + "";

                TimePreference timePreference = new TimePreference(getActivity(), null);
                timePreference.setKey(alarm);
                timePreference.setTitle(getString(R.string.alarm_time) + " " + i + "");
                timePreference.setDefaultValue("12:44");
                timePreference.setSummary(getString(R.string.alarm_time_summary));
                getPreferenceScreen().addPreference(timePreference);
                timePreference.setDependency(SERVICE_ENABLED_KEY);
            }

        } else if (key.startsWith("alarm")) {
            if (enabled) {
                MySharedPreferences.setUpMySharedPreferences(getActivity(), "extraSetting");
                String alarmNum = MySharedPreferences.getData();
                int NumOfAlarm = Integer.valueOf(alarmNum);
                for (int i = 0; i < NumOfAlarm; i++) {
                    String pAlarm = alarmName + i + "";
                    if (key.equals(pAlarm)) {
                        String ss = sharedPreferences.getString(pAlarm, "0:0");
                        MySharedPreferences.setUserSetting(pAlarm,ss);
                        if (!ss.equals("0:0")) {
                            Preference preference = findPreference(key);
                            preference.setSummary(ss);
                            String[] strs = ss.split(":");
                            int h = Integer.valueOf(strs[0]);
                            int m = Integer.valueOf(strs[1]);
                            startHeadService(h, m, 6000 + i);
                        }
                    }
                }

            } else {
                stopHeadService();
            }

        } else if (key.equals("azan")) {
            ctx = getActivity();
            boolean azan = sharedPreferences.getBoolean("azan", false);
            MySharedPreferences.setUpMySharedPreferences(getActivity(), "extraSetting");
            if (azan) {
                if (MySharedPreferences.getUserSetting("Fajr").contains(":")) {
                    String s = " الفجر : " + MySharedPreferences.getUserSetting("Fajr") + "\n"
                            + " الظهر : " + MySharedPreferences.getUserSetting("Dhuhr") + "\n"
                            + " العصر : " + MySharedPreferences.getUserSetting("Asr") + "\n"
                            + " المغرب : " + MySharedPreferences.getUserSetting("Maghrib") + "\n"
                            + " االعشاء :" + MySharedPreferences.getUserSetting("Isha") + "\n";
                    Preference preference = findPreference("azan");
                    preference.setSummary(s);
                    String[] times = new String[5];
                    times[0] = MySharedPreferences.getUserSetting("Fajr");
                    times[1] = MySharedPreferences.getUserSetting("Dhuhr");
                    times[2] = MySharedPreferences.getUserSetting("Asr");
                    times[3] = MySharedPreferences.getUserSetting("Maghrib");
                    times[4] = MySharedPreferences.getUserSetting("Isha");
                    for (int i = 0; i < times.length; i++) {
                        String[] str = times[i].split(":");
                        startAzanService(Integer.valueOf(str[0]), Integer.valueOf(str[1]), i + 8000);
                    }
                    /* MySharedPreferences.getUserSetting("Fajr");
                       MySharedPreferences.getUserSetting("Dhuhr");
                       MySharedPreferences.getUserSetting("Asr");
                       MySharedPreferences.getUserSetting("Maghrib");
                       MySharedPreferences.getUserSetting("Isha");*/
                }
                else if (NetworkState.ConnectionAvailable(ctx)) {
                    FetchAzanData fetchAzanData = new FetchAzanData();
                    fetchAzanData.setNetworkResponse(new NetworkResponse() {
                        @Override
                        public void OnSuccess(String JsonData) {
                            Gson gson = new Gson();
                            PrayerTimes prayerTimes = gson.fromJson(JsonData, PrayerTimes.class);
                            String s = " الفجر : " + prayerTimes.getData().getTimings().Fajr + "\n"
                                    + " الظهر : " + prayerTimes.getData().getTimings().Dhuhr + "\n"
                                    + " العصر : " + prayerTimes.getData().getTimings().Asr + "\n"
                                    + " المغرب : " + prayerTimes.getData().getTimings().Maghrib + "\n"
                                    + " االعشاء :" + prayerTimes.getData().getTimings().Isha + "\n";
                            Preference preference = findPreference("azan");
                            preference.setSummary(s);
                            String[] times = new String[5];
                            times[0] = prayerTimes.getData().getTimings().Fajr;
                            times[1] = prayerTimes.getData().getTimings().Dhuhr;
                            times[2] = prayerTimes.getData().getTimings().Asr;
                            times[3] = prayerTimes.getData().getTimings().Maghrib;
                            times[4] = prayerTimes.getData().getTimings().Isha;

                            MySharedPreferences.setUpMySharedPreferences(getActivity(), "extraSetting");
                            MySharedPreferences.setUserSetting("Fajr", prayerTimes.getData().getTimings().Fajr);
                            MySharedPreferences.setUserSetting("Dhuhr", prayerTimes.getData().getTimings().Dhuhr);
                            MySharedPreferences.setUserSetting("Asr", prayerTimes.getData().getTimings().Asr);
                            MySharedPreferences.setUserSetting("Maghrib", prayerTimes.getData().getTimings().Maghrib);
                            MySharedPreferences.setUserSetting("Isha", prayerTimes.getData().getTimings().Isha);

                            //times[4]="19:30";
                            for (int i = 0; i < times.length; i++) {
                                String[] str = times[i].split(":");
                                startAzanService(Integer.valueOf(str[0]), Integer.valueOf(str[1]), i + 8000);
                            }
                        }

                        @Override
                        public void OnFailure(boolean Failure) {

                        }
                    });
                    fetchAzanData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();

            } else {
                Preference preference = findPreference("azan");
                preference.setSummary("");
                canselAzanAlarms();
            }
        } else if (key.equals("shekh")) {
            MySharedPreferences.setUpMySharedPreferences(getActivity(), "extraSetting");
            String shekhName = sharedPreferences.getString("shekh", "");
            Preference preference = findPreference("shekh");
            preference.setSummary(shekhName);
            MySharedPreferences.setUserSetting("shekhName", shekhName);
            Toast.makeText(getActivity(), shekhName, Toast.LENGTH_LONG).show();
        }

    }

    private void enableHeadServiceCheckbox(boolean enabled) {
        getPreferenceScreen().findPreference(SERVICE_ENABLED_KEY).setEnabled(enabled);
    }

    private void startHeadService(int h, int m, int id) {
        Intent intent = new Intent(getActivity(), HeadService.class);
        PendingIntent pendingIntent = PendingIntent.getService(getActivity(), id, intent, 0);
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

        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, _alarm, AlarmManager.INTERVAL_DAY, pendingIntent);
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,_alarm,2*60*1000,pendingIntent);
        /*Context context = getActivity();
        context.startService(new Intent(context, HeadService.class));*/
    }

    private void stopHeadService() {
        Context context = getActivity();
        context.stopService(new Intent(context, HeadService.class));
        canselAlarms();
    }

    private void canselAlarms() {
        MySharedPreferences.setUpMySharedPreferences(getActivity(), "extraSetting");
        String alarmNumbers=MySharedPreferences.getData();
        int alarmNum=Integer.valueOf(alarmNumbers);
        if(pendingIntentList!=null&&pendingIntentList.size()==alarmNum){
        }else if(pendingIntentList==null||pendingIntentList.size()!=alarmNum){
            if (alarmManager != null) {
                for (int i = 0; i < pendingIntentList.size(); i++) {
                    alarmManager.cancel(pendingIntentList.get(i+6000));
                }
                //pendingIntentList.clear();
            }
            pendingIntentList=new HashMap<>();
            for (int i = 0; i <alarmNum ; i++) {
                Intent intent = new Intent(getActivity(), HeadService.class);
                PendingIntent pendingIntent = PendingIntent.getService(getActivity(), 6000+i, intent, 0);
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
                    alarmManager.cancel(pendingIntentAzanList.get(i+8000));
                }
                //pendingIntentList.clear();
            }
        } else if (pendingIntentAzanList == null || pendingIntentAzanList.size() != 5) {
            pendingIntentAzanList = new HashMap<>();
            for (int i = 0; i < 5; i++) {
                Intent intent = new Intent(getActivity(), AzanService.class);
                PendingIntent pendingIntent = PendingIntent.getService(getActivity(), i + 8000, intent, 0);
                pendingIntentAzanList.put(i+8000, pendingIntent);
            }
            if (alarmManager != null) {
                for (int i = 0; i < pendingIntentAzanList.size(); i++) {
                    alarmManager.cancel(pendingIntentAzanList.get(i+8000));
                }
                //pendingIntentList.clear();
            }
        }
    }
}
