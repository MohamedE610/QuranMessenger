package com.example.e610.quranmessenger;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e610.quranmessenger.Models.PageOfQuran.Edition;
import com.example.e610.quranmessenger.Utils.FetchData;
import com.example.e610.quranmessenger.Utils.MySharedPreferences;
import com.example.e610.quranmessenger.Utils.NetworkResponse;
import com.example.e610.quranmessenger.Utils.NetworkState;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;


public class viewPagerFragment1 extends Fragment implements NetworkResponse , Main2Activity.PassData{

    private ProgressBar  progressBar;

    FloatingActionButton fab;
    FloatingActionButton fab2;
    boolean flag;
    View view;
    ImageView imageView;
    FetchData fetchData;
    String pageNumber;
    int currentSurahNumber;
    int currentJuzNumber;
    boolean isSajda;


    boolean isplaying;
    String shekhName="";

    @Override
    public void onResume() {
        shekhName=MySharedPreferences.getUserSetting("shekhName");
        playSounds(Integer.valueOf(pageNumber), shekhName);
        super.onResume();
    }

    //String[]shekhNameArray={"","","","","",""};
    final String basicUrl="http://www.quranmessenger.life/sound/";
    final String extentionMP3=".mp3";
    final String slash="/";
    private void playSounds(int page ,String name ){

        String url=basicUrl+shekhName+slash;
        if(page<10){
            url+="00"+page+extentionMP3;
        }else if(page<100){
            url+="0"+page+extentionMP3;
        }else {
            url+=page+extentionMP3;
        }
        runMediaPLayer(url);
    }

    MediaPlayer mediaPlayer;
    private void runMediaPLayer(String url ){
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(url);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    progressBar.setVisibility(View.INVISIBLE);
                    mp.start();
                    //fab.setEnabled(true);
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Toast.makeText(getActivity(),"ملف الصوت غير متاح حاليا",Toast.LENGTH_LONG).show();
                    return false;
                }
            });
            //mediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
        }catch (Exception e){}
    }
    public viewPagerFragment1() {
        // Required empty public constructor
    }
    String urlStr="http://www.quranmessenger.life/pages/quran_pages/";
    String extention=".jpg";

    @Override
    public void onStop() {
        if(mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer=null;
        }
        super.onStop();
    }

    @Override
    public void onPause() {
        /*MySharedPreferences.setUpMySharedPreferences(getActivity(),"extraSetting");
        MySharedPreferences.setUserSetting("pageNumber",pageNumber);*/
        /*if(mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer=null;
        }*/
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_view_pager1, container, false);
        imageView=(ImageView)view.findViewById(R.id.img);
        progressBar=(ProgressBar)view.findViewById(R.id.progressBar);

        /*shekhName=MySharedPreferences.getUserSetting("shekhName");*/
        flag=false;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=!flag;
                if(flag){
                   //communicatio.appeare();
                    fab.setVisibility(View.VISIBLE);
                    fab2.setVisibility(View.VISIBLE);
                }else {
                   //communicatio.disappeare();
                    fab.setVisibility(View.INVISIBLE);
                    fab2.setVisibility(View.INVISIBLE);
                }
            }
        });
        Bundle bundle=getArguments();
        pageNumber=bundle.get("pageNumber").toString();

       /* Picasso.with(getContext()).load(urlStr+pageNumber+extention)
                .placeholder(R.drawable.ts_loading_circle)
                .error(R.drawable.cloud_error_120)
                .into(imageView);*/
        Picasso.with(getContext()).load(urlStr+pageNumber+extention)
                .placeholder(R.drawable.loadicon)
                .error(R.drawable.loadicon)
                .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                .into(imageView);

        if(NetworkState.ConnectionAvailable(getContext())) {
            fetchData = new FetchData(pageNumber);
            fetchData.setNetworkResponse(this);
            fetchData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else {
            Toast.makeText(getContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
        }


        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        //fab.setEnabled(false);
        fab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Snackbar.make(v, "قم بالضغط على الزر لتستمع الى تلاوه الايات", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return false;
            }
        });
        isplaying=false;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isplaying=!isplaying;
                if(mediaPlayer!=null && !mediaPlayer.isPlaying()) {
                        try {
                            progressBar.setVisibility(View.VISIBLE);
                            mediaPlayer.prepareAsync();
                        }
                        catch (Exception e){}
                }else if(mediaPlayer!=null && mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
        fab2 = (FloatingActionButton) view.findViewById(R.id.fab2);
        fab2.setVisibility(View.INVISIBLE);
        fab2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Snackbar.make(v, "قم بالضغط لقرأه التفسير", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return false;
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),TafserActivity.class);
                Bundle b=new Bundle();
                b.putString("pageNumber",pageNumber);
                intent.putExtra("bundle",b);
                getActivity().startActivity(intent);
            }
        });
        //fab.setVisibility(View.INVISIBLE);

        //playSounds(Integer.valueOf(pageNumber), shekhName);

        return view ;
    }

    /*MediaPlayer mediaPlayer;
    @Override
    public void onPause() {
        super.onPause();
        mediaPlayer.release();
        mediaPlayer = null;
    }*/

    @Override
    public void OnSuccess(String JsonData) {
        if(isAdded() && getActivity()!=null) {

        }

    }

    @Override
    public void OnFailure(boolean Failure) {

    }

    private TextView createTextView(int i){
        TextView textView=new TextView(getContext());
        textView.setTextSize(25);
        textView.setPadding(0,0,0,0);

        if(i==0){
            ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(lparams);
            textView.setGravity(Gravity.CENTER);
        }else{
            ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(lparams);
        }

        return textView;
    }

    @Override
    public void passData() {
        if(mediaPlayer!=null&&mediaPlayer.isPlaying())
            mediaPlayer.stop();
    }
}
