package com.mazej.todo_list.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mazej.todo_list.R;
import com.mazej.todo_list.database.TodoListAPI;
import com.mazej.todo_list.objects.TodoList;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mazej.todo_list.activities.MainActivity.toolbar;
import static com.mazej.todo_list.database.TodoListAPI.BASE_URL;

public class MainFragment extends Fragment {

    private TodoListAPI todoListAPI;

    private ListView todoLists;
    private ArrayList<TodoList> theList;

    public MainFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Home");

        theList = new ArrayList<>();
        todoLists = view.findViewById(R.id.todoLists);

        // On list item click we change fragment and send object to its constructor
        todoLists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_fragment, new TasksFragment(theList.get(position)), "findThisFragment").addToBackStack(null).commit();
            }
        });

        //Get all lists from current user
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        todoListAPI = retrofit.create(TodoListAPI.class);
        return view;
    }
}
