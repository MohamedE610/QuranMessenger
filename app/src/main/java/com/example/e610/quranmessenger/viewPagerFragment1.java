package com.example.e610.quranmessenger;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e610.quranmessenger.Services.MediaPlayerService;
import com.example.e610.quranmessenger.Utils.BottomNavigationViewHelper;
import com.example.e610.quranmessenger.Utils.FetchData;
import com.example.e610.quranmessenger.Utils.MySharedPreferences;
import com.example.e610.quranmessenger.Utils.NetworkResponse;
import com.example.e610.quranmessenger.Utils.NetworkState;
import com.example.e610.quranmessenger.Utils.ServiceUtils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;


public class viewPagerFragment1 extends Fragment implements NetworkResponse, Main2Activity.PassData {

    private ProgressBar progressBar;

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


    boolean isplaying = false;
    public static String shekhName = "";
    private ProgressDialog progressDialog;
    private int surahPlayedNum;
    ImageView imageView1;
    private BottomNavigationView bottomNavigationView;

    @Override
    public void onResume() {
        shekhName = MySharedPreferences.getUserSetting("shekhName");
        if (shekhName.equals(""))
            shekhName = "hosary";
        //playSounds(Integer.valueOf(pageNumber), shekhName);

        super.onResume();
    }

    //String[]shekhNameArray={"","","","","",""};
    public final static String basicUrl = "http://www.quranmessenger.life/sound/";
    public final static String extentionMP3 = ".mp3";
    public final static String slash = "/";

    public static String playSounds(int page, String name) {

        String url = basicUrl + name + slash;
        if (page < 10) {
            url += "00" + page + extentionMP3;
        } else if (page < 100) {
            url += "0" + page + extentionMP3;
        } else {
            url += page + extentionMP3;
        }
        //runMediaPLayer(url);
        return url;
    }

    Boolean isError = false;
    MediaPlayer mediaPlayer;


    private void runMediaPLayer(String url) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(url);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    //progressBar.setVisibility(View.INVISIBLE);
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    mp.start();
                    //fab.setEnabled(true);
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    isError = true;
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    Toast.makeText(getActivity(), "ملف الصوت غير متاح حاليا", Toast.LENGTH_LONG).show();
                    return false;
                }
            });

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (!isError) {
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
        } catch (Exception e) {
        }
    }

    public viewPagerFragment1() {
        // Required empty public constructor
    }

    String urlStr = "http://www.quranmessenger.life/pages/quran_pages/";
    String extention = ".jpg";

    @Override
    public void onStop() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        /*getActivity().stopService(new Intent(getActivity(),MediaPlayerService.class));*/
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

    private void soundNavMethod(){
        if (!isplaying) {
            try {
                // progressBar.setVisibility(View.VISIBLE);
                progressDialog = ProgressDialog.show(getActivity(), "", progressMsg, false, true);
                //mediaPlayer.prepareAsync();
                           /*Intent intent=new Intent(getActivity(),MediaPlayerService.class);
                            intent.setAction("play");
                            Bundle b=new Bundle();
                            //b.putString("pn",playSounds(Integer.valueOf(pageNumber),shekhName));
                            b.putString("pn", MediaPLayerUtils.createUrl(Integer.valueOf(pageNumber),shekhName));
                            b.putInt("num",Integer.valueOf(pageNumber));
                            b.putString("sh_name",shekhName);
                            intent.putExtra("pn",b);
                            getActivity().startService(intent);*/
                ServiceUtils.startMediaService(getActivity(), pageNumber, shekhName);
                isplaying = !isplaying;
                fab.setImageResource(R.drawable.icon_pause);
                bottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.nav_pause);
            } catch (Exception e) {
            }
                /*}else if(mediaPlayer!=null && mediaPlayer.isPlaying()) {*/
        } else if (isplaying) {
            //mediaPlayer.stop();
            // progressBar.setVisibility(View.INVISIBLE);
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
            getActivity().stopService(new Intent(getActivity(), MediaPlayerService.class));
            isplaying = !isplaying;
            fab.setImageResource(R.drawable.icon_play);
            bottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.nav_play);
        }
    }

    private void readNavMethod(){
        Intent intent = new Intent(getActivity(), TafserActivity.class);
        Bundle b = new Bundle();
        b.putString("pageNumber", pageNumber);
        intent.putExtra("bundle", b);
        getActivity().startActivity(intent);
    }

    private void bookmarkNavMethod(){}

    private void listNavMethod(){
        getActivity().startActivity(new Intent(getActivity(),FahrsActivity.class));
        getActivity().finish();
    }

    private void settingNavMethod(){
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        intent.setAction("main_settings");
        getActivity().startActivity(intent);
    }

    String progressMsg = "جاري تشغيل الملف الصوتي...";
    String pageN="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MySharedPreferences.setUpMySharedPreferences(getActivity(),getString(R.string.shared_pref_file_name));

        Main2Activity.appBarLayout.setVisibility(View.INVISIBLE);

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_pager1, container, false);
        imageView = (ImageView) view.findViewById(R.id.img);
        imageView1 = (ImageView) view.findViewById(R.id.img_mark);

        bottomNavigationView = (BottomNavigationView)
                view.findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setVisibility(View.INVISIBLE);
        //bottomNavigationView.setItemIconTintList(null);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_sound_nav:
                                soundNavMethod();
                                return true;
                            case R.id.action_read_nav:
                                readNavMethod();
                                return true;
                            case R.id.action_bookmark_nav:
                                showPopup(bottomNavigationView);
                                return true;
                            case R.id.action_list_nav:
                                listNavMethod();
                                return true;
                            case R.id.action_setting_nav:
                                settingNavMethod();
                                return true;
                        }
                        return true;
                    }
                });


        //progressBar=(ProgressBar)view.findViewById(R.id.progressBar);
        //progressDialog=new ProgressDialog(getActivity());
        //progressDialog.setMessage("جاري تشغيل الملف الصوتي...");




        /*shekhName=MySharedPreferences.getUserSetting("shekhName");*/
        flag = false;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    pageN =(604-Integer.valueOf(MySharedPreferences.getUserSetting("pageNumber")))+"";
                }catch (Exception e){
                    pageN="";
                }

                flag = !flag;
                if (flag) {
                    //communicatio.appeare();
                    Main2Activity.appBarLayout.setVisibility(View.VISIBLE);
                    /*fab.setVisibility(View.VISIBLE);
                    fab2.setVisibility(View.VISIBLE);*/
                    bottomNavigationView.setVisibility(View.VISIBLE);

                    if(pageN.equals(pageNumber)) {
                        imageView1.setVisibility(View.VISIBLE);
                        bottomNavigationView.getMenu().getItem(2).setIcon(R.drawable.nav_bookmark);
                    }else {
                        imageView1.setVisibility(View.INVISIBLE);
                        bottomNavigationView.getMenu().getItem(2).setIcon(R.drawable.nav_bookmark_outline);
                    }

                    try {
                        surahPlayedNum = Integer.valueOf(MySharedPreferences.getUserSetting("LAST_SURAH"));
                    } catch (Exception e) {
                        surahPlayedNum = 0;
                    }

                   /* if (pageNumber.equals(++surahPlayedNum+"")) {
                        fab.setImageResource(R.drawable.icon_pause);
                        isplaying=true;
                    } else {
                        fab.setImageResource(R.drawable.icon_play);
                        isplaying=false;
                    }*/

                    String state=MySharedPreferences.getMediaPlayerState();
                    if(state.equals("1")){
                        fab.setImageResource(R.drawable.icon_pause);

                        bottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.nav_pause);
                        isplaying=true;
                    }else {
                          fab.setImageResource(R.drawable.icon_play);
                          bottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.nav_play);
                          isplaying=false;
                    }


                } else {
                    //communicatio.disappeare();
                    Main2Activity.appBarLayout.setVisibility(View.INVISIBLE);
                    /*fab.setVisibility(View.INVISIBLE);
                    fab2.setVisibility(View.INVISIBLE);*/
                    bottomNavigationView.setVisibility(View.INVISIBLE);
                    imageView1.setVisibility(View.INVISIBLE);
                }
            }
        });
        Bundle bundle = getArguments();
        pageNumber = bundle.get("pageNumber").toString();


       /* Picasso.with(getContext()).load(urlStr+pageNumber+extention)
                .placeholder(R.drawable.ts_loading_circle)
                .error(R.drawable.cloud_error_120)
                .into(imageView);*/
        Picasso.with(getContext()).load(urlStr + pageNumber + extention)
                .placeholder(R.drawable.loadicon)
                .error(R.drawable.loadicon)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(imageView);

        if (NetworkState.ConnectionAvailable(getContext())) {
            fetchData = new FetchData(pageNumber);
            fetchData.setNetworkResponse(this);
            fetchData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
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


        isplaying = false;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if(mediaPlayer!=null && !mediaPlayer.isPlaying()) {*/
                if (!isplaying) {
                    try {
                        // progressBar.setVisibility(View.VISIBLE);
                        progressDialog = ProgressDialog.show(getActivity(), "", progressMsg, false, false);
                        //mediaPlayer.prepareAsync();
                           /* Intent intent=new Intent(getActivity(),MediaPlayerService.class);
                            intent.setAction("play");
                            Bundle b=new Bundle();
                            //b.putString("pn",playSounds(Integer.valueOf(pageNumber),shekhName));
                            b.putString("pn", MediaPLayerUtils.createUrl(Integer.valueOf(pageNumber),shekhName));
                            b.putInt("num",Integer.valueOf(pageNumber));
                            b.putString("sh_name",shekhName);
                            intent.putExtra("pn",b);
                            getActivity().startService(intent);*/
                        ServiceUtils.startMediaService(getActivity(), pageNumber, shekhName);
                        isplaying = !isplaying;
                        fab.setImageResource(R.drawable.icon_pause);
                    } catch (Exception e) {
                    }
                /*}else if(mediaPlayer!=null && mediaPlayer.isPlaying()) {*/
                } else if (isplaying) {
                    //mediaPlayer.stop();
                    // progressBar.setVisibility(View.INVISIBLE);
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    getActivity().stopService(new Intent(getActivity(), MediaPlayerService.class));
                    isplaying = !isplaying;
                    fab.setImageResource(R.drawable.icon_play);
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
                // getActivity().startService(new Intent(getActivity(), MediaPlayerService.class));
                Intent intent = new Intent(getActivity(), TafserActivity.class);
                Bundle b = new Bundle();
                b.putString("pageNumber", pageNumber);
                intent.putExtra("bundle", b);
                getActivity().startActivity(intent);
            }
        });
        //fab.setVisibility(View.INVISIBLE);

        //playSounds(Integer.valueOf(pageNumber), shekhName);

        return view;
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
        if (isAdded() && getActivity() != null) {

        }

    }

    @Override
    public void OnFailure(boolean Failure) {

    }

    private TextView createTextView(int i) {
        TextView textView = new TextView(getContext());
        textView.setTextSize(25);
        textView.setPadding(0, 0, 0, 0);

        if (i == 0) {
            ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(lparams);
            textView.setGravity(Gravity.CENTER);
        } else {
            ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(lparams);
        }

        return textView;
    }

    /*@Override
    public void passData() {
        if(mediaPlayer!=null&&mediaPlayer.isPlaying())
            mediaPlayer.stop();

        if(isAdded()&&getActivity()!=null && progressDialog!=null && !progressDialog.isShowing())
            getActivity().stopService(new Intent(getActivity(),MediaPlayerService.class));


        if(progressDialog!=null && progressDialog.isShowing())
            progressDialog.dismiss();
    }*/

    @Override
    public void cancelDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void stopMediaService() {

        if(bottomNavigationView!=null)
            bottomNavigationView.setVisibility(View.INVISIBLE);

        if (fab != null)
            fab.setVisibility(View.INVISIBLE);

        if (fab2 != null)
            fab2.setVisibility(View.INVISIBLE);

        if (Main2Activity.appBarLayout != null)
            Main2Activity.appBarLayout.setVisibility(View.INVISIBLE);

        if (isAdded() && getActivity() != null)
            getActivity().stopService(new Intent(getActivity(), MediaPlayerService.class));


        try {
            pageN =(604-Integer.valueOf(MySharedPreferences.getUserSetting("pageNumber")))+"";
        }catch (Exception e){
            pageN="";
        }

        /*Menu menu = Main2Activity.mainMenu;
        if(menu!=null && !Main2Activity.isBookmarked) {
            MenuItem menuItem = menu.getItem(1);
            String s = MySharedPreferences.getUserSetting("pageNumber");
            try {
                if (pageN.equals(pageNumber + "")) {
                    menuItem.setIcon(R.drawable.bookmark);
                    Main2Activity.isBookmarked=true;
                } else {
                    menuItem.setIcon(R.drawable.unbookmark);
                }
            } catch (Exception e) {
            }
        }*/

    }

    @Override
    public void playNextOne() {
        if (getActivity() != null) {
          /*  Intent intent = new Intent(getActivity(), MediaPlayerService.class);
            intent.setAction("play");
            Bundle b = new Bundle();
            b.putString("pn", MediaPLayerUtils.createUrl(Integer.valueOf(pageNumber), shekhName));
            b.putInt("num", Integer.valueOf(pageNumber));
            b.putString("sh_name",shekhName);
            intent.putExtra("pn", b);
            getActivity().startService(intent);*/
            ServiceUtils.startMediaService(this.getActivity(), pageNumber, shekhName);
            isplaying = true;
        }
    }


    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(getActivity() , v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.bookmark_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.id1:
                        Toast.makeText(getActivity(),"تم الحفظ",Toast.LENGTH_SHORT).show();
                        MySharedPreferences.setUserSetting("pageNumber",Main2Activity.viewPager.getCurrentItem()+"");
                        bottomNavigationView.getMenu().getItem(2).setIcon(R.drawable.nav_bookmark);
                        return true;
                    case R.id.id2:
                        //Toast.makeText(AzkarActivity.this,"hi 2",Toast.LENGTH_SHORT).show();
                        String s=MySharedPreferences.getUserSetting("pageNumber");
                        try{
                            int index=Integer.valueOf(s);
                            Main2Activity.viewPager.setCurrentItem(index);
                        }catch (Exception e){
                            Main2Activity.viewPager.setCurrentItem(0);
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }

}

