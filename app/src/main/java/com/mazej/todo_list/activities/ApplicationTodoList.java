package com.mazej.todo_list.activities;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.UUID;

public class ApplicationTodoList extends Application {
    public static final String TAG = ApplicationTodoList.class.getSimpleName();
    public static final String APP_ID = "APP_ID_KEY";

    private SharedPreferences sp;
    public static String idAPP;

    public void onCreate() {
        super.onCreate();
        initData();
    }

    //UUID porabi manj sppomina, je unique
    public void setAppId() {
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (sp.contains(APP_ID)) //ce ze obstaja ga preberemo
            idAPP = sp.getString(APP_ID,"DEFAULT VALUE ERR");
        else { //zgeneriramo ga prvic
            idAPP = UUID.randomUUID().toString().replace("-", "");
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(APP_ID, idAPP);
            editor.apply();
        }
        Log.d(TAG,"appID:" + idAPP);
    }

    public void initData() {
        setAppId();
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }
}
