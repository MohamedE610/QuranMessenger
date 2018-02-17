package com.example.e610.quranmessenger.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.e610.quranmessenger.Fragments.SettingsAzanFragment;
import com.example.e610.quranmessenger.Fragments.SettingsAzkarFragment;
import com.example.e610.quranmessenger.Fragments.SettingsFragment;
import com.example.e610.quranmessenger.R;

public class SettingsActivity extends AppCompatActivity {

    //Fragment settingFragment;

    /*private void addAllSettingFragments(FragmentManager fragmentManager){

        SettingsFragment settingsFragment = new SettingsFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.main_setting_container, settingsFragment)
                .commit();

        SettingsAzanFragment settingsFragment2 = new SettingsAzanFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.azan_setting_container, settingsFragment2)
                .commit();

        SettingsAzkarFragment settingsFragment3 = new SettingsAzkarFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.azkar_setting_container, settingsFragment3)
                .commit();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent=getIntent();
        String action=intent.getAction();
        if(action.equals("main_settings")) {
           // addAllSettingFragments(getFragmentManager());
            SettingsFragment settingsFragment = new SettingsFragment();
            getFragmentManager().beginTransaction()
                    .replace(R.id.setting_container, settingsFragment)
                    .commit();

        }else if(action.equals("azan_settings")){
            SettingsAzanFragment settingsFragment = new SettingsAzanFragment();
            getFragmentManager().beginTransaction()
                    .replace(R.id.setting_container, settingsFragment)
                    .commit();
        }else if(action.equals("azkar_settings")){
            SettingsAzkarFragment settingsFragment = new SettingsAzkarFragment();
            getFragmentManager().beginTransaction()
                    .replace(R.id.setting_container, settingsFragment)
                    .commit();
            /*getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, settingsFragment)
                    .commit();*/
        }


    }
}
