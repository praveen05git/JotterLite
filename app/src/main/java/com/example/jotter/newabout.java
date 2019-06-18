package com.example.jotter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;

public class newabout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        setContentView(R.layout.activity_newabout);
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
