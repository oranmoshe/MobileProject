package om.otsproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.EventObject;

public class EditGroupActivity extends BaseClass
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public final static int REQUEST_CODE = 1;
    private ArrayList<RecycleItem> items;
    private String team = "";
    private String u_id = "";
    Controller controller = Controller.getInstance(this);
    ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);

        SetHeader("Edit Group");

        items = new ArrayList<RecycleItem>();
        mRecyclerView = (RecyclerView) findViewById(R.id.cardList);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // buttons
        Button addEmail = (Button) findViewById(R.id.buttonAddEmail);
        addEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String team = ((EditText) findViewById(R.id.editTextTeamName)).getText().toString();
                String u_id = ParseUser.getCurrentUser().getObjectId();
                controller.UpdateTeamName(team, u_id);
                Intent intent = new Intent(getBaseContext(), CreateTeamMemberActivity.class);
                startActivity(intent);
            }
        });


        FloatingActionButton fabmail = (FloatingActionButton) findViewById(R.id.fabMail);
        fabmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emails[] = new String[users.size()];
                int counter = 0;
                for (User user : users) {
                    if (!user.get_id().equals(ParseUser.getCurrentUser().getObjectId())) {
                        emails[counter] = (user.getEmail());
                        counter++;
                    }
                }
                sendEmail(emails);
            }
        });

        FloatingActionButton fabsave = (FloatingActionButton) findViewById(R.id.fabSave);
        fabsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String team = ((EditText) findViewById(R.id.editTextTeamName)).getText().toString();
                String u_id = ParseUser.getCurrentUser().getObjectId();
                controller.UpdateTeamName(team, u_id);
                Intent intent = new Intent(getBaseContext(), TasksActivity.class);
                startActivity(intent);
            }
        });

        UpdateData();
    }

    protected void sendEmail(String[] TO) {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, TO);
        email.putExtra(Intent.EXTRA_SUBJECT, "Invitation to Join OTS team");
        email.putExtra(Intent.EXTRA_TEXT, "Hi\n" +
                "\tYou have been invited to be a team member in an OTS Team created by me.\n" +
                "\tUse this link to download and install the App from Google Play.\n" +
                "https://play.google.com/store/apps/details?id=om.otsproject\n");
        email.setType("message/rfc822");
        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }

    void UpdateData(){
        try {
            String u_id = ParseUser.getCurrentUser().getObjectId();
            team = ParseUser.getCurrentUser().getString("team");
            users = controller.getLocalUsers(ParseUser.getCurrentUser().getObjectId());
            items.clear();
            for(User lu:users){
                if(!lu.get_id().equals(ParseUser.getCurrentUser().getObjectId())) {
                    items.add(new RecycleItem(lu.getEmail(), lu.get_id()));
                    Log.d("mail:", lu.getEmail());
                }
            }
            mAdapter = new RecycleAdapterManager(items);
            mRecyclerView.setAdapter(mAdapter);
            registerForContextMenu(mRecyclerView);
            mAdapter.notifyDataSetChanged();
            EditText editTextTeamName = (EditText)findViewById(R.id.editTextTeamName);
            editTextTeamName.setText(team);

        }catch (Exception e){

        }
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Friends Option");
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_edit, menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = 0;
        try {
            mAdapter = mRecyclerView.getAdapter();
            RecycleAdapterManager recycleAdapterManager =  ((RecycleAdapterManager)mAdapter);
            position = recycleAdapterManager.getPosition();
        } catch (Exception e) {
            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()) {
            case R.id.id_option_delete:
                RecycleItem currentItem = ((RecycleAdapterManager) mAdapter).getRecyceItem(position);
                String id = currentItem.GetUID();
                Event event = new Event();
                event.setOnEventListener(new OnEventListener() {
                    @Override
                    public void onEvent(EventObject e) {
                        UpdateData();
                    }
                });
                controller.RemoveUser(id, event);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
