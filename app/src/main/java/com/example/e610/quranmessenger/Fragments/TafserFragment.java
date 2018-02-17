package com.example.e610.quranmessenger.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e610.quranmessenger.R;
import com.example.e610.quranmessenger.Utils.FetchTafserData;
import com.example.e610.quranmessenger.Utils.NetworkResponse;
import com.example.e610.quranmessenger.Utils.NetworkState;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TafserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TafserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TafserFragment extends Fragment implements NetworkResponse {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TafserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TafserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TafserFragment newInstance(String param1, String param2) {
        TafserFragment fragment = new TafserFragment();
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

    String pageNumber;
    TextView pageNumText;
    TextView tafserText;
    ProgressBar progressBar;
    FetchTafserData fetchTafserData;
    private FloatingActionButton fab;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_tafser, container, false);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                share(titleStr,contentStr);
            }
        });

        //Intent intent=getIntent();
        Bundle bundle=new Bundle();
        //bundle=intent.getBundleExtra("bundle");
        bundle=getArguments();
        pageNumber=bundle.getInt("pageNumber")+"";
        pageNumText=(TextView) view.findViewById(R.id.name_txt);
        tafserText=(TextView) view.findViewById(R.id.tafser_txt);
        progressBar=(ProgressBar) view.findViewById(R.id.progressBarTafser);
        progressBar.setVisibility(View.VISIBLE);

        if(NetworkState.ConnectionAvailable(getActivity())) {
            try {
                fetchTafserData = new FetchTafserData(pageNumber);
                fetchTafserData.setNetworkResponse(this);
                fetchTafserData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } catch (Exception e) {
                pageNumText.setText(pageNumber);
                tafserText.setText("حدث خطأ اثناء التحميل ");
            }
        }else{
            Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_LONG).show();
        }

        return view ;
    }

    private void share(String title,String content){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody =content;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent,"Share via" ));
    }
    String contentStr="";
    String titleStr="";

    @Override
    public void OnSuccess(String JsonData) {
        if(JsonData!=null && !JsonData.equals("")) {
            progressBar.setVisibility(View.INVISIBLE);
            pageNumText.setText(pageNumber);
            tafserText.setText(JsonData);
            contentStr=JsonData;
            fab.setVisibility(View.VISIBLE);
        }
        else {
            pageNumText.setText(pageNumber);
            tafserText.setText("حدث خطأ اثناء التحميل ");
        }
    }

    @Override
    public void OnFailure(boolean Failure) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);
                pageNumText.setText(pageNumber);
                tafserText.setText("حدث خطأ اثناء التحميل ");
            }
        });
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
