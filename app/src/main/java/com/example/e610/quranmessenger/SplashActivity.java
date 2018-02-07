package com.example.e610.quranmessenger;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

public class SplashActivity extends AppCompatActivity {


    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    private final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE=9;
    private final int MY_PERMISSIONS_REQUEST_PROCESS_OUTGOING_CALLS=8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        checkRuntimePermitions();

         /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                //Create an Intent that will start the Menu-Activity.
                Intent mainIntent = new Intent(SplashActivity.this,Main2Activity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);



    }


    private void checkRuntimePermitions(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_PHONE_STATE) &&ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.PROCESS_OUTGOING_CALLS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.PROCESS_OUTGOING_CALLS},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);

                /*ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS},
                        MY_PERMISSIONS_REQUEST_PROCESS_OUTGOING_CALLS);*/

                // MY_PERMISSIONS_REQUEST_READ_PHONE_STATE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    int ln=grantResults.length;



                    // permission was granted, yay!

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_PROCESS_OUTGOING_CALLS: {
                int ln=grantResults.length;
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
