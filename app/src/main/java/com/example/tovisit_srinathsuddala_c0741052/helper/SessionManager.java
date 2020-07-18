package com.example.tovisit_srinathsuddala_c0741052.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "AgeFriendly";

    private static final String KEY_SELECTED_LAT = "latitude";

    private static final String KEY_SELECTED_LON = "longitude";

    private static final String KEY_SELECTED_RADIUS = "radius";


    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setRadius(int radius) {
        editor.putInt(KEY_SELECTED_RADIUS, radius);
        // commit changes
        editor.commit();

    }

    public void setLatLngValues(String lati, String longi) {
        editor.putString(KEY_SELECTED_LAT, lati);
        editor.putString(KEY_SELECTED_LON, longi);
        // commit changes
        editor.commit();
    }

    public String getLatitiude() {
        return pref.getString(KEY_SELECTED_LAT, null);
    }

    public String getLongitude() {
        return pref.getString(KEY_SELECTED_LON, null);
    }
    public int getRadius() {
        return pref.getInt(KEY_SELECTED_RADIUS, 0);
    }
}