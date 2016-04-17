package om.otsproject;

import java.io.Serializable;

/**
 * Created by oranmoshe on 12/2/15.
 */
public class RecycleItem implements Serializable {

    private String name;
    private String u_id;
    private static final long serialVersionUID = 46543445;

    RecycleItem(String name, String u_id){
        this.name = name;
        this.u_id = u_id;
    }

    public String GetName(){
        return this.name;
    }

    public String GetUID(){
        return this.u_id;
    }


}
