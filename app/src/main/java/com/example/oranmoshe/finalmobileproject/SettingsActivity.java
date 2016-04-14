package com.example.oranmoshe.finalmobileproject;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.parse.ParseUser;

public class SettingsActivity extends BaseClass {

    Spinner spinnerTimer = null;
    ArrayAdapter<CharSequence> adapterTimer=null;
    Controller controller = Controller.getInstance(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        spinnerTimer = (Spinner)findViewById(R.id.spinnerTimer);
        adapterTimer = ArrayAdapter.createFromResource(this,
                R.array.timer, android.R.layout.simple_spinner_item);
        adapterTimer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTimer.setAdapter(adapterTimer);
        String currentTimer = String.valueOf(controller.GetTimer());
        spinnerTimer.setSelection(adapterTimer.getPosition(currentTimer + " minutes"));
        spinnerTimer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        controller.SetTimer(5);
                        break;
                    case 1:
                        controller.SetTimer(15);
                        break;
                    case 2:
                        controller.SetTimer(30);
                        break;
                    case 3:
                        controller.SetTimer(60);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

}
