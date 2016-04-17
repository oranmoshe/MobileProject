package om.otsproject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by oranmoshe on 4/4/16.
 */
public class PreferencesHelper extends Activity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public PreferencesHelper(Context context) {
        this.sharedPreferences = getPreferences(MODE_PRIVATE);
        this.editor = sharedPreferences.edit(); }

    public String GetPreferences(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void SavePreferences(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }
}