package com.mazej.todo_list.fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.mazej.todo_list.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.mazej.todo_list.activities.MainActivity.toolbar;

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
