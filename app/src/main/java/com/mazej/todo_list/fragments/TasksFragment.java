package com.mazej.todo_list.fragments;

import static com.mazej.todo_list.activities.MainActivity.app;
import static com.mazej.todo_list.activities.MainActivity.toolbar;
import static com.mazej.todo_list.database.TodoListAPI.retrofit;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mazej.todo_list.R;
import com.mazej.todo_list.adapters.TaskAdapter;
import com.mazej.todo_list.database.PostTask;
import com.mazej.todo_list.database.PutTask;
import com.mazej.todo_list.database.TodoListAPI;
import com.mazej.todo_list.objects.Task;
import com.mazej.todo_list.objects.TodoList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TasksFragment extends Fragment
{

    private TodoListAPI todoListAPI;

    private TodoList todoList;
    private ListView taskList;
    private ArrayList<Task> theList;
    public static TaskAdapter arrayAdapter;

    private FloatingActionButton addTaskButton;

    public TasksFragment(TodoList todoList)
    {
        this.todoList = todoList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_task, container, false);

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(todoList.getName());

        addTaskButton = view.findViewById(R.id.add_task_btn);

        theList = new ArrayList<>();
        taskList = view.findViewById(R.id.taskList);

        // Set custom adapter
        arrayAdapter = new TaskAdapter(getActivity().getBaseContext(), R.layout.adapter_task, theList, todoList.getId());
        taskList.setAdapter(arrayAdapter);

        // Dodamo opravila v seznam
        if (todoList.getTasks() != null)
        {
            for (int i = 0; i < todoList.getTasks().size(); i++)
            {
                theList.add(todoList.getTasks().get(i));
            }
        }
        arrayAdapter.notifyDataSetChanged();

        // Z dolgim pritiskom na opravilo ga lahko izbrišemo
        taskList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, int i, long l)
            {
                final int item = i;

                new AlertDialog.Builder(getActivity())
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Are you sure ?")
                        .setMessage("Do you want to delete this task")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                // Pošljemo zahtevo za izbris
                                todoListAPI = retrofit.create(TodoListAPI.class);
                                Call<Void> call = todoListAPI.deleteTask(app.idAPP, todoList.getId(), theList.get(item).getId());

                                call.enqueue(new Callback<Void>()
                                {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response)
                                    {
                                        if (!response.isSuccessful())
                                        { // Če zahteva ni uspešna
                                            System.out.println("Response: DeleteTask neuspesno!");
                                        }
                                        else
                                        {
                                            System.out.println("Response: DeleteTask uspešno!");
                                            theList.remove(item);
                                            arrayAdapter.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t)
                                    {
                                        System.out.println("No response: DeleteTask neuspešno!");
                                        System.out.println(t);
                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });

        // Prikažemo dialog za dodajanje opravila
        addTaskButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showAddTaskDialog();
            }
        });

        // Ko pritisnemo na item, ga lahko uredimo
        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @SuppressLint("WrongConstant")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                showEditTaskDialog(position);
            }
        });

        return view;
    }

    void showEditTaskDialog(int position)
    {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.edit_task_dialog);

        EditText nameEt = dialog.findViewById(R.id.name_et);
        Button submitButton = dialog.findViewById(R.id.submit_button_edit);
        EditText descriptionEt = dialog.findViewById(R.id.description_et);
        DatePicker datePickerD = dialog.findViewById(R.id.date_picker);

        Task task = theList.get(position);
        nameEt.setText(task.getName());
        descriptionEt.setText(task.getDescription());

        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v)
            {
                String name = nameEt.getText().toString();
                String description = descriptionEt.getText().toString();
                String date = getDate(datePickerD.getYear(), datePickerD.getMonth(), datePickerD.getDayOfMonth());
                // Pošljemo zahtevo za spremembo opravila
                PutTask list = new PutTask(app.idAPP, task.getId(), name, description, date, task.isCompleted());
                todoListAPI = retrofit.create(TodoListAPI.class);
                Call<PutTask> call = todoListAPI.putTask(list, app.idAPP, todoList.getId(), task.getId());

                call.enqueue(new Callback<PutTask>()
                {
                    @Override
                    public void onResponse(Call<PutTask> call, Response<PutTask> response)
                    {
                        if (!response.isSuccessful())
                        { // Če zahteva ni uspešna
                            System.out.println("Response: PutTask neuspesno!");
                            System.out.println(date);
                        }
                        else
                        {
                            System.out.println("Response: PutTask uspešno!");

                            theList.set(position, new Task(task.getId(), name, description, date, false));
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<PutTask> call, Throwable t)
                    {
                        System.out.println("No response: PutTask neuspešno!");
                        System.out.println(t);
                    }
                });
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    void showAddTaskDialog()
    {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_task_dialog);

        final EditText nameEt = dialog.findViewById(R.id.name_et);
        Button submitButton = dialog.findViewById(R.id.submit_button_task);
        EditText descriptionEt = dialog.findViewById(R.id.description_et);
        DatePicker datePickerD = dialog.findViewById(R.id.date_picker);

        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v)
            {
                String name = nameEt.getText().toString();
                String description = descriptionEt.getText().toString();
                String date = getDate(datePickerD.getYear(), datePickerD.getMonth(), datePickerD.getDayOfMonth());
                // Pošljemo zahtevo za dodajanje opravila
                PostTask list = new PostTask(app.idAPP, "", name, description, date, false);
                todoListAPI = retrofit.create(TodoListAPI.class);
                Call<PostTask> call = todoListAPI.postTask(list, app.idAPP, todoList.getId());

                call.enqueue(new Callback<PostTask>()
                {
                    @Override
                    public void onResponse(Call<PostTask> call, Response<PostTask> response)
                    {
                        if (!response.isSuccessful())
                        { // Če zahteva ni uspešna
                            System.out.println("Response: PostTask neuspesno!");
                            System.out.println(date);
                        }
                        else
                        {
                            System.out.println("Response: PostTask uspešno!");
                            theList.add(new Task(response.body().getId(), name, description, date, false));
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<PostTask> call, Throwable t)
                    {
                        System.out.println("No response: PostTask neuspešno!");
                        System.out.println(t);
                    }
                });
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static String getDate(int year, int month, int day)
    {
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
