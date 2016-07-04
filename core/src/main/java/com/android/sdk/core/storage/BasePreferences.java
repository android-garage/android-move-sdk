package com.android.sdk.core.storage;

import android.content.Context;
import android.content.SharedPreferences;

import static com.android.sdk.core.utils.DesignByContract.*;


/**
 * Created by deepu on 3/13/16.
 */
public abstract class BasePreferences {

    private final SharedPreferences preferences;

    protected BasePreferences(String preferenceName, Context context) {
        preferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
    }

    protected void setString(String key, String value) {
        requireNonEmptyString(key);
        requireAny(value);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    protected String getString(String key) {
        requireNonEmptyString(key);
        return preferences.getString(key, null);
    }


    protected void setBoolean(String key, boolean value) {
        requireNonEmptyString(key);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    protected boolean getBoolean(String key) {
        requireNonEmptyString(key);
        return preferences.getBoolean(key, false);
    }

    protected void setInt(String key, int value) {
        requireNonEmptyString(key);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    protected int getInt(String key) {
        requireNonEmptyString(key);
        return preferences.getInt(key, 0);
    }


}
