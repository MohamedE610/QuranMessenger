package com.example.e610.quranmessenger;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.example.e610.quranmessenger.Services.AzkarService;
import com.example.e610.quranmessenger.Services.HeadService;
import com.example.e610.quranmessenger.Utils.MySharedPreferences;
import com.example.e610.quranmessenger.Utils.PermissionChecker;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Standard settings screen.
 * It allows to enable or disable the head service.
 */
public class SettingsAzkarFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    static HashMap<Integer, PendingIntent> pendingIntentList = new HashMap<>();
    static AlarmManager alarmManager;
    static Context ctx;
    private PermissionChecker mPermissionChecker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ctx=getActivity();
        addPreferencesFromResource(R.xml.azkar_setting);
        //enableHeadServiceCheckbox(false);
        mPermissionChecker = new PermissionChecker(getActivity());
        if (!mPermissionChecker.isRequiredPermissionGranted()) {
            //enableHeadServiceCheckbox(false);
            Intent intent = mPermissionChecker.createRequiredPermissionIntent();
            startActivityForResult(intent, PermissionChecker.REQUIRED_PERMISSION_REQUEST_CODE);
        } else {
           //enableHeadServiceCheckbox(true);
        }

        MySharedPreferences.setUpMySharedPreferences(getActivity(), "extraSetting");

        String azkar_am=MySharedPreferences.getUserSetting("azkar_am");
        Preference preference=findPreference("azkar_am");
        preference.setSummary(azkar_am);

        String azkar_pm=MySharedPreferences.getUserSetting("azkar_pm");
        Preference preference1=findPreference("azkar_pm");
        preference1.setSummary(azkar_pm);

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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        MySharedPreferences.setUpMySharedPreferences(getActivity(),getActivity().getString(R.string.shared_pref_file_name));

        boolean enabledAM = sharedPreferences.getBoolean("azkar_alarm_am", false);
        boolean enabledPM = sharedPreferences.getBoolean("azkar_alarm_pm", false);

        if ("azkar_alarm_am".equals(key)) {
            if (enabledAM) {
                String azkar_am=sharedPreferences.getString("azkar_am","0:0");
                String[] strs = azkar_am.split(":");
                int h = Integer.valueOf(strs[0]);
                int m = Integer.valueOf(strs[1]);
                startHeadService(h, m, 9911,0);

                MySharedPreferences.setUpMySharedPreferences(getActivity(),getActivity().getResources().getString(R.string.shared_pref_file_name));
                MySharedPreferences.setAzkarAmState("1");
                MySharedPreferences.setUserSetting("am_alarm",azkar_am);
                 /*String azkar_pm=sharedPreferences.getString("azkar_pm","0:0");
                 strs = azkar_pm.split(":");
                 h = Integer.valueOf(strs[0]);
                 m = Integer.valueOf(strs[1]);
                 startHeadService(h, m, 1199,1);*/

            } else {
                MySharedPreferences.setUpMySharedPreferences(getActivity(),getActivity().getResources().getString(R.string.shared_pref_file_name));
                MySharedPreferences.setAzkarAmState("-1");
                stopHeadService();

            }
        } else if (key.equals("azkar_am")) {
            if (enabledAM) {

                String azkar_am=sharedPreferences.getString("azkar_am","");
                MySharedPreferences.setUserSetting("azkar_am",azkar_am);
                MySharedPreferences.setUserSetting("am_alarm",azkar_am);
                Preference preference=findPreference("azkar_am");
                preference.setSummary(azkar_am);
                String[] strs = azkar_am.split(":");
                int h = Integer.valueOf(strs[0]);
                int m = Integer.valueOf(strs[1]);
                startHeadService(h, m, 9911,0);

            } else {
                stopHeadService();
            }
        }else if ("azkar_alarm_pm".equals(key)) {
            if (enabledPM) {
                String azkar_pm = sharedPreferences.getString("azkar_pm", "0:0");
                String[] strs = azkar_pm.split(":");
                int h = Integer.valueOf(strs[0]);
                int m = Integer.valueOf(strs[1]);
                startHeadService(h, m, 1199, 1);

                MySharedPreferences.setUpMySharedPreferences(getActivity(),getActivity().getResources().getString(R.string.shared_pref_file_name));
                MySharedPreferences.setAzkarPmState("1");
                MySharedPreferences.setUserSetting("pm_alarm",azkar_pm);

            } else {
                MySharedPreferences.setUpMySharedPreferences(getActivity(),getActivity().getResources().getString(R.string.shared_pref_file_name));
                MySharedPreferences.setAzkarPmState("-1");
                stopHeadService();
            }
        } else if (key.equals("azkar_pm")) {
            if (enabledPM) {
                String azkar_pm=sharedPreferences.getString("azkar_pm","");
                MySharedPreferences.setUserSetting("azkar_pm",azkar_pm);
                MySharedPreferences.setUserSetting("pm_alarm",azkar_pm);
                Preference preference=findPreference("azkar_pm");
                preference.setSummary(azkar_pm);

                String[] strs = azkar_pm.split(":");
                int h = Integer.valueOf(strs[0]);
                int m = Integer.valueOf(strs[1]);
                startHeadService(h, m, 1199 ,1);


            } else {
                stopHeadService();
            }
        }
    }


    private void enableHeadServiceCheckbox(boolean enabled) {
        getPreferenceScreen().findPreference("azkar_alarm_am").setEnabled(enabled);
        getPreferenceScreen().findPreference("azkar_alarm_pm").setEnabled(enabled);
    }

// 0 -> am
// 1 -> pm
    public static void startHeadService(int h, int m, int id , int azkar_type) {
        Intent intent = new Intent(ctx, AzkarService.class);
        intent.setAction("azkar");
        intent.putExtra("azkar_type",azkar_type);
        intent.putExtra("h",h);
        intent.putExtra("m",m);
        intent.putExtra("id",id);
        PendingIntent pendingIntent = PendingIntent.getService(ctx, id, intent, 0);
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

        alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, _alarm, AlarmManager.INTERVAL_DAY, pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, _alarm , pendingIntent);
        }else{
            alarmManager.set(AlarmManager.RTC_WAKEUP, _alarm , pendingIntent);
        }

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

        if(pendingIntentList!=null&&pendingIntentList.size()==2){
            if (alarmManager != null) {
                for (int i = 0; i < pendingIntentList.size(); i++) {
                    if(i==0)
                       alarmManager.cancel(pendingIntentList.get(9911));
                    else
                       alarmManager.cancel(pendingIntentList.get(1199));
                }
                //pendingIntentList.clear();
            }
        }else if(pendingIntentList==null||pendingIntentList.size()!=2){
            pendingIntentList=new HashMap<>();
            for (int i = 0; i <2 ; i++) {
                if(i==0) {
                    Intent intent = new Intent(getActivity(), HeadService.class);
                    PendingIntent pendingIntent = PendingIntent.getService(getActivity(),9911, intent, 0);
                    pendingIntentList.put(9911, pendingIntent);
                }else{
                    Intent intent = new Intent(getActivity(), HeadService.class);
                    PendingIntent pendingIntent = PendingIntent.getService(getActivity(), 1199, intent, 0);
                    pendingIntentList.put(1199, pendingIntent);
                }
            }

            if (alarmManager != null) {
                for (int i = 0; i < pendingIntentList.size(); i++) {
                    if(i==0)
                        alarmManager.cancel(pendingIntentList.get(9911));
                    else
                        alarmManager.cancel(pendingIntentList.get(1199));
                }
                //pendingIntentList.clear();
            }

        }
    }

}
