package om.otsproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.EventObject;


public class CreateUserActivity extends AppCompatActivity {
    public static Activity fa = null;
    Controller controller = Controller.getInstance(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_create_manager);

        fa = this;

        Button btnCreate = (Button)findViewById(R.id.buttonCreateManager);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ((EditText)findViewById(R.id.editTextUsernameManager)).getText().toString();
                String password = ((EditText)findViewById(R.id.editTextPasswordManager)).getText().toString();
                String phone = ((EditText)findViewById(R.id.editTextPhoneManager)).getText().toString();

                Event result = new Event();
                result.setOnEventListener(new OnEventListener() {
                    @Override
                    public void onEvent(EventObject e) {
                        int result = ((EventObjectExtender)e).getId();
                        if(result==1) {
                            Intent intent = new Intent(getBaseContext(), ManageTeamActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(getBaseContext(),"Please try another username.",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                controller.AddManager(username, password, username, password, 0, "", result);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
