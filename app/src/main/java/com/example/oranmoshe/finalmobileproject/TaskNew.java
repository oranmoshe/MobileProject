package com.example.oranmoshe.finalmobileproject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TaskNew extends AppCompatActivity {

    Controller controller = Controller.getInstance(this);

    TextView textViewDueTime = null;
    LinearLayout linearLayoutDuetimeSimple = null;
    LinearLayout linearLayoutDuetime = null;
    Spinner spinnerLocation = null;
    Spinner spinnerDuetime = null;
    TextView textViewPriority = null;
    Spinner spinnerCategory = null;
    EditText editTextName = null;
    Spinner spinnerPriority = null;
    Spinner spinnerStatus = null;
    Spinner spinnerAssign = null;
    public static Activity fa;

    private TextView tvDisplayDate;
    private DatePicker dpResult;
    private Button btnChangeDate;

    private int year;
    private int month;
    private int day;

    static final int DATE_DIALOG_ID = 999;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);

        fa = this;

        // name
        editTextName = (EditText)findViewById(R.id.editTextTaskEditName);

        // category
        spinnerCategory = (Spinner)findViewById(R.id.spinnerCategory);
        ArrayAdapter<CharSequence> adapterCategory = ArrayAdapter.createFromResource(this,
                R.array.category, android.R.layout.simple_spinner_item);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterCategory);

        // location
        spinnerLocation = (Spinner)findViewById(R.id.spinnerLocation);
        ArrayAdapter<CharSequence> adapterLocation = ArrayAdapter.createFromResource(this,
                R.array.location, android.R.layout.simple_spinner_item);
        adapterLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocation.setAdapter(adapterLocation);

        // priority
        spinnerPriority = (Spinner) findViewById(R.id.spinnerTaskViewPriority);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priority, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(adapter);
        String priorityValue = null;

        // status
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearLayout25);
        linearLayout.setVisibility(View.GONE);

        // assign
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

        // time
        textViewDueTime = (TextView)findViewById(R.id.tvDate);

        // time
        spinnerDuetime = (Spinner)findViewById(R.id.spinnerDueTimeSimple);
        ArrayAdapter<CharSequence> adapterDuetime = ArrayAdapter.createFromResource(this,
                R.array.datetime, android.R.layout.simple_spinner_item);
        adapterDuetime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDuetime.setAdapter(adapterDuetime);

        ((LinearLayout) findViewById(R.id.LinearDueTimeCalander)).setVisibility(View.GONE);
        ((LinearLayout) findViewById(R.id.LinearDueTime)).setVisibility(View.VISIBLE);
        spinnerDuetime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if(position==2)
                {
                    ((LinearLayout) findViewById(R.id.LinearDueTimeCalander)).setVisibility(View.VISIBLE);
                    ((LinearLayout) findViewById(R.id.LinearDueTime)).setVisibility(View.GONE);

                }else if(position==0){

                    Calendar calendar = Calendar.getInstance();
                    Date today = calendar.getTime();
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    String todayAsString = dateFormat.format(today);
                    textViewDueTime.setText(todayAsString);

                }
                else if(position==1){

                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                    Date tomorrow = calendar.getTime();
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    String tomorrowAsString = dateFormat.format(tomorrow);
                    textViewDueTime.setText(tomorrowAsString);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        setCurrentDate();
        addListenerOnButton();

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
                spinnerLocation.getSelectedItem().toString(), textViewDueTime.getText().toString(), u_id,0, 1, " ",
                spinnerCategory.getSelectedItem().toString(), fa);

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

}
