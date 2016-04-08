package com.example.oranmoshe.finalmobileproject;

/**
 * Created by oranmoshe on 3/21/16.
 */

import android.app.Application;
import android.content.ContentValues;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.EditText;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 41;

    // Database Name
    private static final String DATABASE_NAME = "taskManager";

    // Users table name
    private static final String TABLE_USERS = "users";

    // Users Table Columns names
    private static final String KEY_U_ID = "u_id";
    private static final String KEY_M_ID = "m_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_T_ID = "t_id";
    private static final String KEY_TEAM = "team";


    private final ArrayList<LocalUser> user_list = new ArrayList<LocalUser>();
    private boolean isUsersChanged = false;
    private boolean isTasksChanged = false;

    // Users table name
    private static final String TABLE_TASKS = "tasks";

    // Users Table Columns names
    private static final String KEY_TASK_ID = "t_id";
    private static final String KEY_USER_ID = "u_id";
    private static final String KEY_MANAGER_ID = "m_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PRIORITY = "priority";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_DUETIME = "dueTime";
    private static final String KEY_ASSIGN = "assign";
    private static final String KEY_ACCEPT = "accept";
    private static final String KEY_STATUS = "status";
    private static final String KEY_PIC = "pic";
    private static final String KEY_CATEGORY = "category";

    private final ArrayList<LocalTask> task_list = new ArrayList<LocalTask>();

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {


        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_U_ID + " TEXT,"
                + KEY_M_ID + " TEXT,"
                + KEY_USERNAME + " TEXT,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_EMAIL +" TEXT,"
                + KEY_PHONE +" TEXT,"
                + KEY_T_ID +" INTEGER,"
                + KEY_TEAM +" TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
                + KEY_TASK_ID  + " TEXT" + ","
                + KEY_MANAGER_ID + " TEXT" + ","
                + KEY_NAME + " TEXT " +","
                + KEY_PRIORITY + " INTEGER" + ","
                + KEY_LOCATION + " TEXT" + ","
                + KEY_DUETIME + " TEXT" + ","
                + KEY_ASSIGN +" INTEGER" + ","
                + KEY_ACCEPT +" INTEGER" + ","
                + KEY_STATUS +" INTEGER" + ","
                + KEY_PIC +" TEXT" + ","
                + KEY_CATEGORY + " TEXT" +")";
        db.execSQL(CREATE_TASKS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new task
    public void Add_Task(LocalTask task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TASK_ID, task.get_t_id());
        values.put(KEY_MANAGER_ID, task.get_m_id());
        values.put(KEY_NAME, task.get_name());
        values.put(KEY_PRIORITY, task.get_priority());
        values.put(KEY_LOCATION, task.get_location());
        values.put(KEY_DUETIME, task.get_due_time());
        values.put(KEY_ASSIGN, task.get_assign());
        values.put(KEY_ACCEPT, task.get_accept());
        values.put(KEY_STATUS, task.get_status());
        values.put(KEY_PIC, task.get_pic());
        values.put(KEY_CATEGORY, task.get_category());
        // Inserting Row
        long id = db.insert(TABLE_TASKS, null, values);
        db.close(); // Closing database connection
    }

    public boolean IsUsersChanged(){
        return isUsersChanged;
    }

    public void IsUsersChangedSeen(){
        isUsersChanged = false;
    }

    // Adding new user
    public void Add_User(final LocalUser user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_U_ID, user.get_id());
        values.put(KEY_M_ID, user.getM_ID());
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_PASSWORD, user.getPassword());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_PHONE, user.getPhone());
        values.put(KEY_T_ID, user.getT_ID());
        values.put(KEY_TEAM, user.getTeam());
        // Inserting Row
        long idLong = 0;
        idLong = db.insert(TABLE_USERS, null, values);
        db.close(); // Closing database connection
        this.isUsersChanged = true;
    }

    // Getting single task
    final LocalTask Get_Task(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASKS, new String[]{KEY_TASK_ID,KEY_MANAGER_ID,KEY_NAME,KEY_PRIORITY,KEY_LOCATION,KEY_DUETIME
                        ,KEY_ASSIGN,KEY_ACCEPT,KEY_STATUS,KEY_PIC,KEY_CATEGORY}, KEY_TASK_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        // return contact
        String _t_id = cursor.getString(0);
        String _m_id = cursor.getString(1);
        String _name = cursor.getString(2);
        int _priority = cursor.getInt(3);
        String _location = cursor.getString(4);
        String _due_time = cursor.getString(5);
        String _assign = cursor.getString(6);
        int _accept = cursor.getInt(7);
        int _status = cursor.getInt(8);
        String _pic = cursor.getString(9);
        String _category = cursor.getString(10);
        LocalTask lt = new LocalTask(_t_id,_m_id,_name,_priority,_location,_due_time,_assign,_accept,_status,_pic,_category);
        // return contact
        cursor.close();
        db.close();

        return lt;
    }

    // Getting single contact
    LocalUser Get_User(String u_id) {
        user_list.clear();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_U_ID,
                        KEY_M_ID, KEY_USERNAME, KEY_PASSWORD, KEY_EMAIL, KEY_PHONE, KEY_T_ID, KEY_TEAM}, KEY_U_ID + "=?",
                new String[]{String.valueOf(u_id)}, null, null, null, null);
        // looping through all rows and adding to list
        LocalUser lu = null;
        if (cursor.moveToFirst()) {
            do {
                // return contact
                lu = new LocalUser(cursor.getString(0),cursor.getString(1),cursor.getString(2),
                        cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getInt(6), cursor.getString(7));
            } while (cursor.moveToNext());
        }

        // return contact
        cursor.close();
        db.close();

        return lu;
    }

    // Getting single contact
    LocalUser Get_User_By_Username(String username) {
        user_list.clear();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[] { KEY_U_ID,
                        KEY_M_ID, KEY_USERNAME, KEY_PASSWORD,KEY_EMAIL, KEY_PHONE,KEY_T_ID,KEY_TEAM}, KEY_USERNAME + "=?",
                new String[] { String.valueOf(username) }, null, null, null, null);
        // looping through all rows and adding to list
        LocalUser lu = null;
        if (cursor.moveToFirst()) {
            do {
                // return contact
                lu = new LocalUser(cursor.getString(0),cursor.getString(1),cursor.getString(2),
                        cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getInt(6), cursor.getString(7));
            } while (cursor.moveToNext());
        }

        // return contact
        cursor.close();
        db.close();

        return lu;
    }

    // Getting single contact
    ArrayList<LocalUser> Get_Users(String m_id) {
        user_list.clear();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[] { KEY_U_ID,
                        KEY_M_ID, KEY_USERNAME, KEY_PASSWORD,KEY_EMAIL, KEY_PHONE,KEY_T_ID,KEY_TEAM}, KEY_M_ID + "=?",
                new String[] { String.valueOf(m_id) }, null, null, null, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                // return contact
                LocalUser lu = new LocalUser(cursor.getString(0),cursor.getString(1),cursor.getString(2),
                        cursor.getString(3),cursor.getString(4),cursor.getString(5),Integer.parseInt(cursor.getString(6)), cursor.getString(7));
                user_list.add((lu));
            } while (cursor.moveToNext());
        }

        // return contact
        cursor.close();
        db.close();

        return user_list;
    }

    public ArrayList<LocalTask>  Get_Tasks_By_Manager(String m_id){
        try {
            task_list.clear();

            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = db.rawQuery("select * from " + TABLE_TASKS +
                    " where " + KEY_MANAGER_ID + " = ?", new String[] { m_id});
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Log.d("size","1");
                    LocalTask task = new LocalTask();
                    String _t_id = cursor.getString(0);
                    String _m_id = cursor.getString(1);
                    String _name = cursor.getString(2);
                    int _priority = cursor.getInt(3);
                    String _location = cursor.getString(4);
                    String _due_time = cursor.getString(5);
                    String _assign = cursor.getString(6);
                    int _accept = cursor.getInt(7);
                    int _status = cursor.getInt(8);
                    String _pic = cursor.getString(9);
                    String _category = cursor.getString(10);
                    LocalTask lt = new LocalTask(_t_id,_m_id,_name,_priority,_location,_due_time,_assign,_accept,_status,_pic,_category);
                    task=lt;
                    // Adding contact to list
                    task_list.add(task);
                } while (cursor.moveToNext());
            }

            // return contact list
            cursor.close();
            db.close();
            return task_list;
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("all_contact", "" + e);
        }

        return task_list;
    }

    public ArrayList<LocalTask>  Get_Tasks_By_User(String u_id){
        try {
            task_list.clear();

            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = db.rawQuery("select * from " + TABLE_TASKS +
                    " where " + KEY_ASSIGN + " = ?", new String[] { u_id});
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Log.d("size","1");
                    LocalTask task = new LocalTask();
                    String _t_id = cursor.getString(0);
                    String _m_id = cursor.getString(1);
                    String _name = cursor.getString(2);
                    int _priority = cursor.getInt(3);
                    String _location = cursor.getString(4);
                    String _due_time = cursor.getString(5);
                    String _assign = cursor.getString(6);
                    int _accept = cursor.getInt(7);
                    int _status = cursor.getInt(8);
                    String _pic = cursor.getString(9);
                    String _category = cursor.getString(10);
                    LocalTask lt = new LocalTask(_t_id,_m_id,_name,_priority,_location,_due_time,_assign,_accept,_status,_pic,_category);
                    task=lt;
                    // Adding contact to list
                    task_list.add(task);
                } while (cursor.moveToNext());
            }

            // return contact list
            cursor.close();
            db.close();
            return task_list;
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("all_contact", "" + e);
        }

        return task_list;
    }

    public ArrayList<LocalTask>  Get_Tasks_By_Manager_And_Status(String m_id, int status){
        try {
            task_list.clear();

            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = db.rawQuery("select * from " + TABLE_TASKS +
                    " where " + KEY_MANAGER_ID + " = ? AND  " + KEY_STATUS + " = ?", new String[] { m_id, String.valueOf(status)});
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Log.d("size","1");
                    LocalTask task = new LocalTask();
                    String _t_id = cursor.getString(0);
                    String _m_id = cursor.getString(1);
                    String _name = cursor.getString(2);
                    int _priority = cursor.getInt(3);
                    String _location = cursor.getString(4);
                    String _due_time = cursor.getString(5);
                    String _assign = cursor.getString(6);
                    int _accept = cursor.getInt(7);
                    int _status = cursor.getInt(8);
                    String _pic = cursor.getString(9);
                    String _category = cursor.getString(10);
                    LocalTask lt = new LocalTask(_t_id,_m_id,_name,_priority,_location,_due_time,_assign,_accept,_status,_pic,_category);
                    task=lt;
                    // Adding contact to list
                    task_list.add(task);
                } while (cursor.moveToNext());
            }

            // return contact list
            cursor.close();
            db.close();
            return task_list;
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("all_contact", "" + e);
        }

        return task_list;
    }

    public ArrayList<LocalTask>  Get_Tasks_By_User_And_Status(String u_id, int status){
        try {
            task_list.clear();

            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = db.rawQuery("select * from " + TABLE_TASKS +
                    " where " + KEY_ASSIGN + " = ? AND  " + KEY_STATUS + " = ?", new String[] { u_id, String.valueOf(status)});
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Log.d("size","1");
                    LocalTask task = new LocalTask();
                    String _t_id = cursor.getString(0);
                    String _m_id = cursor.getString(1);
                    String _name = cursor.getString(2);
                    int _priority = cursor.getInt(3);
                    String _location = cursor.getString(4);
                    String _due_time = cursor.getString(5);
                    String _assign = cursor.getString(6);
                    int _accept = cursor.getInt(7);
                    int _status = cursor.getInt(8);
                    String _pic = cursor.getString(9);
                    String _category = cursor.getString(10);
                    LocalTask lt = new LocalTask(_t_id,_m_id,_name,_priority,_location,_due_time,_assign,_accept,_status,_pic,_category);
                    task=lt;
                    // Adding contact to list
                    task_list.add(task);
                } while (cursor.moveToNext());
            }

            // return contact list
            cursor.close();
            db.close();
            return task_list;
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("all_contact", "" + e);
        }

        return task_list;
    }

    public ArrayList<LocalTask>  Get_Tasks(String key, String value){
        try {
            task_list.clear();

            // Select All Query
            String selectQuery = "SELECT  * FROM " + TABLE_TASKS + " WHERE " + key + " = '"+ value +"'";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Log.d("size","1");
                    LocalTask task = new LocalTask();
                    String _t_id = cursor.getString(0);
                    String _m_id = cursor.getString(1);
                    String _name = cursor.getString(2);
                    int _priority = cursor.getInt(3);
                    String _location = cursor.getString(4);
                    String _due_time = cursor.getString(5);
                    String _assign = cursor.getString(6);
                    int _accept = cursor.getInt(7);
                    int _status = cursor.getInt(8);
                    String _pic = cursor.getString(9);
                    String _category = cursor.getString(10);
                    LocalTask lt = new LocalTask(_t_id,_m_id,_name,_priority,_location,_due_time,_assign,_accept,_status,_pic,_category);
                    task=lt;
                    // Adding contact to list
                    task_list.add(task);
                } while (cursor.moveToNext());
            }

            // return contact list
            cursor.close();
            db.close();
            return task_list;
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("all_contact", "" + e);
        }

        return task_list;
    }

    public LocalTask  Get_Task(String key, String value){
        try {
            task_list.clear();

            // Select All Query
            String selectQuery = "SELECT  * FROM " + TABLE_TASKS + " WHERE " + key + " = '"+ value +"'";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Log.d("size","1");
                    LocalTask task = new LocalTask();
                    String _t_id = cursor.getString(0);
                    String _m_id = cursor.getString(1);
                    String _name = cursor.getString(2);
                    int _priority = cursor.getInt(3);
                    String _location = cursor.getString(4);
                    String _due_time = cursor.getString(5);
                    String _assign = cursor.getString(6);
                    int _accept = cursor.getInt(7);
                    int _status = cursor.getInt(8);
                    String _pic = cursor.getString(9);
                    String _category = cursor.getString(10);
                    LocalTask lt = new LocalTask(_t_id,_m_id,_name,_priority,_location,_due_time,_assign,_accept,_status,_pic,_category);
                    task=lt;
                    // Adding contact to list
                    task_list.add(task);
                } while (cursor.moveToNext());
            }

            // return contact list
            cursor.close();
            db.close();
            return task_list.get(0);
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("all_contact", "" + e);
        }

        return null;
    }

    public void UpdateTeamName(String teamName, String u_id) {
        try {
            LocalUser lu = Get_User(u_id);
            lu.setTeam(teamName);
            Update_User(lu);
        }catch (Exception exc){

        }
    }

    public void ClearUsers(){
        List<LocalUser> localUsers = Get_Users();
        for (LocalUser lu: localUsers) {
             Delete_User(lu.get_id());
        }
    }

    // Getting All Contacts
    public ArrayList<LocalTask> Get_Tasks() {
        try {
            task_list.clear();

            // Select All Query
            String selectQuery = "SELECT  * FROM " + TABLE_TASKS;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            LocalUser lu;
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    LocalTask task = new LocalTask();
                    String _t_id = cursor.getString(0);
                    String _m_id = cursor.getString(1);
                    String _name = cursor.getString(2);
                    int _priority = cursor.getInt(3);
                    String _location = cursor.getString(4);
                    String _due_time = cursor.getString(5);
                    String _assign = cursor.getString(6);
                    int _accept = cursor.getInt(7);
                    int _status = cursor.getInt(8);
                    String _pic = cursor.getString(9);
                    String _category = cursor.getString(10);
                    LocalTask lt = new LocalTask(_t_id,_m_id,_name,_priority,_location,_due_time,_assign,_accept,_status,_pic,_category);
                    task=lt;
                    // Adding contact to list
                    task_list.add(task);
                } while (cursor.moveToNext());
            }

            // return contact list
            cursor.close();
            db.close();
            return task_list;
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("all_contact", "" + e);
        }

        return task_list;
    }

    // Getting All Contacts
    public ArrayList<LocalUser> Get_Users() {
        try {
            user_list.clear();

            // Select All Query
            String selectQuery = "SELECT  * FROM " + TABLE_USERS;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            LocalUser lu;
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    LocalUser user = new LocalUser();
                    lu = new LocalUser(cursor.getString(0),cursor.getString(1),cursor.getString(2),
                            cursor.getString(3),cursor.getString(4),cursor.getString(5),Integer.parseInt(cursor.getString(6)),cursor.getString(7));
                    user=lu;
                    // Adding contact to list
                    user_list.add(user);
                } while (cursor.moveToNext());
            }

            // return contact list
            cursor.close();
            db.close();
            return user_list;
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("all_contact", "" + e);
        }

        return user_list;
    }

    // Updating single contact
    public int Update_User(LocalUser user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_M_ID, user.getM_ID());
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_PASSWORD, user.getPassword());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_PHONE, user.getPhone());
        values.put(KEY_T_ID, user.getT_ID());
        values.put(KEY_TEAM, user.getTeam());

        // updating row
        return db.update(TABLE_USERS, values, KEY_U_ID + " = ?",
                new String[] { String.valueOf(user.get_id()) });
    }

    // Updating single task
    public int Update_Task(String t_id, String name, int priority, String location, String due_time, String assign,
                           int accept, int status, String pic, String category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_PRIORITY, priority);
        values.put(KEY_LOCATION, location);
        values.put(KEY_DUETIME, due_time);
        values.put(KEY_ASSIGN, assign);
        values.put(KEY_ACCEPT, accept);
        values.put(KEY_STATUS, status);
        values.put(KEY_PIC, pic);
        values.put(KEY_CATEGORY, category);
        // updating row
        return db.update(TABLE_TASKS, values, KEY_TASK_ID + " = ?",
                new String[] { String.valueOf(t_id) });
    }

    // Deleting single contact
    public void Delete_User(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, KEY_U_ID + " = ?",
                new String[]{id});
        db.close();
        isUsersChanged=true;
    }

    // Deleting single task
    public void Delete_Task(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, KEY_TASK_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    // Getting users Count
    public int Get_Total_Users() {
        String countQuery = "SELECT  * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    // Getting tasks Count
    public int Get_Total_Tasks() {
        String countQuery = "SELECT  * FROM " + TABLE_TASKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public void InitData() {
        SQLiteDatabase db = this.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.delete(this.TABLE_TASKS, null, null);
        db.delete(this.TABLE_USERS, null, null);
    }
}