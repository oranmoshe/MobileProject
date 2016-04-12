package com.example.oranmoshe.finalmobileproject;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by oranmoshe on 3/21/16.
 */
public class LocalTask implements Comparable<LocalTask>{

    public String _t_id;
    public String _m_id;
    public String _name;
    public int _priority;
    public String _location;
    public String _due_time;
    public String _category;
    public String _assign;
    public int _accept;
    public int _status;
    public String _pic;

    @Override
    public int compareTo(LocalTask o) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String dateInString = o.get_due_time();
        String thisdateInString = get_due_time();
        Date date = new Date();
        Date thisdate = new Date();
        try {
             date = formatter.parse(dateInString);
             thisdate = formatter.parse(thisdateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return thisdate.compareTo(date);
    }

    LocalTask(){

    }

    // constructor
    public LocalTask(String t_id, String m_id, String name, int priority, String location, String due_time, String assign,
                     int accept, int status, String pic, String category) {
        _t_id=t_id;
        _m_id = m_id;
        _name = name;
        _priority=priority;
        _location=location;
        _due_time =due_time;
        _assign=assign;
        _accept=accept;
        _status=status;
        _pic=pic;
        _category=category;
    }

    // constructor
    public LocalTask( String m_id, String name, int priority, String location, String due_time, String assign,
                     int accept, int status, String pic, String category) {
        _name = name;
        _priority=priority;
        _location=location;
        _due_time =due_time;
        _assign=assign;
        _accept=accept;
        _status=status;
        _pic=pic;
        _category=category;
        _m_id = m_id;
    }

    // get t id
    public String get_t_id(){ return this._t_id; }

    // get m id
    public String get_m_id(){ return this._m_id; }

    // set m id
    public void set_m_id(String m_id){ _m_id = m_id; }

    // set t id
    public void set_t_id(String t_id){ _t_id = t_id; }

    // get name
    public String get_name(){ return this._name; }

    // set name
    public void set_name(String name){ this._name = name; }

    // get priority
    public int get_priority(){
        return this._priority;
    }

    // set priority
    public void set_priority(int priority){
        this._priority = priority;
    }

    // get location
    public String get_location(){ return this._location; }

    // set name
    public void set_location(String location){ this._location = location; }

    // get duetime
    public String get_due_time(){ return this._due_time; }
    // set duetime
    public void set_due_time(String due_time){ this._due_time = due_time; }

    // get assign
    public String get_assign(){ return this._assign; }
    // set assign
    public void set_assign(String assign){ this._assign = assign; }

    // get assign
    public int get_accept(){ return this._accept; }
    // set assign
    public void set_accept(int accept){ this._accept = accept; }

    // get status
    public int get_status(){ return this._status; }
    // set status
    public void set_status(int status){ this._status = status; }

    // get pic
    public String get_pic(){ return this._pic; }
    // set pic
    public void set_pic(String pic){ this._pic = pic; }

    // get category
    public String get_category(){ return this._category; }
    // set category
    public void set_category(String category){ this._category = category; }

//    public Task ToParse(){
//        Task parseTask = new Task();
//        parseTask.setAccept(get_accept());
//        parseTask.setAssign(get_assign());
//        parseTask.setCategory(get_category());
//        parseTask.setDueTime(get_due_time());
//        parseTask.setLocation(get_location());
//        parseTask.setName(get_name());
//        parseTask.setPic(get_pic());
//        parseTask.setPriority(get_priority());
//        parseTask.setStatus(get_status());
//        parseTask.setTaskID(get_t_id());
//        parseTask.setUserID(get_u_id());
//        return parseTask;
//    }

}
