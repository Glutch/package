package com.apackage.app.apackage.settings;

import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.apackage.app.apackage.R;

/**
 * Activity that displays the settings fragment.
 */
public class SettingsActivity extends AppCompatActivity {

    public static final String KEY_PHONENUMBER = "number_key";
    public static final String KEY_GENERATEORDERS = "order_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .add(R.id.frameLayout, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
