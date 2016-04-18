package om.otsproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.EventObject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CreateManagerActivity extends AppCompatActivity {

    public static Activity fa = null;
    Controller controller = Controller.getInstance(this);
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_create_manager);

        getSupportActionBar().setTitle("Create Manager");

        fa = this;

        Button btnCreate = (Button)findViewById(R.id.buttonCreateManager);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = ProgressDialog.show(CreateManagerActivity.this, "",
                        "Please wait..", true);
                String username = ((EditText) findViewById(R.id.editTextUsernameManager)).getText().toString();
                String password = ((EditText) findViewById(R.id.editTextPasswordManager)).getText().toString();
                String phone = ((EditText) findViewById(R.id.editTextPhoneManager)).getText().toString();

                if(phone.equals("") || password.equals("") || username.equals("")){
                    Toast.makeText(CreateManagerActivity.this, "Please enter all the fields..", Toast.LENGTH_LONG).show();
                    }else{
                        if(isEmailValid(username)) {
                            Event result = new Event();
                            result.setOnEventListener(new OnEventListener() {
                                @Override
                                public void onEvent(EventObject e) {
                                    int result = ((EventObjectExtender) e).getId();
                                    if (result == 1) {
                                        Intent intent = new Intent(getBaseContext(), EditGroupActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(CreateManagerActivity.this, "Please try another username.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            controller.AddManager(username, password, username, password, 0, "", result);
                        }else{
                            Toast.makeText(CreateManagerActivity.this, "Username field is invalid, (email address).", Toast.LENGTH_LONG).show();
                        }
                }
                progressDialog.dismiss();
            }
        });

    }
    public boolean isEmailValid(String email)
    {

         Pattern pattern;
         Matcher matcher;

        String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        pattern = Pattern.compile(EMAIL_PATTERN);

        matcher = pattern.matcher(email);
        return matcher.matches();

    }
}
