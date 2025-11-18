package h;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Arrays;

/**
 * Represents a student's application to a specific internship.
 * Contains application status and relevant dates.
 */
public class StudentApplication implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private static final List<String> VALID_STATUSES = Arrays.asList(
        "Pending",
        "Successful",
        "Rejected",
        "Withdrawn",
        "Pending Withdrawal"
    );
    
    private int studentId;
    private int internshipId;
    private String status;
    private boolean accepted;
    private Date applicationDate;
    private Date statusUpdatedDate;

    /**
     * Constructs a StudentApplication object.
     * Status is set to "Pending" and dates are set to the current time.
     * @param studentId The ID of the student submitting the application.
     * @param internshipId The ID of the internship the student is applying to.
     */
    public StudentApplication(int studentId, int internshipId) {
        this.studentId = studentId;
        this.internshipId = internshipId;
        this.status = "Pending";
        this.accepted = false;
        this.applicationDate = new Date();
        this.statusUpdatedDate = new Date();
    }

    /**
     * Gets the ID of the student who submitted this application.
     * @return The student's ID.
     */
    public int getStudentId() {
        return studentId;
    }

    /**
     * Gets the ID of the internship this application is for.
     * @return The internship's ID.
     */
    public int getInternshipId() {
        return internshipId;
    }

    /**
     * Gets the current status of the application (e.g., "Pending", "Successful", "Rejected").
     * @return The status string.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets a new status for the application.
     * Updates the status updated date and validates the new status string.
     * @param status The new status string to set.
     * @throws IllegalArgumentException if the status is not a valid status.
     */
    public void setStatus(String status) {
        if (isValidStatus(status)) {
            this.status = status;
            this.statusUpdatedDate = new Date();
        } else {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
    }

    /**
     * Helper method to validate if a given status string is one of the predefined valid statuses.
     * @param status The status string to validate.
     * @return true if the status is valid, false otherwise.
     */
    private boolean isValidStatus(String status) {
        return status != null && VALID_STATUSES.contains(status);
    }

    /**
     * Checks if the placement has been formally accepted by the student.
     * @return true if accepted, false otherwise.
     */
    public boolean isAccepted() {
        return accepted;
    }

    /**
     * Sets the accepted status of the placement.
     * @param accepted true to mark as accepted, false otherwise.
     */
    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    /**
     * Gets the date the application was initially submitted.
     * @return The application date.
     */
    public Date getApplicationDate() {
        return applicationDate;
    }
}