package com.mazej.todo_list;

import static com.mazej.todo_list.database.TodoListAPI.retrofit;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mazej.todo_list.database.PostTodoList;
import com.mazej.todo_list.database.TodoListAPI;
import com.mazej.todo_list.objects.Task;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicationTodoList extends Application {
    public static final String APP_ID = "APP_ID_KEY";

    private TodoListAPI todoListAPI;
    private SharedPreferences sp;
    public static String idAPP;

    public ArrayList<Task> theList;
    public static int listCounter = 0;

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
            generateDefaultLists();
        }
    }

    public void initData() {
        setAppId();
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        theList = new ArrayList<>();
    }

    public void generateDefaultLists() {
        String[] defaults = {"Home", "Groceries", "My Day"};
        for(int i = 0; i < defaults.length; i++){
            // Pošljemo nov seznam opravil
            PostTodoList list = new PostTodoList(idAPP, "", defaults[i], null);
            todoListAPI = retrofit.create(TodoListAPI.class);
            Call<PostTodoList> call = todoListAPI.postTodoList(list, idAPP);

            call.enqueue(new Callback<PostTodoList>() {
                @Override
                public void onResponse(Call<PostTodoList> call, Response<PostTodoList> response) {
                    if (!response.isSuccessful()) { // Če zahteva ni uspešna
                        System.out.println("Response: PostTodoList neuspesno!");
                    } else {
                        System.out.println("Response: PostTodoList uspešno!");
                    }
                }
                @Override
                public void onFailure(Call<PostTodoList> call, Throwable t) {
                    System.out.println("No response: PostTodoList neuspešno!");
                    System.out.println(t);
                }
            });
        }
    }
}
