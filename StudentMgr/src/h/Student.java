import java.util.Vector;

package h;

public class Student extends User {
	private String yearOfStudy, major;
    private Application enrolledIntenshipIds[3] = [];
    private int internshipId = -1;

    public void acceptInternshipById(int id) {
        for (int internshipId : encrolledIntenshipIds) {
            if (internshipId == id) {
                this.internshipId = id;
                return;
            }
        }
        throw new IllegalArgumentException("Internship ID not found in enrolled internships.");
    }

}