package com.example.e610.quranmessenger.Fragments;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.example.e610.quranmessenger.Activities.OtherSettingsActivity;
import com.example.e610.quranmessenger.R;
import com.example.e610.quranmessenger.Services.HeadService;
import com.example.e610.quranmessenger.Utils.MySharedPreferences;
import com.example.e610.quranmessenger.Utils.PermissionChecker;
import com.example.e610.quranmessenger.Utils.TimePreference;

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
            timePreference.setTitle(getString(R.string.alarm_time) + " " + (i+1));
            String strValue=MySharedPreferences.getUserSetting(alarm);
            timePreference.setDefaultValue("12:44");
            timePreference.setSummary(strValue);
            getPreferenceScreen().addPreference(timePreference);
            timePreference.setDependency(SERVICE_ENABLED_KEY);
        }

        eggsMethod();

        Preference preference = findPreference("shekh");
        String shekhName=MySharedPreferences.getUserSetting("shekhName");
        preference.setSummary(getArabicShekhName(shekhName));

        String aNum = MySharedPreferences.getData();
        Preference Pref = findPreference("alarm_numbers");
        Pref.setSummary(aNum);

    }
// -_- >_<
    private void eggsMethod(){
        try {
            getPreferenceScreen().removePreference(findPreference("other"));
        }catch (Exception e){}

        PreferenceCategory preferenceCategory=new PreferenceCategory(getActivity());
        preferenceCategory.setTitle("الاعدادات الاخرى");
        preferenceCategory.setKey("other");
        Preference preferenceAdvanced=new Preference(getActivity());
        preferenceAdvanced.setTitle("الاعدادات المتقدمه");
        preferenceAdvanced.setKey("advanced_setting");

        Preference preferenceAzan=new Preference(getActivity());
        preferenceAzan.setTitle("اعدادات الاذان");
        preferenceAzan.setKey("azan_setting");

        Preference preferenceAzkar=new Preference(getActivity());
        preferenceAzkar.setTitle("اعدادات الاذكار");
        preferenceAzkar.setKey("azkar_setting");
        getPreferenceScreen().addPreference(preferenceCategory);
        preferenceCategory.addPreference(preferenceAzan);
        preferenceCategory.addPreference(preferenceAzkar);
        preferenceCategory.addPreference(preferenceAdvanced);

        preferenceAdvanced.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent=new Intent(getActivity(),OtherSettingsActivity.class);
                intent.setAction("advanced");
                getActivity().startActivity(intent);
                return false;
            }
        });

        preferenceAzan.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent=new Intent(getActivity(),OtherSettingsActivity.class);
                intent.setAction("azan");
                getActivity().startActivity(intent);
                return false;
            }
        });

        preferenceAzkar.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent=new Intent(getActivity(),OtherSettingsActivity.class);
                intent.setAction("azkar");
                getActivity().startActivity(intent);
                return false;
            }
        });
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

        boolean enabled = sharedPreferences.getBoolean(SERVICE_ENABLED_KEY, false);

        /*********************************************/
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
                MySharedPreferences.setUpMySharedPreferences(getActivity(),getActivity().getResources().getString(R.string.shared_pref_file_name));
                MySharedPreferences.setAlarmState("1");
                int NumOfAlarm = Integer.valueOf(alarmNum);
                for (int i = 0; i < NumOfAlarm; i++) {
                    String s = sharedPreferences.getString(alarmName + i + "", "0:0");
                    if (!s.equals("0:0")) {
                        String[] strs = s.split(":");
                        int h = Integer.valueOf(strs[0]);
                        int m = Integer.valueOf(strs[1]);
                        startHeadService(h, m, i);
                        MySharedPreferences.setUserSetting(alarmName+i,s);
                    }
                }
            } else {
                MySharedPreferences.setUpMySharedPreferences(getActivity(),getActivity().getResources().getString(R.string.shared_pref_file_name));
                MySharedPreferences.setAlarmState("-1");
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
                timePreference.setTitle(getString(R.string.alarm_time) + " " + (i+1) + "");
                timePreference.setDefaultValue("12:44");
                timePreference.setSummary(getString(R.string.alarm_time_summary));
                getPreferenceScreen().addPreference(timePreference);
                timePreference.setDependency(SERVICE_ENABLED_KEY);
            }

            eggsMethod();

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

        } else if (key.equals("shekh")) {
            MySharedPreferences.setUpMySharedPreferences(getActivity(), "extraSetting");
            String shekhName = sharedPreferences.getString("shekh", "");
            Preference preference = findPreference("shekh");
            preference.setSummary(getArabicShekhName(shekhName));
            MySharedPreferences.setUserSetting("shekhName", shekhName);
            Toast.makeText(getActivity(),getArabicShekhName(shekhName), Toast.LENGTH_LONG).show();
        }

    }

    public static String  getArabicShekhName(String englishName){
        String arabicName="ماهر المعيقلي";
        String[] names={"ماهر المعيقلي","العجمي","فارس عباد","سعود الشريم","محمود الحصري","العفاسي"};

        if(englishName.equals("mueaqly")){
            return names[0];
        }else if(englishName.equals("ajami")){
            return names[1];
        }else if(englishName.equals("fares")){
            return names[2];
        }else if(englishName.equals("shurim")){
            return names[3];
        }else if(englishName.equals("hosary")){
            return names[4];
        }else if(englishName.equals("afasi")){
            return names[5];
        }

        return arabicName;
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
        MySharedPreferences.setUpMySharedPreferences(getActivity(), getActivity().getString(R.string.shared_pref_file_name));
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
}
