package com.mazej.todo_list.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.mazej.todo_list.R;
import com.mazej.todo_list.activities.ApplicationTodoList;
import com.mazej.todo_list.database.GetTask;
import com.mazej.todo_list.database.GetTodoList;
import com.mazej.todo_list.database.TodoListAPI;
import com.mazej.todo_list.objects.Task;
import com.mazej.todo_list.objects.TodoList;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mazej.todo_list.activities.MainActivity.toolbar;
import static com.mazej.todo_list.database.TodoListAPI.BASE_URL;

public class MainFragment extends Fragment {

    private TodoListAPI todoListAPI;

    private ListView todoLists;
    private ArrayList<TodoList> theList;
    private ArrayList<String> names;

    public MainFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("TODO Lists");

        names = new ArrayList<>();
        theList = new ArrayList<>();
        todoLists = view.findViewById(R.id.todoLists);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, names);
        todoLists.setAdapter(arrayAdapter);

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
        Call<List<GetTodoList>> call = todoListAPI.getTodoLists(ApplicationTodoList.idAPP);

        call.enqueue(new Callback<List<GetTodoList>>() {
            @Override
            public void onResponse(Call<List<GetTodoList>> call, Response<List<GetTodoList>> response) {
                if (!response.isSuccessful()) { // If request is not successful
                    System.out.println("Response: GetTodoList neuspesno!");
                } else {
                    System.out.println("Response: GetTodoList uspešno!");
                    for (int i = 0; i < response.body().size(); i++) { // Add plants to list
                        TodoList tdl = new TodoList("" + response.body().get(i).getId(), response.body().get(i).getName(), response.body().get(i).getTasks());
                        theList.add(tdl);
                        names.add(tdl.getName());
                    }
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<GetTodoList>> call, Throwable t) {
                System.out.println("No response: GetTodoList neuspešno!");
                System.out.println(t);
            }
        });
        return view;
    }
}
