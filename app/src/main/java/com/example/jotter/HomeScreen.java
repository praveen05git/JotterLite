package com.example.jotter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;

import java.io.File;
import java.util.ArrayList;

public class    HomeScreen extends AppCompatActivity implements IUnityAdsListener {

    ListView lView;
    ArrayList<String> ar = new ArrayList<>();
    String fileName;
    NitSettings nitSettings;

    IUnityAdsListener new_listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nitSettings = new NitSettings(this);

        setTitle("My Jots");


        Intent intent=getIntent();
        String pass=intent.getStringExtra("nitVal");

        if(pass.equals("One"))
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                setTheme(R.style.DarkTheme);
            }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_home_screen);

        UnityAds.initialize(HomeScreen.this,"3283238",new_listener);


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
    public void onStart()
    {
        super.onStart();

    }

    @Override
    public void onBackPressed() {

               MainScreen(null);


    }

    public void MainScreen(View view) {

        if(UnityAds.isReady("video"))
        {
            UnityAds.show(HomeScreen.this,"video");
        }
        else
        {
            UnityAds.initialize(HomeScreen.this,"3283238",new_listener);
        }

        Intent MainIntent=new Intent(this,MainActivity.class);
        startActivity(MainIntent);
        overridePendingTransition(android.R.anim.slide_in_left,0);
    }

    @Override
    public void onUnityAdsReady(String s) {

    }

    @Override
    public void onUnityAdsStart(String s) {

    }

    @Override
    public void onUnityAdsFinish(String s, UnityAds.FinishState finishState) {

    }

    @Override
    public void onUnityAdsError(UnityAds.UnityAdsError unityAdsError, String s) {

    }
}
