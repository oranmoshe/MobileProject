package com.example.oranmoshe.finalmobileproject;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by oranmoshe on 3/7/16.
 */
public class DatabaseParse extends Application {
    ParseUser parseUser = null;
    public final List<User> listUsers =null;
    public  List<String> midim = new ArrayList<String>();
    public  List<ParseUser> users = new ArrayList<ParseUser>();
    public boolean IsUsersInProcess = false;
    public boolean IsTasksInProcess = false;


    @Override
    public void onCreate() {
        super.onCreate();

        // [Optional] Power your app with Local Datastore. For more info, go to
        // https://parse.com/docs/android/guide#local-datastore
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Task.class);
        Parse.initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }


    public void Login(String username, String password){
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, com.parse.ParseException e) {
                if (user != null) {
                    parseUser = user;
                } else {
                }
            }
        });
    }


    public void ImportData(final String objectID, final DatabaseHandler dh, final Intent intent,final Context context) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("m_id",objectID);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, com.parse.ParseException e) {
                if (e == null) {
                    users.clear();
                    dh.ClearUsers();
                    for (ParseUser user : objects) {
                        users.add(user);
                        LocalUser lu = new LocalUser(user.getObjectId(), user.getString("m_id"),
                                user.getString("username"), user.getString("passwordClone"),
                                user.getString("email"), user.getString("phone"), user.getInt("t_id"),
                                user.getString("team"));
                        dh.Add_User(lu);
                    }
                }
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Tasks");
                query.whereEqualTo("m_id", objectID);
                final List<ParseObject> list = new ArrayList<ParseObject>();
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, com.parse.ParseException e) {

                        if (e == null) {
                            for (ParseObject o : objects) {
                                LocalTask lt = new LocalTask(o.getObjectId(), o.getString("m_id"), o.getString("name"),
                                        o.getInt("priority"), o.getString("location"), o.getString("due_time"),
                                        o.getString("assign"), o.getInt("accept"), o.getInt("status"), o.getString("pic"),
                                        o.getString("category"));
                                dh.Add_Task(lt);
                            }

                        } else {
                        }
                        if (intent != null) {
                            intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    }
                });
            }
        });
    }

    public void ImportUsers(String m_id, final DatabaseHandler dh, final Intent intent,final Context context){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("m_id",m_id);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, com.parse.ParseException e) {
                if (e == null) {
                    users.clear();
                    dh.ClearUsers();
                    for (ParseUser user : objects) {
                        users.add(user);
                        LocalUser lu = new LocalUser(user.getObjectId(), user.getString("m_id"),
                                user.getString("username"), user.getString("passwordClone"),
                                user.getString("email"), user.getString("phone"), user.getInt("t_id"),
                                user.getString("team"));
                        dh.Add_User(lu);
                    }
                }
                if(intent!=null) {
                    intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }



    public void ImportTasks(String objectID, final DatabaseHandler dh, final Intent intent,final Context context){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
        query.whereEqualTo("objectId", objectID);
        final List<ParseObject> list = new ArrayList<ParseObject>();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {

                if (e == null) {
                    for (ParseObject o : objects) {
                        LocalTask lt = new LocalTask(o.getObjectId(), o.getString("u_id"), o.getString("name"),
                                o.getInt("priority"), o.getString("location"), o.getString("dueTime"),
                                o.getString("assign"), o.getInt("accept"), o.getInt("status"), o.getString("pic"),
                                o.getString("category"));
                        dh.Add_Task(lt);
                    }

                } else {
                }
                if (intent != null) {
                    intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }



    public void UpdateUsers(int id, final List<LocalUser> users){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("m_id",id);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, com.parse.ParseException e) {
                for (ParseUser user : objects) {
                    LocalUser lu = new LocalUser();
                    lu.setTeam(((User) user).getTeam());
                    lu.set_id(((User) user).getU_ID());
                    lu.setUsername(((User) user).getUsername());
                    lu.setEmail(((User) user).getEmail());
                    lu.setM_ID(((User) user).getM_ID());
                    lu.setPassword(((User) user).getPassword());
                    lu.setPhone(((User) user).getPhone());
                    users.add(lu);
                    Log.d("score", lu.getEmail());
                }
            }
        });

    }

    public void RemoveUser(String id){
        // Get User
        ParseUser pu = (GetUser(id)).get(0);
        // Login As user
        Login(pu.getString("username"), pu.getString("passwordClone"));

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("u_id", pu.getInt("u_id"));
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, com.parse.ParseException e) {
                if (e == null) {

                    for (ParseUser user : objects) {
                        user.deleteInBackground(new DeleteCallback() {

                            @Override
                            public void done(com.parse.ParseException arg0) {
                                if (arg0 == null)
                                    // TODO Auto-generated method stub
                                    System.out.println("deleted the tag succesfully");
                                else
                                    System.out.println("deleted the tag crashed" + arg0);
                            }
                        });
                    }
                }
            }
        });
    }

    protected void SignUp(String  m_id, String username, String password, String email, String phone, int t_id,
                          String team, final DatabaseHandler dh,final Activity fa) {

        final ParseUser parseUser = new ParseUser();
        parseUser.put("username", username);
        parseUser.put("password", phone);
        parseUser.put("passwordClone", phone);
        parseUser.put("phone", phone);
        parseUser.put("email", email);
        parseUser.put("t_id", t_id);
        parseUser.put("team", team);
        parseUser.put("m_id", m_id);

        final ParseUser parseUserManager = ParseUser.getCurrentUser();
        if (parseUserManager!=null) {
            // do stuff with the user
            ParseUser.logOut();
        }
        parseUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                ParseUser.logInInBackground(parseUserManager.getUsername(), parseUserManager.getString("passwordClone"), new LogInCallback() {
                    public void done(ParseUser user, com.parse.ParseException e) {
                        if (user != null) {
                            final LocalUser localUser = new LocalUser(parseUser.getObjectId(),
                                    parseUser.getString("m_id"), parseUser.getString("username"),
                                    parseUser.getString("passwordClone"), parseUser.getString("email"),
                                    parseUser.getString("phone"), parseUser.getInt("t_id"), parseUser.getString("team"));
                            dh.Add_User(localUser);
                            if (fa != null) {
                                fa.finish();
                            }
                        } else {
                        }
                    }
                });
            }
        });
    }

    protected void SignUpManager(final String  m_id, final String username,final String password, String email, String phone, int t_id,
                          String team, final DatabaseHandler dh,final Activity fa, final Intent intent) {
        final ParseUser parseUser = new ParseUser();
        parseUser.put("username", username);
        parseUser.put("password", phone);
        parseUser.put("passwordClone", phone);
        parseUser.put("phone", phone);
        parseUser.put("email", email);
        parseUser.put("t_id", t_id);
        parseUser.put("team", team);
        final ParseUser parseUserManager = ParseUser.getCurrentUser();
        if (parseUserManager!=null) {
            // do stuff with the user
            ParseUser.logOut();
        }
        parseUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(com.parse.ParseException e) {


                ParseUser.logInInBackground(username, password, new LogInCallback() {
                    public void done(ParseUser user, com.parse.ParseException e) {
                        if (user != null) {
                            user.put("m_id", user.getObjectId());
                            user.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(com.parse.ParseException e) {
                                    final LocalUser localUser = new LocalUser(parseUser.getObjectId(),
                                            parseUser.getString("m_id"), parseUser.getString("username"),
                                            parseUser.getString("passwordClone"), parseUser.getString("email"),
                                            parseUser.getString("phone"), parseUser.getInt("t_id"), parseUser.getString("team"));
                                    dh.Add_User(localUser);
                                    if (fa != null) {
                                        fa.startActivity(intent);
                                        fa.finish();
                                    }
                                }
                            });

                        } else {
                        }
                    }
                });

            }
        });
    }

    public void UpdateUser(final String  m_id, final String username, final String password, final String email, final String phone, final int t_id,
                           final String team, final DatabaseHandler dh,final Activity fa){
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, com.parse.ParseException e) {
                if (user != null) {
                    user.put("m_id",m_id);
                    user.put("password",password);
                    user.put("passwordClone",password);
                    user.put("email",email);
                    user.put("t_id",t_id);
                    user.put("team",team);
                    user.put("phone",phone);
                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(com.parse.ParseException e) {
                            LocalUser localUser = dh.Get_User(ParseUser.getCurrentUser().getObjectId());
                            localUser.setTeam(team);
                            localUser.setPhone(phone);
                            localUser.setUsername(username);
                            localUser.setEmail(email);
                            localUser.setM_ID(m_id);
                            localUser.setPassword(password);
                            localUser.setT_ID(t_id);
                            dh.Update_User(localUser);
                            if (fa != null) {
                                fa.startActivity(new Intent(getBaseContext(),MainActivityCreateTeam.class));
                                fa.finish();
                            }

                        }
                    });
                } else {
                }
            }
        });

    }
    public void UpdateTeamName(String teamName){
        ParseUser pu = ParseUser.getCurrentUser();
        pu.put("team", teamName);
        pu.saveInBackground();
    }

    public  void AddTask(final String m_id, final String name, final int priority,
                            final String location, final String due_time, final String assign,
                            final int accept, final int status, final String pic, final String category,
                         final DatabaseHandler dh, final Activity fa) {

        final ParseObject testObject = new ParseObject("Tasks");
        testObject.put("m_id", m_id);
        testObject.put("name", name);
        testObject.put("priority", priority);
        testObject.put("location", location);
        testObject.put("due_time", due_time);
        testObject.put("assign", assign);
        testObject.put("accept", accept);
        testObject.put("status", status);
        testObject.put("pic", pic);
        testObject.put("category", category);
        testObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                LocalTask lt = new LocalTask(testObject.getObjectId(), m_id, name, priority, location, due_time, assign, accept, status, pic, category);
                dh.Add_Task(lt);
                fa.finish();
            }
        });
    }




    public  void UpdateTask(final String t_id, final String name, final int priority,
                            final String location, final String due_time, final String assign,
                            final int accept, final int status, final String pic, final String category) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tasks");
        query.whereEqualTo("objectId", t_id);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {

                if (e == null) {
                    if (objects.size() > 0) {
                        ParseObject po = objects.get(0);
                        po.put("name", name);
                        po.put("priority", priority);
                        po.put("location", location);
                        po.put("due_time", due_time);
                        po.put("assign", assign);
                        po.put("accept", accept);
                        po.put("status", status);
                        po.put("pic", pic);
                        po.put("category", category);
                        po.saveInBackground();
                    }
                    Log.d("saved", objects.get(0).get("name").toString());
                } else {
                    Log.d("not saved", "no entries found");
                }
            }
        });

    }

    public  void UpdateTaskPic(final String t_id, final String pic) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tasks");
        query.whereEqualTo("objectId", t_id);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {

                if (e == null) {
                    if (objects.size() > 0) {
                        ParseObject po = objects.get(0);
                        po.put("pic", pic);
                        po.saveInBackground();
                    }
                    Log.d("saved", objects.get(0).get("name").toString());
                } else {
                    Log.d("not saved", "no entries found");
                }
            }
        });

    }
    public  void UpdateTaskStatus(final String t_id, final int status) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tasks");
        query.whereEqualTo("objectId", t_id);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {

                if (e == null) {
                    if (objects.size() > 0) {
                        ParseObject po = objects.get(0);
                        po.put("status", status);
                        po.saveInBackground();
                    }
                    Log.d("saved", objects.get(0).get("name").toString());
                } else {
                    Log.d("not saved", "no entries found");
                }
            }
        });

    }
    public  List<ParseObject>  GetTasks(String key, int id) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
        query.whereEqualTo(key, id);
        final List<ParseObject> list = new ArrayList<ParseObject>();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {

                if (e == null) {
                    for (ParseObject o : objects) {
                        ParseObject po = objects.get(0);
                        po.getInt("u_id");
                        po.getString("name");
                        po.getString("priority");
                        po.getString("location");
                        po.getString("dueTime");
                        po.getInt("assign");
                        po.getInt("accept");
                        po.getInt("status");
                        po.getString("pic");
                        po.getString("category");
                        list.add(po);
                    }

                } else {

                }
            }
        });
        return list;
    }

    public  List<ParseUser>  GetUsers(String m_id) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("m_id", m_id);
        final List<ParseUser> list = new ArrayList<ParseUser>();
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, com.parse.ParseException e) {

                if (e == null) {
                    for (ParseUser user : objects) {
                        list.add(user);
                        Log.d("Ss", user.getString("passwordClone"));
                    }
                } else {

                }
            }
        });
        return list;
    }

    public  List<ParseUser>  GetUser(String u_id) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("u_id", u_id);
        final List<ParseUser> list = new ArrayList<ParseUser>();
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, com.parse.ParseException e) {

                if (e == null) {
                    for (ParseUser user : objects) {
                        list.add(user);
                    }
                } else {

                }
            }
        });
        return list;
    }

    public  void  DeleteCurrrentUser() {
        ParseUser.getCurrentUser().deleteInBackground(new DeleteCallback() {
            @Override
            public void done(com.parse.ParseException arg0) {
                if (arg0 == null)
                    // TODO Auto-generated method stub
                    System.out.println("deleted the tag succesfully");
                else
                    System.out.println("deleted the tag crashed" + arg0);
            }
        });
    }

    public  void  DeleteUser(final String username,final String password) {
        //Logout Admin
        ParseUser currentUser = ParseUser.getCurrentUser();
        final String managerUsername = currentUser.getUsername();
        final String managerPassword = currentUser.getString("passwordClone");
        if (currentUser!=null) {
            ParseUser.logOut();
        }
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, com.parse.ParseException e) {
                if (user != null) {
                    user.deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(com.parse.ParseException arg0) {
                            if (arg0 == null){
                                    ParseUser.logInInBackground(managerUsername, managerPassword, new LogInCallback() {
                                        public void done(ParseUser user, com.parse.ParseException e) {
                                            if (user != null) {

                                            } else {
                                            }
                                        }
                                    });
                                }
                            else{
                                System.out.println("deleted the tag crashed" + arg0);
                            }
                        }
                    });

                } else {
                }
            }
        });

    }
}

