# Attendance Management System
````markdown


![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![SQLite](https://img.shields.io/badge/SQLite-07405E?style=for-the-badge&logo=sqlite&logoColor=white)

An Android application for managing student attendance with class-wise tracking and date-wise filtering capabilities.

---

## 🚀 Features

- **Class Management**
  - Add new classes
  - View all available classes
- **Student Management**
  - Add students to classes
  - View students by class
- **Attendance Tracking**
  - Mark attendance by date
  - View daily attendance reports
  - View comprehensive attendance summary by class
- **Data Persistence**
  - SQLite database storage
  - Persistent across app sessions

---

## 📱 Screenshots

| Main Menu | Add Class | Add Student |
|-----------|-----------|-------------|
| ![Main Menu](screenshots/main.png) | ![Add Class](screenshots/add_class.png) | ![Add Student](screenshots/add_student.png) |

| Mark Attendance | Daily Attendance | Class Summary |
|-----------------|------------------|---------------|
| ![Mark Attendance](screenshots/mark_attendance.png) | ![Daily View](screenshots/daily_view.png) | ![Class Summary](screenshots/class_summary.png) |

---

## 🗃️ Database Schema

```mermaid
erDiagram
    CLASS ||--o{ STUDENT : "1 to many"
    STUDENT ||--o{ ATTENDANCE : "1 to many"
    CLASS {
        int class_id PK
        string class_name
    }
    STUDENT {
        int student_id PK
        string student_name
        int roll_number
        int class_id FK
    }
    ATTENDANCE {
        int attendance_id PK
        string date
        string status
        int student_id FK
    }
````

---

## 🛠️ Installation

### Prerequisites

* Android Studio (latest version)
* Android SDK (API level 23 or higher)
* Java JDK 11+

### Steps

```bash
git clone https://github.com/yourusername/attendance-management-android.git
cd attendance-management-android
```

* Open project in Android Studio
* Sync Gradle dependencies
* Run on emulator or physical device

---

## 📖 Usage Guide

1. **Add a Class**

   * Tap "Add Class" on main menu
   * Enter class name and save

2. **Add Students**

   * Tap "Add Student"
   * Choose class and enter student details

3. **Mark Attendance**

   * Tap "Mark Attendance"
   * Choose class and date, then mark students as present or absent

4. **View Reports**

   * **Daily View**: Shows attendance per day
   * **Class Summary**: Displays total attendance per student

---

## 📂 Code Structure

```
attendance/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/attendance/
│   │   │   │   ├── AddClassActivity.java
│   │   │   │   ├── AddStudentActivity.java
│   │   │   │   ├── AllAttendanceActivity.java
│   │   │   │   ├── DatabaseHelper.java
│   │   │   │   ├── MainActivity.java
│   │   │   │   └── MarkAttendanceActivity.java
│   │   │   └── res/
│   │   │       ├── layout/
│   │   │       │   ├── activity_main.xml
│   │   │       │   └── ...other layouts
├── build.gradle
└── ...
```

---

## 🤝 Contributing

Contributions are welcome!

1. Fork this repo
2. Create a new branch: `git checkout -b feature/your-feature`
3. Commit your changes: `git commit -m 'Add some feature'`
4. Push to the branch: `git push origin feature/your-feature`
5. Open a Pull Request

---

