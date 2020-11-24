package com.katyshevtseva.kikiorgmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.kikiorgmobile.R;

public class TaskCreationActivity extends AppCompatActivity {
    private static final String[] regularity_types = {"regular", "irregular"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creation);
    }
}
