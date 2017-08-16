package arms.attendancemanagement.api;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;

public class Manager {
    private static DBHelper dbHelper;

    public static void createAttendance(Context context, Attendance attendance) {
        makeDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("INSERT INTO attendances VALUES(?,?,?,?,?,?,0)", new String[]{attendance.id + "", attendance.student.id + "",
                "" + attendance.semester, attendance.course.code, attendance.lecture_count + "", attendance.lecture_date});
        db.close();
    }

    public static void confirmAttendance(Context context, long id) {
        makeDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("UPDATE attendances SET confirmed = 1 WHERE id = ?", new String[]{id + ""});
        db.close();
    }

    public static void deleteAttendance(Context context, long id) {
        makeDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM attendances WHERE id = ?", new String[]{id + ""});
        db.close();
    }

    /* ************ Student ************* */

    public static void createStudent(Context context, Student student) {
        makeDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("INSERT INTO students VALUES(?,?,?,?)", new String[]{student.id + "", student.name, student.semester + "", student.email});
        db.close();
    }

    public static void deleteStudent(Context context, int id) {
        makeDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM students WHERE id = ?", new String[]{id + ""});
        db.close();
    }

    public static void updateStudent(Context context, Student student) {
        makeDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("UPDATE students SET id = ?, name = ?, semester = ? WHERE id = ?", new String[]{student.id + "", student.name, student.semester + ""});
        db.close();
    }

    public static ArrayList<Student> getStudentsBySemester(Context context, int semester) {
        makeDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<Student> students = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM students WHERE semester = ?", new String[]{semester + ""});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Student student = new Student();
            student.name = cursor.getString(cursor.getColumnIndex("name"));
            student.email = cursor.getString(cursor.getColumnIndex("email"));
            student.id = cursor.getInt(cursor.getColumnIndex("id"));
            student.semester = cursor.getInt(cursor.getColumnIndex("semester"));
            students.add(student);
        }
        cursor.close();
        db.close();
        return students;
    }

    /* ********* Course ************/
    public static void createCourse(Context context, Course course) {
        makeDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("INSERT INTO courses VALUES(?,?,?,?)", new String[]{course.code, course.title, course.teacher, course.credits + ""});
        db.close();
    }

    public static void deleteCourse(Context context, String code) {
        makeDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM courses WHERE code = ?", new String[]{code});
        db.close();
    }

    public static void updateCourse(Context context, Course course) {
        makeDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("UPDATE courses SET code = ?, title = ?, credits = ? WHERE code = ?",
                new String[]{course.code, course.title, course.credits + ""});
        db.close();
    }

    public static Course getCourse(Context context, String code) {
        makeDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM courses WHERE code LIKE ?", new String[]{code});
        if (cursor.moveToFirst()) {
            Course course = new Course();
            course.code = cursor.getString(cursor.getColumnIndex("code"));
            course.title = cursor.getString(cursor.getColumnIndex("title"));
            course.credits = cursor.getInt(cursor.getColumnIndex("credits"));
            course.teacher = cursor.getString(cursor.getColumnIndex("teacher"));
            cursor.close();
            db.close();
            return course;
        }
        cursor.close();
        db.close();
        return null;
    }

    public static ArrayList<Course> getCourses(Context context) {
        makeDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<Course> courses = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM courses", null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Course course = new Course();
            course.code = cursor.getString(cursor.getColumnIndex("code"));
            course.title = cursor.getString(cursor.getColumnIndex("title"));
            course.teacher = cursor.getString(cursor.getColumnIndex("teacher"));
            course.credits = cursor.getInt(cursor.getColumnIndex("credits"));
            courses.add(course);
        }
        cursor.close();
        db.close();
        return courses;
    }


    private static void makeDbHelper(Context context) {
        if (dbHelper == null)
            dbHelper = new DBHelper(context);
    }

    public static ArrayList<String> getUnconfirmedDates(Context context) {
        makeDbHelper(context);
        ArrayList<String> dates = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT lecture_date FROM attendances WHERE confirmed = 0 ORDER BY lecture_date", null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
            dates.add(cursor.getString(0));
        cursor.close();
        db.close();
        return dates;
    }

    public static ArrayList<Attendance> getAttendancesByInfo(Context context, String semester, String course, String date) {
        makeDbHelper(context);
        ArrayList<Attendance> attendances = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM attendances WHERE confirmed = 0 AND semester LIKE ? AND course LIKE ? " +
                " AND lecture_date LIKE ? ORDER BY id", new String[]{semester, course, date});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Attendance atd = new Attendance();
            atd.id = cursor.getLong(cursor.getColumnIndex("id"));
            atd.semester = cursor.getInt(cursor.getColumnIndex("semester"));
            atd.course = Manager.getCourse(context, cursor.getString(cursor.getColumnIndex("course")));
            atd.student = Manager.getStudentById(context, cursor.getInt(cursor.getColumnIndex("student")));
            atd.lecture_count = cursor.getInt(cursor.getColumnIndex("lecture_count"));
            atd.lecture_date = cursor.getString(cursor.getColumnIndex("lecture_date"));

            attendances.add(atd);
        }
        cursor.close();
        db.close();
        return attendances;
    }

    private static Student getStudentById(Context context, int id) {
        makeDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM students WHERE id = ?", new String[]{id + ""});
        Student student = null;
        if (cursor.moveToFirst()) {
            student = new Student();
            student.id = cursor.getInt(cursor.getColumnIndex("id"));
            student.name = cursor.getString(cursor.getColumnIndex("name"));
            student.email = cursor.getString(cursor.getColumnIndex("email"));
            student.semester = cursor.getInt(cursor.getColumnIndex("semester"));
        }
        cursor.close();
        db.close();
        return student;
    }


    public static String getAttendanceCounts(Context context, String semester, String course) {
        makeDbHelper(context);

        Course crs = Manager.getCourse(context, course);
        String result = "<html><head><style> td {padding:10px;}" +
                "</style></head><body style='text-align:center;'>" +
                "<div>Semester : " + semester + ", Course Code : " + course;
        if (crs != null)
            result += "<br/>Course Title : " + crs.title + "<br/>Course Credit : " + crs.credits + "<br/>Course Teacher : " + crs.teacher;

        result += "</div><table border='1' style='width:100%;text-align:left;'><tr style='background:#9a9;'><td>ID</td><td>Name</td><td>Attendances</td><td>Percentage</td><td>Email</td></tr>";


        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cr = db.rawQuery("SELECT DISTINCT lecture_date FROM attendances WHERE semester = ? AND course = ?", new String[]{
                semester, course
        });
        int total = 0;

      /*  for(cr.moveToFirst(); !cr.isAfterLast(); cr.moveToNext()){
            Cursor ccors=db.rawQuery("SELECT DISTINCT course FROM attendances WHERE semester = ? AND date= ?", new String[]{
                    semester,cr.getString(0)});
        }*/
        for (cr.moveToFirst(); !cr.isAfterLast(); cr.moveToNext()) {
            Cursor qr = db.rawQuery("SELECT MAX(lecture_count) FROM attendances WHERE semester = ? AND course = ? and lecture_date  = ?",
                    new String[]{semester, course, cr.getString(0)});
            if (qr.moveToFirst())
                total += qr.getInt(0);
            qr.close();
        }
        cr.close();
        Cursor cursor = db.rawQuery("SELECT DISTINCT student, name, email FROM attendances, students WHERE students.semester = ? AND attendances.course LIKE ? AND student = students.id", new String[]{
                semester, course
        });

        ArrayList<STD> list = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Cursor cs = db.rawQuery("SELECT SUM(lecture_count) FROM attendances WHERE semester = ? AND course LIKE ? AND student = ? AND confirmed = 1",
                    new String[]{semester, course, cursor.getInt(0) + ""});
            if (cs.moveToFirst()) {
                STD std = new STD();
                std.id = cursor.getInt(0);
                std.name = cursor.getString(1);
                std.email = cursor.getString(2);
                std.attendance = cs.getInt(0);
                list.add(std);
            }
            cs.close();
        }
        cursor.close();
        db.close();
        Collections.sort(list, new Comparator<STD>() {
            @Override
            public int compare(STD o1, STD o2) {
                if (o1.attendance == o2.attendance) return o1.id - o2.id;
                return o2.attendance - o1.attendance;
            }
        });
        for (int i = 0; i < list.size(); i++) {
            STD std = list.get(i);
            result += "<tr><td>" + std.id + "</td><td>" + std.name + "</td><td> " + std.attendance + "</td><td>" + ((std.attendance * 100) / total) + "%</td><td>" + std.email + "</td></tr>";
        }
        result += "</table></body></html>";
        return result;
    }

    private static class STD {
        String name, email;
        int attendance, id;
    }
}
