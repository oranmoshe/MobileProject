package om.otsproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.parse.ParseUser;

/**
 * Created by oranmoshe on 4/5/16.
 */

public class FragmentAllUserTasks extends BaseFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_all_user_tasks, container, false);
        textView=(TextView)rootView.findViewById(R.id.textViewCurrent);
        userObjectID = ParseUser.getCurrentUser().getObjectId();
        groupID = ParseUser.getCurrentUser().getString("m_id");
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerViewAllTask);
        status = 0;

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(status);
            }
        });

        Initializing(status);
        return rootView;
    }
}




