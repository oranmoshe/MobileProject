package com.example.oranmoshe.finalmobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by oranmoshe on 4/5/16.
 */
public class FragmentPandingUserTasks extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public final static int REQUEST_CODE = 1;
    private ArrayList<RecycleTaskItem> items;
    private String team = "";
    Controller controller = Controller.getInstance(getContext());
    private String userObjectID = "";
    protected String groupID = "";
    View rootView;
    private SwipeRefreshLayout swipeContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_panding_user_tasks, container, false);

        userObjectID = ParseUser.getCurrentUser().getObjectId();
        groupID = ParseUser.getCurrentUser().getString("m_id");

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerViewPandingTask);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });

        registerForContextMenu(mRecyclerView);
        LoadData();
        return rootView;
    }

    public void fetchTimelineAsync(int page) {
        LoadData();
        swipeContainer.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        LoadData();
    }

    void LoadData(){
        ArrayList<LocalTask> list = null;
        items = new ArrayList<RecycleTaskItem>();
        if(ParseUser.getCurrentUser().getObjectId().equals(groupID)){
            list = controller.getLocalTasksByManagerAndStatus(groupID, 1);
        }else{
            list = controller.getLocalTasksByUserAndStatus(ParseUser.getCurrentUser().getObjectId(), 1);
        }

        Collections.sort(list, new Comparator<LocalTask>() {
            public int compare(LocalTask o1, LocalTask o2) {
                return o1.compareTo(o2);
            }
        });

        if(list.size() > 0){
            for (LocalTask lt: list) {
                String email = controller.getLocalUser(lt.get_assign()).getEmail();
                items.add(new RecycleTaskItem(lt.get_name(), lt.get_t_id(), email, lt.get_due_time()));
            }
            ((TextView)rootView.findViewById(R.id.textViewCurrent)).setText(String.valueOf(list.size())+ " tasks");
        }

        mAdapter = new RecycleTaskAdapterManager(items);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    int UNIQUE_FRAGMENT_GROUP_ID=2;

    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(UNIQUE_FRAGMENT_GROUP_ID, R.string.option_task_view, 0, R.string.option_task_view);
        if(controller.IsManager()) {
            menu.add(UNIQUE_FRAGMENT_GROUP_ID, R.string.option_task_edit, 0, R.string.option_task_edit);
        }
        else{
            menu.add(UNIQUE_FRAGMENT_GROUP_ID, R.string.option_user_task_pending, 0, R.string.option_user_task_pending);
            menu.add(UNIQUE_FRAGMENT_GROUP_ID, R.string.option_user_task_in_process, 0, R.string.option_user_task_in_process);
            menu.add(UNIQUE_FRAGMENT_GROUP_ID, R.string.option_user_task_done, 0, R.string.option_user_task_done);
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (getUserVisibleHint()) {
            // Handle menu events and return true
        } else
            return false; // Pass the event to the next fragment

        int position = -1;
        try {
            mAdapter = mRecyclerView.getAdapter();
            RecycleTaskAdapterManager recycleTaskAdapterManager =  ((RecycleTaskAdapterManager)mAdapter);
            position = recycleTaskAdapterManager.getPosition();
        } catch (Exception e) {
            return super.onContextItemSelected(item);
        }
        //only this fragment's context menus have group ID of -1
        if (item.getGroupId() == UNIQUE_FRAGMENT_GROUP_ID) {
            switch (item.getItemId()) {
                case R.string.option_task_view:
                    RecycleTaskItem currentItem = ((RecycleTaskAdapterManager) mAdapter).getRecyceItem(position);
                    Intent intent = new Intent(getContext(),TaskView.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable("TASKID", currentItem.GetUID());
                    intent.putExtras(mBundle);
                    getContext().startActivity(intent);
                    break;
                case R.string.option_task_edit:
                    RecycleTaskItem currentItem1 = ((RecycleTaskAdapterManager) mAdapter).getRecyceItem(position);
                    Intent intent1 = new Intent(getContext(),TaskEdit.class);
                    Bundle mBundle1 = new Bundle();
                    mBundle1.putSerializable("TASKID", currentItem1.GetUID());
                    intent1.putExtras(mBundle1);
                    getContext().startActivity(intent1);
                    break;
                case R.string.option_user_task_pending:
                    RecycleTaskItem currentItem2 = ((RecycleTaskAdapterManager) mAdapter).getRecyceItem(position);
                    controller.UpdateTaskStatus(currentItem2.GetUID(), 1);
                    getActivity().finish();
                    startActivity(getActivity().getIntent());
                    break;
                case R.string.option_user_task_in_process:
                    RecycleTaskItem currentItem3 = ((RecycleTaskAdapterManager) mAdapter).getRecyceItem(position);
                    controller.UpdateTaskStatus(currentItem3.GetUID(), 2);
                    getActivity().finish();
                    startActivity(getActivity().getIntent());
                    break;
                case R.string.option_user_task_done:
                    RecycleTaskItem currentItem4 = ((RecycleTaskAdapterManager) mAdapter).getRecyceItem(position);
                    controller.UpdateTaskStatus(currentItem4.GetUID(),3);
                    getActivity().finish();
                    startActivity(getActivity().getIntent());
                    break;
            }
        }
        return true;
    }
}
