import java.util.Date;
import java.util.Vector;

package h;

final enum InternshipLevel {
    BASIC,
    INTERMEDIATE,
    ADVANCED
}

static int nextId = 1;

class Internship {
    private int id;
    private String title;
    private String description;
    private InternshipLevel level;
    private Majors preferredMajor;
    private Date openingDate;
    private Date closingDate;
    private String status;
    private String companyName;
    private String representativeUsername;
    private int totalSlots;
    private int filledSlots;
    private boolean isVisible;
    private Vector<Application> applications;

    public Internship(String title, String description, InternshipLevel level, 
                     Majors preferredMajor, Date openingDate, Date closingDate, 
                     String companyName, String representativeUsername, int slots) {

        this.id = nextId++;
        this.title = title;
        this.description = description;
        this.level = level;
        this.preferredMajor = preferredMajor;
        this.openingDate = openingDate;
        this.closingDate = closingDate;
        this.status = "Pending";
        this.companyName = companyName;
        this.representativeUsername = representativeUsername;
        this.totalSlots = slots;
        this.filledSlots = 0;
        this.isVisible = true;
        this.applications = new Vector<Application>();
    }

    public int getId() {
        return id;
    }

    public void toggleVisibility() {
        this.isVisible = !this.isVisible;
    }

    public void processApplication(int studentId, boolean isApproved) {
        for (Application app : applications) {
            if (app.getStudentId() == studentId) {
                app.setStatus(isApproved ? "Successful" : "Rejected");
                if (isApproved && app.isAccepted()) {
                    filledSlots++;
                    if (filledSlots >= totalSlots) {
                        this.status = "Filled";
                    }
                }
                return;
            }
        }
    }
}

class Application {
    private int studentId;
    private String status; // "Pending", "Successful", "Rejected"
    private boolean accepted;

    public Application(int studentId) {
        this.studentId = studentId;
        this.status = "Pending";
        this.accepted = false;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}