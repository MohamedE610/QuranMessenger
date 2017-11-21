package com.example.e610.quranmessenger;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Standard settings screen.
 * It allows to enable or disable the head service.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final static String SERVICE_ENABLED_KEY = "serviceEnabledKey";

    private PermissionChecker mPermissionChecker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        addPreferencesFromResource(R.xml.settings);
        enableHeadServiceCheckbox(false);
        mPermissionChecker = new PermissionChecker(getActivity());
        if(!mPermissionChecker.isRequiredPermissionGranted()){
            enableHeadServiceCheckbox(false);
            Intent intent = mPermissionChecker.createRequiredPermissionIntent();
            startActivityForResult(intent, PermissionChecker.REQUIRED_PERMISSION_REQUEST_CODE);
        } else {
            enableHeadServiceCheckbox(true);
        }

        MySharedPreferences.setUpMySharedPreferences(getActivity(),"extraSetting");
        String alarmNum=MySharedPreferences.getData();
        int NumOfAlarm=Integer.valueOf(alarmNum);
        for (int i = 0; i <NumOfAlarm ; i++) {
            String alarm=alarmName+i+"";

            TimePreference timePreference=new TimePreference(getActivity(),null);
            timePreference.setKey(alarm);
            timePreference.setTitle(getString(R.string.alarm_time));
            timePreference.setDefaultValue("12:44");
            timePreference.setSummary(getString(R.string.alarm_time_summary));
            getPreferenceScreen().addPreference(timePreference);
            timePreference.setDependency(SERVICE_ENABLED_KEY);
        }
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



    final String alarmName="alarm";
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

        if(SERVICE_ENABLED_KEY.equals(key)) {
            if(enabled) {
               /* String alarmNum=sharedPreferences.getString("alarm_numbers","0");
                int NumOfAlarm=Integer.valueOf(alarmNum);
                for (int i = 0; i <NumOfAlarm ; i++) {
                    String s = sharedPreferences.getString(alarmName+i+"", "0:0");
                    if(!s.equals("0:0")) {
                        String[] strs = s.split(":");
                        int h = Integer.valueOf(strs[0]);
                        int m = Integer.valueOf(strs[1]);
                        startHeadService(h, m);
                    }
                }*/
            } else {
                stopHeadService();
            }
        }
        else if(key.equals("alarm_numbers")){
            MySharedPreferences.setUpMySharedPreferences(getActivity(),"extraSetting");
            String alarmNum=MySharedPreferences.getData();
            int NumOfAlarm=Integer.valueOf(alarmNum);
            for (int i = 0; i <NumOfAlarm ; i++) {
                String pAlarm=alarmName+i+"";
                Preference timePref=findPreference(pAlarm);
                if(timePref!=null)
                    getPreferenceScreen().removePreference(timePref);
            }
            if (alarmManager!= null) {
                alarmManager.cancel(pendingIntent);
                if (pendingIntent!= null)
                    pendingIntent.cancel();
            }
            alarmNum=sharedPreferences.getString("alarm_numbers","0");
            MySharedPreferences.saveData(alarmNum);
            NumOfAlarm=Integer.valueOf(alarmNum);
            for (int i = 0; i <NumOfAlarm; i++) {
                String alarm=alarmName+i+"";

                TimePreference timePreference=new TimePreference(getActivity(),null);
                timePreference.setKey(alarm);
                timePreference.setTitle(getString(R.string.alarm_time)+" "+i+"");
                timePreference.setDefaultValue("12:44");
                timePreference.setSummary(getString(R.string.alarm_time_summary));
                getPreferenceScreen().addPreference(timePreference);
                timePreference.setDependency(SERVICE_ENABLED_KEY);
            }

        }
        else if(key.startsWith("alarm")){
            if(enabled) {

                MySharedPreferences.setUpMySharedPreferences(getActivity(),"extraSetting");
                String alarmNum=MySharedPreferences.getData();
                int NumOfAlarm=Integer.valueOf(alarmNum);
                for (int i = 0; i <NumOfAlarm ; i++) {
                    String pAlarm=alarmName+i+"";
                    String ss=sharedPreferences.getString(pAlarm,"0:0");
                    if (!ss.equals("0:0")) {
                        String[] strs = ss.split(":");
                        int h = Integer.valueOf(strs[0]);
                        int m = Integer.valueOf(strs[1]);
                        startHeadService(h, m);
                    }
                }

            } else {
                stopHeadService();
            }

        }

        else if(key.startsWith("asd")){
            Toast.makeText(getActivity(),"asd",Toast.LENGTH_LONG).show();
        }

    }

    private void enableHeadServiceCheckbox(boolean enabled) {
        getPreferenceScreen().findPreference(SERVICE_ENABLED_KEY).setEnabled(enabled);
    }

    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    private void startHeadService(int h, int m) {
        Intent intent=new Intent(getActivity(),HeadService.class);
         pendingIntent=PendingIntent.getService(getActivity(),0,intent,0);

        Calendar calendar=Calendar.getInstance();
        //calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,h);
        calendar.set(Calendar.MINUTE,m);

        alarmManager=(AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        /*Context context = getActivity();
        context.startService(new Intent(context, HeadService.class));*/
    }

    private void stopHeadService() {
        Context context = getActivity();
        context.stopService(new Intent(context, HeadService.class));
        if (alarmManager!= null) {
            alarmManager.cancel(pendingIntent);
        }

    }
}
