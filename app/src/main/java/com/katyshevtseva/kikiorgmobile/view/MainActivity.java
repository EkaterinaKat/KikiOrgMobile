package com.katyshevtseva.kikiorgmobile.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kikiorgmobile.R;

public class MainActivity extends AppCompatActivity {
    private LinearLayout taskPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskPane = findViewById(R.id.task_pane);
        findViewById(R.id.admin_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAdminActivity();
            }
        });
    }

    @Override
    protected void onResume() {
        updateTaskPane();
        super.onResume();
    }

    private void openAdminActivity() {
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
    }

    private void updateTaskPane() {

    }
}
