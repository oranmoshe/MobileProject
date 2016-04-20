package om.otsproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventObject;

public class TasksActivity extends BaseClass {
    Controller controller;
    TabLayout tabLayout;
    ViewPager viewPager;
    FloatingActionButton fabRefresh = null;
    private ProgressDialog progressDialog;
    public static int currentItem = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_tasks);

        mHandler = new Handler(Looper.getMainLooper());


        viewPager = (ViewPager)findViewById(R.id.viewTabPager);
        viewPager.setAdapter(new CustomAdapter(getSupportFragmentManager(), getApplicationContext()));

        tabLayout = (TabLayout)findViewById(R.id.viewTabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                currentItem = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                currentItem = tab.getPosition();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                currentItem = tab.getPosition();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                ArrayList<User> list = controller.getLocalUsers(ParseUser.getCurrentUser().getString("m_id"));
                if (list.size() > 1) {
                    Intent intent = new Intent(getBaseContext(), CreateTaskActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getBaseContext(), "There are no users in the team, please add team members.", Toast.LENGTH_LONG).show();
                }
            }
        });

        if(!ParseUser.getCurrentUser().getString("m_id").equals(ParseUser.getCurrentUser().getObjectId())){
            fab.setVisibility(View.GONE);
        }

        fabRefresh = (FloatingActionButton)findViewById(R.id.fabRefresh);
        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = ProgressDialog.show(TasksActivity.this, "",
                        "Please wait..", true);
                final Intent intent = getIntent();
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("FRAGMENT", String.valueOf(viewPager.getCurrentItem()));
                intent.putExtras(mBundle);
                Event event = new Event();
                event.setOnEventListener(new OnEventListener() {
                    @Override
                    public void onEvent(EventObject e) {
                        startActivity(intent);
                    }
                });
                String groupID = ParseUser.getCurrentUser().getString("m_id");
                controller.UpdateLocalDatabase(event, groupID);
                progressDialog.dismiss();
            }
        });

        controller = Controller.getInstance(this);

        SetHeader("Task");

    }



    @Override
    protected void onResume() {
        super.onResume();
        SetFragment();
    }

    public void Refresh(){
        finish();
        Intent intent = getIntent();
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("FRAGMENT", String.valueOf(viewPager.getCurrentItem()));
        intent.putExtras(mBundle);
        startActivity(intent);
    }

    public void SetFragment(){
            viewPager.setCurrentItem(currentItem);
    }



    private class CustomAdapter extends FragmentPagerAdapter {

        String [] fragments = {"All","Requests","Pending","In Process","Done"};

        public CustomAdapter(FragmentManager supportFragmentManager, Context applicationContext) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new FragmentAllUserTasks();
                case 1:
                    return new FragmentRequestsTasks();
                case 2:
                    return new FragmentPandingUserTasks();
                case 3:
                    return new FragmentInProgressUserTasks();
                case 4:
                    return new FragmentDoneUserTasks();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments[position];
        }
    }

    private Handler mHandler;
    Runnable myTask;

    {
        myTask = new Runnable() {
            @Override
            public void run() {
                //do work
                Refresh();
                Calendar c = Calendar.getInstance();
                int seconds = c.get(Calendar.SECOND);
                int hour = c.get(Calendar.HOUR);
                int minute = c.get(Calendar.MINUTE);
                String time = String.valueOf(hour)+"/"+String.valueOf(seconds)+"/"+String.valueOf(minute);
                Log.d("time", time);
                mHandler.postDelayed(this, controller.GetTimer() * 1000*60);
            }
        };
    }

    //just as an example, we'll start the task when the activity is started
    @Override
    public void onStart() {
        super.onStart();
        mHandler.postDelayed(myTask, controller.GetTimer()*1000*60);
    }

    //at some point in your program you will probably want the handler to stop (in onStop is a good place)
    @Override
    public void onStop() {
        super.onStop();
        mHandler.removeCallbacks(myTask);
    }
}
