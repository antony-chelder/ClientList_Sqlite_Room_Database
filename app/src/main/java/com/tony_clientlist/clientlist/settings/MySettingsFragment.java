package com.tony_clientlist.clientlist.settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.tony_clientlist.clientlist.R;

public class MySettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.main_settings,rootKey);
    }
}
