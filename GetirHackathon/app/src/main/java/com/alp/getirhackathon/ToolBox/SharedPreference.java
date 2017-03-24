package com.alp.getirhackathon.ToolBox;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by AlparslanSelçuk on 24.03.2017.
 */

public class SharedPreference {
    public static final String PREFS_NAME = "UserPrefsFile";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String USERID = "userId";


    public static SharedPreference sharedPreference;
    public static int pageDetector;

    private SharedPreferences mUserPreferences;
    private SharedPreferences.Editor editor;

    public SharedPreference(Context mContext) {
        super();
        mUserPreferences = mContext.getSharedPreferences(PREFS_NAME, 0);
        editor = mUserPreferences.edit();
        pageDetector = 1;
    }

    public static SharedPreference getInstance(Context mContext) {
        if (sharedPreference == null) {
            sharedPreference = new SharedPreference(mContext);
        }
        return sharedPreference;
    }

    public void setStringValue(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getStringValue(String key) {
        return mUserPreferences.getString(key, null);
    }

    public void setBooleanValue(String key, boolean statu) {
        editor.putBoolean(key, statu);
        editor.commit();
    }

    public Boolean getBooleanValue(String key) {
        return mUserPreferences.getBoolean(key, false);
    }

    public void setIntValue(String key, int num) {
        editor.putInt(key, num);
        editor.commit();
    }

    public Integer getIntValue(String key) {
        return mUserPreferences.getInt(key, 0);
    }

    public void setPopularIntValue(String key, int num) {
        editor.putInt(key, num);
        editor.commit();
    }

    public Integer getPopularIntValue(String key) {
        return mUserPreferences.getInt(key, 1);
    }
}
