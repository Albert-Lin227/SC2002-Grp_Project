package h;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Career Center Staff user.
 * Handles system-level actions such as authorizing company representatives,
 * approving internships, and generating reports.
 */
public class CareerCenterStaff extends User {

    /**
     * Constructs a CareerCenterStaff object.
     * @param username The staff's username.
     * @param password The staff's password.
     * @param id The unique user ID.
     */
    public CareerCenterStaff(String username, String password, int id) {
        super(username, password, id);
    }

    /**
     * Sets the approval status for a Company Representative's account.
     * @param rep The CompanyRep object to authorize.
     * @param isApproved true to approve, false to reject.
     * @return true if the action was successful, false if the representative object was null.
     */
    public boolean authorizeCompanyRep(CompanyRep rep, boolean isApproved) {
        if (rep == null) return false;
        
        rep.setApproved(isApproved);
        System.out.println("Staff: Company Representative " + rep.getUsername() + " has been " + (isApproved ? "approved." : "rejected."));
        return isApproved;
    }
    
    /**
     * Sets the official system status for an internship (Approved or Rejected).
     * This static method directly updates the internship object.
     * @param internship The Internship object to authorize.
     * @param isAuthorized true to set status to "Approved", false to set to "Rejected".
     * @return true if the action was successful, false if the internship object was null.
     */
    public static boolean authorizeInternship(Internship internship, boolean isAuthorized) {
        if (internship == null) {
            System.out.println("Staff: Error: Internship object is null.");
            return false;
        }

        String status = isAuthorized ? "Approved" : "Rejected";
        internship.setStatus(status);
        System.out.println("Staff: Internship ID " + internship.getId() + " (" + internship.getTitle() + ") has been " + status + ".");
        return true;
    }

    /**
     * Processes a student's request to withdraw from an application.
     * If approved, the application status is set to "Withdrawn".
     * Handles specific logic if the student is withdrawing from an accepted placement.
     * @param student The Student object who submitted the request.
     * @param internshipId The ID of the internship for the withdrawal request.
     * @param isApproved true to approve the withdrawal, false to reject the withdrawal request.
     * @return true if the request was processed, false if the student or application was not found or not in the correct status.
     */
    public boolean processWithdrawalRequest(Student student, int internshipId, boolean isApproved) {
        if (student == null) return false;
        
        StudentApplication app = student.getApplicationDetails(internshipId);
        
        if (app == null || !app.getStatus().equals("Pending Withdrawal")) {
            System.out.println("Staff: Error: Application not found or not in 'Pending Withdrawal' status.");
            return false;
        }
        
        if (isApproved) {
            app.setStatus("Withdrawn");
            if (student.getAcceptedInternshipId() == internshipId) {
                student.resetAcceptedInternshipId(); 
                System.out.println("Staff: Withdrawal approved. Placement reset for student " + student.getUsername() + ".");
            } else {
                System.out.println("Staff: Withdrawal approved for student " + student.getUsername() + ".");
            }
        } else {
            String revertStatus = app.isAccepted() ? "Successful" : "Pending";
            app.setStatus(revertStatus); 
            System.out.println("Staff: Withdrawal rejected. Status reverted to " + revertStatus + " for student " + student.getUsername() + ".");
        }
        
        return true;
    }

    /**
     * Generates a filtered report of internships based on various criteria.
     * This method prints the details of the matching internships to the console.
     * @param allInternships A list of all available internships in the system.
     * @param filterStatus The status to filter by (e.g., "Approved", "Pending", null for any).
     * @param filterMajor The major to filter by (null for any).
     * @param filterLevel The experience level to filter by (null for any).
     */
    public void generateInternshipReport(List<Internship> allInternships, String filterStatus, Majors filterMajor, InternshipLevel filterLevel) {
        System.out.println("\n--- Staff: Comprehensive Internship Report ---");
        List<Internship> filteredList = new ArrayList<>();

        for (Internship internship : allInternships) {
            boolean statusMatch = filterStatus == null || filterStatus.isEmpty() || internship.getStatus().equalsIgnoreCase(filterStatus);
            boolean majorMatch = filterMajor == null || internship.getPreferredMajor() == filterMajor;
            boolean levelMatch = filterLevel == null || internship.getLevel() == filterLevel;

            if (statusMatch && majorMatch && levelMatch) {
                filteredList.add(internship);
            }
        }

        System.out.println("Filters: Status=" + (filterStatus == null ? "Any" : filterStatus) + 
                           ", Major=" + (filterMajor == null ? "Any" : filterMajor.name()) + 
                           ", Level=" + (filterLevel == null ? "Any" : filterLevel.name()));
                           
        if (filteredList.isEmpty()) {
            System.out.println("Result: No internships match the specified filters.");
            return;
        }

        System.out.println("Result: Found " + filteredList.size() + " matching internships.");
        for (Internship internship : filteredList) {
            System.out.println(internship.toString());
        }
    }
}