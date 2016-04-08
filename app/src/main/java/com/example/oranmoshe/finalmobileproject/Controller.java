package com.example.oranmoshe.finalmobileproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;
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

    public void setContext(Context _ctx){
        this.ctx = _ctx;
    }

    public Context getContext(){
        return this.ctx;
    }

    public String getUsername(){
        return this.username;
    }

    public void setUsername(String _username){
        this.username = _username;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String _password){
        this.password = _password;
    }

    // Functionality

    public void UpdateLocal(String m_id, Intent intent){
      //  db.ImportUsers(m_id, dbLocal, intent,ctx);
      //  db.ImportTasks(m_id, dbLocal, intent, ctx);
    }

    public void ImportData(String m_id, Intent intent) {
        dbLocal.InitData();
        db.ImportData(m_id, dbLocal, intent, ctx);
    }

    public String GetManager(){
        return ParseUser.getCurrentUser().getString("m_id");
    }

    public void RemoveUser(String id){
        db.DeleteUser(id, dbLocal);
    }

    public String AddUser(String  m_id, String username, String password, String email, String phone, int t_id,
                          String team, Context context){
        ParseUser parseUser = new ParseUser();
        parseUser.put("username", username);
        parseUser.put("password", phone);
        parseUser.put("passwordClone", phone);
        parseUser.put("phone", phone);
        parseUser.put("email", email);
        parseUser.put("t_id", 0);
        parseUser.put("team", ParseUser.getCurrentUser().getString("team"));
        parseUser.put("m_id", ParseUser.getCurrentUser().getObjectId());
        db.SignUp(parseUser, dbLocal);
        return "";
    }

    public void UpdateTeamName(String teamName, String u_id){
        db.UpdateTeamName(teamName);
        dbLocal.UpdateTeamName(teamName, u_id);
    }

    public void AddTask(String m_id, String name, int priority, String location, String due_time, String assign,
                        int accept, int status, String pic, String category){
        LocalTask lt = new LocalTask(m_id,  name,  priority,  location,  due_time,  assign, accept,  status,  pic,  category);
        db.AddTask(m_id, name, priority, location, due_time, assign, accept, status, pic, category);
        dbLocal.Add_Task(lt);
    }

    public boolean IsUsersChanged(){
        return dbLocal.IsUsersChanged();
    }

    public void IsUsersChangedSeen(){
        dbLocal.IsUsersChangedSeen();
    }


    public List<LocalUser> getUsers(){
        return this.dbLocal.Get_Users();
    }

    public List<LocalTask> getTasks(){
        return this.dbLocal.Get_Tasks();
    }

    public  ArrayList<LocalUser> getLocalUsers(String m_id){
        return dbLocal.Get_Users(m_id);
    }

    public  LocalUser getLocalUser(String u_id){
        return dbLocal.Get_User(u_id);
    }

    public  LocalUser getLocalUserByUsername(String username){
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

    public void UpdateUser(String m_id, String username, String password, String email, String phone, int t_id, String team){
        // Add User Locally
        LocalUser localUser = new LocalUser( m_id,  username,  password,  email,  phone,  t_id, team);
        dbLocal.Add_User(localUser);
        // Add User Cloud
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseUser parseUser=new ParseUser();
        parseUser.setUsername(username);
        parseUser.setPassword(password);
        db.SignUp(parseUser, dbLocal);
    }

    public void Login(String _username, String _password){
        this.username = _username;
        this.password = _password;
        this.db.Login(_username, _password);
    }

    public int  Get_Total_Tasks(){
        return dbLocal.Get_Total_Tasks();
    }

    public int  Get_Total_Users(){
        return dbLocal.Get_Total_Users();
    }

    public boolean IsManager(){
        if(ParseUser.getCurrentUser().getString("m_id").equals(ParseUser.getCurrentUser().getObjectId())){
            return true;
        }
        return false;
    }

    public  ArrayList<LocalTask> getLocalTasks(String key, String value){
        return dbLocal.Get_Tasks(key, value);
    }

    public  ArrayList<LocalTask> getLocalTasksByUserAndStatus(String assign, int status){
        return dbLocal.Get_Tasks_By_User_And_Status(assign, status);
    }

    public  ArrayList<LocalTask> getLocalTasksByManagerAndStatus(String assign, int status){
        return dbLocal.Get_Tasks_By_Manager_And_Status(assign, status);
    }

    public  ArrayList<LocalTask> getLocalTasksByUser(String assign){
        return dbLocal.Get_Tasks_By_User(assign);
    }
    public  ArrayList<LocalTask> getLocalTasksByManager(String assign){
        return dbLocal.Get_Tasks_By_Manager(assign);
    }

    public  LocalTask getLocalTask(String key, String value){
        return dbLocal.Get_Task(key,value);
    }

}
