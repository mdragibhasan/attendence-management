package arms.attendancemanagement.api;

/**
 * Created by ARMs on 26-Jul-17.
 */

public class Student {
    public int id, semester;
    public String name,email;


    @Override
    public String toString() {
        return id+"" ;//+ " - " + name;
    }
}
