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

public class FragmentInProgressUserTasks extends BaseFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        status = 2;
        Initializing(status, inflater, container);
        return rootView;
    }

}




