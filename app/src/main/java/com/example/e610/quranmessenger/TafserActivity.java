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
import android.widget.Toast;

import com.example.e610.quranmessenger.Utils.FetchTafserData;
import com.example.e610.quranmessenger.Utils.NetworkResponse;
import com.example.e610.quranmessenger.Utils.NetworkState;

public class TafserActivity extends AppCompatActivity implements NetworkResponse{

    String pageNumber;
    TextView pageNumText;
    TextView tafserText;
    ProgressBar progressBar;
    FetchTafserData fetchTafserData;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tafser);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

         fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                share(titleStr,contentStr);
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

        if(NetworkState.ConnectionAvailable(this)) {
            try {
                fetchTafserData = new FetchTafserData(pageNumber);
                fetchTafserData.setNetworkResponse(this);
                fetchTafserData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } catch (Exception e) {
                pageNumText.setText(pageNumber);
                tafserText.setText("حدث خطأ اثناء التحميل ");
            }
        }else{
            Toast.makeText(this,"No Internet Connection",Toast.LENGTH_LONG).show();
        }

    }

    private void share(String title,String content){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody =content;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent,"Share via" ));
    }
    String contentStr="";
    String titleStr="";

    @Override
    public void OnSuccess(String JsonData) {
        if(JsonData!=null && !JsonData.equals("")) {
            progressBar.setVisibility(View.INVISIBLE);
            pageNumText.setText(pageNumber);
            tafserText.setText(JsonData);
            contentStr=JsonData;
            fab.setVisibility(View.VISIBLE);
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
