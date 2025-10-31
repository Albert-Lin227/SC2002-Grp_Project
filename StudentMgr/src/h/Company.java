import java.util.Date;
import java.util.Vector;

package h;


public class Company {
    private String companyName;
    private boolean isApproved;
    private Vector<Internship> internships;
    private static final int MAX_INTERNSHIPS = 5;

    public Company(String username, String password, String companyName) {
        this.username = username;
        this.password = password;
        this.companyName = companyName;
        this.isApproved = false;
        this.internships = new Vector<Internship>();
    }

    public boolean createInternship(String title, String description, InternshipLevel level, 
                                  Majors preferredMajor, Date openingDate, Date closingDate, 
                                  int slots) {
        if (internships.size() >= MAX_INTERNSHIPS) {
            return false;
        }
        if (slots > 10) {
            return false;
        }

        Internship internship = new Internship(
            title, description, level, preferredMajor, openingDate, 
            closingDate, companyName, this.username, slots
        );
        internships.add(internship);
        return true;
    }

    public Vector<Internship> getInternships() {
        return internships;
    }

    public void setApproved(boolean approved) {
        this.isApproved = approved;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void toggleInternshipVisibility(int internshipId) {
        for (Internship internship : internships) {
            if (internship.getId() == internshipId) {
                internship.toggleVisibility();
                return;
            }
        }
    }

    public void processApplication(int internshipId, int studentId, boolean isApproved) {
        for (Internship internship : internships) {
            if (internship.getId() == internshipId) {
                internship.processApplication(studentId, isApproved);
                return;
            }
        }
    }
}
