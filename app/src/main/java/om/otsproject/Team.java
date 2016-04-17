package om.otsproject;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by oranmoshe on 3/14/16.
 */
@ParseClassName("Team")
public class Team extends ParseObject{

    public void setTeamID(int teamID){
        put("teamID",teamID);
    }

    public int getTeamID() {
        return getInt("teamID");
    }

    public void setManagerID(int managerID){
        put("managerID",managerID);
    }

    public int getManagerID() {
        return getInt("managerID");
    }

    public String getName(){
        return getString("name");
    }

    public void setName(String name){
        put("name",name);
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
