package om.otsproject;

import java.util.EventObject;

/**
 * Created by oranmoshe on 4/14/16.
 */
public class Event {
    private OnEventListener mOnEventListener;

    public void setOnEventListener(OnEventListener listener) {
        mOnEventListener = listener;
    }

    public void doEvent(EventObject event) {

        if (mOnEventListener != null)
            mOnEventListener.onEvent(event); // event object :)
    }

}