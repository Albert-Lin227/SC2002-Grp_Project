package h;

import java.util.Date;
import java.util.Vector;

public class Company {
    
    private String companyName;
    private Vector<Internship> internships;
    private Vector<CompanyRep> representatives;
    private Vector<CareerCenterStaff> careerCenterStaffs; // For comprehensive system management
    private static final int MAX_INTERNSHIPS = 5;

    public Company(String companyName) {
        this.companyName = companyName;
        this.internships = new Vector<>();
        this.representatives = new Vector<>();
        this.careerCenterStaffs = new Vector<>();
    }
    
    public Vector<Internship> getInternships() {
        return internships;
    }
    
    public Internship getInternshipById(int internshipId) {
        for (Internship internship : internships) {
            if (internship.getId() == internshipId) {
                return internship;
            }
        }
        return null;
    }

    public boolean addRepresentative(CompanyRep representative) {
        if (representative != null && !representatives.contains(representative)) {
            representatives.add(representative);
            representative.setCompany(this);
            return true;
        }
        return false;
    }

    public boolean addCareerCenterStaff(CareerCenterStaff staff) {
         if (staff != null && !careerCenterStaffs.contains(staff)) {
            careerCenterStaffs.add(staff);
            return true;
        }
        return false;
    }

    public Internship createInternship(String title, String description, InternshipLevel level,
                                        Majors preferredMajor, Date openingDate, Date closingDate,
                                        String representativeUsername, int slots) {
        
        if (internships.size() >= MAX_INTERNSHIPS) {
            System.out.println("Error: Company " + companyName + " has reached the maximum of " + MAX_INTERNSHIPS + " internships.");
            return null;
        }

        if (slots > 10 || slots < 1) {
            System.out.println("Error: Number of slots must be between 1 and 10.");
            return null;
        }

        Internship internship = new Internship(
            title, description, level, preferredMajor,
            openingDate, closingDate, companyName, representativeUsername, slots
        );
        internships.add(internship);
        System.out.println("Internship '" + title + "' created successfully (Status: Pending Staff Approval).");
        return internship;
    }

    public boolean processApplication(int internshipId, int studentId, boolean isApproved) {
        Internship internship = getInternshipById(internshipId);
        if (internship != null) {
            internship.processApplication(studentId, isApproved);
            String action = isApproved ? "approved" : "rejected";
            System.out.println("Company " + companyName + " processed application for Internship ID " + internshipId + ": " + action + ".");
            return true;
        }
        System.out.println("Error: Internship ID " + internshipId + " not found for application processing.");
        return false;
    }
    
    public boolean toggleInternshipVisibility(int internshipId) {
        Internship internship = getInternshipById(internshipId);
        if (internship != null) {
            internship.toggleVisibility();
            String visibility = internship.isVisible() ? "on" : "off";
            System.out.println("Internship ID " + internshipId + " visibility toggled " + visibility + ".");
            return true;
        }
        System.out.println("Error: Internship ID " + internshipId + " not found for visibility toggle.");
        return false;
    }
}