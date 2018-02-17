package com.example.e610.quranmessenger.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.e610.quranmessenger.Adapter.SurahAdapter;
import com.example.e610.quranmessenger.Activities.Main2Activity;
import com.example.e610.quranmessenger.Models.SurahOfQuran.SurahOfQuran;
import com.example.e610.quranmessenger.R;
import com.example.e610.quranmessenger.Utils.FetchSurahData;
import com.example.e610.quranmessenger.Utils.NetworkResponse;
import com.example.e610.quranmessenger.Utils.NetworkState;
import com.google.gson.Gson;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FahrsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FahrsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FahrsFragment extends Fragment implements NetworkResponse,SurahAdapter.RecyclerViewClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FahrsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FahrsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FahrsFragment newInstance(String param1, String param2) {
        FahrsFragment fragment = new FahrsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    SurahOfQuran surahOfQuran;
    FetchSurahData fetchSurahData;
    SurahAdapter surahAdapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    final static public int[] surahPageNumbers={ 1,2,50,77,106,128,151,177,187,208,221,235,249,255,262,267,282,293,305,312,322,332,342,350,359,367,377,385,396,404,411,415,418,428,434,440,
            446,453,458,467,477,483,489,496,499,502,507,511,515,518,520,523,526,528,531,534,537,542,545,549,551,553,554,556,558,560,562,564,566,568,570,
            572,574,575,577,578,580,582,583,585,586,587,587,589,590,591,591,592,593,594,595,595,596,596,597,597,598,598,599,599,600,600,601,
            601,601,602,602,602,603,603,603,604,604,604 };


    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_fahrs, container, false);

        recyclerView=(RecyclerView)view.findViewById(R.id.recycler);
        progressBar=(ProgressBar)view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        if(NetworkState.ConnectionAvailable(getActivity())){
            fetchSurahData=new FetchSurahData();
            fetchSurahData.setNetworkResponse(this);
            fetchSurahData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

        return view;
    }



    @Override
    public void OnSuccess(String JsonData) {
        progressBar.setVisibility(View.INVISIBLE);
        Gson gson=new Gson();
        surahOfQuran=gson.fromJson(JsonData,SurahOfQuran.class);
        surahAdapter=new SurahAdapter(surahOfQuran,getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        surahAdapter.setClickListener(this);
        recyclerView.setAdapter(surahAdapter);


    }

    @Override
    public void OnFailure(boolean Failure) {

    }

    private void startMain2ActivtiyList(int position){
        // Toast.makeText(this,"hi",Toast.LENGTH_LONG).show();
        Intent intent=new Intent(getActivity(),Main2Activity.class);
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
        getActivity().finish();

    }

    @Override
    public void ItemClicked(View v, int position) {
        startMain2ActivtiyList(position);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
