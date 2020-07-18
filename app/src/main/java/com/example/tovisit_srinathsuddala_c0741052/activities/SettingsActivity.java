package com.example.tovisit_srinathsuddala_c0741052.activities;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tovisit_srinathsuddala_c0741052.R;


public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView text_select_radius,text_select_maptype;
    private RelativeLayout lay_radius,layMaptype;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private int kms_pref,map_pref;
    final String[] kms = {"1KMS", "2KMS", "3KMS", "5KMS", "10KMS","15KMS"};
    final String[] maptypes = {"NORMAL","SATELLITE",
            "TERRAIN", "HYBRID"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolBarSetup();

        prefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        editor = prefs.edit();
        kms_pref = prefs.getInt("kms_radius", 0);
        map_pref = prefs.getInt("map_type", 0);

        lay_radius = (RelativeLayout) findViewById(R.id.lay_radius);
        lay_radius.setOnClickListener(this);
        text_select_radius = (TextView) findViewById(R.id.text_select_radius);
        text_select_radius.setText("" + kms[kms_pref]);

        layMaptype = (RelativeLayout) findViewById(R.id.lay_MapType);
        layMaptype.setOnClickListener(this);
        text_select_maptype = (TextView) findViewById(R.id.text_select_maptype);
        text_select_maptype.setText("" + maptypes[map_pref]);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.lay_radius:
                showRadiusDialogue();
                break;

            case R.id.lay_MapType:
                showMapTypeDialogue();
                break;

        }
    }

    void toolBarSetup() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void showRadiusDialogue() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        builder.setTitle("Select Radius")
                //.setMessage("You can buy our products without registration too. Enjoy the shopping")
                .setSingleChoiceItems(kms, kms_pref, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        kms_pref = which;
                        editor.putInt("kms_radius", which);
                        editor.putInt("radius", (Integer.parseInt(kms[which].toString().replace("KMS", ""))) * 1000);

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        text_select_radius.setText("" + kms[kms_pref]);
                        dialog.dismiss();
                        editor.commit();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.create().show();
    }

    private void showMapTypeDialogue() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        builder.setTitle("Select MapType")
                //.setMessage("You can buy our products without registration too. Enjoy the shopping")
                .setSingleChoiceItems(maptypes, map_pref, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        map_pref = which;
                        editor.putInt("map_type", which);
                        Log.d("MapType",which+"");
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        text_select_maptype.setText("" + maptypes[map_pref]);
                        dialog.dismiss();
                        editor.commit();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.create().show();
    }

}
