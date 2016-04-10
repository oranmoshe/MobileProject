package com.example.oranmoshe.finalmobileproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MakePhotoActivity extends AppCompatActivity {
    int TAKE_PHOTO_CODE = 0;
    public static int count = 0;
    public File imagefile;
    private ProgressDialog progress;
    public Controller controller = Controller.getInstance(this);

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_photo);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imagefile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"test.jpg");
        Uri tempuri = Uri.fromFile(imagefile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempuri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
        startActivityForResult(intent,0);

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
                        final ParseFile file = new ParseFile("androidbegin.png", image);
                        // Upload the image into Parse Cloud
                        file.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                // Create a New Class called "ImageUpload" in Parse
                                final ParseObject imgupload = new ParseObject("ImageUpload");

                                // Create a column named "ImageName" and set the string
                                imgupload.put("ImageName", "AndroidBegin Logo");

                                // Create a column named "ImageFile" and insert the image
                                imgupload.put("ImageFile", file);

                                // Create the class and the columns
                                imgupload.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        String taskID = (String) getIntent().getSerializableExtra("TASKID");
                                        LocalTask localTask = controller.getLocalTask("t_id", taskID);
                                        controller.UpdateTaskPic(localTask.get_t_id(), imgupload.getObjectId());
                                        finish();
                                    }
                                });
                            }
                        });

                    }
                    break;
                case Activity.RESULT_CANCELED:
                    if(imagefile.exists()){
                        Toast.makeText(this,"bb",Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    break;
            }
        }
        finish();
    }

}