package com.mazej.sunrise_detection_app.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mazej.sunrise_detection_app.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.mazej.sunrise_detection_app.activities.MainActivity.toolbar;

public class PrivacyFragment extends Fragment {

    public PrivacyFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_privacy, container, false);

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Privacy Policy");

        return view;
    }
}
