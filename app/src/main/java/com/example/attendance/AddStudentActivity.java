package com.example.attendance;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AddStudentActivity extends AppCompatActivity {

    private Spinner classSpinner;
    private EditText studentNameEditText, rollNumberEditText;
    private Button saveStudentButton, deleteStudentButton;
    private ListView studentListView;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        classSpinner = findViewById(R.id.classSpinner);
        studentNameEditText = findViewById(R.id.studentNameEditText);
        rollNumberEditText = findViewById(R.id.rollNumberEditText);
        saveStudentButton = findViewById(R.id.saveStudentButton);
        deleteStudentButton = findViewById(R.id.deleteStudentButton);
        studentListView = findViewById(R.id.studentListView);
        dbHelper = new DatabaseHelper(this);

        loadClasses();
        loadStudents();

        saveStudentButton.setOnClickListener(v -> {
            String studentName = studentNameEditText.getText().toString().trim();
            String rollNumberStr = rollNumberEditText.getText().toString().trim();
            String className = classSpinner.getSelectedItem().toString();

            if (studentName.isEmpty() || rollNumberStr.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                int rollNumber = Integer.parseInt(rollNumberStr);
                long classId = dbHelper.getClassId(className);
                long result = dbHelper.addStudent(studentName, rollNumber, classId);

                if (result != -1) {
                    Toast.makeText(this, "Student added successfully", Toast.LENGTH_SHORT).show();
                    studentNameEditText.setText("");
                    rollNumberEditText.setText("");
                    loadStudents();
                } else {
                    Toast.makeText(this, "Failed to add student", Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteStudentButton.setOnClickListener(v -> {
            String rollNumberStr = rollNumberEditText.getText().toString().trim();
            if (rollNumberStr.isEmpty()) {
                Toast.makeText(this, "Please enter roll number", Toast.LENGTH_SHORT).show();
            } else {
                int rollNumber = Integer.parseInt(rollNumberStr);
                long studentId = dbHelper.getStudentId(rollNumber);
                if (studentId != -1) {
                    boolean deleted = dbHelper.deleteStudent(studentId);
                    if (deleted) {
                        Toast.makeText(this, "Student deleted successfully", Toast.LENGTH_SHORT).show();
                        rollNumberEditText.setText("");
                        loadStudents();
                    } else {
                        Toast.makeText(this, "Failed to delete student", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Student not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadClasses() {
        List<String> classes = dbHelper.getAllClasses();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(adapter);
    }

    private void loadStudents() {
        String className = classSpinner.getSelectedItem().toString();
        long classId = dbHelper.getClassId(className);
        List<String> students = dbHelper.getStudentsInClass(classId);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, students);
        studentListView.setAdapter(adapter);
    }
}