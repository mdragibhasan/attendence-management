package arms.attendancemanagement;

import android.content.Intent;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import arms.attendancemanagement.api.Manager;
import arms.attendancemanagement.api.Student;


public class NewStudentForm extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_student_form);
        setTitle("Add Students");
    }

    public void takePic(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void addNewStudent(View view) {
        EditText nameField = (EditText) findViewById(R.id.name),
                idField = (EditText) findViewById(R.id.id),
                emailField = (EditText) findViewById(R.id.email),
                semesterField = (EditText) findViewById(R.id.semester);
        // validate std info
        if (image == null || nameField.getText().toString().matches("^\\s*$") || emailField.getText().toString().matches("^\\s*$") || idField.getText().toString().matches("^\\s*$") || semesterField.getText().toString().matches("^\\s*$")) {
            Utility.showMessage(this, "Please fill up all the fields.");
            return;
        }
        Student student = new Student();
        student.id = Integer.parseInt(idField.getText().toString());
        student.name = nameField.getText().toString();
        student.semester = Integer.parseInt(semesterField.getText().toString());
        student.email = emailField.getText().toString();
        try {
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(getExternalFilesDir("Avatars") + File.separator +
                        student.id + ".png");
                image.compress(Bitmap.CompressFormat.PNG, 100, out);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Manager.createStudent(this, student);
            Toast.makeText(this, "Student Added Successfully", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            e.printStackTrace();
            Utility.showMessage(this, "Student with this ID already exists.");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            image = (Bitmap) extras.get("data");
            ((ImageView) findViewById(R.id.image)).setImageBitmap(image);
        }
    }
}
