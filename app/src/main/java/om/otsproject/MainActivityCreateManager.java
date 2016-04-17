package om.otsproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



public class MainActivityCreateManager extends AppCompatActivity {
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
                EditText username = (EditText)findViewById(R.id.editTextUsernameManager);
                EditText password = (EditText)findViewById(R.id.editTextPasswordManager);
                EditText phone = (EditText)findViewById(R.id.editTextPhoneManager);
                Intent intent = new Intent(getBaseContext(), MainActivityCreateTeam.class);
                controller.AddManager("0", username.getText().toString(), password.getText().toString(), username.getText().toString(), phone.getText().toString(), 0, "", fa,intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
