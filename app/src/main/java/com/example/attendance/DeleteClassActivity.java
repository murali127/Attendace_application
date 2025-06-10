package com.example.attendance;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ArrayAdapter;
public class DeleteClassActivity extends AppCompatActivity {

    private EditText classNameEditText;
    private Button deleteClassButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_class);

        classNameEditText = findViewById(R.id.classNameEditText);
        deleteClassButton = findViewById(R.id.deleteClassButton);
        dbHelper = new DatabaseHelper(this);

        deleteClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String className = classNameEditText.getText().toString().trim();

                if (className.isEmpty()) {
                    Toast.makeText(DeleteClassActivity.this, "Please enter a class name", Toast.LENGTH_SHORT).show();
                    return;
                }

                long classId = dbHelper.getClassId(className);
                boolean isDeleted = dbHelper.deleteClass(classId);
                if (isDeleted) {
                    Toast.makeText(DeleteClassActivity.this, "Class deleted successfully", Toast.LENGTH_SHORT).show();
                    classNameEditText.setText("");
                } else {
                    Toast.makeText(DeleteClassActivity.this, "Failed to delete class", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
