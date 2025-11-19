package h;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a student user, handling student-specific actions like applying for and accepting internships.
 */
public class Student extends User {
    private int yearOfStudy;
    private Majors major;
    private Vector<StudentApplication> applications;
    private int acceptedInternshipId;
    private static final int MAX_APPLICATIONS = 3;

    /**
     * Constructs a Student object.
     * @param username The student's username.
     * @param password The student's password.
     * @param id The unique student ID.
     * @param yearOfStudy The current year of study (1-4).
     * @param major The student's academic major.
     */
    public Student(String username, String password, int id, int yearOfStudy, Majors major) {
        super(username, password, id);

        Pattern pattern = Pattern.compile("^U\\d{7}[a-zA-Z]$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(username);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid username format. Must be 'U' followed by 7 digits.");
        }
        this.yearOfStudy = yearOfStudy;
        this.major = major;
        this.applications = new Vector<>();
        this.acceptedInternshipId = -1;
    }

    /**
     * Gets the student's current year of study.
     * @return The year of study integer.
     */
    public int getYearOfStudy() { return yearOfStudy; }
    
    /**
     * Gets the student's academic major.
     * @return The Majors enum representing the student's major.
     */
    public Majors getMajor() { return major; }
    /**
     * Gets the ID of the internship the student has accepted a placement for.
     * Returns -1 if no placement has been accepted.
     * @return The accepted internship ID.
     */
    public int getAcceptedInternshipId() { return acceptedInternshipId; }

    /**
     * Resets the accepted internship ID to -1, typically after a successful withdrawal.
     */
    public void resetAcceptedInternshipId() {
        this.acceptedInternshipId = -1;
    }

    /**
     * Checks if the student meets the level requirement for the given internship level.
     * Students in Year 1 or 2 are restricted to BASIC level internships.
     * @param internshipLevel The level of the internship.
     * @return true if the student is eligible, false otherwise.
     */
    private boolean isEligibleByLevel(InternshipLevel internshipLevel) {
        if (yearOfStudy <= 2) {
            return internshipLevel == InternshipLevel.BASIC;
        }
        return true; // Year 3 and above can apply for any level
    }

    /**
     * Checks if the student's major matches the internship's preferred major.
     * @param preferredMajor The preferred major of the internship.
     * @return true if the majors match, false otherwise.
     */
    private boolean isEligibleByMajor(Majors preferredMajor) {
        return this.major == preferredMajor;
    }

    /**
     * Counts the number of active (Pending or Successful) applications the student currently has.
     * Used to enforce the maximum application limit.
     * @return The count of active applications.
     */
    private int getActiveApplicationCount() {
        int count = 0;
        for (StudentApplication app : applications) {
            String status = app.getStatus();
            
            if (!status.equals("Rejected") && !status.equals("Withdrawn")) {
                count++;
            }
        }
        return count;
    }

    /**
     * Allows a student to apply for an internship.
     * Checks for eligibility, application limits, and if the internship is open/visible.
     * @param internship The Internship object to apply for.
     * @return true if the application was successfully submitted, false otherwise.
     */
    public boolean applyForInternship(Internship internship) {
        if (!isLoggedIn) {
            System.out.println("Error: Student must be logged in to apply.");
            return false;
        }
        if (internship == null || !internship.getStatus().equals("Approved") || !internship.isVisible()) {
            System.out.println("Error: Internship is not available for application.");
            return false;
        }
        if (!isEligibleByLevel(internship.getLevel()) || !isEligibleByMajor(internship.getPreferredMajor())) {
            System.out.println("Error: Not eligible based on year of study or major.");
            return false;
        }
        if (getActiveApplicationCount() >= MAX_APPLICATIONS) {
            System.out.println("Error: Maximum of 3 active applications reached.");
            return false;
        }

        if (getApplicationDetails(internship.getId()) != null) {
            System.out.println("Error: Already applied for this internship.");
            return false;
        }

        StudentApplication newApp = new StudentApplication(this.id, internship.getId());
        applications.add(newApp);
        internship.addApplication(newApp);
        System.out.println("Applied for: " + internship.getTitle());
        return true;
    }

    /**
     * Accepts a successful internship placement.
     * Sets the accepted internship ID, increments the internship's filled slots, and automatically
     * withdraws the student from all other active applications.
     * @param internshipId The ID of the internship placement to accept.
     * @return true if the placement was accepted, false if it could not be accepted.
     */
    public boolean acceptInternshipPlacement(int internshipId) {
        if (acceptedInternshipId != -1) {
            System.out.println("Error: Already accepted an internship placement (ID: " + acceptedInternshipId + ")");
            return false;
        }
        StudentApplication targetApp = getApplicationDetails(internshipId);
        if (targetApp == null || !targetApp.getStatus().equals("Successful")) {
            System.out.println("Error: Cannot accept. Application status is not 'Successful'.");
            return false;
        }

        targetApp.setAccepted(true);
        this.acceptedInternshipId = internshipId;
        
        for (StudentApplication app : applications) {
            if (app.getInternshipId() != internshipId && (app.getStatus().equals("Pending") || app.getStatus().equals("Successful"))) {
                app.setStatus("Withdrawn");
            }
        }
        
        System.out.println("Placement accepted for Internship ID: " + internshipId);
        return true;
    }

    /**
     * Retrieves the application details for a specific internship ID.
     * @param internshipId The ID of the internship.
     * @return The StudentApplication object, or null if no application is found.
     */
    public StudentApplication getApplicationDetails(int internshipId) {
        for (StudentApplication app : applications) {
            if (app.getInternshipId() == internshipId) {
                return app;
            }
        }
        return null;
    }
    
    /**
     * Submits a request to withdraw from an application.
     * Sets the application status to "Pending Withdrawal" for Career Center Staff review.
     * @param internshipId The ID of the internship to withdraw from.
     * @return true if the withdrawal request was submitted, false otherwise (e.g., invalid status).
     */
    public boolean requestWithdrawal(int internshipId) {
        StudentApplication app = getApplicationDetails(internshipId);
        if (app == null) {
            System.out.println("Error: Application not found.");
            return false;
        }

        String currentStatus = app.getStatus();
        if (currentStatus.equals("Rejected") || currentStatus.equals("Withdrawn") || currentStatus.equals("Pending Withdrawal")) {
            System.out.println("Error: Cannot withdraw from an application with status: " + currentStatus);
            return false;
        }

        app.setStatus("Pending Withdrawal");
        System.out.println("Withdrawal request submitted for approval by Career Center Staff.");
        return true;
    }

    public void printApplications() {
        System.out.println("Applications for Student ID: " + id);
        for (StudentApplication app : applications) {
            System.out.println(app.toString());
        }
    }
}