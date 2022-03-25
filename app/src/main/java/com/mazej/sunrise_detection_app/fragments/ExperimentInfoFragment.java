package com.mazej.sunrise_detection_app.fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.mazej.sunrise_detection_app.ApplicationSunrise;
import com.mazej.sunrise_detection_app.R;
import com.mazej.sunrise_detection_app.objects.Experiment;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.mazej.sunrise_detection_app.activities.MainActivity.toolbar;

public class ExperimentInfoFragment extends Fragment {

    private Experiment exp;
    private TextView tester;
    private TextView scheduled_sunrise;
    private TextView detected_sunrise;

    public ExperimentInfoFragment(Experiment exp) {
        this.exp = exp;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_experiment_info, container, false);

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(exp.getName());

        tester = view.findViewById(R.id.testerTV);
        scheduled_sunrise = view.findViewById(R.id.scheduledTV);
        detected_sunrise = view.findViewById(R.id.detectedTV);

        // Nastavimo podatke
        tester.setText("Tester: " + exp.getTester());
        scheduled_sunrise.setText("Scheduled sunrise: " + exp.getScheduled_sunrise());
        detected_sunrise.setText("Detected sunrise: " + exp.getDetected_sunrise());

        return view;
    }
}