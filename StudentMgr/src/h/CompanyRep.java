package h;

import java.util.Date;
import java.util.Vector;

/**
 * Represents a company representative user.
 * Handles tasks like creating internships and processing student applications.
 */
public class CompanyRep extends User {
    
    private String name;
    private String companyName;
    private String email;
    private boolean isApproved = false;
    private Company company; 

    /**
     * Constructs a CompanyRep object.
     * @param username The representative's username.
     * @param password The representative's password.
     * @param id The unique user ID.
     * @param userId The company-specific employee ID (if applicable).
     * @param name The representative's full name.
     * @param companyName The name of the company the representative works for.
     * @param department The department of the representative.
     * @param position The job position of the representative.
     */
    public CompanyRep(String username, String password, int id, String userId, 
                     String name, String companyName, String department, String position) {
        super(username, password, id);
        this.name = name;
        this.companyName = companyName;
        this.isApproved = false;
        this.email = username + "@" + companyName.replaceAll(" ", "").toLowerCase() + ".com";
    }

    /**
     * Links this representative to the managing Company object.
     * This is necessary for delegation of company-wide actions.
     * @param company The Company object.
     */
    public void setCompany(Company company) {
        this.company = company;
    }

    /**
     * Overrides the base User login. Performs the standard login check *after*
     * verifying that the representative's account has been approved by the Career Center Staff.
     * @param username The username for login.
     * @param password The password for login.
     * @return true if approved and login is successful, false otherwise.
     */
    @Override
    public boolean login(String username, String password) {
        if (!isApproved) {
            System.out.println("Error: Your account has not been approved by Career Center Staff.");
            return false;
        }
        return super.login(username, password);
    }

    /**
     * Gets the name of the representitive.
     * @return The representitve's name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the company of the representitive.
     * @return The representitve's company's name.
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Gets the status of the representitve.
     * @return The representitve's approval status.
     */
    public boolean isApproved() {
        return isApproved;
    }

    /**
     * Sets the approval status of the representative's account.
     * This method is typically called by Career Center Staff.
     * @param isApproved true to approve the account, false to reject/revoke.
     */
    public void setApproved(boolean isApproved) {
        this.isApproved = isApproved;
    }

    public Vector<Internship> getInternship() {
        if (company == null) return new Vector<>();
        return company.getInternships();
    }

    /**
     * Creates a new internship opportunity for the company.
     * Checks for account approval and delegates the actual creation and storage to the linked Company object.
     * @param title The title of the internship.
     * @param description A brief description.
     * @param level The required experience level.
     * @param preferredMajor The preferred major.
     * @param openingDate The application opening date.
     * @param closingDate The application closing date.
     * @param slots The total number of available slots.
     * @return The newly created Internship object, or null if creation failed (e.g., not approved).
     */
    public Internship createInternship(String title, String description, InternshipLevel level,
                                     Majors preferredMajor, Date openingDate, Date closingDate,
                                     int slots) {
        if (!isApproved) {
            System.out.println("Error: Account not approved. Cannot create internship.");
            return null;
        }
        
        if (company == null) {
            System.out.println("Error: Representative is not linked to a company.");
            return null;
        }
        
        return company.createInternship(title, description, level, preferredMajor,
                                        openingDate, closingDate, this.username, slots);
    }

    /**
     * 
     * @param internshipId
     * @return true if deletion was successful, false otherwise.
     */
    public boolean deleteInternship(int internshipId) {
        if (company == null) return false;
        return company.deleteInternship(internshipId);
    }
    /**
     * Processes a student application for a specific internship.
     * Delegates the action to the linked Company object for central processing.
     * @param internshipId The ID of the internship.
     * @param studentId The ID of the student.
     * @param isApproved true to approve the application, false to reject.
     * @return true if the application was successfully processed, false otherwise.
     */
    public boolean processApplication(int internshipId, int studentId, boolean isApproved) {
        if (company == null) return false;
        return company.processApplication(internshipId, studentId, isApproved);
    }

    /**
     * Toggles the visibility status of a specific internship.
     * Delegates the action to the linked Company object for enforcement of company rules.
     * @param internshipId The ID of the internship to toggle.
     * @return true if visibility was successfully toggled, false otherwise.
     */
    public boolean toggleInternshipVisibility(int internshipId) {
        if (company == null) return false;
        return company.toggleInternshipVisibility(internshipId);
    }
}