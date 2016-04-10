package com.example.oranmoshe.finalmobileproject;

import java.io.Serializable;

/**
 * Created by oranmoshe on 12/2/15.
 */
public class RecycleTaskItem implements Serializable {

    private String name;
    private String u_name;
    private String u_id;
    private static final long serialVersionUID = 46543445;

    RecycleTaskItem(String name, String u_id, String u_name){
        this.name = name;
        this.u_name = u_name;
        this.u_id = u_id;
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


}
