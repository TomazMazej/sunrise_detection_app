package com.mazej.todo_list.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mazej.todo_list.R;
import com.mazej.todo_list.database.PutTask;
import com.mazej.todo_list.database.TodoListAPI;
import com.mazej.todo_list.fragments.TasksFragment;
import com.mazej.todo_list.objects.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mazej.todo_list.activities.MainActivity.app;
import static com.mazej.todo_list.database.TodoListAPI.retrofit;
import static com.mazej.todo_list.fragments.TasksFragment.arrayAdapter;

public class TaskAdapter extends ArrayAdapter<Task> {

    private TodoListAPI todoListAPI;

    private Context mContext;
    private int mResource;
    private LayoutInflater inflater;

    private String id;
    private String name;

    private TextView tvName;
    private TextView tvDate;
    public CheckBox simpleCheckBox;

    public ArrayList<Task> tasksList;
    private String todoListId;

    public TaskAdapter(Context context, int resource, ArrayList<Task> objects, String todoListId) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        this.tasksList = objects;
        this.todoListId = todoListId;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Task task = getItem(position);
        id = getItem(position).getId();
        name = getItem(position).getName();

        DateTimeFormatter inputParser = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        LocalDate date = LocalDate.parse(getItem(position).getDueDate(), inputParser);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH);
        String output = date.format(formatter);


        inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        tvName = (TextView) convertView.findViewById(R.id.nameText);
        tvDate = (TextView) convertView.findViewById(R.id.dateText);
        simpleCheckBox = (CheckBox) convertView.findViewById(R.id.simpleCheckBox);

        tvName.setText(name);
        tvDate.setText(output);
        if(task.isCompleted()){
            simpleCheckBox.setChecked(true);
            tvName.setPaintFlags(tvName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        // On checkbox change
        simpleCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                String name = task.getName();
                String description = task.getDescription();
                String date = task.getDueDate();
                // Pošljemo zahtevo za spremembo opravila
                PutTask list = new PutTask(app.idAPP, task.getId(), name, description, date, !task.isCompleted());
                todoListAPI = retrofit.create(TodoListAPI.class);
                Call<PutTask> call = todoListAPI.putTask(list, app.idAPP, todoListId, task.getId());

                call.enqueue(new Callback<PutTask>() {
                    @Override
                    public void onResponse(Call<PutTask> call, Response<PutTask> response) {
                        if (!response.isSuccessful()) { // Če zahteva ni uspešna
                            System.out.println("Response: CheckboxPut neuspesno!");
                            System.out.println(date);
                        } else {
                            System.out.println("Response: CheckboxPut uspešno!");
                        }
                    }
                    @Override
                    public void onFailure(Call<PutTask> call, Throwable t) {
                        System.out.println("No response: CheckboxPut neuspešno!");
                        System.out.println(t);
                    }
                });
            }
        });

        return convertView;
    }
}
