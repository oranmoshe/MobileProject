package com.example.oranmoshe.finalmobileproject;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by oranmoshe on 3/14/16.
 */
@ParseClassName("User")
public class User extends ParseUser {

    public User(){}

    public String getUsername(){
        return getString("username");
    }

    public void setUsername(String username){
        put("username",username);
    }

    public String getPassword(){
        return getString("passwordClone");
    }

    public void setPassword(String password){
        put("password",password);put("passwordClone",password);
    }

    public String getEmail(){
        return getString("email");
    }

    public void setEmail(String email) {
        put("email", email);
    }

    public String getPhone(){
        return getString("phone");
    }

    public void setPhone(String phone) {
        put("phone", phone);
    }

    public String getM_ID(){
        return getString("m_id");
    }

    public void setM_ID (String m_id) {
        put("m_id", m_id);
    }

    public int getT_ID(){
        return getInt("t_id");
    }

    public void setT_ID (int t_id) {
        put("t_id", t_id);
    }

    public String getU_ID(){
        return getString("u_id");
    }

    public void setU_ID (String u_id) {
        put("u_id", u_id);
    }

    public String getTeam(){
        return getString("team");
    }

    public void setTeam(String team){
        put("team",team);
    }

    @Override
    public String toString() {
        return this.getUsername();
    }
}
