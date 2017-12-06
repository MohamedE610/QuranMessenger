package com.example.e610.quranmessenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;


public class PrayerTimesActivity extends AppCompatActivity implements AzanFragment.IDataRecieved{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer_times);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        AzanFragment azanFragment=new AzanFragment();
        getFragmentManager().beginTransaction().add(R.id.azan_fragment_container,azanFragment).commit();

         textView = (TextView) findViewById(R.id.fagr);
         textView1 = (TextView) findViewById(R.id.duhr);
         textView2 = (TextView) findViewById(R.id.asr);
         textView3 = (TextView) findViewById(R.id.maghrp);
         textView4 = (TextView) findViewById(R.id.isha);

    }
    TextView textView;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;

    @Override
    public void onSuccess(String[] times) {
        if(times!=null&& times.length>0) {
            textView.setText(times[0]);
            textView1.setText(times[1]);
            textView2.setText(times[2]);
            textView3.setText(times[3]);
            textView4.setText(times[4]);
        }
    }
}
