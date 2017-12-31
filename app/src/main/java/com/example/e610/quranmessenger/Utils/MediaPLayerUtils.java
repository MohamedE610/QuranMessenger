package com.example.e610.quranmessenger.Utils;

import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.widget.Toast;

/**
 * Created by E610 on 12/29/2017.
 */
public class MediaPLayerUtils {

    public  final static String basicUrl="http://www.quranmessenger.life/sound/";
    public  final static String extentionMP3=".mp3";
    public final static String slash="/";

    public static String createUrl(int page ,String name ){

        String url=basicUrl+name+slash;
        if(page<10){
            url+="00"+page+extentionMP3;
        }else if(page<100){
            url+="0"+page+extentionMP3;
        }else {
            url+=page+extentionMP3;
        }
        //runMediaPLayer(url);
        return url;
    }

    Boolean isError=false;
    MediaPlayer mediaPlayer;
   /* private void runMediaPLayer(String url ){
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
                    isError=true;
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                    Toast.makeText(getActivity(),"ملف الصوت غير متاح حاليا",Toast.LENGTH_LONG).show();
                    return false;
                }
            });

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if(!isError) {
                        int pn = Integer.valueOf(pageNumber);
                        pn++;
                        pageNumber = pn + "";
                        playSounds(pn, shekhName);
                        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                            progressDialog = ProgressDialog.show(getActivity(), "", progressMsg, false, false);
                            mediaPlayer.prepareAsync();
                        }
                        Toast.makeText(getContext(), "hi man", Toast.LENGTH_LONG).show();
                    }
                }
            });
            //mediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
        }catch (Exception e){}*/
}


