package h;

import java.util.Vector;

public class Student extends User {
    private int yearOfStudy;
    private Majors major;
    private Vector<StudentApplication> applications;
    private int acceptedInternshipId;
    private static final int MAX_APPLICATIONS = 3;

    public Student(String username, String password, int id, int yearOfStudy, Majors major) {
        super(username, password, id);
        this.yearOfStudy = yearOfStudy;
        this.major = major;
        this.applications = new Vector<>();
        this.acceptedInternshipId = -1;
    }

    public int getYearOfStudy() { return yearOfStudy; }
    public Majors getMajor() { return major; }
    public int getAcceptedInternshipId() { return acceptedInternshipId; }
    
    public void resetAcceptedInternshipId() {
        this.acceptedInternshipId = -1;
    }

    private boolean isEligibleByLevel(InternshipLevel internshipLevel) {
        if (yearOfStudy <= 2) {
            return internshipLevel == InternshipLevel.BASIC;
        }
        return true; // Year 3 and above can apply for any level
    }

    private boolean isEligibleByMajor(Majors preferredMajor) {
        return this.major == preferredMajor;
    }
    
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

    public StudentApplication getApplicationDetails(int internshipId) {
        for (StudentApplication app : applications) {
            if (app.getInternshipId() == internshipId) {
                return app;
            }
        }
        return null;
    }
    
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
}