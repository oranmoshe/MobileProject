package com.example.oranmoshe.finalmobileproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseUser;

import java.util.ArrayList;

public class UserTasks extends AppCompatActivity {
    Controller controller;
    TabLayout tabLayout;
    ViewPager viewPager;
    private ProgressDialog progressDialog;

    public interface FragmentRefreshListener{
        void onRefresh();
    }
    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    private FragmentRefreshListener fragmentRefreshListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_tasks);

        viewPager = (ViewPager)findViewById(R.id.viewTabPager);
        viewPager.setAdapter(new CustomAdapter(getSupportFragmentManager(),getApplicationContext()));

        tabLayout = (TabLayout)findViewById(R.id.viewTabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                ArrayList<LocalUser> list = controller.getLocalUsers(ParseUser.getCurrentUser().getString("m_id"));
                if (list.size() > 1) {
                    Intent intent = new Intent(getBaseContext(), TaskNew.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getBaseContext(), "There are no users in the team, please add team members.", Toast.LENGTH_LONG).show();
                }
            }
        });

        if(!ParseUser.getCurrentUser().getString("m_id").equals(ParseUser.getCurrentUser().getObjectId())){
            fab.setVisibility(View.GONE);
        }

        FloatingActionButton fabRefresh = (FloatingActionButton)findViewById(R.id.fabRefresh);
        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = getIntent();
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("FRAGMENT", String.valueOf(viewPager.getCurrentItem()));
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });

        controller = Controller.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Refresh(getIntent());
    }

    public void Refresh(Intent i){
        String givenFregment = (String)getIntent().getSerializableExtra("FRAGMENT");
        if(givenFregment!=null) {
            int fragment = Integer.parseInt(givenFregment);
            viewPager.setCurrentItem(fragment);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(controller.IsManager()) {
            getMenuInflater().inflate(R.menu.manager, menu);
        }
        else{
            getMenuInflater().inflate(R.menu.user, menu);
        }
        Log.d("manager","sdfssfsF");
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class CustomAdapter extends FragmentPagerAdapter {

        String [] fragments = {"All","Pending","InProgress","Done"};

        public CustomAdapter(FragmentManager supportFragmentManager, Context applicationContext) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new FragmentAllUserTasks();
                case 1:
                    return new FragmentPandingUserTasks();
                case 2:
                    return new FragmentInProgressUserTasks();
                case 3:
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
}
