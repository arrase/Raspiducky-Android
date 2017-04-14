package io.github.arrase.raspiducky.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import io.github.arrase.raspiducky.R;


public class RaspiduckySettings extends PreferenceFragment {

    public RaspiduckySettings() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.raspiducky_settings);
    }
}
