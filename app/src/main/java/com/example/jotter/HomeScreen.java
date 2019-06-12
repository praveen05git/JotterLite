package com.example.jotter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/*import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;*/

import java.io.File;
import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity {

    ListView lView;
    ArrayList<String> ar = new ArrayList<>();
    String fileName;
   // private InterstitialAd mInterstitialAd;
    NitSettings nitSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nitSettings = new NitSettings(this);

        setTitle("My Jots");

        Intent intent=getIntent();
        String pass=intent.getStringExtra("nitVal");

        if(pass.equals("One"))
 //           if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
//        if (nitSettings.loadNitState() == true)
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                setTheme(R.style.DarkTheme);
            }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_home_screen);
/*//ADs
implementation 'com.google.android.gms:play-services-ads:17.2.1'
        MobileAds.initialize(this,"ca-app-pub-3940256099942544~3347511713");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Toast.makeText(getApplicationContext(),"The interstitial wasn't loaded yet.",Toast.LENGTH_SHORT).show();
        }
*/

        lView = findViewById(R.id.File_list);

        try {
            File sdCard = Environment.getExternalStorageDirectory();
            File jotDir = new File(sdCard, "MyJots");

            for (File f : jotDir.listFiles()) {
                ar.add(f.getName());
            }

            ArrayAdapter<String> fileAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ar);

            lView.setAdapter(fileAdapter);

            lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    fileName = ar.get(position);
                    viewFiles(null);

                }
            });

            lView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    AlertDialog alt=new AlertDialog.Builder(HomeScreen.this).create();
                    alt.setTitle("Warning!");
                    fileName=ar.get(position);
                    alt.setMessage("Do you want to delete, "+fileName+"?");

                    alt.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            File f=new File(Environment.getExternalStorageDirectory()+"/MyJots"+"/"+fileName);
                            f.delete();
                            finish();
                            startActivity(getIntent());
                            Toast.makeText(getApplicationContext(),"File Deleted",Toast.LENGTH_SHORT).show();

                        }
                    });

                    alt.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    alt.show();
                    return true;
                }
            });

        } catch (Exception e) {

        }
    }

    public void viewFiles(View view) {
        Intent FilesIntent = new Intent(this, fileView.class);
        FilesIntent.putExtra("message", fileName);
        startActivity(FilesIntent);
        overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
    }

    @Override
    public void onBackPressed() {

                MainScreen(null);

    }

    public void MainScreen(View view) {

        Intent MainIntent=new Intent(this,MainActivity.class);
        startActivity(MainIntent);
        overridePendingTransition(android.R.anim.slide_in_left,0);
    }


}
