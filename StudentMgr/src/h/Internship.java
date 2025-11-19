package h;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;
import java.util.List;
import java.util.Arrays;

/**
 * Represents an internship opportunity posted by a company.
 * Manages its details, status, and associated applications.
 */
public class Internship implements Serializable {
    
    private static int nextId = 1;
    private static final List<String> VALID_STATUSES = Arrays.asList(
        "Pending",
        "Approved",
        "Rejected",
        "Filled"
    );

    private int id;
    private String title;
    private String description;
    private InternshipLevel level;
    private Majors preferredMajor;
    private Date openingDate;
    private Date closingDate;
    private String status;
    private int totalSlots;
    private int filledSlots;
    private boolean isVisible;
    private Vector<StudentApplication> applications;

    /**
     * Constructs an Internship object, assigning a unique ID and setting the default status to "Pending".
     * @param title The title of the internship.
     * @param description A brief description of the role.
     * @param level The required experience level (e.g., BASIC, ADVANCED).
     * @param preferredMajor The major preferred for this role.
     * @param openingDate The date the internship becomes open for applications.
     * @param closingDate The final date for applications.
     * @param companyName The name of the posting company.
     * @param representativeUsername The username of the company representative who created it.
     * @param slots The total number of available positions.
     */
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
        this.status = "Pending"; // Default status
        this.totalSlots = slots;
        this.filledSlots = 0;
        this.isVisible = false; // Not visible until approved
        this.applications = new Vector<>();
    }
    /**
     * Gets the unique ID of the internship.
     * @return The internship ID.
     */
    public int getId() { return id; }
    public String getTitle() { return title; }
    public InternshipLevel getLevel() { return level; }
    public Majors getPreferredMajor() { return preferredMajor; }
    /**
     * Gets the current status of the internship (e.g., "Pending", "Approved", "Filled").
     * @return The status string.
     */
    public String getStatus() { return status; }

    /**
     * Gets the total number of available slots for this internship.
     * @return The total slots.
     */
    public int getTotalSlots() { return totalSlots; }

    /**
     * Gets the number of slots that have been filled.
     * @return The filled slots count.
     */
    public int getFilledSlots() { return filledSlots; }

    /**
     * Checks if the internship is currently visible to students for application.
     * @return true if visible, false otherwise.
     */
    public boolean isVisible() { return isVisible; }

    /**
     * Sets a new status for the internship, ensuring the status is valid.
     * @param status The new status string to set.
     */
    public void setStatus(String status) {
        if (VALID_STATUSES.contains(status)) {
            this.status = status;
            if (status.equals("Approved")) {
                this.isVisible = true; 
            }
        }
    }

    /**
     * Toggles the visibility of the internship, but only if its status is "Approved".
     * Prints an error if the status is not "Approved".
     */
    public void toggleVisibility() {
        if (this.status.equals("Approved")) {
            this.isVisible = !this.isVisible;
        } else {
             System.out.println("Error: Cannot toggle visibility. Internship status must be Approved.");
        }
    }

    /**
     * Adds a new student application to the internship's list of applications.
     * Prevents adding null applications or duplicates.
     * @param application The StudentApplication object to add.
     */
    public void addApplication(StudentApplication application) {
        if (application != null && !applications.contains(application)) {
            applications.add(application);
        }
    }

    /**
     * Processes a specific student's application by updating its status (Successful or Rejected).
     * @param studentId The ID of the student whose application is being processed.
     * @param isApproved true to set status to "Successful", false to set status to "Rejected".
     */
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

    /**
     * Increments the count of filled slots.
     * If the filled slots reach the total slots, the internship status is set to "Filled" and visibility is turned off.
     */
    public void incrementFilledSlots() {
        if (filledSlots < totalSlots) {
            filledSlots++;
            if (filledSlots >= totalSlots) {
                this.status = "Filled";
                this.isVisible = false; // Students cannot apply when Filled
            }
        }
    }

    /**
     * Returns a string representation of the internship's key details.
     * @return A formatted string with ID, Title, Status, Major, Level, and Slots.
     */
    @Override
    public String toString() {
        return "ID: " + this.getId() +
                ", Title: " + this.getTitle() + 
                ", Status: " + this.getStatus() + 
                ", Major: " + this.getPreferredMajor().name() + 
                ", Level: " + this.getLevel().name() +
                ", Slots: " + this.getFilledSlots() + "/" + this.getTotalSlots();
    }
}