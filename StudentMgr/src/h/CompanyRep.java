package h;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class CompanyRep extends User {
    
    private String userId;
    private String name;
    private List<Internship> internships;
    private String companyName;
    private String department;
    private String position;
    private boolean isApproved;
    private static final int MAX_INTERNSHIPS = 5;

    public CompanyRep(String username, String password, int id, String userId, 
                     String name, String companyName, String department, String position) {
        super(username, password, id);
        this.userId = userId;
        this.name = name;
        this.companyName = companyName;
        this.department = department;
        this.position = position;
        this.internships = new ArrayList<>();
        this.isApproved = false;
    }

     //the code below is for the methods

    @Override
    public boolean login(String username, String password) {
        if (!isApproved) {
            System.out.println("Error: Your account has not been approved by Career Center Staff.");
            return false;
        }
        return super.login(username, password);
    }

    @Override
    public void logout() {
        super.logout();
    }

    public boolean changePwd(String oldPassword, String newPassword) {
        return super.changePwd(oldPassword, newPassword);
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getDepartment() {
        return department;
    }

    public String getPosition() {
        return position;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        this.isApproved = approved;
    }

    public Internship createInternship(String title, String description, InternshipLevel level,
                                      Majors preferredMajor, Date openingDate,
                                      Date closingDate, int slots) {
        if (internships.size() >= MAX_INTERNSHIPS) {
            System.out.println("Error: Maximum number of internships (5) reached.");
            return null;
        }

        if (slots > 10 || slots < 1) {
            System.out.println("Error: Number of slots must be between 1 and 10.");
            return null;
        }

        if (title == null || title.isEmpty()) {
            System.out.println("Error: Internship title cannot be empty.");
            return null;
        }

        Internship internship = new Internship(
            title, description, level, preferredMajor,
            openingDate, closingDate, companyName, this.username, slots
        );
        internships.add(internship);
        System.out.println("Internship created successfully. Waiting for approval.");
        return internship;
    }

    public List<Internship> getInternships() {
        return internships;
    }

    public List<Internship> getPostedInternships() {
        return new ArrayList<>(internships);
    }

    public boolean toggleInternshipVisibility(int internshipId) {
        for (Internship internship : internships) {
            if (internship.getId() == internshipId) {
                internship.toggleVisibility();
                return true;
            }
        }
        System.out.println("Error: Internship not found.");
        return false;
    }

    public boolean processApplication(int internshipId, int studentId, boolean isApproved) {
        for (Internship internship : internships) {
            if (internship.getId() == internshipId) {
                internship.processApplication(studentId, isApproved);
                if (isApproved) {
                    System.out.println("Application approved.");
                } else {
                    System.out.println("Application rejected.");
                }
                return true;
            }
        }
        System.out.println("Error: Internship not found.");
        return false;
    }

    public List<StudentApplication> getApplicationsByInternship(Internship internship) {
        if (internship != null) {
            return new ArrayList<>(internship.getApplications());
        }
        System.out.println("Error: Internship is null.");
        return new ArrayList<>();
    }

    public void approveInternship(Internship internship) {
        if (internship != null) {
            internship.setStatus("Approved");
        }
    }

    public void rejectInternship(Internship internship) {
        if (internship != null) {
            internship.setStatus("Rejected");
        }
    }

    @Override
    public String toString() {
        return "CompanyRep{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", companyName='" + companyName + '\'' +
                ", position='" + position + '\'' +
                ", isApproved=" + isApproved +
                ", internships=" + internships.size() +
                '}';
    }
}