package com.example.e610.quranmessenger.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.e610.quranmessenger.Fragments.SettingsAzanFragment;
import com.example.e610.quranmessenger.Fragments.SettingsAzkarFragment;
import com.example.e610.quranmessenger.Fragments.SettingsFragment;
import com.example.e610.quranmessenger.R;

public class OtherSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_settings);

        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent=getIntent();
        String action=intent.getAction();
        if(action.equals("advanced")) {
            // addAllSettingFragments(getFragmentManager());
            SettingsFragment settingsFragment = new SettingsFragment();
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, settingsFragment)
                    .commit();

        }else if(action.equals("azan")){
            SettingsAzanFragment settingsFragment = new SettingsAzanFragment();
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, settingsFragment)
                    .commit();

        }else if(action.equals("azkar")){
            SettingsAzkarFragment settingsFragment = new SettingsAzkarFragment();
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, settingsFragment)
                    .commit();
        }
    }
}
