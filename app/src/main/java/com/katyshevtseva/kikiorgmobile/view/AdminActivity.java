package com.katyshevtseva.kikiorgmobile.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kikiorgmobile.R;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.new_task_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewTaskActivity();
            }
        });
    }

    private void openNewTaskActivity() {
        Intent intent = new Intent(this, TaskCreationActivity.class);
        startActivity(intent);
    }
}
