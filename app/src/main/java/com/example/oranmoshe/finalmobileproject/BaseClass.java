package com.example.oranmoshe.finalmobileproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseUser;

/**
 * Created by oranmoshe on 4/14/16.
 */
public class BaseClass extends AppCompatActivity {

    Controller controller = Controller.getInstance(this);
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
            case R.id.menuUserAbout:
            case R.id.menuManagerAbout:
                i = new Intent(this,AboutActivity.class);
                this.startActivity(i);
                return true;
            case R.id.menuUserTasks:
            case R.id.menuManagerTasks:
                i = new Intent(this,UserTasks.class);
                this.startActivity(i);
                return true;
            case R.id.menuUserSettings:
            case R.id.menuManagerSettings:
                i = new Intent(this,SettingsActivity.class);
                this.startActivity(i);
                return true;
            case R.id.menuUserLogout:
            case R.id.menuManagerLogout:
                ParseUser.logOut();
                i = new Intent(this,MainLogin.class);
                this.startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
