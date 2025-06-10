package com.example.attendance;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AddClassActivity extends AppCompatActivity {

    private EditText classNameEditText;
    private Button saveClassButton, deleteClassButton;
    private ListView classListView;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        classNameEditText = findViewById(R.id.classNameEditText);
        saveClassButton = findViewById(R.id.saveClassButton);
        deleteClassButton = findViewById(R.id.deleteClassButton);
        classListView = findViewById(R.id.classListView);
        dbHelper = new DatabaseHelper(this);

        loadClasses();

        saveClassButton.setOnClickListener(v -> {
            String className = classNameEditText.getText().toString().trim();
            if (className.isEmpty()) {
                Toast.makeText(this, "Please enter a class name", Toast.LENGTH_SHORT).show();
            } else {
                long result = dbHelper.addClass(className);
                if (result != -1) {
                    Toast.makeText(this, "Class added successfully", Toast.LENGTH_SHORT).show();
                    classNameEditText.setText("");
                    loadClasses();
                } else {
                    Toast.makeText(this, "Failed to add class", Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteClassButton.setOnClickListener(v -> {
            String className = classNameEditText.getText().toString().trim();
            if (className.isEmpty()) {
                Toast.makeText(this, "Please enter a class name", Toast.LENGTH_SHORT).show();
            } else {
                long classId = dbHelper.getClassId(className);
                if (classId != -1) {
                    boolean deleted = dbHelper.deleteClass(classId);
                    if (deleted) {
                        Toast.makeText(this, "Class deleted successfully", Toast.LENGTH_SHORT).show();
                        classNameEditText.setText("");
                        loadClasses();
                    } else {
                        Toast.makeText(this, "Failed to delete class", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Class not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadClasses() {
        List<String> classes = dbHelper.getAllClasses();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, classes);
        classListView.setAdapter(adapter);
    }
}