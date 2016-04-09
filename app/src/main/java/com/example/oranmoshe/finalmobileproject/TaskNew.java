package com.example.oranmoshe.finalmobileproject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.ArrayList;

public class TaskNew extends AppCompatActivity {

    Controller controller = Controller.getInstance(this);

    TextView textViewDueTime = null;
    TextView textViewLoaction = null;
    TextView textViewPriority = null;
    TextView textViewCategory = null;
    EditText editTextName = null;
    Spinner spinnerPriority = null;
    Spinner spinnerStatus = null;
    Spinner spinnerAssign = null;
    public static Activity fa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);

        fa = this;

        editTextName = (EditText)findViewById(R.id.editTextTaskEditName);
        textViewCategory = (TextView)findViewById(R.id.textViewCategoryTaskEdit);
        textViewLoaction = (TextView)findViewById(R.id.textViewLoactionTaskEdit);
        textViewDueTime = (TextView)findViewById(R.id.textViewDueTimeTaskEdit);
        spinnerPriority = (Spinner) findViewById(R.id.spinnerTaskViewPriority);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priority, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(adapter);
        String priorityValue = null;

        spinnerStatus = (Spinner) findViewById(R.id.spinnerTaskViewStatus);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.status, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter2);


        ArrayList<LocalUser> users = controller.getLocalUsers(ParseUser.getCurrentUser().getObjectId());
        ArrayList<String> usernameArr = new ArrayList<String>();
        for (LocalUser user: users) {
            if(!user.get_id().equals(ParseUser.getCurrentUser().getObjectId()))
            usernameArr.add(user.getEmail());
        }

        spinnerAssign = (Spinner) findViewById(R.id.spinnerTaskViewAssign);
        ArrayAdapter<CharSequence> adapterAssign = new ArrayAdapter(this,android.R.layout.simple_spinner_item,usernameArr);
        adapterAssign.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAssign.setAdapter(adapterAssign);
        adapterAssign.notifyDataSetChanged();


        LinearLayout layout = (LinearLayout)findViewById(R.id.linearLayout25);
        layout.setVisibility(View.GONE);

        Button btnSave = (Button) findViewById(R.id.buttonSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
            }
        });

    }

    void Save(){
        LocalUser user = controller.getLocalUserByUsername(spinnerAssign.getSelectedItem().toString());
        int priority = 0;
        switch (spinnerPriority.getSelectedItem().toString()){
            case "High":
                priority=3;
                break;
            case "Medium":
                priority=2;
                break;
            case "Low":
                priority=1;
                break;
        }
        String  u_id = controller.getLocalUserByUsername(spinnerAssign.getSelectedItem().toString()).get_id();
        controller.AddTask(ParseUser.getCurrentUser().getObjectId(), editTextName.getText().toString(), priority,
                textViewLoaction.getText().toString(), textViewDueTime.getText().toString(), u_id,0, 1, " ",
                textViewCategory.getText().toString(), fa);

    }


}
