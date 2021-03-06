package com.example.jotter;

import com.google.android.material.snackbar.Snackbar;
import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity {

    EditText name, cont;
    FloatingActionButton btn_save;
    private String save_name;
    private static final String state_name = "Empty";
    int nit = 0;
    NitSettings nitSettings;


    final private UnityAdsListener unityAdsListener = new UnityAdsListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nitSettings = new NitSettings(this);

//        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES)

        if (nitSettings.loadNitState() == true) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_main);

        UnityAds.initialize(MainActivity.this, "YOUR_UNITY_GAME_ID", unityAdsListener);

//        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES)
        if (nitSettings.loadNitState() == true) {
            nit = 1;
        }

        name = findViewById(R.id.edit_head);
        save_name = name.getText().toString();
        cont = findViewById(R.id.edit_body);
        btn_save = findViewById(R.id.btn_save);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (name.getText().toString().equals("")) {
                    Snackbar.make(v, "Enter file name", Snackbar.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(),"Enter file name",Toast.LENGTH_SHORT).show();
                } else if (cont.getText().toString().equals("")) {
                    Snackbar.make(v, "Enter content", Snackbar.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(),"Enter content",Toast.LENGTH_SHORT).show();
                } else {
                    String state = Environment.getExternalStorageState();

                    if (Environment.MEDIA_MOUNTED.equals(state) && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        final String filename = name.getText().toString() + ".txt";
                        final String content = cont.getText().toString();
                        final String folder_main = "MyJots";
                        int check = 0;

                        try {
                            File sdCard = Environment.getExternalStorageDirectory();
                            File jotDir = new File(sdCard, "MyJots");
                            for (File f : jotDir.listFiles()) {
                                if (filename.equals(f.getName())) {
                                    check = check + 1;
                                }
                            }
                        } catch (Exception e) {
                        }

                        if (check < 1) {
                            File f = new File(Environment.getExternalStorageDirectory() + "/" + folder_main);
                            if (!f.exists()) {
                                f.mkdirs();
                            }
                            try {
                                File nFile = new File(f + "/" + filename);
                                FileWriter fw = new FileWriter(nFile);
                                fw.write(content);
                                fw.close();
                                Snackbar.make(v, "File saved to " + f, Snackbar.LENGTH_LONG)
                                        .setAction("View File", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                HomeActivity(null);
                                            }
                                        }).show();
                                //Toast.makeText(getApplicationContext(),"File saved to "+f,Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            AlertDialog altr_dia = new AlertDialog.Builder(MainActivity.this).create();
                            altr_dia.setTitle("Filename Exists!");
                            altr_dia.setMessage("Do you want to overwrite it?");

                            altr_dia.setButton(AlertDialog.BUTTON_POSITIVE, "Overwrite", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    File f = new File(Environment.getExternalStorageDirectory() + "/" + folder_main);
                                    if (!f.exists()) {
                                        f.mkdirs();
                                    }
                                    try {
                                        File nFile = new File(f + "/" + filename);
                                        FileWriter fw = new FileWriter(nFile);
                                        fw.write(content);
                                        fw.close();

                                        Snackbar.make(v, "File saved to" + f, Snackbar.LENGTH_LONG)
                                                .setAction("View File", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        HomeActivity(null);
                                                    }
                                                }).show();
                                        //Toast.makeText(getApplicationContext(),"File saved to "+f,Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            altr_dia.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            altr_dia.show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "No permission has been granted", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        save_name = savedInstanceState.getString(state_name);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (!TextUtils.isEmpty(save_name)) {
            outState.putString(state_name, save_name);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.opt_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int opt_id = item.getItemId();

        switch (opt_id) {
            case R.id.opt_new:
                AlertDialog alt_dia = new AlertDialog.Builder(MainActivity.this).create();
                alt_dia.setTitle("Warning!");
                alt_dia.setMessage("Are you sure, you want to create a new file?");

                alt_dia.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        name.setText("");
                        cont.setText("");
                        Toast.makeText(getApplicationContext(), "New File created", Toast.LENGTH_SHORT).show();
                    }
                });

                alt_dia.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alt_dia.show();
                return true;

            case R.id.opt_home:
                HomeActivity(null);
                return true;

            case R.id.nitBtn:
                AlertDialog alt_dia1 = new AlertDialog.Builder(MainActivity.this).create();
                alt_dia1.setTitle("Warning!");
                alt_dia1.setMessage("This will erase the unsaved file. Are you sure, you want to continue?");

                alt_dia1.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (nit == 0) {
                            nit = 1;
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            nitSettings.setNitState(true);
                            restartApp();
                        } else {
                            nit = 0;
//                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            nitSettings.setNitState(false);
                            restartApp();
                        }
                    }
                });
                alt_dia1.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alt_dia1.show();
                return true;

            case R.id.opt_rate:
                Intent playStore = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.jotter.notes"));
                startActivity(playStore);
                return true;

            case R.id.more_apps_opt:
                try {
                    //replace &quot;Unified+Apps&quot; with your developer name https://play.google.com/store/apps/developer?id=Hence+Simplified&hl=en
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/dev?id=7031227816779180923")));

                    //market://search?q=pub:Hence Simplified
                } catch (android.content.ActivityNotFoundException e) {
                    //replace &quot;Unified+Apps&quot; with your developer name
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/search?q=pub:Hence Simplified")));
                }
                return true;

            case R.id.menu_remove_Ads:
                AlertDialog alert_dia1 = new AlertDialog.Builder(MainActivity.this).create();
                alert_dia1.setTitle("Remove Ads!");
                alert_dia1.setMessage("Download the Jotter Lite Pro to enjoy Ad-free service!! Download Now?");

                alert_dia1.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.jotterpro.notes&hl=en_IN")));
                    }
                });
                alert_dia1.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert_dia1.show();
                return true;

            case R.id.opt_about:
                if (UnityAds.isReady("video")) {
                    UnityAds.show(MainActivity.this, "video");
                } else {
                    UnityAds.initialize(MainActivity.this, "YOUR_UNITY_GAME_ID", unityAdsListener);
                }

                if (nit == 1) {
                    Intent AboutIntent = new Intent(this, AboutActivity.class);
                    AboutIntent.putExtra("nitVal", "One");
                    startActivity(AboutIntent);
                    overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
                } else {
                    Intent HomeIntent = new Intent(this, AboutActivity.class);
                    HomeIntent.putExtra("nitVal", "Zero");
                    startActivity(HomeIntent);
                    overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
                }
                return true;

            case R.id.opt_privacy:
                Intent privacyPolicy = new Intent(Intent.ACTION_VIEW, Uri.parse("https://hencesimplified.wixsite.com/website/privacy-policy"));
                startActivity(privacyPolicy);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {

        AlertDialog alt_dia = new AlertDialog.Builder(MainActivity.this).create();
        alt_dia.setTitle("Warning!");
        alt_dia.setMessage("Are you sure, you want to exit?");

        alt_dia.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ExitActivity(null);


            }
        });

        alt_dia.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alt_dia.show();


    }

    public boolean checkPermission(String permission) {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    public void HomeActivity(View view) {

        if (UnityAds.isReady("video")) {
            UnityAds.show(MainActivity.this, "video");
        } else {
            UnityAds.initialize(MainActivity.this, "YOUR_UNITY_GAME_ID", unityAdsListener);
        }

        if (nit == 1) {
            Intent HomeIntent = new Intent(this, HomeScreen.class);
            HomeIntent.putExtra("nitVal", "One");
            startActivity(HomeIntent);
            overridePendingTransition(R.anim.right_enter, R.anim.left_out);
        } else {
            Intent HomeIntent = new Intent(this, HomeScreen.class);
            HomeIntent.putExtra("nitVal", "Zero");
            startActivity(HomeIntent);
            overridePendingTransition(R.anim.right_enter, R.anim.left_out);
        }
    }

    public void restartApp() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("val", nit);
        startActivity(i);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }

    public void ExitActivity(View view) {
        Intent ExitIntent = new Intent(Intent.ACTION_MAIN);
        ExitIntent.addCategory(Intent.CATEGORY_HOME);
        ExitIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(ExitIntent);

    }


    private class UnityAdsListener implements IUnityAdsListener {

        @Override
        public void onUnityAdsReady(String s) {

        }

        @Override
        public void onUnityAdsStart(String s) {

        }

        @Override
        public void onUnityAdsFinish(String s, UnityAds.FinishState finishState) {

            // HomeActivity(null);
        }

        @Override
        public void onUnityAdsError(UnityAds.UnityAdsError unityAdsError, String s) {

        }
    }


}
