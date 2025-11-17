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

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public Majors getMajor() {
        return major;
    }

    public void setYearOfStudy(int yearOfStudy) {
        if (yearOfStudy >= 1 && yearOfStudy <= 4) {
            this.yearOfStudy = yearOfStudy;
        }
    }

    public void setMajor(Majors major) {
        this.major = major;
    }

    private boolean isEligibleByLevel(InternshipLevel internshipLevel) {
        if (yearOfStudy <= 2) {
            return internshipLevel == InternshipLevel.BASIC;
        }
        return true;
    }

    private boolean isEligibleByMajor(Majors preferredMajor) {
        return this.major == preferredMajor;
    }

    public boolean applyForInternship(Internship internship) {
        if (applications.size() >= MAX_APPLICATIONS) {
            System.out.println("Error: Maximum number of applications (3) reached.");
            return false;
        }

        for (StudentApplication app : applications) {
            if (app.getInternshipId() == internship.getId()) {
                System.out.println("Error: Already applied for this internship.");
                return false;
            }
        }

        if (!isEligibleByLevel(internship.getLevel())) {
            System.out.println("Error: Not eligible for this internship level based on year of study.");
            return false;
        }

        if (!isEligibleByMajor(internship.getPreferredMajor())) {
            System.out.println("Error: Your major does not match the preferred major for this internship.");
            return false;
        }

        if (!internship.isVisible()) {
            System.out.println("Error: This internship opportunity is not currently visible.");
            return false;
        }

        StudentApplication application = new StudentApplication(this.id, internship.getId());
        applications.add(application);
        internship.addApplication(application);
        
        System.out.println("Successfully applied for internship: " + internship.getTitle());
        return true;
    }

    public boolean acceptInternshipPlacement(int internshipId) {
        StudentApplication acceptedApp = null;
        for (StudentApplication app : applications) {
            if (app.getInternshipId() == internshipId) {
                if (!app.getStatus().equals("Successful")) {
                    System.out.println("Error: Can only accept internships with 'Successful' status.");
                    return false;
                }
                acceptedApp = app;
                break;
            }
        }

        if (acceptedApp == null) {
            System.out.println("Error: Internship not found in applications.");
            return false;
        }

        this.acceptedInternshipId = internshipId;
        acceptedApp.setAccepted(true);

        for (StudentApplication app : applications) {
            if (app.getInternshipId() != internshipId) {
                app.setStatus("Withdrawn");
            }
        }

        System.out.println("Successfully accepted internship placement.");
        return true;
    }

    public Vector<StudentApplication> getApplications() {
        return applications;
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

        if (app.getStatus().equals("Withdrawn") || app.getStatus().equals("Rejected")) {
            System.out.println("Error: Cannot withdraw from this application.");
            return false;
        }

        app.setStatus("Pending Withdrawal");
        System.out.println("Withdrawal request submitted for approval by Career Center Staff.");
        return true;
    }

    public int getAcceptedInternshipId() {
        return acceptedInternshipId;
    }

    public boolean hasAcceptedPlacement() {
        return acceptedInternshipId != -1;
    }

    public int getActiveApplicationCount() {
        int count = 0;
        for (StudentApplication app : applications) {
            String status = app.getStatus();
            if (!status.equals("Rejected") && !status.equals("Withdrawn")) {
                count++;
            }
        }
        return count;
    }
}