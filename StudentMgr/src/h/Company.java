package h;

import java.util.Date;
import java.util.Vector;

/**
 * Represents a company entity.
 * Acts as a central repository (Facade) for all internships, representatives, and staff associated with the company.
 * It enforces company-wide business rules, like maximum internships.
 */
public class Company {
    
    private String companyName;
    private Vector<Internship> internships;
    private Vector<CompanyRep> representatives;
    private Vector<CareerCenterStaff> careerCenterStaffs; // For comprehensive system management
    private static final int MAX_INTERNSHIPS = 5;

    /**
     * Constructs a Company object.
     * @param companyName The official name of the company.
     */
    public Company(String companyName) {
        this.companyName = companyName;
        this.internships = new Vector<>();
        this.representatives = new Vector<>();
        this.careerCenterStaffs = new Vector<>();
    }
    
    /**
     * Returns a vector of Internships.
     * @return A vector of internships.
     */
    public Vector<Internship> getInternships() {
        return internships;
    }
    
    /**
     * @param internshipId an internship ID.
     * @return the internship with the corresponding ID.
     */
    public Internship getInternshipById(int internshipId) {
        for (Internship internship : internships) {
            if (internship.getId() == internshipId) {
                return internship;
            }
        }
        return null;
    }

    /**
     * Adds a Company Representative to the company's list and links the rep back to this company object.
     * @param representative The CompanyRep object to add.
     * @return true if the representative was added, false if null or a duplicate.
     */
    public boolean addRepresentative(CompanyRep representative) {
        if (representative != null && !representatives.contains(representative)) {
            representatives.add(representative);
            representative.setCompany(this);
            return true;
        }
        return false;
    }

    /**
     * Adds a Career Center Staff member to the company's list for internal management/records.
     * @param staff The CareerCenterStaff object to add.
     * @return true if the staff member was added, false if null or a duplicate.
     */
    public boolean addCareerCenterStaff(CareerCenterStaff staff) {
         if (staff != null && !careerCenterStaffs.contains(staff)) {
            careerCenterStaffs.add(staff);
            return true;
        }
        return false;
    }

    /**
     * Creates a new internship for the company.
     * This method contains the core business logic, including:
     * 1. Checking the maximum number of internships limit (MAX_INTERNSHIPS).
     * 2. Validating the number of slots (1-10).
     * 3. Creating the Internship object and adding it to the company's central list.
     * @param title The title of the internship.
     * @param description A brief description.
     * @param level The experience level.
     * @param preferredMajor The preferred major.
     * @param openingDate The application opening date.
     * @param closingDate The application closing date.
     * @param representativeUsername The username of the representative creating the internship.
     * @param slots The total number of available slots.
     * @return The newly created Internship object, or null if creation failed due to business rules.
     */
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

    /**
     * Finds the relevant internship by ID and delegates the application status update to it.
     * @param internshipId The ID of the internship.
     * @param studentId The ID of the student.
     * @param isApproved true to approve, false to reject.
     * @return true if the application was processed, false if the internship was not found.
     */
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
    
    /**
     * Finds the relevant internship by ID and delegates the visibility toggle to it.
     * @param internshipId The ID of the internship.
     * @return true if visibility was toggled, false if the internship was not found.
     */
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