package com.mazej.sunrise_detection_app.fragments;

import static android.content.Context.SENSOR_SERVICE;
import static com.mazej.sunrise_detection_app.activities.MainActivity.app;
import static com.mazej.sunrise_detection_app.activities.MainActivity.toolbar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
import android.widget.Toast;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mazej.sunrise_detection_app.ApplicationSunrise;
import com.mazej.sunrise_detection_app.R;
import com.mazej.sunrise_detection_app.objects.Experiment;
import com.mazej.sunrise_detection_app.objects.ExperimentList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private FloatingActionButton addExperimentButton;
    private ListView experimentList;
    private ArrayList<String> theList;
    private ArrayAdapter<String> adapter;

    public MainFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Home");

        addExperimentButton = view.findViewById(R.id.add_exp_btn);
        experimentList = view.findViewById(R.id.experiments);

        theList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, theList);
        experimentList.setAdapter(adapter);

        for(int i = 0; i < ApplicationSunrise.experimentList.getList().size(); i++){
            theList.add(ApplicationSunrise.experimentList.getList().get(i).getName());
        }
        adapter.notifyDataSetChanged();

        experimentList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
                                try {
                                    app.deleteFromList(item);
                                    theList.remove(item);
                                    adapter.notifyDataSetChanged();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
                return true;
            }
        });

        addExperimentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddListDialog();
            }
        });
        return view;
    }

    void showAddListDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_experiment_dialog);

        final EditText nameEt = dialog.findViewById(R.id.exp_name);
        final EditText testerEt = dialog.findViewById(R.id.tester);
        final EditText scheduledEt = dialog.findViewById(R.id.sunrise);
        Button submitButton = dialog.findViewById(R.id.submit_button_list);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEt.getText().toString();
                String tester = testerEt.getText().toString();
                String scheduled_sunrise = scheduledEt.getText().toString();

                Experiment exp = new Experiment(name, tester, scheduled_sunrise, "");
                dialog.dismiss();
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment, new ExperimentFragment(exp));
                fragmentTransaction.commit();
            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_style);
    }

}
