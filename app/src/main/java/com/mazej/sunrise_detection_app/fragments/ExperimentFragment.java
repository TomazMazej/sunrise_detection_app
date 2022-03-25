package com.mazej.sunrise_detection_app.fragments;

import static android.content.Context.SENSOR_SERVICE;
import static com.mazej.sunrise_detection_app.activities.MainActivity.app;
import static com.mazej.sunrise_detection_app.activities.MainActivity.toolbar;

import android.app.Dialog;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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

public class ExperimentFragment extends Fragment {
    public static final int THRESHOLD = 10000;

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightEventListener;
    private View root;
    private float maxValue;

    private Experiment exp;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private boolean x;

    public ExperimentFragment(Experiment exp) {
        this.exp = exp;
        x = true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_experiment, container, false);

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Experiment");

        root = view.findViewById(R.id.root);
        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (lightSensor == null) {
            Toast.makeText(getActivity(), "The device has no light sensor !", Toast.LENGTH_SHORT).show();
        }

        // max value for light sensor
        maxValue = lightSensor.getMaximumRange();

        lightEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float value = sensorEvent.values[0];
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Luminosity : " + value + " lx");

                // between 0 and 255
                int newValue = (int) (255f * value / maxValue);
                root.setBackgroundColor(Color.rgb(newValue, newValue, newValue));

                if(value >= THRESHOLD && x){
                    x = false;
                    exp.setDetected_sunrise(new Time().toString());
                    try {
                        ApplicationSunrise.experimentList.add(exp);
                        app.saveData();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_fragment, new MainFragment());
                    fragmentTransaction.commit();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(lightEventListener, lightSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(lightEventListener);
    }

}
