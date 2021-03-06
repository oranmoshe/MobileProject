package om.otsproject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.EventObject;
import java.util.List;

public class EditTaskActivity extends BaseClass
        implements NavigationView.OnNavigationItemSelectedListener {

    Controller controller = Controller.getInstance(this);
    Task task =  null;
    User user = null;
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

    private Button btnChangeDate;

    private int year;
    private int month;
    private int day;
    static final int DATE_DIALOG_ID = 999;

    Bitmap bp;
    boolean isBpChanged = false;
    private  static int REQUEST_QR_CODE = 0;
    private  static int REQUEST_CAMERA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        SetHeader("Edit Task");

        String taskID = (String)getIntent().getSerializableExtra("TASKID");

        task = controller.getLocalTask("t_id", taskID);

        user = controller.getLocalUser(task.get_assign());
        String userMail = null;
        if(user!=null){
            user.getEmail();
        }


        spinnerCategory = (Spinner)findViewById(R.id.spinnerCategory);
        adapterCategory = ArrayAdapter.createFromResource(EditTaskActivity.this,
                R.array.category, android.R.layout.simple_spinner_item);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterCategory);

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
        editTextName.setText(task.get_name());


        // Time
        ((LinearLayout) findViewById(R.id.LinearDueTime)).setVisibility(View.GONE);
        textViewDueTime = (TextView)findViewById(R.id.tvDate);
        textViewDueTime.setText(task.get_due_time());

        spinnerPriority = (Spinner) findViewById(R.id.spinnerTaskViewPriority);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(EditTaskActivity.this,
                R.array.priority, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(adapter);
        int priority = task.get_priority();
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
        int status = task.get_status();
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
            case 0:
                statusValue="Request";
                break;
            default:
                statusValue="Request";
                break;
        }
        if (!statusValue.equals(null)) {
            int spinnerPosition = adapter2.getPosition(statusValue);
            spinnerStatus.setSelection(spinnerPosition);
        }


        ArrayList<User> users = controller.getLocalUsers(ParseUser.getCurrentUser().getObjectId());
        ArrayList<String> usernameArr = new ArrayList<String>();
        for (User user: users) {
            if(!user.get_id().equals(ParseUser.getCurrentUser().getObjectId()))
                usernameArr.add(user.getEmail());
        }

        spinnerAssign = (Spinner) findViewById(R.id.spinnerTaskViewAssign);

        ArrayAdapter<CharSequence> adapterAssign = new ArrayAdapter(this,android.R.layout.simple_spinner_item,usernameArr);
        adapterAssign.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAssign.setAdapter(adapterAssign);
        if (userMail!=null) {
            int spinnerPosition = adapterAssign.getPosition(userMail);
            spinnerAssign.setSelection(spinnerPosition);
        }
        adapterAssign.notifyDataSetChanged();

        setCurrentDate();
        addListenerOnButton();

        ImageView imageView = (ImageView)findViewById(R.id.imageEditTask);
        GetParseImage(task.get_pic(),imageView);

        ImageButton imageButton = (ImageButton)findViewById(R.id.imageButtonCamera);
        if(task.get_status()!=3){
            imageButton.setVisibility(View.GONE);
        }
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

        Button btnSave = (Button) findViewById(R.id.buttonSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save picture for all rolls
                if(bp!=null && isBpChanged) {
                    SaveOnParse(bp, task.get_t_id());
                }
                if(!(ParseUser.getCurrentUser().getString("m_id").equals(ParseUser.getCurrentUser().getObjectId()))) {
                    Save();
                }else{
                    SaveForManager();
                }
            }
        });


        if(!(ParseUser.getCurrentUser().getString("m_id").equals(ParseUser.getCurrentUser().getObjectId()))) {
            textViewDueTime.setKeyListener(null);
            editTextName.setKeyListener(null);
            spinnerCategory.setEnabled(false);
            spinnerLocation.setEnabled(false);
            spinnerAssign.setEnabled(false);
            btnChangeDate.setEnabled(false);
            textViewDueTime.setEnabled(false);
            spinnerPriority.setEnabled(false);
        }
    }

    void SaveForManager() {

        User user = controller.getLocalUserByUsername(spinnerAssign.getSelectedItem().toString());
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
        String t_id = task.get_t_id();
        String name = editTextName.getText().toString();
        String location = spinnerLocation.getSelectedItem().toString();
        String duetime = textViewDueTime.getText().toString();
        String u_id =  user.get_id();
        int accept = task.get_accept();
        String pic = task.get_pic();
        String category = spinnerCategory.getSelectedItem().toString();
        controller.UpdateTask(t_id,name , priority,location ,duetime,u_id , accept, status,pic,category);

        finish();
    }
    void Save(){

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
        controller.UpdateTaskStatus(task.get_t_id(),status);

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

            Toast.makeText(getBaseContext(), String.valueOf(day), Toast.LENGTH_LONG).show();
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
                isBpChanged = true;
                ImageView imageView = (ImageView) findViewById(R.id.imageEditTask);
                imageView.setImageBitmap(bp);
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
                                if(e.getSource().toString().equals("Done")){

                                }else{
                                    Toast.makeText(getBaseContext(),"Somthing went wrong..",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        controller.UpdateTaskPic(task.get_t_id(), imgupload.getObjectId(), event);
                    }
                });
            }
        });
    }


    public void GetParseImage(String pic, final ImageView imageView) {
        try {
            progressDialog = ProgressDialog.show(EditTaskActivity.this, "",
                    "Downloading Image...", true);

            // Locate the class table named "ImageUpload" in Parse.com
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                    "ImageUpload");

            // Locate the objectId from the class
            query.getInBackground(pic, new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        // Locate the column named "ImageName" and set
                        // the string
                        ParseFile fileObject = (ParseFile) object
                                .get("ImageFile");
                        fileObject
                                .getDataInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] data, ParseException e) {

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
