package com.example.e610.quranmessenger.Utils;


import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by E610 on 21/09/2016.
 */

public class FetchAzkarData extends AsyncTask<Void,Void,String> {

    final String azkar_sabah_txt="http://www.quranmessenger.life/azkar/azkar_sabah/1.txt";
    final String azkar_masaa_txt ="http://www.quranmessenger.life/azkar/azkar_masaa/1.txt";

    String azkarType;

    public FetchAzkarData(String azkarType) {
        this.azkarType=azkarType;
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
            //reader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
            //reader = new BufferedReader(new InputStreamReader(inputStream,"cp1256"));
            String sss="unicode";
             reader = new BufferedReader(new InputStreamReader(inputStream,sss));
            //

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


    @Override
    protected String doInBackground(Void... voids) {
        String JsonData = "";
        try {
            if(azkarType.equals("am")) {
                JsonData = Fetching_Data(azkar_sabah_txt);
            }else
                JsonData = Fetching_Data(azkar_masaa_txt);

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


