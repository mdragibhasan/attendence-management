package arms.attendancemanagement;

import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import arms.attendancemanagement.api.Course;
import arms.attendancemanagement.api.Manager;

public class NewCourseForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_course_form);
        setTitle("Add New Course");
    }

    public void addNewCourse(View view) {
        EditText codeField = (EditText) findViewById(R.id.course_code),
                titleField = (EditText) findViewById(R.id.course_title),
                creditField = (EditText) findViewById(R.id.course_credit),
                 teacherField = (EditText) findViewById(R.id.course_teacher);

        String code = codeField.getText().toString();
        String title = titleField.getText().toString();
        String teacher = teacherField.getText().toString();
        //empty check course table data
        if (code.matches("^\\s*$") || title.matches("^\\s*$") || creditField.getText().toString().matches("^\\s*$")) {
            Utility.showMessage(this, "Please fill up all the fields.");
            return;
        }
        int credits = Integer.parseInt(creditField.getText().toString());
        Course course = new Course();
        course.code = code;
        course.title = title;
        course.teacher=teacher;
        course.credits = credits;
        try {
            Manager.createCourse(this, course);

            Toast.makeText(this, "Course Added Successfully", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            Utility.showMessage(this, "Course with this code already exists.");
        }
    }
}
