package com.example.oranmoshe.finalmobileproject;

import com.parse.ParseClassName;
import com.parse.ParseObject;
/**
 * Created by oranmoshe on 3/15/16.
 */
@ParseClassName("Task")
public class Task extends ParseObject {

    public int getTaskID() {
        return getInt("t_id");
    }

    public void setTaskID(int taskID){
        put("t_id",taskID);
    }

    public int getUserID() {
        return getInt("u_id");
    }

    public void setUserID(int userID){
        put("u_id",userID);
    }

    public String getCategory() {
        return getString("category");
    }

    public void setCategory(String category){
        put("category",category);
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name){
        put("name",name);
    }

    public int getAssign() {
        return getInt("assign");
    }

    public void setAssign(int assign){
        put("assign",assign);
    }

    public void setAccept(int accept){
        put("accept",accept);
    }

    public int getAccept() {
        return getInt("accept");
    }

    public void setStatus(int status){
        put("status",status);
    }

    public int getStatus() {
        return getInt("status");
    }

    public String getPic() {
        return getString("pic");
    }

    public void setPic(String pic){
        put("pic",pic);
    }

    public String getDueTime() {
        return getString("dueTime");
    }

    public void setDueTime(String dueTime){
        put("dueTime",dueTime);
    }

    public int getPriority() {
        return getInt("priority");
    }

    public void setPriority(int priority){
        put("priority",priority);
    }

    public String getLocation() {
        return getString("location");
    }

    public void setLocation(String location){
        put("location",location);
    }

    @Override
    public String toString() {
        return this.getCategory();
    }

}
