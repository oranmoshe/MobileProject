package om.otsproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.EventObject;



public class ReportTaskActivity extends BaseClass
        implements NavigationView.OnNavigationItemSelectedListener {

    protected Controller controller = Controller.getInstance(this);;
    protected Task task;
    protected Bitmap bp;
    protected String taskID;
    protected  static int REQUEST_CAMERA = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_task);

        SetHeader("Report Task");

        taskID = (String)getIntent().getSerializableExtra("TASKID");

        task = controller.getLocalTask("t_id", taskID);

        if(!controller.IsManager()) {
            controller.UpdateTaskAccept(taskID, 1);
        }

        UpdateComponnents();

    }


    public void UpdateComponnents(){

        TextView textViewName = (TextView)findViewById(R.id.textViewName);
        textViewName.setText(task.get_name());

        TextView textViewCategory = (TextView)findViewById(R.id.textViewCategory);
        textViewCategory.setText(task.get_category());

        String priorityValue = null;
        switch (task.get_priority()){
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
        textViewLoaction.setText(task.get_location());

        TextView textViewDueTime = (TextView)findViewById(R.id.textViewDueTime);
        textViewDueTime.setText(task.get_due_time());

        String statusValue = null;
        switch (task.get_status()){
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
        try {
            textViewAssign.setText(controller.getLocalUser(task.get_assign()).getEmail());
        }catch(Exception exc){
            Log.d("Error:", "No member signed.");
            textViewAssign.setText("No member signed.");
        }
        ImageView image = (ImageView) findViewById(R.id.imageViewTask);
        GetParseImage(task.get_pic(),image);

        ImageButton imageButton = (ImageButton)findViewById(R.id.imageButtonCamera);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPhoto = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                Bundle mBundlePhoto = new Bundle();
                mBundlePhoto.putSerializable("TASKID", task.get_t_id());
                intentPhoto.putExtras(mBundlePhoto);
                startActivityForResult(intentPhoto,  REQUEST_CAMERA);
            }
        });
    }

    public void GetParseImage(String pic, final ImageView imageView) {
        try {


            // Locate the class table named "ImageUpload" in Parse.com
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                    "ImageUpload");

            // Locate the objectId from the class
            query.getInBackground(pic, new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, com.parse.ParseException e) {
                    if (e == null) {
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


                                            // Set the Bitmap into the
                                            // ImageView
                                            imageView.setImageBitmap(bmp);



                                        } else {
                                            Log.d("test",
                                                    "There was a problem downloading the data.");
                                        }
                                    }
                                });
                    } else {

                    }
                }
            });
        }catch (Exception exc){
            Log.d("Error",exc.toString());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==  REQUEST_CAMERA){
            if (resultCode == RESULT_OK) {
                // TODO Auto-generated method stub
                super.onActivityResult(requestCode, resultCode, data);
                bp = (Bitmap) data.getExtras().get("data");
                ImageView imageView = (ImageView) findViewById(R.id.imageViewTask);
                imageView.setImageBitmap(bp);
                SaveOnParse(bp,taskID);
            }else if(resultCode == RESULT_CANCELED){
                //handle cancel
            }
        }
    }

    public void SaveOnParse(final Bitmap bitmap, String t_id) {

        // Convert it to byte
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        final byte[] image = stream.toByteArray();

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
                        Event event = new Event();
                        event.setOnEventListener(new OnEventListener() {
                            @Override
                            public void onEvent(EventObject e) {
                                if (e.getSource().toString().equals("Done")) {

                                } else {
                                    Toast.makeText(getBaseContext(), "Somthing went wrong..", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        controller.UpdateTaskPic(task.get_t_id(), imgupload.getObjectId(), event);
                    }
                });
            }
        });
    }


}
