package com.example.haddad.managemyrounds;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by haddads on 13/12/2017.
 */

public class Session {
    private static String PREF_NAME = "Memory";
    private static String FBID = "FBID ";
    public static boolean saveSessionId(String FBID , Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, 0).edit();
        editor.putString(FBID , FBID);
        return editor.commit();
    }

    public static String getSessionId(Context context) {
        SharedPreferences savedSession = context.getSharedPreferences(
                PREF_NAME, 0);
        return savedSession.getString(FBID , null);
    }
}