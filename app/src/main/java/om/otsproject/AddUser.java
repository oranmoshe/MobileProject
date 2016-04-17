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

import com.parse.ParseUser;

import java.util.EventObject;


public class AddUser extends AppCompatActivity {
    Controller controller = Controller.getInstance(this);
    String username = null;
    String password = null;
    String u_id = "0";
    public static Activity fa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        fa = this;
        Button btn = (Button)findViewById(R.id.btnAddUserAdd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent returnIntent = new Intent(getBaseContext(), MainActivityCreateTeam.class);
                username = ((EditText)findViewById(R.id.etAddUserUsername)).getText().toString();
                password = ((EditText)findViewById(R.id.etAddUserPassword)).getText().toString();
                ParseUser pu = ParseUser.getCurrentUser();
                if(pu!= null) {
                    String u_id = (ParseUser.getCurrentUser()).getObjectId();
                    Event result = new Event();
                    result.setOnEventListener(new OnEventListener() {
                        @Override
                        public void onEvent(EventObject e) {
                            if((Boolean)e.getSource()) {
                                Intent intent = new Intent(getBaseContext(), ManageTeamActivity.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(getBaseContext(),"Please try another username.",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    controller.AddUser(u_id, username, password, username, password, 0, "", result);
                }
            }
        });
    }

    @Override
    public void finish() {
//        Intent returnIntent = new Intent(controller.getContext(), MainActivityCreateTeam.class);
//        setResult(RESULT_OK, returnIntent);
//        returnIntent.putExtra("passed_item", "1");
        super.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
