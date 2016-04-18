package om.otsproject;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.Spinner;

public class SettingActivity extends BaseClass
        implements NavigationView.OnNavigationItemSelectedListener {

    Spinner spinnerTimer = null;
    ArrayAdapter<CharSequence> adapterTimer=null;
    Controller controller = Controller.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        SetHeader("Settings");

        spinnerTimer = (Spinner)findViewById(R.id.spinnerTimer);
        adapterTimer = ArrayAdapter.createFromResource(this,
                R.array.timer, android.R.layout.simple_spinner_item);
        adapterTimer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTimer.setAdapter(adapterTimer);
        String currentTimer = String.valueOf(controller.GetTimer());
        spinnerTimer.setSelection(adapterTimer.getPosition(currentTimer + " minutes"));
        spinnerTimer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        controller.SetTimer(5);
                        break;
                    case 1:
                        controller.SetTimer(15);
                        break;
                    case 2:
                        controller.SetTimer(30);
                        break;
                    case 3:
                        controller.SetTimer(60);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
