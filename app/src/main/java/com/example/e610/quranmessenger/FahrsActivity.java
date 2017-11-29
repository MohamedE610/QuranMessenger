package com.example.e610.quranmessenger;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class FahrsActivity extends AppCompatActivity implements NetworkResponse ,SurahAdapter.RecyclerViewClickListener{

    SurahOfQuran surahOfQuran;
    FetchSurahData  fetchSurahData;
    SurahAdapter surahAdapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    final int[] surahPageNumbers={ 1,2,50,77,106,128,151,177,187,208,221,235,249,255,262,267,282,293,305,312,322,332,342,350,359,367,377,385,396,404,411,415,418,428,434,440,
            446,453,458,467,477,483,489,496,499,502,507,511,515,518,520,523,526,528,531,534,537,542,545,549,551,553,554,556,558,560,562,564,566,568,570,
            572,574,575,577,578,580,582,583,585,586,587,587,589,590,591,591,592,593,594,595,595,596,596,597,597,598,598,599,599,600,600,601,
            601,601,602,602,602,603,603,603,604,604,604 };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fahrs);


        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        recyclerView=(RecyclerView)findViewById(R.id.recycler);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        if(NetworkState.ConnectionAvailable(this)){
            fetchSurahData=new FetchSurahData();
            fetchSurahData.setNetworkResponse(this);
            fetchSurahData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id ==android.R.id.home) {
            startActivity(new Intent(this, Main2Activity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
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

    }

    @Override
    public void ItemClicked(View v, int position) {
       // Toast.makeText(this,"hi",Toast.LENGTH_LONG).show();
        Intent intent=new Intent(this,Main2Activity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Bundle bundle=new Bundle();

        int i=603;
        if(position<surahPageNumbers.length){
            i=surahPageNumbers[position]-1;
        }
        bundle.putString("fahrs",i+"");
        intent.putExtra("fahrs",bundle);
        startActivity(intent);
        finish();

    }

    @Override
    public void onBackPressed() {
            startActivity(new Intent(this,Main2Activity.class));
            super.onBackPressed();
    }


}
