package com.example.attendance;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.List;

public class ViewAttendanceActivity extends AppCompatActivity {

    private Button selectDateButton;
    private TextView selectedDateText;
    private ListView attendanceListView;
    private Spinner classSpinner;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);

        dbHelper = new DatabaseHelper(this);
        selectDateButton = findViewById(R.id.selectDateButton);
        selectedDateText = findViewById(R.id.selectedDateText);
        attendanceListView = findViewById(R.id.attendanceListView);
        classSpinner = findViewById(R.id.classSpinner);

        loadClasses();
        selectDateButton.setOnClickListener(v -> showDatePickerDialog());
    }

    private void loadClasses() {
        List<String> classes = dbHelper.getAllClasses();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(adapter);
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, (view, year1, month1, dayOfMonth) -> {
            String selectedDate = year1 + "-" + (month1 + 1) + "-" + dayOfMonth;
            selectedDateText.setText("Date: " + selectedDate);
            loadAttendance(selectedDate);
        }, year, month, day);

        datePickerDialog.show();
    }

    private void loadAttendance(String date) {
        String className = classSpinner.getSelectedItem().toString();
        long classId = dbHelper.getClassId(className);
        if (classId == -1) {
            Toast.makeText(this, "Class not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> attendanceRecords = dbHelper.getAttendanceForDateAndClass(date, classId);
        if (attendanceRecords.isEmpty()) {
            Toast.makeText(this, "No attendance records found for this date and class!", Toast.LENGTH_SHORT).show();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, attendanceRecords);
        attendanceListView.setAdapter(adapter);
    }
}