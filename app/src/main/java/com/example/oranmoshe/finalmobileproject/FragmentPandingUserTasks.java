package com.example.oranmoshe.finalmobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by oranmoshe on 4/5/16.
 */
public class FragmentPandingUserTasks extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public final static int REQUEST_CODE = 1;
    private ArrayList<RecycleItem> items;
    private String team = "";
    Controller controller = Controller.getInstance(getContext());
    private String userObjectID = "";
    View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_panding_user_tasks,container,false);

        userObjectID = ParseUser.getCurrentUser().getObjectId();

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerViewPandingTask);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        registerForContextMenu(mRecyclerView);

        LoadData();

        return rootView;
    }

    void LoadData(){
        // specify an adapter (see also next example)
        items = new ArrayList<RecycleItem>();
        ArrayList<LocalTask> list = null;
        if(controller.IsManager()){
            list = controller.getLocalTasksByManagerAndStatus(userObjectID,2);
        }else{
            list = controller.getLocalTasksByUserAndStatus(userObjectID,2);
        }

        mAdapter = new RecycleAdapterManager(items);
        mRecyclerView.setAdapter(mAdapter);
    }

    int UNIQUE_FRAGMENT_GROUP_ID=2,MENU_OPTION_1=1,MENU_OPTION_2=2;

    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(UNIQUE_FRAGMENT_GROUP_ID, 0, 0, R.string.option_task_view);
        if(controller.IsManager()) {
            menu.add(UNIQUE_FRAGMENT_GROUP_ID, 1, 0, R.string.option_task_edit);
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (getUserVisibleHint()) {
            // Handle menu events and return true
        } else
            return false; // Pass the event to the next fragment

        Log.d("isit","uniq");
        int position = -1;
        try {
            position =  ((RecycleAdapterManager)mAdapter).getPosition();
        } catch (Exception e) {

            return super.onContextItemSelected(item);
        }
        //only this fragment's context menus have group ID of -1
        if (item.getGroupId() == UNIQUE_FRAGMENT_GROUP_ID) {


            switch (item.getItemId()) {
                case 1:
                    RecycleItem currentItem = items.get(position);
                    Intent intent = new Intent(getContext(),TaskView.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable("TASKID", currentItem.GetUID());
                    intent.putExtras(mBundle);
                    getContext().startActivity(intent);
                    break;
                case 2:
                    RecycleItem currentItem1 = items.get(position);
                    Intent intent1 = new Intent(getContext(),TaskEdit.class);
                    Bundle mBundle1 = new Bundle();
                    mBundle1.putSerializable("TASKID", currentItem1.GetUID());
                    intent1.putExtras(mBundle1);
                    getContext().startActivity(intent1);
                    break;
            }
        }
        else
            Log.d("isit","uniq");
        return true;
    }
}
