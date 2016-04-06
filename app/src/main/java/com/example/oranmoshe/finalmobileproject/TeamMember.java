package com.example.oranmoshe.finalmobileproject;

import com.parse.ParseClassName;
import com.parse.ParseObject;


/**
 * Created by oranmoshe on 3/14/16.
 */
@ParseClassName("TeamMember")
public class TeamMember extends ParseObject {

    public void setUserID(int teamID){
        put("userID",teamID);
    }

    public int getUserID() {
        return getInt("userID");
    }

    public void setTeamID(int teamID){
        put("teamID",teamID);
    }

    public int getTeamID() {
        return getInt("teamID");
    }

}
