package com.example.oranmoshe.finalmobileproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseUser;

public class AboutActivity extends AppCompatActivity {

    Controller controller = Controller.getInstance(getBaseContext());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
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

}
