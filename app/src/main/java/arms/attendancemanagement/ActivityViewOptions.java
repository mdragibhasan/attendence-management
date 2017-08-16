package arms.attendancemanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;

import arms.attendancemanagement.api.Course;
import arms.attendancemanagement.api.Manager;


public class ActivityViewOptions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_options);

        ArrayList<Course> courses = Manager.getCourses(this);
        SpinnerAdapter adapter = new ArrayAdapter<>(this, R.layout.id_list_sample, courses);
        ((Spinner) findViewById(R.id.input_course_code)).setAdapter(adapter);
    }

    public void showAttendances(View view) {
        String semester = ((EditText) findViewById(R.id.input_semester)).getText().toString();
        String course = ((Spinner) findViewById(R.id.input_course_code)).getSelectedItem().toString();
        //TODO:Empty check

        Intent intent = new Intent(this, Print.class);
        intent.putExtra("semester", semester);
        intent.putExtra("course", course);

        startActivity(intent);
    }
}
