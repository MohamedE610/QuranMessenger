package com.example.e610.quranmessenger.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
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

import com.example.e610.quranmessenger.R;
import com.example.e610.quranmessenger.Utils.FetchAzkarData;
import com.example.e610.quranmessenger.Utils.NetworkResponse;
import com.example.e610.quranmessenger.Utils.NetworkState;

public class AzkarDetailedActivity extends AppCompatActivity implements NetworkResponse{

    FetchAzkarData fetchAzkarData;
    TextView azkarNameText;
    TextView azkarText;
    String methodStr;
    private ProgressBar progressBar;
    MediaPlayer mediaPlayer;
    private ProgressDialog progressDialog;
    private String azkarTypeStr;
    private FloatingActionButton fab;
    /*String basicUrl="http://www.quranmessenger.life/sound/hosary/001.mp3";
    private void runMediaPLayer(String url ){
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(url);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    //progressBar.setVisibility(View.INVISIBLE);
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                    mp.start();
                    //fab.setEnabled(true);
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Toast.makeText(AzkarDetailedActivity.this,"ملف الصوت غير متاح حاليا",Toast.LENGTH_LONG).show();
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                    return false;
                }
            });
            //mediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
        }catch (Exception e){}
    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_azkar_detailed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //runMediaPLayer(basicUrl);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                titleStr=azkarNameText.getText().toString();
                String s=titleStr+"\n"+contentStr;
                contentStr=s;
                share(titleStr,contentStr);
            }
        });



          azkarNameText = (TextView) findViewById(R.id.azkar_type_txt);
          azkarText = (TextView) findViewById(R.id.azkar_txt);
          progressBar= (ProgressBar) findViewById(R.id.progressBar);
          progressBar.setVisibility(View.VISIBLE);

        Intent intent=getIntent();
        if(intent!=null) {
            Bundle bundle = intent.getBundleExtra("bundle");
            if(bundle!=null){
                 azkarTypeStr=bundle.getString("azkar");
                methodStr=bundle.getString("method");
                if(azkarTypeStr!=null&& azkarTypeStr.equals("am"))
                    azkarNameText.setText("اذكار الصباح");
                else if(azkarTypeStr!=null&& azkarTypeStr.equals("pm"))
                    azkarNameText.setText("اذكار المساء");
            }
        }

        if(methodStr!=null&&methodStr.equals("0")) {
            /*if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                progressDialog = ProgressDialog.show(this, "", "جارى تشغيل ملف الصوت", false, false);
                mediaPlayer.prepareAsync();
            }*/
        }

        if(NetworkState.ConnectionAvailable(this)) {
            fetchAzkarData = new FetchAzkarData(azkarTypeStr);
            fetchAzkarData.setNetworkResponse(this);
            fetchAzkarData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else
            Toast.makeText(AzkarDetailedActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();

    }

    private void share(String title,String content){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody =content;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent,"Share via" ));
    }
    String contentStr;
    String titleStr;
    @Override
    public void OnSuccess(String JsonData) {

        if(JsonData!=null && !JsonData.equals("")) {
            progressBar.setVisibility(View.INVISIBLE);
            azkarText.setText(JsonData);
            contentStr=JsonData;
            fab.setVisibility(View.VISIBLE);
        }
        else {
            progressBar.setVisibility(View.INVISIBLE);
            azkarText.setText("حدث خطأ اثناء التحميل ");
        }
    }

    @Override
    public void OnFailure(boolean Failure) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);
                azkarText.setText("حدث خطأ اثناء التحميل ");
            }
        });

    }
}
