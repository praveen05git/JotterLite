package com.example.jotter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class Main2Activity extends AppCompatActivity {

    EditText fileHead,fileBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fileHead=findViewById(R.id.edit_head);
        fileBody=findViewById(R.id.edit_body);
        Intent intent=getIntent();
        String pass=intent.getStringExtra("message");
        fileHead.setText(pass);

                File f = new File(Environment.getExternalStorageDirectory()+"/"+"MyJots","ko.txt");
                StringBuilder readTxt=new StringBuilder();
                try{

                    Reader reader=new InputStreamReader(new FileInputStream(f));
                    char[] buff=new char[500];
                    for(int charsRead;(charsRead=reader.read(buff))!=-1;)
                    {
                        readTxt.append(buff,0,charsRead);
                    }

                    Toast.makeText(getApplicationContext(),"File saved to "+f,Toast.LENGTH_SHORT).show();
                }catch(Exception e){Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();}

                fileBody.setText(readTxt);



    }

}
