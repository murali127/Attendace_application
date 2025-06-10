package com.example.attendance;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AllAttendanceActivity extends AppCompatActivity {

    private Spinner classSpinner;
    private ListView attendanceListView;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_attendance);

        dbHelper = new DatabaseHelper(this);
        classSpinner = findViewById(R.id.classSpinner);
        attendanceListView = findViewById(R.id.attendanceListView);

        loadClasses();
        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadAllAttendance();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void loadClasses() {
        List<String> classes = dbHelper.getAllClasses();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, classes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(adapter);
    }

    private void loadAllAttendance() {
        String className = classSpinner.getSelectedItem().toString();
        long classId = dbHelper.getClassId(className);

        if (classId == -1) {
            Toast.makeText(this, "Class not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get attendance summary for all students in the class
        Map<String, Integer> attendanceSummary = dbHelper.getAttendanceSummaryForClass(classId);

        if (attendanceSummary.isEmpty()) {
            Toast.makeText(this, "No attendance records found for this class!", Toast.LENGTH_SHORT).show();
        }

        // Convert the map to a list of strings for display
        List<String> displayList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : attendanceSummary.entrySet()) {
            displayList.add(entry.getKey() + " - " + entry.getValue() + " days present");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, displayList);
        attendanceListView.setAdapter(adapter);
    }
}