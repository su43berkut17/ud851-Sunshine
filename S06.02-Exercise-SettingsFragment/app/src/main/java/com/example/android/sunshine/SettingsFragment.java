package com.example.android.sunshine;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.support.v7.preference.PreferenceScreen;

import java.util.List;

/**
 * Created by su43b on 2018-02-06.
 */
// COMPLETED(4) Create SettingsFragment and extend PreferenceFragmentCompat
// Do steps 5 - 11 within SettingsFragment
// COMPLETED(10) Implement OnSharedPreferenceChangeListener from SettingsFragment
public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{
    // COMPLETED (5) Override onCreatePreferences and add the preference xml file using addPreferencesFromResource
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings);

        // Do step 9 within onCreatePreference
        // COMPLETED (9) Set the preference summary on each preference that isn't a CheckBoxPreference
        SharedPreferences sharedPreferences=getPreferenceScreen().getSharedPreferences();
        PreferenceScreen prefScree=getPreferenceScreen();
        int numPref=prefScree.getPreferenceCount();

        for (int i=0;i<numPref;i++){
            Preference pref=prefScree.getPreference(i);

            if (!(pref instanceof CheckBoxPreference)){
                String value=sharedPreferences.getString(pref.getKey(),"");
                setPreferenceSummary(pref,value);
            }
        }
    }

    // COMPLETED(8) Create a method called setPreferenceSummary that accepts a Preference and an Object and sets the summary of the preference
    public void setPreferenceSummary(Preference preference,Object object){
        String value=object.toString();
        String key=preference.getKey();

        if (preference instanceof EditTextPreference){
            preference.setSummary(value);
        }else if(preference instanceof ListPreference){
            ListPreference prefL=(ListPreference) preference;
            int count = prefL.findIndexOfValue(value);
            if (count>=0){
                preference.setSummary(prefL.getEntries()[count]);
            }
        }
    }


    // COMPLETED (12) Register SettingsFragment (this) as a SharedPreferenceChangedListener in onStart
    @Override
    public void onStart() {
        super.onStart();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    // COMPLETED (13) Unregister SettingsFragment (this) as a SharedPreferenceChangedListener in onStop
    @Override
    public void onStop() {
        super.onStop();

        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    // COMPLETED (11) Override onSharedPreferenceChanged to update non CheckBoxPreferences when they are changed
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref=findPreference(key);

        if (null!= pref) {
            if (!(pref instanceof CheckBoxPreference)) {
                setPreferenceSummary(pref,sharedPreferences.getString(key,""));
            }
        }
    }
}
