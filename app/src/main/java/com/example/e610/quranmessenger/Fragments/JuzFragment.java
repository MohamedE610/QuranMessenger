package com.example.e610.quranmessenger.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.e610.quranmessenger.Adapter.SurahAdapter;
import com.example.e610.quranmessenger.Activities.Main2Activity;
import com.example.e610.quranmessenger.Models.SurahOfQuran.Surah;
import com.example.e610.quranmessenger.Models.SurahOfQuran.SurahOfQuran;
import com.example.e610.quranmessenger.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JuzFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JuzFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JuzFragment extends Fragment implements SurahAdapter.RecyclerViewClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public JuzFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JuzFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JuzFragment newInstance(String param1, String param2) {
        JuzFragment fragment = new JuzFragment();
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

    String [] names={"الأول","الثاني","الثالث","الرابع","الخامس","السادس","السابع","الثامن","التاسع", "العاشر"
            ,"الحادي عشر","الثاني عشر","الثالث عشر","الرابع عشر","الخامس عشر","السادس عشر","السابع عشر","الثامن عشر","التاسع عشر","العشرون",
            "الحادي والعشرون","الثاني والعشرون","الثالث والعشرون","الرابع والعشرون","الخامس والعشرون","السادس والعشرون"
            ,"السابع والعشرون","الثامن والعشرون","التاسع والعشرون","الثلاثون"};

    private void setUpData(){
     surahOfQuran=new SurahOfQuran();
        surahOfQuran.data=new ArrayList<>();
        for (int i = 0; i <30 ; i++) {
            Surah surah=new Surah();
            surah.name="الجزء "+names[i];
            surah.number=i;
           surahOfQuran.data.add(surah);
        }

    }
    SurahOfQuran surahOfQuran;
    SurahAdapter surahAdapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;

   public final static int[] arr = {1,22,42,62,82,102,122,142,162,182,202,222,242,262,282,302,322,342,362,382,
            402,422,442,462,482,502,522,542,562,582};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_juz, container, false);
        setUpData();
        surahAdapter=new SurahAdapter(surahOfQuran,getActivity());
        recyclerView=(RecyclerView)view.findViewById(R.id.recycler1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        surahAdapter.setClickListener(this);
        recyclerView.setAdapter(surahAdapter);
        return view;
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
        if(position<arr.length){
            i=arr[position]-1;
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
