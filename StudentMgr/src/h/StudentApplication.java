package h;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Arrays;

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

    public StudentApplication(int studentId, int internshipId) {
        this.studentId = studentId;
        this.internshipId = internshipId;
        this.status = "Pending";
        this.accepted = false;
        this.applicationDate = new Date();
        this.statusUpdatedDate = new Date();
    }

    public int getStudentId() {
        return studentId;
    }

    public int getInternshipId() {
        return internshipId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (isValidStatus(status)) {
            this.status = status;
            this.statusUpdatedDate = new Date();
        } else {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
    }

    private boolean isValidStatus(String status) {
        return status != null && VALID_STATUSES.contains(status);
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }
}