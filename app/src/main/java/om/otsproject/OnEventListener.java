package om.otsproject;

import java.util.EventObject;

/**
 * Created by oranmoshe on 4/14/16.
 */
public interface OnEventListener {
    void onEvent(EventObject e);
    // or void onEvent(); as per your need
}