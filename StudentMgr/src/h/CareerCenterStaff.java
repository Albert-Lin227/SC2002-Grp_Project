package h;

import java.util.ArrayList;
import java.util.List;

public class CareerCenterStaff extends User {

    public CareerCenterStaff(String username, String password, int id) {
        super(username, password, id);
    }

    public boolean authorizeCompanyRep(CompanyRep rep, boolean isApproved) {
        if (rep == null) return false;
        
        rep.setApproved(isApproved);
        System.out.println("Staff: Company Representative " + rep.getUsername() + " has been " + (isApproved ? "approved." : "rejected."));
        return isApproved;
    }

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