package om.otsproject;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.List;

/**
 * Created by oranmoshe on 4/14/16.
 */
public class BaseClass extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Controller controller = Controller.getInstance(this);

    public void SetHeader(String title){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setIcon(R.drawable.ic_launcher_new);

        View view = navigationView.getHeaderView(0);
        TextView textView = (TextView)view.findViewById(R.id.textViewUserEmailAddress);
        textView.setText("Hi, "+ ParseUser.getCurrentUser().getEmail());

        if(!controller.IsManager()) {
            Menu menu = navigationView.getMenu();
            MenuItem nav_edit = menu.findItem(R.id.nav_edit_group);
            nav_edit.setVisible(false);
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent i = null;
        if (id == R.id.nav_tasks) {
            i = new Intent(this,TasksActivity.class);
            this.startActivity(i);
        } else if (id == R.id.nav_about) {
            i = new Intent(this,AboutUsActivity.class);
            this.startActivity(i);
        } else if (id == R.id.nav_edit_group) {
            i = new Intent(this,EditGroupActivity.class);
            this.startActivity(i);
        } else if (id == R.id.nav_logout) {
            ParseUser.logOut();
            i = new Intent(this,LoginActivity.class);
            this.startActivity(i);
        } else if (id == R.id.nav_settings) {
            i = new Intent(this,SettingActivity.class);
            this.startActivity(i);
        } else if (id == R.id.nav_share) {
            String urlToShare = "https://play.google.com/store/apps/details?id=om.otsproject";
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            // intent.putExtra(Intent.EXTRA_SUBJECT, "Foo bar"); // NB: has no effect!
            intent.putExtra(Intent.EXTRA_TEXT, urlToShare);

            // See if official Facebook app is found
            boolean facebookAppFound = false;
            List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
            for (ResolveInfo info : matches) {
                if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
                    intent.setPackage(info.activityInfo.packageName);
                    facebookAppFound = true;
                    break;
                }
            }

            // As fallback, launch sharer.php in a browser
            if (!facebookAppFound) {
                String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + urlToShare;
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
            }

            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
