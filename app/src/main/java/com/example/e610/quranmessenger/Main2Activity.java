package com.example.e610.quranmessenger;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.e610.quranmessenger.Models.PrayerTimes.PrayerTimes;
import com.example.e610.quranmessenger.Services.MediaPlayerService;
import com.example.e610.quranmessenger.Utils.MySharedPreferences;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {


  public  interface PassData{
        void cancelDialog();
        void stopMediaService();
      void playNextOne();
    }

    /**************************/
    /*protected PowerManager.WakeLock mWakeLock;*/
    private ViewPager viewPager;
    PassData passData;
    OnPageChangeListener  onPageChangeListener= new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
             passData=(PassData)adapter.getItem(position);
             passData.stopMediaService();

            if(position<adapter.getCount()-1) {
                passData = (PassData) adapter.getItem(position+1);
                passData.stopMediaService();
            }

            if(position>1){
                passData = (PassData) adapter.getItem(position-1);
                passData.stopMediaService();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
/*    FloatingActionButton fab;
    FloatingActionButton fab2;*/
    //String surahPageNum="-1";

    Intent intent;

    @Override
    public void onAttachFragment(Fragment fragment) {
       // passData=(PassData)fragment;
        super.onAttachFragment(fragment);
    }

    /* boolean isplaying;
    String shekhName="";
    //String[]shekhNameArray={"","","","","",""};
    final String basicUrl="http://www.quranmessenger.life/sound/";
    final String extention=".mp3";
    final String slash="/";
    private void playSounds(int page ,String name ){

        String url=basicUrl+name+slash;
        if(page<10){
          url+="00"+page+extention;
        }else if(page<100){
            url+="0"+page+extention;
        }else {
            url+=page+extention;
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
                    mp.start();
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Toast.makeText(Main2Activity.this,"ملف الصوت غير متاح حاليا",Toast.LENGTH_LONG).show();
                    return false;
                }
            });
            mediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
        }catch (Exception e){}
    }*/

    /**************************/
    @Override
    public void onDestroy() {
        //this.mWakeLock.release();
        //stopService(new Intent(this,MediaPlayerService.class));
        //Log.d("asdasd","asdasd");

        super.onDestroy();
    }


    private void loadAds(){

        /*// Code to get device id
        String androidIdDevice = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);*/

        AdView mAdView = (AdView) findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
       /* AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(androidIdDevice)
                .build();*/
        mAdView.loadAd(adRequest);

    }
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if("cancelDialog".equals(action)) {
                Bundle bundle=intent.getBundleExtra("b");
                if(bundle!=null){
                    int page_num=bundle.getInt("num");
                    passData=(PassData)adapter.getItem(604-page_num);
                    passData.cancelDialog();
                }
                // Do your work
            }
        }
    };

    private final BroadcastReceiver broadcastReceiver1 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
             if("playNextOne".equals(action)) {
                Bundle bundle1 = intent.getBundleExtra("b");
                if (bundle1 != null) {
                    int page_num = bundle1.getInt("num");
                    page_num++;
                    viewPager.setCurrentItem(604 - page_num);
                    passData = (PassData) adapter.getItem(604 - page_num);
                    passData.playNextOne();
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Android disable screen timeout while app is running
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        loadAds();

        /**************************/
        /* This code together with the one in onDestroy()
         * will make the screen be always on until this Activity gets destroyed. */
        /*final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire();*/

        /*fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
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
                if(isplaying) {
                    playSounds(604-viewPager.getCurrentItem(), shekhName);
                }else{
                    if(mediaPlayer!=null){
                        mediaPlayer.stop();
                        mediaPlayer.release();
                    }
                }
            }
        });
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
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

            }
        });
        //fab.setVisibility(View.INVISIBLE);*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        viewPager = (ViewPager) findViewById(R.id.viewpager1);
        viewPager=setupViewPager(viewPager);
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        viewPager.addOnPageChangeListener(onPageChangeListener);
        MySharedPreferences.setUpMySharedPreferences(this,"extraSetting");

        //shekhNameArray=(String [])getResources().getTextArray(R.array.shekhNamesValues);
       /* shekhName=MySharedPreferences.getUserSetting("shekhName");*/

        intent=getIntent();
        Bundle bundle=intent.getBundleExtra("fahrs");
        if(bundle==null) {
            if (MySharedPreferences.IsFirstTime()) {
                MySharedPreferences.FirstTime();
                viewPager.setCurrentItem(603);
            } else {
                String pageNumber = MySharedPreferences.getUserSetting("pageNumber");
                if (!pageNumber.equals(""))
                    viewPager.setCurrentItem(Integer.valueOf(pageNumber));
                else
                    viewPager.setCurrentItem(603);
            }
        }else{
            String surahPageNum=bundle.getString("fahrs");
            try {
                viewPager.setCurrentItem(603-Integer.valueOf(surahPageNum));
            }catch (Exception e){
                viewPager.setCurrentItem(603);
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        MySharedPreferences.setUpMySharedPreferences(this,"extraSetting");
        /*shekhName=MySharedPreferences.getUserSetting("shekhName");*/
        MySharedPreferences.setUserSetting("isBackground","0");
        IntentFilter intentFilter = new IntentFilter("cancelDialog");
        IntentFilter intentFilter1 = new IntentFilter("playNextOne");

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(broadcastReceiver, intentFilter);
        manager.registerReceiver(broadcastReceiver1, intentFilter1);

        super.onResume();
    }

    @Override
    protected void onPause() {
        MySharedPreferences.setUserSetting("isBackground","1");
        if(viewPager!=null) {
            MySharedPreferences.setUpMySharedPreferences(this, "extraSetting");
            MySharedPreferences.setUserSetting("pageNumber", viewPager.getCurrentItem() + "");
           /* if(mediaPlayer!=null) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }*/
        }

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.unregisterReceiver(broadcastReceiver);
        manager.unregisterReceiver(broadcastReceiver1);

        super.onPause();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent= new Intent(this,SettingsActivity.class);
            intent.setAction("main_settings");
            startActivity(intent);
            return true;
        }else if(id == R.id.action_fahrs){
            startActivity(new Intent(this,FahrsActivity.class));
            finish();
        }else if(id == R.id.action_prayer_times){
            startActivity(new Intent(this,PrayerTimesActivity.class));
        }



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_alfahrs) {
            // Handle the camera action
            startActivity(new Intent(this,FahrsActivity.class));
            finish();
        } else if (id == R.id.nav_alazan) {
            startActivity(new Intent(this,PrayerTimesActivity.class));
        } else if (id == R.id.nav_alazkar) {
            startActivity(new Intent(this,AzkarActivity.class));

        } else if (id == R.id.nav_setting) {
            Intent intent= new Intent(this,SettingsActivity.class);
            intent.setAction("main_settings");
            startActivity(intent);
        } else if (id == R.id.nav_facebook) {
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
            String facebookUrl = getFacebookPageURL(this);
            facebookIntent.setData(Uri.parse(facebookUrl));
            startActivity(facebookIntent);
        }else if (id == R.id.nav_insta) {
            Uri uri = Uri.parse("https://www.instagram.com/quranmessengerofficial/");
            Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
            likeIng.setPackage("com.instagram.android");
            try {
                startActivity(likeIng);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.instagram.com/quranmessengerofficial/")));
            }
        } else if (id == R.id.nav_share) {
            share();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void share(){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody =FACEBOOK_URL;

        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent,"Share via" ));
    }

    static String FACEBOOK_URL = "https://www.facebook.com/quranmessenger";
    public static String FACEBOOK_PAGE_ID = "178298802574861";

    //method to get the right URL to use in the intent
    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }

    ViewPagerAdapter adapter;
    private ViewPager setupViewPager(ViewPager viewPager) {

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (int i =604; i >=1 ; i--) {
            viewPagerFragment1 fragment = new viewPagerFragment1();
            Bundle bundle = new Bundle();
            bundle.putInt("pageNumber", i);
            fragment.setArguments(bundle);
            adapter.addFragment(fragment, i + "");
        }
        viewPager.setAdapter(adapter);

        return viewPager;
    }

    /*
    class ViewPagerAdapter extends FragmentPagerAdapter {
        this line cause logic error "display fragments in view pager when i reset it in wrong way"
        */

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }
}
