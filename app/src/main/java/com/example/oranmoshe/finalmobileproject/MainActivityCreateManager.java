package com.example.oranmoshe.finalmobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;

public class MainActivityCreateManager extends AppCompatActivity {

    Controller controller = Controller.getInstance(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_create_manager);

        Button btnCreate = (Button)findViewById(R.id.buttonCreateManager);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username = (EditText)findViewById(R.id.editTextUsernameManager);
                EditText password = (EditText)findViewById(R.id.editTextPasswordManager);
                EditText phone = (EditText)findViewById(R.id.editTextPhoneManager);
                Intent intent = new Intent(MainActivityCreateManager.this,MainActivityCreateTeam.class);
                controller.AddUser("0", username.getText().toString(), password.getText().toString(), username.getText().toString(), phone.getText().toString(), 0, "", getBaseContext());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
