package com.mazej.todo_list.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mazej.todo_list.R;
import com.mazej.todo_list.activities.MainActivity;
import com.mazej.todo_list.adapters.TaskAdapter;
import com.mazej.todo_list.objects.Task;
import com.mazej.todo_list.objects.TodoList;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.mazej.todo_list.activities.MainActivity.toolbar;

public class TasksFragment extends Fragment {

    private TodoList todoList;
    private ListView taskList;
    private ArrayList<Task> theList;
    public static TaskAdapter arrayAdapter;

    public TasksFragment(TodoList todoList) {
        this.todoList = todoList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_task, container, false);

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(todoList.getName());
        MainActivity.hideButtons();

        theList = new ArrayList<>();
        taskList = view.findViewById(R.id.taskList);

        // Set custom adapter
        arrayAdapter = new TaskAdapter(getActivity().getBaseContext(), R.layout.adapter_task, theList);
        taskList.setAdapter(arrayAdapter);

        // Get all tasks from current list

        return view;
    }

}
