package com.example.oranmoshe.finalmobileproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class TaskView extends AppCompatActivity {

    Controller controller = Controller.getInstance(this);
    public File imagefile;
    LocalTask localTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);

        String taskID = (String)getIntent().getSerializableExtra("TASKID");

        localTask = controller.getLocalTask("t_id", taskID);


        TextView textViewName = (TextView)findViewById(R.id.textViewName);
        textViewName.setText(localTask.get_name());

        TextView textViewCategory = (TextView)findViewById(R.id.textViewCategory);
        textViewCategory.setText(localTask.get_category());

        String priorityValue = null;
        switch (localTask.get_priority()){
            case 3:
                priorityValue= "Urgent";
            break;
            case 2:
                priorityValue= "Normal";
                break;
            case 1:
                priorityValue="Low";
                break;
            default:
                priorityValue="Normal";
                break;
        }
        TextView textViewPriority = (TextView)findViewById(R.id.textViewPriority);
        textViewPriority.setText(String.valueOf(priorityValue));

        TextView textViewLoaction = (TextView)findViewById(R.id.textViewLoaction);
        textViewLoaction.setText(localTask.get_location());

        TextView textViewDueTime = (TextView)findViewById(R.id.textViewDueTime);
        textViewDueTime.setText(localTask.get_due_time());

        String statusValue = null;
        switch (localTask.get_status()){
            case 3:
                statusValue= "Done";
                break;
            case 2:
                statusValue= "In Process";
                break;
            case 1:
                statusValue="Pending";
                break;
            default:
                statusValue="Pending";
                break;
        }
        TextView textViewStatus = (TextView) findViewById(R.id.textViewStaus);
        textViewStatus.setText(String.valueOf(statusValue));

        TextView textViewAssign = (TextView) findViewById(R.id.textViewAssign);
        textViewAssign.setText(controller.getLocalUser(localTask.get_assign()).getEmail());

        Button btn = (Button)findViewById(R.id.buttonTakePic);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MakePhotoActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("TASKID", localTask.get_t_id());
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });


        ParseQuery<ParseObject> query = ParseQuery.getQuery("ImageUpload");
        query.whereEqualTo("objectId", localTask.get_t_id());

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {

                if (e == null) {
                    if (objects.size() > 0) {
                        ParseFile fileObject = (ParseFile) objects.get(0).get("ImageFile");
                        fileObject.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, com.parse.ParseException e) {
                                Toast.makeText(getBaseContext(), "yessss", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    Log.d("saved", objects.get(0).get("name").toString());
                } else {
                    Log.d("not saved", "no entries found");
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode ==0){
            switch (resultCode){
                case Activity.RESULT_OK:
                    if(imagefile.exists()){

                        Bitmap bitmap = BitmapFactory.decodeFile(imagefile.getAbsolutePath());
                        // Convert it to byte
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        // Compress image to lower quality scale 1 - 100
                        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                        byte[] image = stream.toByteArray();

                        // Create the ParseFile
                        ParseFile file = new ParseFile("androidbegin.png", image);
                        // Upload the image into Parse Cloud
                        file.saveInBackground();

                        // Create a New Class called "ImageUpload" in Parse
                        ParseObject imgupload = new ParseObject("ImageUpload");

                        // Create a column named "ImageName" and set the string
                        imgupload.put("ImageName", "AndroidBegin Logo");

                        // Create a column named "ImageFile" and insert the image
                        imgupload.put("ImageFile", file);

                        // Create the class and the columns
                        imgupload.saveInBackground();



                    }
                    break;
                case Activity.RESULT_CANCELED:
                    if(imagefile.exists()){
                        Toast.makeText(this, "bb", Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    break;
            }
        }
        finish();
    }
}