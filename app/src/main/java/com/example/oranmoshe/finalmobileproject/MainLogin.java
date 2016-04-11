package com.example.oranmoshe.finalmobileproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
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
    private ProgressDialog progress;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Button btnLogin =  (Button)findViewById(R.id.buttonLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToListTasks(v);
            }
        });

        Button btnSignup =  (Button)findViewById(R.id.buttonSignup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCreate(v);
            }
        });

        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Button btnLogin =  (Button)findViewById(R.id.buttonLogin);
        btnLogin.setEnabled(true);
    }

    public void goToListTasks(View v) {
        v.setEnabled(false);
        final View v1 = v;
        progressDialog = ProgressDialog.show(MainLogin.this, "",
                "Downloading Image...", true);

        String username = ((EditText) findViewById(R.id.editTextUsername)).getText().toString();
        String password = ((EditText) findViewById(R.id.editTextPassword)).getText().toString();
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    Intent intent = new Intent(getBaseContext(), UserTasks.class);
                    controller.ImportData(user.getString("m_id"), intent);
                    controller.SetCurrentUser(controller.getLocalUser(ParseUser.getCurrentUser().getObjectId()));
                } else {
                    Toast.makeText(getBaseContext(), "Invalid username or password..", Toast.LENGTH_LONG).show();
                }
                // Close progress dialog
                progressDialog.dismiss();
                v1.setEnabled(true);
            }
        });
    }

    public void goToCreate(View v) {
        Intent intent  = new Intent(getBaseContext(),MainActivityCreateManager.class);
        startActivity(intent);
    }


}
