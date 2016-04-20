package om.otsproject;

import java.util.EventObject;

/**
 * Created by oranmoshe on 4/17/16.
 */

public class EventObjectExtender extends EventObject {
    private int id;

    public EventObjectExtender(Object source, int id) {
        super(source);
        this.id = id;
    }

    /** return a String representation of the date */
    public int getId() {
        // return only a String representation
        //   so the user cannot modify the real date
        return id;
    }

}
