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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.mazej.sunrise_detection_app.activities.MainActivity.toolbar;

public class SettingsFragment extends Fragment {

    private SharedPreferences sp;
    private ApplicationSunrise app;

    private Switch sw;
    private TextView userTextView;
    private TextView counter;

    public SettingsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        app = (ApplicationSunrise) getActivity().getApplication();

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Settings");

        sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        userTextView = view.findViewById(R.id.usernameTV);
        sw = view.findViewById(R.id.Notifications_switch);

        return view;
    }
}