package com.example.e610.quranmessenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.e610.quranmessenger.Services.MediaPlayerService;
import com.example.e610.quranmessenger.Utils.MySharedPreferences;
import com.example.e610.quranmessenger.Utils.ServiceUtils;

public class AzkarActivity extends AppCompatActivity {

    /*private Object mActionMode;

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.id1:
                    Toast.makeText(AzkarActivity.this,"hi 1",Toast.LENGTH_SHORT).show();
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                case R.id.id2:
                    Toast.makeText(AzkarActivity.this,"hi 2",Toast.LENGTH_SHORT).show();
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };*/

    /*
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.context_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.id1:
                        Toast.makeText(AzkarActivity.this,"hi 1",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.id2:
                        Toast.makeText(AzkarActivity.this,"hi 2",Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }
*/



    boolean isplaying=false;
    boolean isplaying1=false;
    String azkarStr="";
    private FloatingActionButton fabSound;
    private FloatingActionButton fabSound1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_azkar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*String state= MySharedPreferences.getMediaPlayerState();
        if(state.equals("1")){
            fabSound.setImageResource(R.drawable.icon_pause);
            fabSound.setImageResource(R.drawable.icon_pause);
            isplaying=true;
        }else {
            fabSound.setImageResource(R.drawable.icon_play);
            fabSound.setImageResource(R.drawable.icon_play);
            isplaying=false;
        }*/


        CardView cardAm=(CardView) findViewById(R.id.card_am);
        CardView cardPm=(CardView) findViewById(R.id.card_pm);
        cardAm.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                azkarStr="am";
                /*******/
                //openContextMenu(v);
                /*******/

                //showPopup(view);
                /*if (mActionMode != null) {
                    return ;
                }

                // Start the CAB using the ActionMode.Callback defined above
                mActionMode = startActionMode(mActionModeCallback);
                view.setSelected(true);*/

                return false;
            }
        });

        cardPm.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                azkarStr="pm";
                /*******/
                //openContextMenu(v);
                /*******/

                //showPopup(view);
                /*if (mActionMode != null) {
                    return ;
                }

                // Start the CAB using the ActionMode.Callback defined above
                mActionMode = startActionMode(mActionModeCallback);
                view.setSelected(true);*/
                return false;
            }
        });




        FloatingActionButton fabRead = (FloatingActionButton) findViewById(R.id.fab_read);
        fabRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                azkarStr="am";
                azkarMethod(azkarStr,"1");
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                /*azkarStr="am";
                openContextMenu(view);*/
                //showPopup(view);
                /*if (mActionMode != null) {
                    return ;
                }

                // Start the CAB using the ActionMode.Callback defined above
                mActionMode = startActionMode(mActionModeCallback);
                view.setSelected(true);*/
            }
        });

        fabSound = (FloatingActionButton) findViewById(R.id.fab_sound);
        fabSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //azkarStr="am";
               String state=MySharedPreferences.getMediaPlayerState();
                if(!isplaying && state.equals("0") ) {
                    isplaying=!isplaying;
                    azkarStr = "am";
                    Intent okIntent1 = new Intent(AzkarActivity.this, MediaPlayerService.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("azkar", azkarStr);
                    bundle1.putString("method", "0");
                    okIntent1.putExtra("bundle", bundle1);
                    okIntent1.setAction("azkar");
                    startService(okIntent1);
                    fabSound.setImageResource(R.drawable.icon_pause);
                }else  if(!isplaying && state.equals("1") ) {
                    /*********/
                    ServiceUtils.stopMediaService(AzkarActivity.this);
                    fabSound1.setImageResource(R.drawable.icon_play);
                    isplaying1=false;
                    /*********/

                    isplaying=!isplaying;
                    azkarStr = "am";
                    Intent okIntent1 = new Intent(AzkarActivity.this, MediaPlayerService.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("azkar", azkarStr);
                    bundle1.putString("method", "0");
                    okIntent1.putExtra("bundle", bundle1);
                    okIntent1.setAction("azkar");
                    startService(okIntent1);
                    fabSound.setImageResource(R.drawable.icon_pause);
                } else if(isplaying && state.equals("1")){
                    isplaying=!isplaying;
                    Intent  intent = new Intent(AzkarActivity.this, MediaPlayerService.class);
                    intent.setAction("cancel");
                    startService(intent);
                    fabSound.setImageResource(R.drawable.icon_play);
                }
                //azkarMethod(azkarStr,"0");
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                azkarStr="pm";
                openContextMenu(view);*/
                //showPopup(view);
               /* if (mActionMode != null) {
                    return ;
                }

                // Start the CAB using the ActionMode.Callback defined above
                mActionMode = startActionMode(mActionModeCallback);
                view.setSelected(true);*/
            }
        });

        FloatingActionButton fabRead1 = (FloatingActionButton) findViewById(R.id.fab_read1);
        fabRead1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                azkarStr="pm";
                azkarMethod(azkarStr,"1");
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                /*azkarStr="am";
                openContextMenu(view);*/
                //showPopup(view);
                /*if (mActionMode != null) {
                    return ;
                }

                //Start the CAB using the ActionMode.Callback defined above
                mActionMode = startActionMode(mActionModeCallback);
                view.setSelected(true);*/
            }
        });

         fabSound1 = (FloatingActionButton) findViewById(R.id.fab_sound1);
        fabSound1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String state=MySharedPreferences.getMediaPlayerState();
                if (!isplaying1&&state.equals("0")) {
                    isplaying1=!isplaying1;
                    azkarStr = "pm";
                    Intent okIntent1 = new Intent(AzkarActivity.this, MediaPlayerService.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("azkar", azkarStr);
                    bundle1.putString("method", "0");
                    okIntent1.putExtra("bundle", bundle1);
                    okIntent1.setAction("azkar");
                    startService(okIntent1);
                    fabSound1.setImageResource(R.drawable.icon_pause);
                }else if (!isplaying1&&state.equals("1")) {
                    /*********/
                    ServiceUtils.stopMediaService(AzkarActivity.this);
                    fabSound.setImageResource(R.drawable.icon_play);
                    isplaying=false;
                    /*********/

                    isplaying1=!isplaying1;
                    azkarStr = "pm";
                    Intent okIntent1 = new Intent(AzkarActivity.this, MediaPlayerService.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("azkar", azkarStr);
                    bundle1.putString("method", "0");
                    okIntent1.putExtra("bundle", bundle1);
                    okIntent1.setAction("azkar");
                    startService(okIntent1);
                    fabSound1.setImageResource(R.drawable.icon_pause);
                }else if(isplaying1&&state.equals("1")) {
                    isplaying1=!isplaying1;
                    Intent intent = new Intent(AzkarActivity.this, MediaPlayerService.class);
                    intent.setAction("cancel");
                    startService(intent);
                    fabSound1.setImageResource(R.drawable.icon_play);
                }
                //azkarStr="pm";
                //azkarMethod(azkarStr,"0");
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                azkarStr="pm";
                openContextMenu(view);*/
                //showPopup(view);
               /* if (mActionMode != null) {
                    return ;
                }
                // Start the CAB using the ActionMode.Callback defined above
                mActionMode = startActionMode(mActionModeCallback);
                view.setSelected(true);*/
            }
        });

        /*registerForContextMenu(cardAm);
        registerForContextMenu(cardPm);*/

    }

    ContextMenu contextMenu;
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu,menu);
        menu.setHeaderTitle("الاذكار");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        /*Intent intent=new Intent(this,AzkarDetailedActivity.class);
        Bundle bundle=new Bundle();*/
        switch (item.getItemId()) {
            case R.id.id1:
                // your first action code
                azkarMethod(azkarStr,"0");
               /* bundle.putString("azkar",azkarStr);
                bundle.putString("method","0");
                intent.putExtra("bundle",bundle);
                startActivity(intent);
                Toast.makeText(AzkarActivity.this,"AM",Toast.LENGTH_SHORT).show();*/
                return true;
            case R.id.id2:
                // your second action code
                azkarMethod(azkarStr,"1");
               /* bundle.putString("azkar",azkarStr);
                bundle.putString("method","1");
                intent.putExtra("bundle",bundle);
                startActivity(intent);
                Toast.makeText(AzkarActivity.this,"PM",Toast.LENGTH_SHORT).show();*/
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.azkar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.azkar_settings) {
            Intent intent= new Intent(this,SettingsActivity.class);
            intent.setAction("azkar_settings");
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    private void azkarMethod(String azkarStr ,String method){
        Intent intent=new Intent(this,AzkarDetailedActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("azkar",azkarStr);
        bundle.putString("method",method);
        intent.putExtra("bundle",bundle);
        startActivity(intent);
    }

}

