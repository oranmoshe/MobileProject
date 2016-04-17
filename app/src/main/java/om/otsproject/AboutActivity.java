package om.otsproject;

import android.os.Bundle;


public class AboutActivity extends BaseClass {

    Controller controller = Controller.getInstance(getBaseContext());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

}
