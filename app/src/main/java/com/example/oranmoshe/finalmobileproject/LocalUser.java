package com.example.oranmoshe.finalmobileproject;

import com.parse.ParseUser;

/**
 * Created by oranmoshe on 3/21/16.
 */
public class LocalUser {

    // private variables
    private String _id;
    private String _m_id;
    private String _username;
    private String _password;
    private String _email;
    private String _phone;
    private int _t_id;
    private String _team;

    public LocalUser() {
    }

    // constructor
    public LocalUser(String id, String m_id, String username, String password, String email, String phone, int t_id, String team) {
        this._id = id;
        this._m_id = m_id;
        this._username = username;
        this._password = password;
        this._email = email;
        this._phone = phone;
        this._t_id = t_id;
        this._team = team;
    }

    // constructor
    public LocalUser(String m_id, String username, String password, String email, String phone, int t_id, String team) {
        this._m_id = m_id;
        this._username = username;
        this._password = password;
        this._email = email;
        this._phone = phone;
        this._t_id = t_id;
        this._team = team;
    }
    // get m_id
    public String get_id(){
        return this._id;
    }
    public void set_id(String id){
        this._id = id;
    }
    // get m_id
    public String getM_ID(){
        return this._m_id;
    }
    // set m_id
    public void setM_ID(String m_id){
        this._m_id = m_id;
    }

    // get username
    public String getUsername(){
        return this._username;
    }
    // set username
    public void setUsername(String username){
        this._username = username;
    }

    // get password
    public String getPassword(){
        return this._password;
    }
    // set password
    public void setPassword(String password){
        this._password = password;
    }

    // get email
    public String getEmail(){
        return this._email;
    }
    // set email
    public void setEmail(String email){
        this._email = email;
    }

    // get phone
    public String getPhone(){
        return this._phone;
    }
    // set phone
    public void setPhone(String phone){
        this._phone = phone;
    }

    // get phone
    public String getTeam(){
        return this._team;
    }
    // set phone
    public void setTeam(String team){
        this._team = team;
    }

    // get t_id
    public int getT_ID(){
        return this._t_id;
    }
    // set t_id
    public void setT_ID(int t_id){
        this._t_id = t_id;
    }

    public ParseUser ToParse(){
        User parseUser=new User();
        parseUser.setM_ID(_m_id);
        parseUser.setU_ID(_id);
        parseUser.setUsername(_username);
        parseUser.setPassword(_password);
        parseUser.setPhone(_phone);
        parseUser.setEmail(_email);
        parseUser.setT_ID(_t_id);
        parseUser.setTeam(_team);
        return parseUser;
    }

}
