package om.otsproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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


import com.parse.ParseUser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by oranmoshe on 4/14/16.
 */
public class BaseFragment extends Fragment {

    private ProgressDialog progressDialog;
    protected  RecyclerView mRecyclerView;
    protected RecyclerView.Adapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected ArrayList<RecycleTaskItem> items;
    protected SwipeRefreshLayout swipeContainer;
    protected Controller controller = Controller.getInstance(getActivity());
    protected String userObjectID = "";
    protected String groupID = "";
    protected View rootView;
    protected int status;
    protected String task_clicked;
    TextView textView;


    public void fetchTimelineAsync(int status) {
        LoadData(status);
        swipeContainer.setRefreshing(false);
    }

    void LoadData(int status){
        controller.UpdateRecycleTasks(mRecyclerView, mAdapter, textView, groupID, status);
    }

    void Initializing(int status, LayoutInflater inflater, final ViewGroup container){
        try {

            rootView = inflater.inflate(R.layout.fragment_all_user_tasks, container, false);
            textView=(TextView)rootView.findViewById(R.id.textViewCurrent);
            userObjectID = ParseUser.getCurrentUser().getObjectId();
            groupID = ParseUser.getCurrentUser().getString("m_id");
            mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerViewAllTask);

            // swipeContainer listener
            final int status_f = status;
            swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // Your code to refresh the list here.
                    // Make sure you call swipeContainer.setRefreshing(false)
                    // once the network request has completed successfully.
                    fetchTimelineAsync(status_f);
                }
            });

            // mRecyclerView init
            if (mRecyclerView != null) {

                RecyclerView.LayoutManager mLayoutManager;
                ArrayList<RecycleTaskItem> items;
                mRecyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(getContext());
                mRecyclerView.setLayoutManager(mLayoutManager);
                registerForContextMenu(mRecyclerView);

                items = new ArrayList<RecycleTaskItem>();
                List<Task> list = null;

                if (status==0) {
                    if (ParseUser.getCurrentUser().getObjectId().equals(groupID)) {
                        list = controller.getLocalTasksByManager(userObjectID);
                    } else {
                        list = controller.getLocalTasksByUser(userObjectID);
                    }
                }else{
                    if (ParseUser.getCurrentUser().getObjectId().equals(groupID)) {
                        list = controller.getLocalTasksByManagerAndStatus(userObjectID, status);
                    } else {
                        list = controller.getLocalTasksByUserAndStatus(userObjectID, status);
                    }
                }

                Collections.sort(list);
                Task[] sorted = new Task[list.size()];
                sorted = list.toArray(sorted);
                if (list.size() > 0) {
                    for(int i=0; i<sorted.length;i++){
                        String email = controller.getLocalUser(sorted[i].get_assign()).getEmail();
                        items.add(new RecycleTaskItem(sorted[i].get_name(), sorted[i].get_t_id(), email, sorted[i].get_due_time()));
                    }

                    textView.setText(String.valueOf(list.size()) + " tasks");
                }
                Log.d("<<<<<<<<", "status: All size: " + String.valueOf(items.size()));
                mAdapter = new RecycleTaskAdapterManager(items);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

            }
        }
        catch (Exception exc){
            Log.d("Error","on initialize components, "+ exc);
        }
    }
    int UNIQUE_FRAGMENT_GROUP_ID=status;

    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(UNIQUE_FRAGMENT_GROUP_ID, R.string.option_task_view, 0, R.string.option_task_view);
        menu.add(UNIQUE_FRAGMENT_GROUP_ID, R.string.option_task_edit, 0, R.string.option_task_edit);
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
                    Intent intent = new Intent(getContext(),ReportTaskActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable("TASKID", currentItem.GetUID());
                    intent.putExtras(mBundle);
                    getActivity().startActivity(intent);
                    break;
                case R.string.option_task_edit:
                    RecycleTaskItem currentItem1 = ((RecycleTaskAdapterManager) mAdapter).getRecyceItem(position);
                    Intent intent1 = new Intent(getContext(),EditTaskActivity.class);
                    Bundle mBundle1 = new Bundle();
                    mBundle1.putSerializable("TASKID", currentItem1.GetUID());
                    intent1.putExtras(mBundle1);
                    getActivity().startActivity(intent1);
                    break;
                case R.string.option_task_add_pending:
                    RecycleTaskItem currentItem2 = ((RecycleTaskAdapterManager) mAdapter).getRecyceItem(position);
                    controller.UpdateTaskStatus(currentItem2.GetUID(), 1);
                    getActivity().finish();
                    startActivity(getActivity().getIntent());
                    break;
                case R.string.option_task_add_in_process:
                    RecycleTaskItem currentItem3 = ((RecycleTaskAdapterManager) mAdapter).getRecyceItem(position);
                    controller.UpdateTaskStatus(currentItem3.GetUID(), 2);
                    getActivity().finish();
                    startActivity(getActivity().getIntent());
                    break;
                case R.string.option_task_add_done:
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