package om.otsproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

/**
 * Created by oranmoshe on 3/17/16.
 */


public class Controller {

    private static Controller mInstance = null;
    private DatabaseHandler dbLocal;
    private DatabaseParse db;
    private static String username;
    private static String password;
    private static User currentUser;
    public static Context ctx;


    private Controller(Context _ctx){
        db = new DatabaseParse();
        dbLocal = new DatabaseHandler(_ctx);
        username ="1";
        password = "1";
    }

    public static Controller getInstance(Context _ctx){
        if(mInstance == null) {
            mInstance = new Controller(_ctx);
            ctx = _ctx;
        }
        return mInstance;
    }

    public void SetCurrentUser(User user){
        this.currentUser = user;
    }
    public User GetCurrentUser(){
        return this.currentUser;
    }

    public void setContext(Context _ctx){
        this.ctx = _ctx;
    }

    public Context getContext(){
        return this.ctx;
    }



    // Functionality

    public void UpdateRecycleTasks( RecyclerView mRecyclerView,RecyclerView.Adapter mAdapter, TextView textView,String m_id, int status){
        dbLocal.InitData();
        db.ImportDataAndUpdateRecycler(m_id, dbLocal, mRecyclerView, mAdapter, textView, getContext(), status);
    }


    public void ImportData(String m_id, Intent intent) {
        dbLocal.InitData();
        db.ImportData(m_id, dbLocal, intent, ctx);
    }

    public void RemoveUser(final String id, final Event result){
        User user = getLocalUser(id);

        Event parseEvent = new Event();
        db.DeleteUser(user.getUsername(), user.getPassword(), parseEvent);
        parseEvent.setOnEventListener(new OnEventListener() {
            @Override
            public void onEvent(EventObject e) {
                if ((Boolean) e.getSource()) {
                    dbLocal.Delete_User(id);
                    result.doEvent(new EventObject(true));
                } else {
                    result.doEvent(new EventObject(false));
                }
            }
        });
    }

    public void AddUser(final String  m_id, final String username, final String password, final String email,
                          final  String phone, final int t_id, final String team, final Event result){
        Event parseEvent = new Event();
        parseEvent.setOnEventListener(new OnEventListener() {
            @Override
            public void onEvent(EventObject objectId) {
                if (!objectId.getSource().toString().equals("Error")) {
                    final User user = new User(objectId.toString(), m_id, username,
                            password, email, phone, t_id, team);
                    dbLocal.Add_User(user);
                    result.doEvent(new EventObject(true));
                } else {
                    result.doEvent(new EventObject(false));
                }
            }
        });
        db.SignUp(m_id, username, password, email, phone, t_id, team, dbLocal, parseEvent);
    }

    public void SetTimer(int minutes){
        db.SetTimer(minutes, dbLocal);
    }

    public int GetTimer(){
        return db.GetTimer();
    }


    public void AddManager(final String username, final String password, final String email,
                        final  String phone, final int t_id, final String team, final Event result){
        Event parseEvent = new Event();
        parseEvent.setOnEventListener(new OnEventListener() {
            @Override
            public void onEvent(EventObject e) {
                if (((EventObjectExtender) e).getId() == 1) {
                    final User user = new User(e.getSource().toString(), username,
                            password, email, phone, t_id, team);
                    dbLocal.Add_User(user);
                }
                result.doEvent(e);
            }
        });
        db.SignUpManager(username, password, email, phone, t_id, team, dbLocal, parseEvent);
    }


    public void UpdateTeamName(String teamName, String u_id){
        db.UpdateTeamName(teamName);
        dbLocal.UpdateTeamName(teamName, u_id);
    }

    public void AddTask(String m_id, String name, int priority, String location, String due_time, String assign,
                        int accept, int status, String pic, String category, Activity fa){
        db.AddTask(m_id, name, priority, location, due_time, assign, accept, status, pic, category, dbLocal, fa);
    }

    public  ArrayList<User> getLocalUsers(String m_id){
        return dbLocal.Get_Users(m_id);
    }

    public User getLocalUser(String u_id){
        return dbLocal.Get_User(u_id);
    }

    public User getLocalUserByUsername(String username){
        return dbLocal.Get_User_By_Username(username);
    }

    public void UpdateTask(String t_id, String name, int priority, String location, String due_time, String assign,
                           int accept, int status, String pic, String category){
        // Update Task Locally
        dbLocal.Update_Task(t_id, name, priority, location, due_time, assign,
                accept, status, pic, category);
        // Update Task Cloud
        db.UpdateTask(t_id, name, priority, location, due_time, assign,
                accept, status, pic, category);
    }

    public void UpdateTaskStatus(String t_id,int status){
        // Update Task Locally
        dbLocal.Update_Task_Status(t_id, status);
        // Update Task Cloud
        db.UpdateTaskStatus(t_id, status);
    }

    public void UpdateTaskAssign(String t_id,String assign){
        // Update Task Locally
        dbLocal.Update_Task_Assign(t_id, assign);
        // Update Task Cloud
        db.UpdateTaskAssign(t_id, assign);
    }

    public void UpdateTaskPic(String t_id,String pic, final Event eventUploading){
        // Update Task Locally
        dbLocal.Update_Task_Pic(t_id, pic);
        // Update Task Cloud
        Event eventParse = new Event();
        eventParse.setOnEventListener(new OnEventListener() {
            @Override
            public void onEvent(EventObject e) {
                if(e.getSource().toString().equals("Done")){
                    eventUploading.doEvent(e);
                }
                else{
                    eventUploading.doEvent(e);
                }
            }
        });
        db.UpdateTaskPic(t_id, pic,eventParse);
    }

    public boolean IsManager(){
        if(ParseUser.getCurrentUser().getString("m_id").equals(ParseUser.getCurrentUser().getObjectId())){
            return true;
        }else {
            return false;
        }
    }

    public  ArrayList<Task> getLocalTasksByUserAndStatus(String assign, int status){
        return dbLocal.Get_Tasks_By_User_And_Status(assign, status);
    }

    public  ArrayList<Task> getLocalTasksByManagerAndStatus(String assign, int status){
        return dbLocal.Get_Tasks_By_Manager_And_Status(assign, status);
    }

    public  ArrayList<Task> getLocalTasksByUser(String assign){
        return dbLocal.Get_Tasks_By_User(assign);
    }
    public  ArrayList<Task> getLocalTasksByManager(String assign){
        return dbLocal.Get_Tasks_By_Manager(assign);
    }

    public Task getLocalTask(String key, String value){
        return dbLocal.Get_Task(key, value);
    }

}
