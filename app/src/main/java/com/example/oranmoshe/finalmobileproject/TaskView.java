package com.example.oranmoshe.finalmobileproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TaskView extends AppCompatActivity {

    Controller controller = Controller.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);

        String taskID = (String)getIntent().getSerializableExtra("TASKID");

        LocalTask localTask = controller.getLocalTask("t_id", taskID);


        TextView textViewName = (TextView)findViewById(R.id.textViewName);
        textViewName.setText(localTask.get_name());

        TextView textViewCategory = (TextView)findViewById(R.id.textViewCategory);
        textViewCategory.setText(localTask.get_category());

        TextView textViewPriority = (TextView)findViewById(R.id.textViewPriority);
        textViewPriority.setText(String.valueOf(localTask.get_priority()));

        TextView textViewLoaction = (TextView)findViewById(R.id.textViewLoaction);
        textViewLoaction.setText(localTask.get_location());

        TextView textViewDueTime = (TextView)findViewById(R.id.textViewDueTime);
        textViewDueTime.setText(localTask.get_due_time());
    }
}