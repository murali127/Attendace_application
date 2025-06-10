package com.example.attendance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Attendance.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    private static final String TABLE_CLASS = "class";
    private static final String TABLE_STUDENT = "student";
    private static final String TABLE_ATTENDANCE = "attendance";

    // Class table columns
    private static final String COLUMN_CLASS_ID = "class_id";
    private static final String COLUMN_CLASS_NAME = "class_name";

    // Student table columns
    private static final String COLUMN_STUDENT_ID = "student_id";
    private static final String COLUMN_STUDENT_NAME = "student_name";
    private static final String COLUMN_ROLL_NUMBER = "roll_number";
    private static final String COLUMN_CLASS_FK = "class_id";

    // Attendance table columns
    private static final String COLUMN_ATTENDANCE_ID = "attendance_id";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_STUDENT_FK = "student_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_CLASS + "(" +
                COLUMN_CLASS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_CLASS_NAME + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_STUDENT + "(" +
                COLUMN_STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_STUDENT_NAME + " TEXT," +
                COLUMN_ROLL_NUMBER + " INTEGER," +
                COLUMN_CLASS_FK + " INTEGER," +
                "FOREIGN KEY(" + COLUMN_CLASS_FK + ") REFERENCES " + TABLE_CLASS + "(" + COLUMN_CLASS_ID + "))");

        db.execSQL("CREATE TABLE " + TABLE_ATTENDANCE + "(" +
                COLUMN_ATTENDANCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_DATE + " TEXT," +
                COLUMN_STATUS + " TEXT," +
                COLUMN_STUDENT_FK + " INTEGER," +
                "FOREIGN KEY(" + COLUMN_STUDENT_FK + ") REFERENCES " + TABLE_STUDENT + "(" + COLUMN_STUDENT_ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE);
        onCreate(db);
    }

    public long addClass(String className) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CLASS_NAME, className);
        long result = db.insert(TABLE_CLASS, null, values);
        db.close();
        return result;
    }

    public boolean updateClass(long classId, String newClassName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CLASS_NAME, newClassName);
        int rowsAffected = db.update(TABLE_CLASS, values, COLUMN_CLASS_ID + "=?", new String[]{String.valueOf(classId)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean deleteClass(long classId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_CLASS, COLUMN_CLASS_ID + "=?", new String[]{String.valueOf(classId)});
        db.close();
        return rowsAffected > 0;
    }

    public List<String> getAllClasses() {
        List<String> classes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_CLASS_NAME + " FROM " + TABLE_CLASS, null);
        if (cursor.moveToFirst()) {
            do {
                classes.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return classes;
    }

    public long addStudent(String studentName, int rollNumber, long classId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STUDENT_NAME, studentName);
        values.put(COLUMN_ROLL_NUMBER, rollNumber);
        values.put(COLUMN_CLASS_FK, classId);
        long result = db.insert(TABLE_STUDENT, null, values);
        db.close();
        return result;
    }

    public boolean updateStudent(long studentId, String newName, int newRollNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STUDENT_NAME, newName);
        values.put(COLUMN_ROLL_NUMBER, newRollNumber);
        int rowsAffected = db.update(TABLE_STUDENT, values, COLUMN_STUDENT_ID + "=?", new String[]{String.valueOf(studentId)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean deleteStudent(long studentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_STUDENT, COLUMN_STUDENT_ID + "=?", new String[]{String.valueOf(studentId)});
        db.close();
        return rowsAffected > 0;
    }

    public List<String> getStudentsInClass(long classId) {
        List<String> students = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_STUDENT_NAME + " FROM " + TABLE_STUDENT + " WHERE " + COLUMN_CLASS_FK + "=?", new String[]{String.valueOf(classId)});
        if (cursor.moveToFirst()) {
            do {
                students.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return students;
    }

    // Add this method to DatabaseHelper to check for existing attendance records
    public boolean hasAttendanceRecord(String date, long studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ATTENDANCE +
                        " WHERE " + COLUMN_DATE + " = ? AND " + COLUMN_STUDENT_FK + " = ?",
                new String[]{date, String.valueOf(studentId)});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Update the markAttendance method to handle updates
    public void markAttendance(String date, long studentId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_STATUS, status);
        values.put(COLUMN_STUDENT_FK, studentId);

        // Check if attendance record already exists
        if (hasAttendanceRecord(date, studentId)) {
            // Update existing record
            db.update(TABLE_ATTENDANCE, values,
                    COLUMN_DATE + " = ? AND " + COLUMN_STUDENT_FK + " = ?",
                    new String[]{date, String.valueOf(studentId)});
        } else {
            // Insert new record
            db.insert(TABLE_ATTENDANCE, null, values);
        }
        db.close();
    }

    // Update the getAttendanceForDateAndClass method to ensure we get the latest status
    public List<String> getAttendanceForDateAndClass(String date, long classId) {
        List<String> attendanceList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // This query now ensures we only get the latest status for each student
        Cursor cursor = db.rawQuery("SELECT s." + COLUMN_STUDENT_NAME + ", a." + COLUMN_STATUS +
                        " FROM " + TABLE_STUDENT + " s LEFT JOIN (" +
                        "   SELECT " + COLUMN_STUDENT_FK + ", " + COLUMN_STATUS +
                        "   FROM " + TABLE_ATTENDANCE +
                        "   WHERE " + COLUMN_DATE + " = ?" +
                        "   GROUP BY " + COLUMN_STUDENT_FK +
                        ") a ON s." + COLUMN_STUDENT_ID + " = a." + COLUMN_STUDENT_FK +
                        " WHERE s." + COLUMN_CLASS_FK + " = ?",
                new String[]{date, String.valueOf(classId)});

        if (cursor.moveToFirst()) {
            do {
                String status = cursor.getString(1);
                if (status == null) {
                    status = "Absent"; // Default status if no attendance record exists
                }
                attendanceList.add(cursor.getString(0) + ": " + status);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return attendanceList;
    }

    public long getClassId(String className) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_CLASS_ID + " FROM " + TABLE_CLASS + " WHERE " + COLUMN_CLASS_NAME + " = ?", new String[]{className});
        if (cursor.moveToFirst()) {
            long id = cursor.getLong(0);
            cursor.close();
            return id;
        }
        cursor.close();
        return -1;
    }
    public long getStudentId(int rollNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_STUDENT_ID + " FROM " + TABLE_STUDENT + " WHERE " + COLUMN_ROLL_NUMBER + " = ?", new String[]{String.valueOf(rollNumber)});
        if (cursor.moveToFirst()) {
            long id = cursor.getLong(0);
            cursor.close();
            return id;
        }
        cursor.close();
        return -1;
    }
    public long getStudentIdByNameAndClass(String studentName, long classId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_STUDENT_ID + " FROM " + TABLE_STUDENT +
                " WHERE " + COLUMN_STUDENT_NAME + " = ? AND " + COLUMN_CLASS_FK + " = ?", new String[]{studentName, String.valueOf(classId)});
        if (cursor.moveToFirst()) {
            long id = cursor.getLong(0);
            cursor.close();
            return id;
        }
        cursor.close();
        return -1;
    }
    // Add this method to DatabaseHelper class
    public Map<String, Integer> getAttendanceSummaryForClass(long classId) {
        Map<String, Integer> attendanceSummary = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to get student names and their present count
        String query = "SELECT s." + COLUMN_STUDENT_NAME + ", " +
                "COUNT(a." + COLUMN_ATTENDANCE_ID + ") as present_count " +
                "FROM " + TABLE_STUDENT + " s " +
                "LEFT JOIN " + TABLE_ATTENDANCE + " a " +
                "ON s." + COLUMN_STUDENT_ID + " = a." + COLUMN_STUDENT_FK + " " +
                "WHERE s." + COLUMN_CLASS_FK + " = ? " +
                "AND a." + COLUMN_STATUS + " = 'Present' " +
                "GROUP BY s." + COLUMN_STUDENT_ID;

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(classId)});

        if (cursor.moveToFirst()) {
            do {
                String studentName = cursor.getString(0);
                int presentCount = cursor.getInt(1);
                attendanceSummary.put(studentName, presentCount);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // Also include students with no attendance records (0 days present)
        List<String> allStudents = getStudentsInClass(classId);
        for (String student : allStudents) {
            if (!attendanceSummary.containsKey(student)) {
                attendanceSummary.put(student, 0);
            }
        }

        return attendanceSummary;
    }
}