package com.wei.btsearch.components;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.wei.btsearch.R;

/**
 * Created by wei on 17-3-31.
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
