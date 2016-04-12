package com.example.oranmoshe.finalmobileproject;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class TaskView extends AppCompatActivity {

    Controller controller = Controller.getInstance(this);
    public File imagefile;
    LocalTask localTask;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);

        String taskID = (String)getIntent().getSerializableExtra("TASKID");

        localTask = controller.getLocalTask("t_id", taskID);

        UpdateComponnents();

    }

    public void UpdateComponnents(){

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

        GetParseImage(localTask.get_pic());
    }

    public void GetParseImage(String pic) {
        progressDialog = ProgressDialog.show(TaskView.this, "",
                "Downloading Image...", true);

        // Locate the class table named "ImageUpload" in Parse.com
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                "ImageUpload");

        // Locate the objectId from the class
        query.getInBackground(pic, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, com.parse.ParseException e) {
                if(e==null) {
                    // Locate the column named "ImageName" and set
                    // the string
                    ParseFile fileObject = (ParseFile) object
                            .get("ImageFile");
                    fileObject
                            .getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, com.parse.ParseException e) {

                                    if (e == null) {
                                        Log.d("test",
                                                "We've got data in data.");
                                        // Decode the Byte[] into
                                        // Bitmap
                                        Bitmap bmp = BitmapFactory
                                                .decodeByteArray(
                                                        data, 0,
                                                        data.length);

                                        // Get the ImageView from
                                        // main.xml
                                        ImageView image = (ImageView) findViewById(R.id.imageViewTask);

                                        // Set the Bitmap into the
                                        // ImageView
                                        image.setImageBitmap(bmp);

                                         // Close progress dialog
                                        progressDialog.dismiss();

                                    } else {
                                        Log.d("test",
                                                "There was a problem downloading the data.");
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                }else{
                    progressDialog.dismiss();
                }
            }
        });
    }
}