package com.example.attendance;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MarkAttendanceActivity extends AppCompatActivity {

    private Spinner classSpinner;
    private ListView studentListView;
    private Button markAttendanceButton, selectDateButton;
    private TextView selectedDateText;
    private DatabaseHelper dbHelper;
    private ArrayAdapter<String> studentAdapter;
    private List<String> students;
    private String selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance);

        classSpinner = findViewById(R.id.classSpinner);
        studentListView = findViewById(R.id.studentListView);
        markAttendanceButton = findViewById(R.id.markAttendanceButton);
        selectDateButton = findViewById(R.id.selectDateButton);
        selectedDateText = findViewById(R.id.selectedDateText);

        dbHelper = new DatabaseHelper(this);
        loadClassSpinner();

        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String className = parent.getItemAtPosition(position).toString();
                loadStudents(className);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        selectDateButton.setOnClickListener(v -> showDatePickerDialog());
        markAttendanceButton.setOnClickListener(v -> markAttendance());
    }

    private void loadClassSpinner() {
        List<String> classes = dbHelper.getAllClasses();
        if (classes.isEmpty()) {
            Toast.makeText(this, "No classes found!", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classes);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(classAdapter);
    }

    private void loadStudents(String className) {
        long classId = dbHelper.getClassId(className);
        students = dbHelper.getStudentsInClass(classId);

        if (students == null || students.isEmpty()) {
            students = new ArrayList<>();
            Toast.makeText(this, "No students found in this class!", Toast.LENGTH_SHORT).show();
        }

        studentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, students);
        studentListView.setAdapter(studentAdapter);
        studentListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, (view, year1, month1, dayOfMonth) -> {
            selectedDate = year1 + "-" + (month1 + 1) + "-" + dayOfMonth;
            selectedDateText.setText("Date: " + selectedDate);
        }, year, month, day);

        datePickerDialog.show();
    }

    private void markAttendance() {
        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Please select a date!", Toast.LENGTH_SHORT).show();
            return;
        }

        String className = classSpinner.getSelectedItem().toString();
        long classId = dbHelper.getClassId(className);

        // First, mark all students in the class as absent
        List<String> allStudents = dbHelper.getStudentsInClass(classId);
        for (String studentName : allStudents) {
            long studentId = dbHelper.getStudentIdByNameAndClass(studentName, classId);
            if (studentId != -1) {
                dbHelper.markAttendance(selectedDate, studentId, "Absent");
            }
        }

        // Then mark the selected students as present
        List<String> selectedStudents = new ArrayList<>();
        for (int i = 0; i < studentListView.getCount(); i++) {
            if (studentListView.isItemChecked(i)) {
                selectedStudents.add(students.get(i));
            }
        }

        for (String studentName : selectedStudents) {
            long studentId = dbHelper.getStudentIdByNameAndClass(studentName, classId);
            if (studentId != -1) {
                dbHelper.markAttendance(selectedDate, studentId, "Present");
            } else {
                Toast.makeText(this, "Student not found: " + studentName, Toast.LENGTH_SHORT).show();
            }
        }

        Toast.makeText(this, "Attendance marked successfully for " + selectedDate, Toast.LENGTH_SHORT).show();
    }}