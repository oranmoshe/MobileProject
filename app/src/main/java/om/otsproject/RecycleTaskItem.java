package om.otsproject;

import java.io.Serializable;

/**
 * Created by oranmoshe on 12/2/15.
 */
public class RecycleTaskItem implements Serializable {

    private String name;
    private String u_name;
    private String u_id;
    private String dueTime;
    private boolean isReaden;
    private static final long serialVersionUID = 46543445;

    RecycleTaskItem(String name, String u_id, String u_name,String dueTime, boolean isReaden){
        this.name = name;
        this.u_name = u_name;
        this.u_id = u_id;
        this.dueTime = dueTime;
        this.isReaden = isReaden;
    }

    public String GetName(){
        return this.name;
    }

    public String GetUserName(){
        return this.u_name;
    }

    public String GetUID(){
        return this.u_id;
    }

    public String GetDate(){
        return this.dueTime;
    }

    public boolean GetIsReaden(){ return  isReaden;}

}
