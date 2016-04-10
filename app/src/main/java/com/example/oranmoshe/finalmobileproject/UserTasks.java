package com.example.oranmoshe.finalmobileproject;

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

import com.parse.Parse;
import com.parse.ParseUser;

public class UserTasks extends AppCompatActivity {
    Controller controller;
    TabLayout tabLayout;
    ViewPager viewPager;


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
                Intent intent = new Intent(getBaseContext(), TaskNew.class);
                startActivity(intent);
            }
        });
        if(controller.IsManager()){
            fab.setVisibility(View.GONE);
        }

        controller = Controller.getInstance(this);

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

        String [] fragments = {"All","Panding","InProgress","Done"};

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
