package com.iamyeong.aboutyou;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private static final String PREFERENCES_NAME = "com.iamyeong.aboutyou.sharedpreferences";
    //0 : en, 1 : ko, ...
    private static final int PREFERENCE_LANGUAGE = 1;

    //false : first, true : not
    private static final boolean PREFERENCE_FIRST_LOGIN = false;

    //true : light mode, false : night mode
    private static final boolean PREFERENCE_DESIGN_MODE = true;

    private static final int PREFERENCE_MEMO_FILTER = 0;

    private static SharedPreferences getSharedPreference(Context context) {

        return context.getSharedPreferences(PREFERENCES_NAME, context.MODE_PRIVATE);
    }

    public static String getPreviousEmail(Context context, String key) {
        SharedPreferences preferences = getSharedPreference(context);
        return preferences.getString(key, "PRE_EMAIL");
    }

    public static boolean setPreviousEmail(Context context, String key, String value) {
        SharedPreferences preferences = getSharedPreference(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static int getPreferenceLanguage(Context context, String key) {

        SharedPreferences preferences = getSharedPreference(context);
        return preferences.getInt(key, 0);
    }

    public static boolean setPreferenceLanguage(Context context, String key, int value) {

        SharedPreferences preferences = getSharedPreference(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        return editor.commit();

    }



}
