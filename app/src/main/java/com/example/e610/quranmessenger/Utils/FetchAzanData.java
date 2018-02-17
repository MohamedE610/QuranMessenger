package com.example.e610.quranmessenger.Utils;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.e610.quranmessenger.R;
import com.example.e610.quranmessenger.Utils.GPSUtils.MyAlertDialog;
import com.example.e610.quranmessenger.Utils.GPSUtils.MyGPSTracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by E610 on 21/09/2016.
 */

public class FetchAzanData extends AsyncTask<Void,Void,String> {

    //private final static String BasicUrl = "http://api.aladhan.com/timingsByCity?city=Cairo&country=Egypt&method=5";
    //private String BasicUrl = "http://api.aladhan.com/calendar?latitude=29.9875777&longitude=30.9520906";
    private String BasicUrl = "http://api.aladhan.com/calendar?";
    private String latitude="latitude=";
    private String longitude="&longitude=";
    private String method="&method=5";

    String lati,longi;
    String finalUrl=BasicUrl+latitude+lati+longitude+longi+method;
    Context context;
    public FetchAzanData(Context ctx) {
        context=ctx;
        lati=longi="0";
    }

    NetworkResponse networkResponse;

    public void setNetworkResponse(NetworkResponse networkResponse) {
        this.networkResponse = networkResponse;
    }

    public String Fetching_Data(String UrlKey) {
        HttpURLConnection urlConnect = null;
        BufferedReader reader = null;
        String JsonData = null;
        try {

            String UrlWithKey = UrlKey;
            URL url = new URL(UrlWithKey);
            urlConnect = (HttpURLConnection) url.openConnection();
            urlConnect.setRequestMethod("GET");
            urlConnect.connect();
            InputStream inputStream = urlConnect.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            JsonData = buffer.toString();
            Log.d("JSON", JsonData);
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnect != null) {
                urlConnect.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }
        return JsonData;
    }

    MyGPSTracker myGPSTracker;
    private void getLatLong(){
         myGPSTracker = new MyGPSTracker(context);

        // check if GPS location can get
        if (myGPSTracker.canGetLocation()) {
            lati=myGPSTracker.getLatitude()+"";
            MySharedPreferences.setUserSetting("gps_lat",lati);
            longi= myGPSTracker.getLongitude()+"";
            MySharedPreferences.setUserSetting("gps_long",longi);
            finalUrl=BasicUrl+latitude+lati+longitude+longi+method;
            Log.d("Your Location", "latitude:" + myGPSTracker.getLatitude() + ", longitude: " + myGPSTracker.getLongitude());
        } else {
            // Can't get user's current location
            MyAlertDialog alert=new MyAlertDialog();
            alert.showAlertDialog(context, "GPS Status",
                    "Couldn't get location information. Please enable GPS",
                    false);
        }
    }

    @Override
    protected void onPreExecute() {
        MySharedPreferences.setUpMySharedPreferences(context,context.getString(R.string.shared_pref_file_name));
        String latStr=MySharedPreferences.getUserSetting("gps_lat");
        String longStr=MySharedPreferences.getUserSetting("gps_long");
        if(latStr.equals("")||latStr.equals("0.0"))
            getLatLong();
        else
            finalUrl=BasicUrl+latitude+latStr+longitude+longStr+method;

        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {

        String JsonData = "";
        try {
            /*lati=myGPSTracker.getLatitude()+"";
            longi= myGPSTracker.getLongitude()+"";
            finalUrl=BasicUrl+latitude+lati+longitude+longi+method;*/

            JsonData = Fetching_Data(finalUrl);
            if(JsonData==null||JsonData.equals("")) {
                networkResponse.OnFailure(true);
            }
        }catch (Exception ex){
            networkResponse.OnFailure(true);
        }

        return JsonData;
    }

    @Override
    protected void onPostExecute(String JsonData) {
           super.onPostExecute(JsonData);
            networkResponse.OnSuccess(JsonData);
        //Toast.makeText(MainActivity.this, string, Toast.LENGTH_SHORT).show();
    }

}


