package com.example.oranmoshe.finalmobileproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.ArrayList;

public class TaskEdit extends AppCompatActivity {

    Controller controller = Controller.getInstance(this);
    LocalTask localTask =  null;
    LocalUser localUser = null;
    TextView textViewDueTime = null;
    TextView textViewLoaction = null;
    TextView textViewPriority = null;
    TextView textViewCategory = null;
    TextView textViewName = null;
    Spinner spinnerPriority = null;
    Spinner spinnerStatus = null;
    Spinner spinnerAssign = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);

        String taskID = (String)getIntent().getSerializableExtra("TASKID");

         localTask = controller.getLocalTask("t_id", taskID);

         localUser = controller.getLocalUser(localTask.get_assign());


        textViewName = (TextView)findViewById(R.id.textViewNameTaskEdit);
        textViewName.setText(localTask.get_name());

        textViewCategory = (TextView)findViewById(R.id.textViewCategoryTaskEdit);
        textViewCategory.setText(localTask.get_category());

        textViewLoaction = (TextView)findViewById(R.id.textViewLoactionTaskEdit);
        textViewLoaction.setText(localTask.get_location());

        textViewDueTime = (TextView)findViewById(R.id.textViewDueTimeTaskEdit);
        textViewDueTime.setText(localTask.get_due_time());


        spinnerPriority = (Spinner) findViewById(R.id.spinnerTaskViewPriority);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priority, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(adapter);
        int priority = localTask.get_priority();
        String priorityValue = null;
        switch (priority){
            case 3:
                priorityValue="High";
                break;
            case 2:
                priorityValue= "Medium";
                break;
            case 1:
                priorityValue="Low";
                break;
            default:
                priorityValue="Low";
                break;
        }
        if (!priorityValue.equals(null)) {
            int spinnerPosition = adapter.getPosition(priorityValue);
            spinnerPriority.setSelection(spinnerPosition);
        }

        spinnerStatus = (Spinner) findViewById(R.id.spinnerTaskViewStatus);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.status, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter2);
        int status = localTask.get_status();
        String statusValue = null;
        switch (status){
            case 3:
                statusValue="Done";
                break;
            case 2:
                statusValue= "In Progress";
                break;
            case 1:
                statusValue="Pending";
                break;
            default:
                statusValue="Pending";
                break;
        }
        if (!statusValue.equals(null)) {
            int spinnerPosition = adapter2.getPosition(statusValue);
            spinnerStatus.setSelection(spinnerPosition);
        }

        spinnerAssign = (Spinner)findViewById(R.id.spinnerTaskViewAssign);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAssign.setAdapter(spinnerAdapter);

        ArrayList<LocalUser> users = controller.getLocalUsers(ParseUser.getCurrentUser().getObjectId());
        for (LocalUser user: users) {
                spinnerAdapter.add(user.getUsername());
        }
        if (!localTask.get_assign().equals(null)) {
            int spinnerPosition = adapter.getPosition(localTask.get_assign());
            spinnerAssign.setSelection(spinnerPosition);
        }
        spinnerAdapter.notifyDataSetChanged();


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
        int status = 0;
        switch (spinnerStatus.getSelectedItem().toString()){
            case "Done":
                status=3;
                break;
            case "In Progress":
                status=2;
                break;
            case "Pending":
                status=1;
                break;
        }
        controller.UpdateTask(localTask.get_t_id(), localTask.get_name(), priority,
                textViewLoaction.getText().toString(),textViewDueTime.getText().toString(),
                user.get_id(), localTask.get_accept(),
                status,localTask.get_pic(),textViewCategory.getText().toString());
        finish();
    }


}
