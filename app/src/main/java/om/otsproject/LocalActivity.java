package om.otsproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;



import java.util.List;

public class LocalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);
        Controller c = Controller.getInstance(this);
        Intent intent = new Intent(LocalActivity.this ,MainActivityCreateTeam.class);
        List<LocalUser> lu = c.getUsers();
        lu = c.getUsers();
        //c.AddUser("d", "fgsdfH22Hg44gdfs", "sdf54224gdsfgdd", "sdfgd4224fgs5d@wal.dl", "sd22g5sdfg", 4452222,"s22s5s");

    }

}
