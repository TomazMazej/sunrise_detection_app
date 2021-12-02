package com.mazej.todo_list.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mazej.todo_list.R;
import com.mazej.todo_list.objects.Task;

import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;

public class TaskAdapter extends ArrayAdapter<Task> {

    private Context mContext;
    private int mResource;
    private LayoutInflater inflater;

    private String id;
    private String name;
    private String date;

    private TextView tvName;
    private TextView tvDate;
    public CheckBox simpleCheckBox;

    public ArrayList<Task> tasksList;

    public TaskAdapter(Context context, int resource, ArrayList<Task> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        this.tasksList = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        id = getItem(position).getId();
        name = getItem(position).getName();
        date = getItem(position).getDueDate();

        inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        tvName = (TextView) convertView.findViewById(R.id.nameText);
        tvDate = (TextView) convertView.findViewById(R.id.dateText);
        simpleCheckBox = (CheckBox) convertView.findViewById(R.id.simpleCheckBox);

        tvName.setText(name);
        tvDate.setText(date.toString());

        return convertView;
    }
}
