package com.zekitez.brixsgcalculator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
//import android.util.Log;

import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class PrefsFragment extends PreferenceFragmentCompat {

    private final String TAG = "PrefsFragment";
    private ListPreference listConvMethods, listSolvMethods;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

        EditTextPreference editTextPreference = getPreferenceManager().findPreference(getString(R.string.key_sw_sugar));
        assert editTextPreference != null;
        editTextPreference.setOnBindEditTextListener(editText -> editText.setInputType(InputType.TYPE_CLASS_NUMBER));

        listConvMethods = getPreferenceManager().findPreference(getString(R.string.key_convMethod));
        assert listConvMethods != null;
        listConvMethods.setTitle(listConvMethods.getTitle() + "\n" + getConvMethod(""));

        listConvMethods.setOnPreferenceChangeListener((preference, newVal) -> {
            listConvMethods.setTitle(getResources().getString(R.string.txt_convMethodBrixSG) + "\n" + getConvMethod(newVal.toString()));
            return true;
        });

        listSolvMethods = getPreferenceManager().findPreference(getString(R.string.key_solvMethod));
        assert listSolvMethods != null;
        listSolvMethods.setTitle(listSolvMethods.getTitle() + "\n" + getSolvMethod(""));

        listSolvMethods.setOnPreferenceChangeListener((preference, newVal) -> {
            listSolvMethods.setTitle(getResources().getString(R.string.txt_solvMethodSugar) + "\n" + getSolvMethod(newVal.toString()));
            return true;
        });


    }

    private String getConvMethod(String newVal) {
        String convMethod = newVal;
        if (newVal.length() < 2) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
            convMethod = prefs.getString(getString(R.string.key_convMethod), getString(R.string.pref_conv_eu_value));
        }
//        Log.d(TAG, "getConvMethod [" + newVal + "] " + convMethod);

        if (convMethod.equals(getResources().getString(R.string.pref_conv_le_value))) {
            convMethod = getResources().getString(R.string.pref_conv_label_le);

        } else if (convMethod.equals(getResources().getString(R.string.pref_conv_wh_value))) {
            convMethod = getResources().getString(R.string.pref_conv_label_wh);

        } else if (convMethod.equals(getResources().getString(R.string.pref_conv_bf_value))) {
            convMethod = getResources().getString(R.string.pref_conv_label_bf);

        } else if (convMethod.equals(getResources().getString(R.string.pref_conv_eu_value))){
            convMethod = getResources().getString(R.string.pref_conv_label_eu);

        } else {
            convMethod = getResources().getString(R.string.pref_conv_label_av);

        }
        return convMethod;
    }

    private String getSolvMethod(String newVal) {
        String solvMethod = newVal;
        if (newVal.length() < 2) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
            solvMethod = prefs.getString(getString(R.string.key_solvMethod), getString(R.string.pref_solv_121_value));
        }
//        Log.d(TAG, "getSolvMethod [" + newVal + "] " + solvMethod);

        if (solvMethod.equals(getResources().getString(R.string.pref_solv_121_value))) {
            solvMethod = getResources().getString(R.string.pref_solv_label_121);

        } else if (solvMethod.equals(getResources().getString(R.string.pref_solv_none_value))) {
            solvMethod = getResources().getString(R.string.pref_solv_label_none);

        } else {
            solvMethod = getResources().getString(R.string.pref_solv_label_none);
        }
        return solvMethod;
    }

}

