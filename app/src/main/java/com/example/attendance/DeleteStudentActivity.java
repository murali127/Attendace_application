package com.example.attendance;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ArrayAdapter;
public class DeleteStudentActivity extends AppCompatActivity {

    private EditText rollNumberEditText;
    private Button deleteStudentButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_student);

        rollNumberEditText = findViewById(R.id.rollNumberEditText);
        deleteStudentButton = findViewById(R.id.deleteStudentButton);
        dbHelper = new DatabaseHelper(this);

        deleteStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rollNumberStr = rollNumberEditText.getText().toString().trim();

                if (rollNumberStr.isEmpty()) {
                    Toast.makeText(DeleteStudentActivity.this, "Please enter a roll number", Toast.LENGTH_SHORT).show();
                    return;
                }

                int rollNumber = Integer.parseInt(rollNumberStr);
                boolean isDeleted = dbHelper.deleteStudent(rollNumber);

                if (isDeleted) {
                    Toast.makeText(DeleteStudentActivity.this, "Student deleted successfully", Toast.LENGTH_SHORT).show();
                    rollNumberEditText.setText("");
                } else {
                    Toast.makeText(DeleteStudentActivity.this, "Failed to delete student", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
