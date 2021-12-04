package com.mazej.todo_list.fragments;

import static com.mazej.todo_list.activities.MainActivity.app;
import static com.mazej.todo_list.activities.MainActivity.toolbar;
import static com.mazej.todo_list.database.TodoListAPI.retrofit;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mazej.todo_list.R;
import com.mazej.todo_list.database.GetTodoList;
import com.mazej.todo_list.database.PostTodoList;
import com.mazej.todo_list.database.TodoListAPI;
import com.mazej.todo_list.objects.TodoList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment {

    private TodoListAPI todoListAPI;

    private ListView todoLists;
    private ArrayList<TodoList> theList;
    private ArrayList<String> names;
    private ArrayAdapter<String> arrayAdapter;

    private TextView dateTimeDisplay;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;

    private FloatingActionButton addListButton;

    public MainFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("TODO Lists");

        //datum
        dateTimeDisplay = view.findViewById(R.id.date_text);
        calendar = Calendar.getInstance();

        dateFormat = new SimpleDateFormat("EEEE, d MMMM");
        date = dateFormat.format(calendar.getTime());
        dateTimeDisplay.setText(date);

        names = new ArrayList<>();
        theList = new ArrayList<>();
        todoLists = view.findViewById(R.id.todoLists);
        addListButton = view.findViewById(R.id.add_list_btn);

        arrayAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, names);
        todoLists.setAdapter(arrayAdapter);

        // Gumb za dodajanje novega seznama opravil
        addListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddListDialog();
            }
        });

        // Ko pritisnemo na item, nas preusmeri na opravila v seznamu
        todoLists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_fragment, new TasksFragment(theList.get(position)), "findThisFragment").addToBackStack(null).commit();
            }
        });

        // Z dolgim pritiskom na opravilo ga lahko izbrišemo
        todoLists.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, int i, long l) {
                final int item = i;

                new AlertDialog.Builder(getActivity())
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Are you sure ?")
                        .setMessage("Do you want to delete this list")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Pošljemo zahtevo za izbris
                                todoListAPI = retrofit.create(TodoListAPI.class);
                                //Call<Void> call = todoListAPI.deleteTodoList("test", theList.get(item).getId());
                                Call<Void> call = todoListAPI.deleteTodoList(app.idAPP, theList.get(item).getId());

                                call.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if (!response.isSuccessful()) { // Če zahteva ni uspešna
                                            System.out.println("Response: DeleteList neuspesno!");
                                        } else {
                                            System.out.println("Response: DeleteList uspešno!");
                                            theList.remove(item);
                                            names.remove(item);
                                            arrayAdapter.notifyDataSetChanged();
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        System.out.println("No response: DeleteList neuspešno!");
                                        System.out.println(t);
                                    }
                                });
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
                return true;
            }
        });

        // Dobimo vse sezname opravil
        todoListAPI = retrofit.create(TodoListAPI.class);
        //Call<List<GetTodoList>> call = todoListAPI.getTodoLists("test");
        System.out.println("ID APLIKACIJE: " + app.idAPP);
        Call<List<GetTodoList>> call = todoListAPI.getTodoLists(app.idAPP);

        call.enqueue(new Callback<List<GetTodoList>>() {
            @Override
            public void onResponse(Call<List<GetTodoList>> call, Response<List<GetTodoList>> response) {
                if (!response.isSuccessful()) { // Če zahteva ni uspešna
                    System.out.println("Response: GetTodoList neuspesno!");
                } else {
                    System.out.println("Response: GetTodoList uspešno!");
                    for (int i = 0; i < response.body().size(); i++) { // Dodamo sezname opravil v naš seznam
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

    void showAddListDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_list_dialog);

        final EditText nameEt = dialog.findViewById(R.id.name_et);
        Button submitButton = dialog.findViewById(R.id.submit_button_list);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEt.getText().toString();
                // Pošljemo nov seznam opravil
                PostTodoList list = new PostTodoList(app.idAPP, "", name, null);
                todoListAPI = retrofit.create(TodoListAPI.class);
                Call<PostTodoList> call = todoListAPI.postTodoList(list, app.idAPP);

                call.enqueue(new Callback<PostTodoList>() {
                    @Override
                    public void onResponse(Call<PostTodoList> call, Response<PostTodoList> response) {
                        if (!response.isSuccessful()) { // Če zahteva ni uspešna
                            System.out.println("Response: PostTodoList neuspesno!");
                        } else {
                            System.out.println("Response: PostTodoList uspešno!");
                            theList.add(new TodoList(response.body().getId(), name, null));
                            names.add(name);
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onFailure(Call<PostTodoList> call, Throwable t) {
                        System.out.println("No response: PostTodoList neuspešno!");
                        System.out.println(t);
                    }
                });
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
