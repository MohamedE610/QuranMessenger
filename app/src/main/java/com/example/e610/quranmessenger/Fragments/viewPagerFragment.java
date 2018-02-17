package com.example.e610.quranmessenger.Fragments;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e610.quranmessenger.Models.PageOfQuran.PageOfQuran;
import com.example.e610.quranmessenger.R;
import com.example.e610.quranmessenger.Utils.FetchData;
import com.example.e610.quranmessenger.Utils.NetworkResponse;
import com.example.e610.quranmessenger.Utils.NetworkState;
import com.google.gson.Gson;
import com.nex3z.flowlayout.FlowLayout;


public class viewPagerFragment extends Fragment implements NetworkResponse{

    LayoutInflater layoutInflater;
    FlowLayout flowLayout;
    ViewGroup viewGroup;
    LinearLayout linearLayout;
    View view;
    FetchData fetchData;
    int currentSurahNumber;
    int currentJuzNumber;
    boolean isSajda;
    public viewPagerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        layoutInflater=inflater;
        viewGroup=container;
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_view_pager, container, false);
        linearLayout=(LinearLayout) view.findViewById(R.id.content);
        //flowLayout=(FlowLayout) view.findViewById(R.id.content);
        //textView=(TextView)view.findViewById(R.id.txt);
        Bundle bundle=getArguments();
        String pageNumber=bundle.get("pageNumber").toString();
        //textView.setText(pageNumber);

        if(NetworkState.ConnectionAvailable(getContext())) {
            fetchData = new FetchData(pageNumber);
            fetchData.setNetworkResponse(this);
            fetchData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else {
            Toast.makeText(getContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
        }

        return view ;
    }

    @Override
    public void OnSuccess(String JsonData) {
        //textView.setText(JsonData);
        String ayahStr="";
        if(isAdded() && getActivity()!=null) {
            Gson gson = new Gson();
            PageOfQuran pageOfQuran = gson.fromJson(JsonData, PageOfQuran.class);

            TextView textView=(TextView) view.findViewById(R.id.surah_name_txt);
            TextView textView1=(TextView) view.findViewById(R.id.juz_txt);
            textView.setText(pageOfQuran.getData().getAyahs().get(0).getSurah().getName());
            textView1.setText(pageOfQuran.getData().getAyahs().get(0).getJuz().toString());

            for (int i = 0; i < pageOfQuran.getData().getAyahs().size(); i++) {
                if(pageOfQuran.getData().getAyahs().get(i).getNumberInSurah()==1){
                    if(!ayahStr.equals("")) {
                        TextView AyahTextView = createTextView(1);
                        AyahTextView.setText(ayahStr);
                        linearLayout.addView(AyahTextView);
                    }
                    TextView surahNameText=createTextView(0);
                    String nameStr=getString(R.string.r_Ayah)+pageOfQuran.getData().getAyahs().get(i).getSurah().getName()+getString(R.string.l_Ayah);
                    surahNameText.setText(nameStr);
                    linearLayout.addView(surahNameText);
                    TextView bsmAllah=createTextView(0);
                    bsmAllah.setText(getString(R.string.bsm_allah));
                    linearLayout.addView(bsmAllah);
                    String[] strs=pageOfQuran.getData().getAyahs().get(i).getText().split(" ");
                    String firstAyah="";
                    for (int j = 0; j < strs.length; j++) {
                        if(j>3)
                            firstAyah+=strs[j]+" ";
                    }
                    firstAyah+=getString(R.string.r_Ayah)+pageOfQuran.getData().getAyahs().get(i).getNumberInSurah()+getString(R.string.l_Ayah);
                    ayahStr=firstAyah;
                    /*TextView firstAyahText=createTextView(1);
                    firstAyahText.setText(firstAyah);
                    flowLayout.addView(firstAyahText);*/
                }else {
                   /*String ayahStr="";
                    String[] strs=pageOfQuran.getData().getAyahs().get(i).getText().split(" ");
                    for (int j = 0; j < strs.length; j++) {
                            ayahStr+=strs[j]+" ";
                    }
                    ayahStr+=getString(R.string.r_Ayah)+pageOfQuran.getData().getAyahs().get(i).getNumberInSurah()+getString(R.string.l_Ayah);
                    TextView AyahTextView=createTextView(1);
                    AyahTextView.setText(ayahStr);
                    linearLayout.addView(AyahTextView);*/

                    String[] strs=pageOfQuran.getData().getAyahs().get(i).getText().split(" ");
                    for (int j = 0; j < strs.length; j++) {
                        ayahStr+=strs[j]+" ";
                }
                    ayahStr+=getString(R.string.r_Ayah)+pageOfQuran.getData().getAyahs().get(i).getNumberInSurah()+getString(R.string.l_Ayah);
             }
        }
            /*for (int i = 0; i < ayahStr.length() ; i++) {

            }*/
            TextView AyahTextView=createTextView(1);
            AyahTextView.setText(ayahStr);
            linearLayout.addView(AyahTextView);
        }

    }

    @Override
    public void OnFailure(boolean Failure) {

    }

    private TextView createTextView(int i){

        TextView textView=new TextView(getContext());
        textView.setTextSize(25);
        textView.setPadding(0,0,0,10);

        if(i==0){
            ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(lparams);
            textView.setGravity(Gravity.CENTER);
        }else{
            ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(lparams);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
        }

        return textView;
    }
}
