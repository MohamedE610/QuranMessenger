package com.example.e610.quranmessenger;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SearchViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.e610.quranmessenger.Adapter.SurahAdapter;
import com.example.e610.quranmessenger.Models.SurahOfQuran.SurahOfQuran;
import com.example.e610.quranmessenger.Utils.FetchSurahData;
import com.example.e610.quranmessenger.Utils.NetworkResponse;
import com.example.e610.quranmessenger.Utils.NetworkState;
import com.google.gson.Gson;
import com.google.gson.internal.Excluder;

public class FahrsActivity extends AppCompatActivity implements JuzFragment.OnFragmentInteractionListener
        ,FahrsFragment.OnFragmentInteractionListener {

   /* SurahOfQuran surahOfQuran;
    FetchSurahData  fetchSurahData;
    SurahAdapter surahAdapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    final static public int[] surahPageNumbers={ 1,2,50,77,106,128,151,177,187,208,221,235,249,255,262,267,282,293,305,312,322,332,342,350,359,367,377,385,396,404,411,415,418,428,434,440,
            446,453,458,467,477,483,489,496,499,502,507,511,515,518,520,523,526,528,531,534,537,542,545,549,551,553,554,556,558,560,562,564,566,568,570,
            572,574,575,577,578,580,582,583,585,586,587,587,589,590,591,591,592,593,594,595,595,596,596,597,597,598,598,599,599,600,600,601,
            601,601,602,602,602,603,603,603,604,604,604 };*/
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fahrs);


        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        TabLayout tabLayout =(TabLayout)findViewById(R.id.tab_layout);
        tabLayout.setSelected(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fahrs_container,new FahrsFragment()).commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==0){
                getSupportFragmentManager().beginTransaction().replace(R.id.fahrs_container,new FahrsFragment()).commit();
                }else if(tab.getPosition()==1){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fahrs_container,new JuzFragment()).commit();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

      /*  recyclerView=(RecyclerView)findViewById(R.id.recycler);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        if(NetworkState.ConnectionAvailable(this)){
            fetchSurahData=new FetchSurahData();
            fetchSurahData.setNetworkResponse(this);
            fetchSurahData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
        }*/
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id ==android.R.id.home) {
            Intent intent=new Intent(this,Main2Activity.class);
            intent.setAction("closeFahrs");
            startActivity(intent);
            finish();
            return true;
        } else if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public void OnSuccess(String JsonData) {
        progressBar.setVisibility(View.INVISIBLE);
        Gson gson=new Gson();
        surahOfQuran=gson.fromJson(JsonData,SurahOfQuran.class);
        surahAdapter=new SurahAdapter(surahOfQuran,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        surahAdapter.setClickListener(this);
        recyclerView.setAdapter(surahAdapter);


    }

    @Override
    public void OnFailure(boolean Failure) {

    }*/


    /*private void startMain2ActivtiyList(int position){
        // Toast.makeText(this,"hi",Toast.LENGTH_LONG).show();
        Intent intent=new Intent(FahrsActivity.this,Main2Activity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Bundle bundle=new Bundle();

        SurahAdapter.MyViewHolder[] asd=SurahAdapter.rowViews;
        if(asd!=null) {
            int z = asd.length;
        }

        int i=603;
        if(position<surahPageNumbers.length){
            i=surahPageNumbers[position]-1;
        }
        bundle.putString("fahrs",i+"");
        intent.putExtra("fahrs",bundle);
        startActivity(intent);
        finish();

    }*/
    /*@Override
    public void ItemClicked(View v, int position) {
        startMain2ActivtiyList(position);
    }*/

    @Override
    public void onBackPressed() {
            Intent intent=new Intent(this,Main2Activity.class);
            intent.setAction("closeFahrs");
            startActivity(intent);
            finish();
            super.onBackPressed();
    }


    private void startMain2ActivtiySearch(int position){
        // Toast.makeText(this,"hi",Toast.LENGTH_LONG).show();
        Intent intent=new Intent(FahrsActivity.this,Main2Activity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Bundle bundle=new Bundle();

       /* SurahAdapter.MyViewHolder[] asd=SurahAdapter.rowViews;
        if(asd!=null) {
            int z = asd.length;
        }*/

        int i=603;
        if(position<604&&position>0){
            i=(position-1);
        }
        bundle.putString("fahrs",i+"");
        intent.putExtra("fahrs",bundle);
        startActivity(intent);
        finish();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fahrs_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
        searchView.setQueryHint("ادخل رقم الصفحه");

        if (searchView != null) {
           // SearchViewCompat.setInputType(searchView, InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
        }
        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                int position=0;
                try {
                     position = Integer.valueOf(query);
                }catch (Exception e){
                    position=0;
                }

                startMain2ActivtiySearch(position);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                return false;
            }
        });
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
