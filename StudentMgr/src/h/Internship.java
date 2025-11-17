package h;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

public class Internship implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static int nextId = 1;

    private int id;
    private String title;
    private String description;
    private InternshipLevel level;
    private Majors preferredMajor;
    private Date openingDate;
    private Date closingDate;
    private String status; // "Pending", "Approved", "Rejected", "Filled"
    private String companyName;
    private String representativeUsername;
    private int totalSlots;
    private int filledSlots;
    private boolean isVisible;
    private Vector<StudentApplication> applications;

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
        this.isVisible = false; // Initially not visible until approved
        this.applications = new Vector<>();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public InternshipLevel getLevel() {
        return level;
    }

    public Majors getPreferredMajor() {
        return preferredMajor;
    }

    public Date getOpeningDate() {
        return openingDate;
    }

    public Date getClosingDate() {
        return closingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (isValidStatus(status)) {
            this.status = status;
            if ("Approved".equals(status)) {
                this.isVisible = true;
            }
        }
    }

    private boolean isValidStatus(String status) {
        return status != null && (
            status.equals("Pending") ||
            status.equals("Approved") ||
            status.equals("Rejected") ||
            status.equals("Filled")
        );
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getRepresentativeUsername() {
        return representativeUsername;
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public int getFilledSlots() {
        return filledSlots;
    }

    public int getRemainingSlots() {
        return totalSlots - filledSlots;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void toggleVisibility() {
        this.isVisible = !this.isVisible;
    }

    public void addApplication(StudentApplication application) {
        if (application != null && !applications.contains(application)) {
            applications.add(application);
        }
    }

    public Vector<StudentApplication> getApplications() {
        return applications;
    }

    public void processApplication(int studentId, boolean isApproved) {
        for (StudentApplication app : applications) {
            if (app.getStudentId() == studentId) {
                if (isApproved) {
                    app.setStatus("Successful");
                } else {
                    app.setStatus("Rejected");
                }
                return;
            }
        }
    }

    public void incrementFilledSlots() {
        if (filledSlots < totalSlots) {
            filledSlots++;
            if (filledSlots >= totalSlots) {
                this.status = "Filled";
            }
        }
    }

    @Override
    public String toString() {
        return "Internship{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", level=" + level +
                ", preferredMajor=" + preferredMajor +
                ", status='" + status + '\'' +
                ", companyName='" + companyName + '\'' +
                ", totalSlots=" + totalSlots +
                ", filledSlots=" + filledSlots +
                ", isVisible=" + isVisible +
                '}';
    }
}

