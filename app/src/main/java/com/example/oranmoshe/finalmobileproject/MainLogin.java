package com.example.oranmoshe.finalmobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class MainLogin extends AppCompatActivity {

    Controller controller = Controller.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);



        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void Login(String username, String password) {

    }

    public void goToListTasks(View v) {
        String username = ((EditText) findViewById(R.id.editTextUsername)).getText().toString();
        String password = ((EditText) findViewById(R.id.editTextPassword)).getText().toString();
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    Intent intent = new Intent(getBaseContext(), UserTasks.class);
                    controller.ImportData(user.getString("m_id"), intent);
                } else {
                    Toast.makeText(getBaseContext(), "Invalid username or password..", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void goToCreate(View v) {

        String username = ((EditText) findViewById(R.id.editTextUsername)).getText().toString();
        String password = ((EditText) findViewById(R.id.editTextPassword)).getText().toString();

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    Log.d(user.getString("m_id"), user.getObjectId());
                    Intent intent  = new Intent(getBaseContext(),MainActivityCreateTeam.class);
                    controller.ImportData(user.getString("m_id"), intent);

                } else {
                    Toast.makeText(getBaseContext(), "Invalid username or password..", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}
