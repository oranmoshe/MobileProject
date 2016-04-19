package om.otsproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by oranmoshe on 4/5/16.
 */

public class FragmentRequestsTasks extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        status = 0;
        Initializing(status,inflater,container);
        return rootView;
    }

    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(UNIQUE_FRAGMENT_GROUP_ID, R.string.option_task_view, 0, R.string.option_task_view);
        if(!controller.IsManager()) {
            menu.add(UNIQUE_FRAGMENT_GROUP_ID, R.string.option_task_reject, 0, R.string.option_task_reject);
            menu.add(UNIQUE_FRAGMENT_GROUP_ID, R.string.option_task_accept, 0, R.string.option_task_accept);
        }else{
            menu.add(UNIQUE_FRAGMENT_GROUP_ID, R.string.option_task_edit, 0, R.string.option_task_edit);
        }
    }

}




