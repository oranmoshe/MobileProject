package com.example.oranmoshe.finalmobileproject;

import android.widget.Toast;

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
        /*
         * code code code
         */

        // and in the end

        if (mOnEventListener != null)
            mOnEventListener.onEvent(event); // event object :)
    }

    // on the implementation
//    Event updateRecycleview = new Event();
//    updateRecycleview.setOnEventListener(new OnEventListener() {
//        public void onEvent(EventObject e) {
//            // do your work.
//            Toast.makeText(getBaseContext(), e.getSource().toString(), Toast.LENGTH_LONG).show();
//        }
//    });
//    updateRecycleview.doEvent(new EventObject(true));
}