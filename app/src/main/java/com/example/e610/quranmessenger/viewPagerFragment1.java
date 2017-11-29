package com.example.e610.quranmessenger;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e610.quranmessenger.Utils.FetchData;
import com.example.e610.quranmessenger.Utils.MySharedPreferences;
import com.example.e610.quranmessenger.Utils.NetworkResponse;
import com.example.e610.quranmessenger.Utils.NetworkState;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;


public class viewPagerFragment1 extends Fragment implements NetworkResponse{

   public interface activityCommunication{
        void appeare();
        void disappeare();
    }
    boolean flag;
    View view;
    ImageView imageView;
    FetchData fetchData;
    String pageNumber;
    activityCommunication communicatio;
    int currentSurahNumber;
    int currentJuzNumber;
    boolean isSajda;
    public viewPagerFragment1() {
        // Required empty public constructor
    }

    String urlStr="http://www.quranmessenger.life/pages/quran_pages/";
    String extention=".jpg";

    @Override
    public void onPause() {
        /*MySharedPreferences.setUpMySharedPreferences(getActivity(),"extraSetting");
        MySharedPreferences.setUserSetting("pageNumber",pageNumber);*/
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_view_pager1, container, false);
        imageView=(ImageView)view.findViewById(R.id.img);
        flag=false;
        communicatio=(activityCommunication)getActivity();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=!flag;
                if(flag){
                   communicatio.appeare();
                }else {
                   communicatio.disappeare();
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

        /*try {
            String url = "http://www.quranmessenger.life/sound/hosary/003.mp3"; // your URL here
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(url);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mediaPlayer.prepareAsync(); // might take long! (for buffering, etc)

        }catch (Exception e){}*/

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
}
