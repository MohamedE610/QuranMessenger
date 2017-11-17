package com.example.e610.quranmessenger;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
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
        String s=sharedPreferences.getString("alarm_time","0:0");
        String [] strs=s.split(":");
        int h=Integer.valueOf(strs[0]);
        int m=Integer.valueOf(strs[1]);
        Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();

        boolean enabled = sharedPreferences.getBoolean(SERVICE_ENABLED_KEY, false);

        if(SERVICE_ENABLED_KEY.equals(key)) {
            if(enabled) {
                startHeadService(h,m);
            } else {
                stopHeadService();
            }
        }
        else if("alarm_time".equals(key)){
            if(enabled) {
                startHeadService(h,m);
            } else {
                stopHeadService();
            }

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
        calendar.setTimeInMillis(System.currentTimeMillis());
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
