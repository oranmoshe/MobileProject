package om.otsproject;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;
import java.util.List;

/**
 * Created by oranmoshe on 3/7/16.
 */
public class DatabaseParse extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    public void SetTimer(final int minutes, final DatabaseHandler databaseHandler){
        ParseUser parseUser = ParseUser.getCurrentUser();
        parseUser.put("t_id", minutes);
        parseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                databaseHandler.SetTimer(ParseUser.getCurrentUser().getObjectId(), minutes);
            }
        });
    }

    public void ImportData(final String objectID, final DatabaseHandler dh, final Event event) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("m_id",objectID);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, com.parse.ParseException e) {
                if (e == null) {
                    dh.ClearUsers();
                    for (ParseUser user : objects) {
                        User lu = new User(user.getObjectId(), user.getString("m_id"),
                                user.getString("username"), user.getString("passwordClone"),
                                user.getString("email"), user.getString("phone"), user.getInt("t_id"),
                                user.getString("team"));
                        dh.Add_User(lu);
                    }
                    event.doEvent(new EventObjectExtender(new EventObject("Done"),1));
                }
                else{
                    event.doEvent(new EventObjectExtender(new EventObject("Error"),0));
                }
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Tasks");
                query.whereEqualTo("m_id", objectID);
                final List<ParseObject> list = new ArrayList<ParseObject>();
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, com.parse.ParseException e) {

                        if (e == null) {
                            for (ParseObject o : objects) {
                                Task lt = new Task(o.getObjectId(), o.getString("m_id"), o.getString("name"),
                                        o.getInt("priority"), o.getString("location"), o.getString("due_time"),
                                        o.getString("assign"), o.getInt("accept"), o.getInt("status"), o.getString("pic"),
                                        o.getString("category"));
                                dh.Add_Task(lt);
                            }
                            event.doEvent(new EventObjectExtender(new EventObject("Done"),1));
                        } else {
                            event.doEvent(new EventObjectExtender(new EventObject("Error"),0));
                        }
                    }
                });
            }
        });
    }


    protected void SignUp(final String  m_id, final String username, final String password,
                          final String email, final String phone, final int t_id, final String team,
                          final DatabaseHandler dh,final Event result) {
        // Checks if username available
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("email", email);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, com.parse.ParseException e) {

                if (e == null && objects.size() ==0) {
                    final ParseUser parseUser = new ParseUser();
                    parseUser.put("username", username);
                    parseUser.put("password", phone);
                    parseUser.put("passwordClone", phone);
                    parseUser.put("phone", phone);
                    parseUser.put("email", email);
                    parseUser.put("t_id", 15);
                    parseUser.put("team", team);
                    parseUser.put("m_id", m_id);

                    final ParseUser parseUserManager = ParseUser.getCurrentUser();
                    if (parseUserManager != null) {
                        // do stuff with the user
                        ParseUser.logOut();
                    }
                    parseUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(com.parse.ParseException e) {
                            try {
                                ParseUser.logInInBackground(parseUserManager.getUsername(), parseUserManager.getString("passwordClone"), new LogInCallback() {
                                    public void done(ParseUser user, com.parse.ParseException e) {
                                        try {
                                            if (user != null) {
                                                result.doEvent(new EventObjectExtender(parseUser.getObjectId(),1));
                                            } else {
                                                result.doEvent(new EventObjectExtender(new EventObject(e),-1));
                                            }
                                        } catch (Exception exc) {
                                            result.doEvent(new EventObjectExtender(new EventObject(e),-1));
                                        }
                                    }
                                });
                            } catch (Exception exc) {
                                result.doEvent(new EventObjectExtender(new EventObject(e),-1));
                            }
                        }
                    });
                } else {
                    if(objects.size() >0){
                        result.doEvent(new EventObjectExtender(new EventObject("Username not available"),0));
                    }else{
                        result.doEvent(new EventObjectExtender(new EventObject(e.toString()),-1));
                    }

                }
            }
        });
    }

    protected void SignUpManager( final String username, final String password,
                          final String email, final String phone, final int t_id, final String team,
                          final DatabaseHandler dh,final Event event) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser!=null) {
            // do stuff with the user
            currentUser.logOut();
        }
        // Checks if username available
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("email", email);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, com.parse.ParseException e) {

                if (e == null && objects.size() == 0) {

                    final ParseUser parseUser = new ParseUser();
                    parseUser.put("username", username);
                    parseUser.put("password", password);
                    parseUser.put("passwordClone", password);
                    parseUser.put("phone", phone);
                    parseUser.put("t_id", 15);
                    parseUser.put("email", username);
                    parseUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(com.parse.ParseException e) {
                            if (e == null) {
                                ParseUser.logInInBackground(username, phone, new LogInCallback() {
                                            public void done(ParseUser user, com.parse.ParseException e) {
                                                ParseUser p = ParseUser.getCurrentUser();
                                                p.put("m_id", p.getObjectId());
                                                p.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        if (e == null) {
                                                            event.doEvent(new EventObjectExtender(new EventObject("Done"), 1));
                                                        } else {
                                                            event.doEvent(new EventObjectExtender(new EventObject("Error"), -1));
                                                        }

                                                    }
                                                });
                                            }
                                        }
                                );
                            }
                        }
                    });

                } else {
                    if (objects.size() > 0) {
                        event.doEvent(new EventObjectExtender(new EventObject("Error"), 0));
                    } else {
                        event.doEvent(new EventObjectExtender(new EventObject("Error"), -1));
                    }
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
                Task lt = new Task(testObject.getObjectId(), m_id, name, priority, location, due_time, assign, accept, status, pic, category);
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

    public  void UpdateTaskPic(final String t_id, final String pic,final Event event) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tasks");
        query.whereEqualTo("objectId", t_id);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {

                if (e == null) {
                    if (objects.size() > 0) {
                        ParseObject po = objects.get(0);
                        po.put("pic", pic);
                        po.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(com.parse.ParseException e) {
                                if(e==null) {
                                    event.doEvent(new EventObject("Done"));
                                }
                                else{
                                    event.doEvent(new EventObject("Error"));
                                }
                            }
                        });
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

    public  void UpdateTaskAccept(final String t_id, final int accept) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tasks");
        query.whereEqualTo("objectId", t_id);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {

                if (e == null) {
                    if (objects.size() > 0) {
                        ParseObject po = objects.get(0);
                        po.put("accept", accept);
                        po.saveInBackground();
                    }
                    Log.d("saved", objects.get(0).get("name").toString());
                } else {
                    Log.d("not saved", "no entries found");
                }
            }
        });

    }

    public  void UpdateTaskAssign(final String t_id, final String assign) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tasks");
        query.whereEqualTo("objectId", t_id);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {

                if (e == null) {
                    if (objects.size() > 0) {
                        ParseObject po = objects.get(0);
                        po.put("assign", assign);
                        po.saveInBackground();
                    }
                    Log.d("saved", objects.get(0).get("name").toString());
                } else {
                    Log.d("not saved", "no entries found");
                }
            }
        });

    }

    public  void  DeleteUser(final String username,final String password, final Event event) {
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
                            if (arg0 == null) {
                                ParseUser.logInInBackground(managerUsername, managerPassword, new LogInCallback() {
                                    public void done(ParseUser user, com.parse.ParseException e) {
                                        if (user != null) {
                                            event.doEvent(new EventObject(true));
                                        } else {
                                            event.doEvent(new EventObject(false));
                                        }
                                    }
                                });
                            } else {
                                System.out.println("deleted the tag crashed" + arg0);
                            }
                        }
                    });
                } else {
                    event.doEvent(new EventObject(false));
                }
            }
        });

    }

    public int GetTimer() {
        int timer = 0;
        timer = ParseUser.getCurrentUser().getInt("t_id");
        return timer;
    }
}

