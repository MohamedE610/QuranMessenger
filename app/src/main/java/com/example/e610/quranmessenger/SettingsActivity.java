package com.example.e610.quranmessenger;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    //Fragment settingFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent=getIntent();
        String action=intent.getAction();
        if(action.equals("main_settings")) {
            SettingsFragment settingsFragment = new SettingsFragment();
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, settingsFragment)
                    .commit();
        }else if(action.equals("azan_settings")){
            SettingsAzanFragment settingsFragment = new SettingsAzanFragment();
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, settingsFragment)
                    .commit();
        }else if(action.equals("azkar_settings")){
            SettingsAzkarFragment settingsFragment = new SettingsAzkarFragment();
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, settingsFragment)
                    .commit();
        }


    }
}
