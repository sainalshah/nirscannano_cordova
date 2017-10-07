//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.kstechnologies.nirscannanolibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SettingsManager {
    public static final boolean CELSIUS = false;
    public static final boolean FAHRENHEIT = true;
    public static final boolean WAVELENGTH = true;
    public static final boolean WAVENUMBER = false;

    public SettingsManager() {
    }

    public static void storeStringPref(Context context, String name, String value) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(name, value);
        editor.apply();
    }

    public static void storeBooleanPref(Context context, String name, boolean value) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(name, value);
        editor.apply();
    }

    public static String getStringPref(Context context, String key, String _default) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, _default);
    }

    public static boolean getBooleanPref(Context context, String key, boolean _default) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, _default);
    }

    public static class SharedPreferencesKeys {
        public static final String tempUnits = "tempUnits";
        public static final String spatialFreq = "spatialFreq";
        public static final String prefix = "prefix";
        public static final String saveSD = "saveSD";
        public static final String saveOS = "saveOS";
        public static final String continuousScan = "continuousScan";
        public static final String scanConfiguration = "scanConfiguration";
        public static final String preferredDevice = "preferredDevice";

        public SharedPreferencesKeys() {
        }
    }
}
