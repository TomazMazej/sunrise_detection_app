package com.mazej.sunrise_detection_app;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mazej.sunrise_detection_app.objects.Experiment;
import com.mazej.sunrise_detection_app.objects.ExperimentList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class ApplicationSunrise extends Application {
    public static final String TAG = ApplicationSunrise.class.getSimpleName();
    public static final String APP_ID = "APP_ID_KEY";
    private static final String MY_FILE_NAME = "data.json";

    public static ExperimentList experimentList;

    private SharedPreferences sp;
    public static String idAPP;

    private Gson gson;
    private File file;

    public void onCreate() {
        super.onCreate();
        initData();
    }

    // Zgeneriramo edinstven UUID ključ, ki služi kot access_token
    public void setAppId() {
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (sp.contains(APP_ID)) { // Ce ze obstaja ga preberemo
            idAPP = sp.getString(APP_ID, "DEFAULT VALUE ERR");
        }
        else { // Zgeneriramo ga prvic
            idAPP = UUID.randomUUID().toString().replace("-", "");
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(APP_ID, idAPP);
            editor.apply();
        }
    }

    public void initData() {
        setAppId();
        experimentList = new ExperimentList();
        readFromFile();
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    public void deleteFromList(int i) throws IOException {
        experimentList.getList().remove(i);
        saveData();
    }

    public void saveData() throws IOException {
        try {
            FileUtils.writeStringToFile(getFile(), getGson().toJson(experimentList));
        } catch (IOException e) {
            Log.d(TAG, "Can't save " + file.getPath());
        }
    }

    private boolean readFromFile() {
        if (!getFile().exists())  return false;
        try {
            experimentList = getGson().fromJson(FileUtils.readFileToString(getFile()), ExperimentList.class);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private File getFile() {
        if (file == null) {
            File filesDir = getFilesDir();
            file = new File(filesDir, MY_FILE_NAME);
        }
        Log.i(TAG, file.getPath());
        return file;
    }

    private Gson getGson() {
        if (gson == null)
            gson = new GsonBuilder().setPrettyPrinting().create();
        return gson;
    }

}
