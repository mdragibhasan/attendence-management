package arms.attendancemanagement.api;

public class Course {
    public String code, title;
    public int credits;
    public String teacher;

    @Override
    public String toString() {
        return code;
    }
}
