package arms.attendancemanagement;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by ARMs on 26-Jul-17.
 */

public class Utility {
    public static void showMessage(Context context, String msg) {
        (new AlertDialog.Builder(context).setMessage(msg).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })).create().show();
    }
}
