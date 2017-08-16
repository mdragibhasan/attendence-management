package arms.attendancemanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       setTitle("Home");
    }

    public void openAttendanceForm(View view) {
        startActivity(new Intent(this, ActivityAttendanceOptions.class));
    }

    public void openAddStudentForm(View view) {
        startActivity(new Intent(this, NewStudentForm.class));
    }

    public void openAddCourseForm(View view) {
        startActivity(new Intent(this, NewCourseForm.class));
    }

    public void openConfirmAttendanceOptions(View view) {
        startActivity(new Intent(this, ActivityConfirmOptions.class));
    }

    public void showInfo(View view) {
        startActivity(new Intent(this, ActivityViewOptions.class));
    }
}
