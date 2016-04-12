package com.example.oranmoshe.finalmobileproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivityCreateTeam extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public final static int REQUEST_CODE = 1;
    private ArrayList<RecycleItem> items;
    private String team = "";
    private String u_id = "";
    Controller controller = Controller.getInstance(this);
    ArrayList<LocalUser> users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_activity_create_team);
        items = new ArrayList<RecycleItem>();
        mRecyclerView = (RecyclerView) findViewById(R.id.cardList);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // buttons
        Button addEmail = (Button) findViewById(R.id.buttonAddEmail);
        addEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  WaitForChanges();
                Intent intent = new Intent(getBaseContext(), AddUser.class);
                startActivity(intent);
            }
        });


        FloatingActionButton fabmail = (FloatingActionButton) findViewById(R.id.fabMail);
        fabmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emails[] = new String[users.size()];
                int counter = 0;
                for (LocalUser user : users) {
                    if (!user.get_id().equals(ParseUser.getCurrentUser().getObjectId())) {
                        emails[counter] = (user.getEmail());
                        counter++;
                    }
                }
                sendEmail(emails);
            }
        });

        FloatingActionButton fabsave = (FloatingActionButton) findViewById(R.id.fabSave);
        fabsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String team = ((EditText) findViewById(R.id.editTextTeamName)).getText().toString();
                String u_id = ParseUser.getCurrentUser().getObjectId();
                controller.UpdateTeamName(team, u_id);
                Intent intent = new Intent(getBaseContext(),UserTasks.class);
                startActivity(intent);
            }
        });

        UpdateData();


    }

    protected void sendEmail(String[] TO) {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, TO);
        email.putExtra(Intent.EXTRA_SUBJECT, "Invitation to Join OTS team");
        email.putExtra(Intent.EXTRA_TEXT, "Hi\n" +
                "\tYou have been invited to be a team member in an OTS Team created by me.\n" +
                "\tUse this link to download and install the App from Google Play.\n");
        email.setType("message/rfc822");
        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }

    void UpdateData(){
        try {
            String u_id = ParseUser.getCurrentUser().getObjectId();
            team = ParseUser.getCurrentUser().getString("team");
            users = controller.getLocalUsers(ParseUser.getCurrentUser().getObjectId());
            items.clear();
            for(LocalUser lu:users){
                if(!lu.get_id().equals(ParseUser.getCurrentUser().getObjectId())) {
                    items.add(new RecycleItem(lu.getEmail(), lu.get_id()));
                    Log.d("mail:", lu.getEmail());
                }
            }
            mAdapter = new RecycleAdapterManager(items);
            mRecyclerView.setAdapter(mAdapter);
            registerForContextMenu(mRecyclerView);
            mAdapter.notifyDataSetChanged();
            EditText editTextTeamName = (EditText)findViewById(R.id.editTextTeamName);
            editTextTeamName.setText(team);

        }catch (Exception e){

        }
    }

//    private void WaitForChanges(){
//        (new Thread(new Runnable()
//        {
//
//            @Override
//            public void run()
//            {
//                while (!Thread.interrupted())
//                    try
//                    {
//                        Thread.sleep(100);
//                        runOnUiThread(new Runnable() // start actions in UI thread
//                        {
//
//                            @Override
//                            public void run()
//                            {
//                                if (controller.IsUsersChanged()) {
//                                    List<LocalUser> users = controller.getUsers();
//                                    Toast.makeText(getBaseContext(), "changes: " + users.size() + " size..", Toast.LENGTH_LONG).show();
//                                    UpdateData();
//                                    controller.IsUsersChangedSeen();
//                                    Thread.interrupted();
//                                }
//                            }
//                        });
//                    }
//                    catch (InterruptedException e)
//                    {
//                        // ooops
//                    }
//            }
//        })).start(); // the while thread will start in BG thread
//
//    }

    @Override
    protected void onResume() {
        super.onResume();
        UpdateData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(controller.IsManager()) {
            getMenuInflater().inflate(R.menu.manager, menu);
        }
        else{
            getMenuInflater().inflate(R.menu.user, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = null;
        switch (item.getItemId()) {
            case R.id.menuManagerEditGroup:
                i = new Intent(this,MainActivityCreateTeam.class);
                this.startActivity(i);
                return true;
            case R.id.menuUserLogout:
            case R.id.menuManagerLogout:
                ParseUser.logOut();
                i = new Intent(this,MainLogin.class);
                this.startActivity(i);
                return true;
            case R.id.menuManagerAbout:
                i = new Intent(this,AboutActivity.class);
                this.startActivity(i);
                return true;
            case R.id.menuManagerTasks:
                i = new Intent(this,UserTasks.class);
                this.startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Friends Option");
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_edit, menu);
    }


    public void goToLogin(View v){
        Intent intent = new Intent(this, MainLogin.class);
        startActivity(intent);
    }
}
