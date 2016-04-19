package om.otsproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class LoginActivity extends AppCompatActivity {

    Controller controller = Controller.getInstance(this);
    private ProgressDialog progressDialog;

    public static GoogleAnalytics analytics;
    public static Tracker tracker;

    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREF_USERNAME = "username";
    private static final String PREF_PASSWORD = "password";
    private static final String PREF_REMEMBER = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Welcome OTS");
        getSupportActionBar().setIcon(R.drawable.ic_launcher_new);


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

        // Google analytics issues

        analytics = GoogleAnalytics.getInstance(this);
        analytics.setLocalDispatchPeriod(1800);

        tracker = analytics.newTracker("UA-76567317-1"); // Replace with actual tracker id
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);

        // SharedPreferences for user login info
        SharedPreferences pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String username = pref.getString(PREF_USERNAME, null);
        String password = pref.getString(PREF_PASSWORD, null);
        String remember = pref.getString(PREF_REMEMBER, "0");
        TextView textViewUsername = (TextView)findViewById(R.id.editTextUsername);
        TextView textViewPassword = (TextView)findViewById(R.id.editTextPassword);
        if (username != null || password != null) {
            //Prompt for username and password
            textViewUsername.setText(username);
            textViewPassword.setText(password);
        }
        if(remember.equals("1")){
            CheckBox checkBox = (CheckBox)findViewById(R.id.checkBoxRemember);
            checkBox.setChecked(true);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Button btnLogin =  (Button)findViewById(R.id.buttonLogin);
        btnLogin.setEnabled(true);
    }

    public void goToListTasks(View v) {
        final View v1 = v;
        v1.setEnabled(false);
        progressDialog = ProgressDialog.show(LoginActivity.this, "",
                "Please wait..", true);

        final String username = ((EditText) findViewById(R.id.editTextUsername)).getText().toString();
        final String password = ((EditText) findViewById(R.id.editTextPassword)).getText().toString();
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    Intent intent = new Intent(getBaseContext(), TasksActivity.class);
                    controller.SetCurrentUser(controller.getLocalUser(ParseUser.getCurrentUser().getObjectId()));
                    CheckBox checkBox = (CheckBox)findViewById(R.id.checkBoxRemember);
                    if(checkBox.isChecked()) {
                        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                                .edit()
                                .putString(PREF_USERNAME, username)
                                .putString(PREF_PASSWORD, password)
                                .putString(PREF_REMEMBER, "1")
                                .commit();
                    }else{
                        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                                .edit()
                                .putString(PREF_USERNAME, "")
                                .putString(PREF_PASSWORD, "")
                                .putString(PREF_REMEMBER, "0")
                                .commit();
                    }

                    Intent intent1 = new Intent(getBaseContext(), TasksActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getBaseContext().startActivity(intent1);

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
        Intent intent  = new Intent(getBaseContext(),CreateManagerActivity.class);
        startActivity(intent);
    }


}
