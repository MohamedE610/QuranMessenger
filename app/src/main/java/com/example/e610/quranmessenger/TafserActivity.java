package com.example.e610.quranmessenger;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.e610.quranmessenger.Utils.FetchTafserData;
import com.example.e610.quranmessenger.Utils.NetworkResponse;

public class TafserActivity extends AppCompatActivity implements NetworkResponse{

    String pageNumber;
    TextView pageNumText;
    TextView tafserText;
    ProgressBar progressBar;
    FetchTafserData fetchTafserData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tafser);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent intent=getIntent();
        Bundle bundle=new Bundle();
        bundle=intent.getBundleExtra("bundle");
        pageNumber=bundle.getString("pageNumber");
        pageNumText=(TextView) findViewById(R.id.name_txt);
        tafserText=(TextView) findViewById(R.id.tafser_txt);
        progressBar=(ProgressBar) findViewById(R.id.progressBarTafser);
        progressBar.setVisibility(View.VISIBLE);

        try {
            fetchTafserData = new FetchTafserData(pageNumber);
            fetchTafserData.setNetworkResponse(this);
            fetchTafserData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }catch (Exception e){
            pageNumText.setText(pageNumber);
            tafserText.setText("حدث خطأ اثناء التحميل ");
        }

    }

    @Override
    public void OnSuccess(String JsonData) {
        if(JsonData!=null && !JsonData.equals("")) {
            progressBar.setVisibility(View.INVISIBLE);
            pageNumText.setText(pageNumber);
            tafserText.setText(JsonData);
        }
        else {
            pageNumText.setText(pageNumber);
            tafserText.setText("حدث خطأ اثناء التحميل ");
        }
    }

    @Override
    public void OnFailure(boolean Failure) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);
                pageNumText.setText(pageNumber);
                tafserText.setText("حدث خطأ اثناء التحميل ");
            }
        });
    }
}
