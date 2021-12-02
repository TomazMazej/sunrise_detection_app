package com.mazej.todo_list.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mazej.todo_list.R;
import com.mazej.todo_list.activities.MainActivity;
import com.mazej.todo_list.adapters.TaskAdapter;
import com.mazej.todo_list.database.PostTask;
import com.mazej.todo_list.database.PostTodoList;
import com.mazej.todo_list.database.TodoListAPI;
import com.mazej.todo_list.objects.Task;
import com.mazej.todo_list.objects.TodoList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mazej.todo_list.activities.MainActivity.myMenu;
import static com.mazej.todo_list.activities.MainActivity.toolbar;
import static com.mazej.todo_list.database.TodoListAPI.retrofit;

public class TasksFragment extends Fragment {

    private TodoListAPI todoListAPI;

    private TodoList todoList;
    private ListView taskList;
    private ArrayList<Task> theList;
    public static TaskAdapter arrayAdapter;

    private FloatingActionButton addTaskButton;

    public TasksFragment(TodoList todoList) {
        this.todoList = todoList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_task, container, false);

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(todoList.getName());
        addTaskButton = view.findViewById(R.id.add_task_btn);

        theList = new ArrayList<>();
        taskList = view.findViewById(R.id.taskList);

        // Set custom adapter
        arrayAdapter = new TaskAdapter(getActivity().getBaseContext(), R.layout.adapter_task, theList);
        taskList.setAdapter(arrayAdapter);

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddTaskDialog();
            }
        });

        for(int i = 0; i < todoList.getTasks().size(); i++){
            theList.add(todoList.getTasks().get(i));
        }
        arrayAdapter.notifyDataSetChanged();

        return view;
    }

    void showAddTaskDialog() {
        final Dialog dialog = new Dialog(getActivity());
        //We have added a title in the custom layout. So let's disable the default title.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
        dialog.setCancelable(true);
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.add_task_dialog);

        //Initializing the views of the dialog.
        final EditText nameEt = dialog.findViewById(R.id.name_et);
        Button submitButton = dialog.findViewById(R.id.submit_button);
        EditText descriptionEt = dialog.findViewById(R.id.description_et);
        DatePicker datePickerD = dialog.findViewById(R.id.date_picker);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String name = nameEt.getText().toString();
                String description = descriptionEt.getText().toString();
                String date = getDate(datePickerD.getYear(), datePickerD.getMonth(), datePickerD.getDayOfMonth());

                PostTask list = new PostTask("test", "", name, description, date, false);
                todoListAPI = retrofit.create(TodoListAPI.class);
                Call<PostTask> call = todoListAPI.postTask(list, "test", todoList.getId());

                call.enqueue(new Callback<PostTask>() {
                    @Override
                    public void onResponse(Call<PostTask> call, Response<PostTask> response) {
                        if (!response.isSuccessful()) { // If request is not successful
                            System.out.println("Response: PostTask neuspesno!");
                            System.out.println(date);
                        } else {
                            System.out.println("Response: PostTask uspešno!");
                            theList.add(new Task(response.body().getId(), name, description, date, false));
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<PostTask> call, Throwable t) {
                        System.out.println("No response: PostTask neuspešno!");
                        System.out.println(t);
                    }
                });
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static String getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String nowAsString = df.format(cal.getTime());

        return nowAsString + "+01:00";
    }

}
