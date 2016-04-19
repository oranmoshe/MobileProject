package om.otsproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.parse.ParseUser;

/**
 * Created by oranmoshe on 4/5/16.
 */
public class FragmentPandingUserTasks extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        status = 1;
        Initializing(status, inflater, container);
        return rootView;
    }

    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(UNIQUE_FRAGMENT_GROUP_ID, R.string.option_task_view, 0, R.string.option_task_view);
        menu.add(UNIQUE_FRAGMENT_GROUP_ID, R.string.option_task_edit, 0, R.string.option_task_edit);
    }

}
