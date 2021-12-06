package com.mazej.todo_list.adapters;

import static com.mazej.todo_list.database.TodoListAPI.retrofit;

import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.mazej.todo_list.ApplicationTodoList;
import com.mazej.todo_list.R;
import com.mazej.todo_list.database.PutTask;
import com.mazej.todo_list.database.TodoListAPI;
import com.mazej.todo_list.objects.Task;
import com.mazej.todo_list.objects.TodoList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodoListAdapter extends ArrayAdapter<TodoList> {

    private final Context mContext;
    private final int mResource;
    private LayoutInflater inflater;

    private String id;
    private String name;

    private TextView tvName;

    public TodoListAdapter(Context context, int resource, ArrayList<TodoList> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        id = getItem(position).getId();
        name = getItem(position).getName();

        inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        tvName = (TextView) convertView.findViewById(R.id.nameTextList);
        tvName.setText(name);

        return convertView;
    }
}
