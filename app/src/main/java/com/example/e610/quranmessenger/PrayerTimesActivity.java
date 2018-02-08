package com.example.e610.quranmessenger;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e610.quranmessenger.Models.PrayerTimes.PrayerTimes;
import com.example.e610.quranmessenger.Utils.FetchAzanData;
import com.example.e610.quranmessenger.Utils.MySharedPreferences;
import com.example.e610.quranmessenger.Utils.NetworkResponse;
import com.example.e610.quranmessenger.Utils.NetworkState;
import com.google.gson.Gson;

import java.util.Calendar;


public class PrayerTimesActivity extends AppCompatActivity implements NetworkResponse{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer_times);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if(NetworkState.ConnectionAvailable(this)) {
            FetchAzanData fetchAzanData = new FetchAzanData(this);
            fetchAzanData.setNetworkResponse(this);
            fetchAzanData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        /*SettingsAzanFragment azanFragment=new SettingsAzanFragment();
        getFragmentManager().beginTransaction().add(R.id.azan_fragment_container,azanFragment).commit();*/

         textView = (TextView) findViewById(R.id.fagr);
         textView1 = (TextView) findViewById(R.id.duhr);
         textView2 = (TextView) findViewById(R.id.asr);
         textView3 = (TextView) findViewById(R.id.maghrp);
         textView4 = (TextView) findViewById(R.id.isha);
         textView5 = (TextView) findViewById(R.id.shrouq);

    }
    TextView textView;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.azan_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.azan_settings) {
            Intent intent= new Intent(this,SettingsActivity.class);
            intent.setAction("azan_settings");
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void OnSuccess(String JsonData) {

        try {
            Calendar calendar = Calendar.getInstance();
            int i = calendar.get(Calendar.DAY_OF_MONTH);


            Gson gson = new Gson();
            PrayerTimes prayerTimes = gson.fromJson(JsonData, PrayerTimes.class);

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

            if (times.length > 0) {
                textView.setText(times[0]);
                textView1.setText(times[1]);
                textView2.setText(times[2]);
                textView3.setText(times[3]);
                textView4.setText(times[4]);
                textView5.setText(times[5]);

            }
        }catch (Exception e){
            Toast.makeText(PrayerTimesActivity.this,"لقد حدث خطاء",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void OnFailure(boolean Failure) {

    }
}
