package com.example.attendance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button addClassButton, addStudentButton, markAttendanceButton, viewAttendanceButton,viewAllAttendanceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize buttons
        addClassButton = findViewById(R.id.addClassButton);
        addStudentButton = findViewById(R.id.addStudentButton);
        markAttendanceButton = findViewById(R.id.markAttendanceButton);
        viewAttendanceButton = findViewById(R.id.viewAttendanceButton);
        viewAllAttendanceButton = findViewById(R.id.viewAllAttendanceButton);
        // Set click listeners
        addClassButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddClassActivity.class)));
        addStudentButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddStudentActivity.class)));
        markAttendanceButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MarkAttendanceActivity.class)));
        viewAttendanceButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ViewAttendanceActivity.class)));
        viewAllAttendanceButton.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AllAttendanceActivity.class)));
    }
}
