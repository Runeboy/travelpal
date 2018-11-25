package com.rune.travelpal.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import com.rune.travelpal.R;

/**
 * Activity for preferences
 */
public class AppPreferenceActivity extends PreferenceActivity {

    // Event handlers


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(
                android.R.id.content,
                new AppPreferenceFragment()
        ).commit();
    }

    public static class AppPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }

    }

}
