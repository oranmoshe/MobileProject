package com.example.oranmoshe.finalmobileproject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.EventObject;
import java.util.List;

public class TaskEdit extends BaseClass {

    Controller controller = Controller.getInstance(this);
    LocalTask localTask =  null;
    LocalUser localUser = null;
    TextView textViewDueTime = null;
    Spinner spinnerCategory = null;
    Spinner spinnerLocation = null;
    EditText editTextName = null;
    Spinner spinnerPriority = null;
    Spinner spinnerStatus = null;
    Spinner spinnerAssign = null;
    ArrayAdapter<CharSequence> adapterLocation=null;
    ArrayAdapter<CharSequence> adapterCategory=null;
    private ProgressDialog progressDialog;
    Bitmap bp;

    private TextView tvDisplayDate;
    private DatePicker dpResult;
    private Button btnChangeDate;

    private int year;
    private int month;
    private int day;

    static final int DATE_DIALOG_ID = 999;

    private  static int REQUEST_QR_CODE = 0;
    private  static int REQUEST_CAMERA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task_edit);

        String taskID = (String)getIntent().getSerializableExtra("TASKID");

         localTask = controller.getLocalTask("t_id", taskID);

         localUser = controller.getLocalUser(localTask.get_assign());

        spinnerCategory = (Spinner)findViewById(R.id.spinnerCategory);
        adapterCategory = ArrayAdapter.createFromResource(TaskEdit.this,
                R.array.category, android.R.layout.simple_spinner_item);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterCategory);
//
        spinnerLocation = (Spinner)findViewById(R.id.spinnerLocation);
        adapterLocation = ArrayAdapter.createFromResource(this,
                R.array.location, android.R.layout.simple_spinner_item);
        adapterLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocation.setAdapter(adapterLocation);
        spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {
                    // your code here
                    if (position == 3) {
                        try {

                            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                            intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes

                            startActivityForResult(intent,REQUEST_QR_CODE);

                        } catch (Exception e) {

                            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
                            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                            startActivity(marketIntent);
                        }
                    }
                } catch (Exception exc) {
                    Log.d("Error: ", exc.toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });



        editTextName = (EditText)findViewById(R.id.editTextTaskEditName);
        editTextName.setText(localTask.get_name());


        // Time
        ((LinearLayout) findViewById(R.id.LinearDueTime)).setVisibility(View.GONE);
        textViewDueTime = (TextView)findViewById(R.id.tvDate);
        textViewDueTime.setText(localTask.get_due_time());

        spinnerPriority = (Spinner) findViewById(R.id.spinnerTaskViewPriority);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(TaskEdit.this,
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
                statusValue= "In Process";
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


        ArrayList<LocalUser> users = controller.getLocalUsers(ParseUser.getCurrentUser().getObjectId());
        ArrayList<String> usernameArr = new ArrayList<String>();
        for (LocalUser user: users) {
            if(!user.get_id().equals(ParseUser.getCurrentUser().getObjectId()))
            usernameArr.add(user.getEmail());
        }
        String assignMail = controller.getLocalUser(localTask.get_assign()).getEmail();
        spinnerAssign = (Spinner) findViewById(R.id.spinnerTaskViewAssign);

        ArrayAdapter<CharSequence> adapterAssign = new ArrayAdapter(this,android.R.layout.simple_spinner_item,usernameArr);
        adapterAssign.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAssign.setAdapter(adapterAssign);
        if (!localTask.get_assign().equals(null)) {
            int spinnerPosition = adapterAssign.getPosition(assignMail);
            spinnerAssign.setSelection(spinnerPosition);
        }
        adapterAssign.notifyDataSetChanged();

        setCurrentDate();
        addListenerOnButton();

        ImageView imageView = (ImageView)findViewById(R.id.imageEditTask);
        GetParseImage(localTask.get_pic(),imageView);

        ImageButton imageButton = (ImageButton)findViewById(R.id.imageButtonCamera);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPhoto = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                Bundle mBundlePhoto = new Bundle();
                mBundlePhoto.putSerializable("TASKID", localTask.get_t_id());
                intentPhoto.putExtras(mBundlePhoto);
                startActivityForResult(intentPhoto,  REQUEST_CAMERA);
            }
        });

        Button btnSave = (Button) findViewById(R.id.buttonSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
            }
        });

    }

    void Save(){
        if(bp!=null) {
            SaveOnParse(bp, localTask.get_t_id());
        }
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
        String statusStr = spinnerStatus.getSelectedItem().toString();
        switch (statusStr){
            case "Done":
                status=3;
                break;
            case "In Process":
                status=2;
                break;
            case "Pending":
                status=1;
                break;
        }

        String t_id = localTask.get_t_id();
        String name = editTextName.getText().toString();
        String location = spinnerLocation.getSelectedItem().toString();
        String duetime = textViewDueTime.getText().toString();
        String u_id =  user.get_id();
        int accept = localTask.get_accept();
        String pic =localTask.get_pic();
        String category = spinnerCategory.getSelectedItem().toString();
        controller.UpdateTask(t_id,name , priority,location ,duetime,u_id , accept, status,pic,category);

        finish();
    }

    // display current date
    public void setCurrentDate() {

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

    }

    public void addListenerOnButton() {

        btnChangeDate = (Button) findViewById(R.id.btnChangeDate);

        btnChangeDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(DATE_DIALOG_ID, null);


            }

        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener,
                        year, month,day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // set selected date into textview
            textViewDueTime.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));

            Toast.makeText(getBaseContext(),String.valueOf(day),Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==  REQUEST_QR_CODE) {

            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                String[] mTestArray;
                mTestArray = getResources().getStringArray(R.array.location);
                List<CharSequence> stringList = new ArrayList<CharSequence>(Arrays.asList(mTestArray));
                stringList.add(contents);
                adapterLocation =  new ArrayAdapter<CharSequence>(getBaseContext(),
                        android.R.layout.simple_spinner_item, stringList);
                adapterLocation.notifyDataSetChanged();
                spinnerLocation.setAdapter(adapterLocation);
                spinnerLocation.setSelection(stringList.indexOf(contents));
            }
            if(resultCode == RESULT_CANCELED){
                //handle cancel
            }
        }
        else if(requestCode ==  REQUEST_CAMERA){
            if (resultCode == RESULT_OK) {
                // TODO Auto-generated method stub
                super.onActivityResult(requestCode, resultCode, data);
                bp = (Bitmap) data.getExtras().get("data");
                ImageView imageView = (ImageView) findViewById(R.id.imageEditTask);
                imageView.setImageBitmap(bp);
            }
            if(resultCode == RESULT_CANCELED){
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
                    public void done(com.parse.ParseException e) {
                        Event event = new Event();
                        event.setOnEventListener(new OnEventListener() {
                            @Override
                            public void onEvent(EventObject e) {
                                if(e.getSource().toString().equals("Done")){

                                }else{
                                    Toast.makeText(getBaseContext(),"Somthing went wrong..",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        controller.UpdateTaskPic(localTask.get_t_id(), imgupload.getObjectId(), event);
                    }
                });
            }
        });
    }


    public void GetParseImage(String pic, final ImageView imageView) {
        try {
            progressDialog = ProgressDialog.show(TaskEdit.this, "",
                    "Downloading Image...", true);

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

                                            // Close progress dialog
                                            progressDialog.dismiss();

                                        } else {
                                            Log.d("test",
                                                    "There was a problem downloading the data.");
                                            progressDialog.dismiss();
                                        }
                                    }
                                });
                    } else {
                        progressDialog.dismiss();
                    }
                }
            });
        }catch (Exception exc){
            Log.d("Error",exc.toString());
        }
    }

}
