package com.example.e610.quranmessenger;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e610.quranmessenger.Utils.FetchTafserData;
import com.example.e610.quranmessenger.Utils.NetworkResponse;
import com.example.e610.quranmessenger.Utils.NetworkState;

import java.util.ArrayList;
import java.util.List;

public class TafserActivity extends AppCompatActivity implements NetworkResponse ,TafserFragment.OnFragmentInteractionListener {

    int pageNum;
    /*String pageNumber;
    TextView pageNumText;
    TextView tafserText;
    ProgressBar progressBar;
    FetchTafserData fetchTafserData;
    private FloatingActionButton fab;*/

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tafser);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

         /*fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                *//*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*//*
                share(titleStr,contentStr);
            }
        });*/

        Intent intent=getIntent();
        Bundle bundle=new Bundle();
        bundle=intent.getBundleExtra("bundle");
        pageNum=Integer.valueOf(bundle.getString("pageNumber"));
       /* TafserFragment tafserFragment=new TafserFragment();
        tafserFragment.setArguments(bundle);*/
        //getSupportFragmentManager().beginTransaction().add(R.id.container,tafserFragment).commit();
        viewPager = (ViewPager) findViewById(R.id.viewpager1);
        viewPager=setupViewPager(viewPager);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.setCurrentItem(604-(pageNum-1));

       // pageNumber=bundle.getString("pageNumber");
       /* pageNumText=(TextView) findViewById(R.id.name_txt);
        tafserText=(TextView) findViewById(R.id.tafser_txt);
        progressBar=(ProgressBar) findViewById(R.id.progressBarTafser);
        progressBar.setVisibility(View.VISIBLE);*/

        /*if(NetworkState.ConnectionAvailable(this)) {
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
        }*/
    }

    private void share(String title,String content){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody =content;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent,"Share via" ));
    }
/*    String contentStr="";
    String titleStr="";*/

    @Override
    public void OnSuccess(String JsonData) {
        /*if(JsonData!=null && !JsonData.equals("")) {
            progressBar.setVisibility(View.INVISIBLE);
            pageNumText.setText(pageNumber);
            tafserText.setText(JsonData);
            contentStr=JsonData;
            fab.setVisibility(View.VISIBLE);
        }
        else {
            pageNumText.setText(pageNumber);
            tafserText.setText("حدث خطأ اثناء التحميل ");
        }*/
    }

    @Override
    public void OnFailure(boolean Failure) {
        /*runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);
                pageNumText.setText(pageNumber);
                tafserText.setText("حدث خطأ اثناء التحميل ");
            }
        });*/
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    ViewPagerAdapter adapter;
    private ViewPager setupViewPager(ViewPager viewPager) {

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (int i = 604; i >0 ; i--) {
            TafserFragment fragment = new TafserFragment();
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
}
