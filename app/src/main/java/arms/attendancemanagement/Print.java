package arms.attendancemanagement;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import arms.attendancemanagement.api.Manager;


public class Print extends AppCompatActivity {
    private String html;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendances);
        html = Manager.getAttendanceCounts(this,
                getIntent().getStringExtra("semester"), getIntent().getStringExtra("course"));
        ((WebView) findViewById(R.id.webView)).loadData(html, "text/html", "UTF-8");

    }

    public void exportHtml(View view) {
        try {
            String out = Environment.getExternalStoragePublicDirectory("Exported-Data") + File.separator +
                    getIntent().getStringExtra("semester") + "-" + getIntent().getStringExtra("course") + ".html";
            Environment.getExternalStoragePublicDirectory("Exported-Data").mkdirs();
            BufferedWriter writer = new BufferedWriter(new FileWriter(out));
            writer.write(html);
            writer.close();
            Utility.showMessage(this, "HTML exported in " + out);
        } catch (Exception e) {
            e.printStackTrace();
            Utility.showMessage(this, "Error Exporting Data" + e.getMessage());
        }
    }
}
