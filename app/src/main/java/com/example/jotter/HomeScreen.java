package com.example.jotter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity {

    ListView lView;
    ArrayList<String> ar = new ArrayList<>();
    String fileName;

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
        lView = findViewById(R.id.File_list);

        try {
            File sdCard = Environment.getExternalStorageDirectory();
            File jotDir = new File(sdCard, "MyJots");

            for (File f : jotDir.listFiles()) {
                ar.add(f.getName());
            }

            ArrayAdapter<String> fileAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ar);
            /*
            {

            @Override
            public View getView ( int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);
                TextView tv = view.findViewById(R.id.listcontent);
                tv.setTextColor(Color.RED);
                return view;
            }
        };

*/
            lView.setAdapter(fileAdapter);

            lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    fileName = ar.get(position);
                    viewFiles(null);

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