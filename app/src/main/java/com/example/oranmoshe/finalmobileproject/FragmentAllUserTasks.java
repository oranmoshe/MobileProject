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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by oranmoshe on 4/5/16.
 */

public class FragmentAllUserTasks extends Fragment {

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
        rootView = inflater.inflate(R.layout.fragment_all_user_tasks,container,false);

        userObjectID = ParseUser.getCurrentUser().getObjectId();

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerViewAllTask);
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
        ArrayList<LocalTask> list = controller.getLocalTasks("assign",userObjectID);
        for (LocalTask lt: list) {
            items.add(new RecycleItem(lt.get_name(),lt.get_t_id()));
        }

        mAdapter = new RecycleAdapterManager(items);
        mRecyclerView.setAdapter(mAdapter);
    }

    public boolean onContextItemSelected(MenuItem item) {

        int position = -1;
        try {
            position =  ((RecycleAdapterManager)mAdapter).getPosition();
        } catch (Exception e) {
            Log.d("dd", e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()) {
            case R.id.id_option_view:
                RecycleItem currentItem = items.get(position);
                Intent intent = new Intent(getContext(),TaskView.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("TASKID", currentItem.GetUID());
                intent.putExtras(mBundle);
                getContext().startActivity(intent);
                break;
            case R.id.id_option_edit:
                RecycleItem currentItem1 = items.get(position);
                Intent intent1 = new Intent(getContext(),TaskEdit.class);
                Bundle mBundle1 = new Bundle();
                mBundle1.putSerializable("TASKID", currentItem1.GetUID());
                intent1.putExtras(mBundle1);
                getContext().startActivity(intent1);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Friends Option");
        getActivity().getMenuInflater().inflate(R.menu.menu_task_item, menu);
    }

}
