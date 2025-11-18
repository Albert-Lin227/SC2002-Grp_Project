package h;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class CareerCenterStaff extends User {

    public CareerCenterStaff(String username, String password, int id) {
        super(username, password, id);
    }

    public boolean authorizeCompanyRep(CompanyRep rep, boolean isApproved) {
        if (rep == null) {
            return false;
        }

        rep.setApproved(isApproved);
        return isApproved;
    }

    public boolean approveInternship(Internship internship) {
        if (internship == null) {
            return false;
        }

        internship.setStatus("Approved");
        return true;
    }

    public boolean rejectInternship(Internship internship) {
        if (internship == null) {
            return false;
        }

        internship.setStatus("Rejected");
        return true;
    }

    public boolean processWithdrawalRequest(Student student, int internshipId, boolean isApproved) {
        if (student == null) {
            return false;
        }
        
        StudentApplication app = student.getApplicationDetails(internshipId);
        
        if (app == null) {
            return false;
        }
        
        if (!app.getStatus().equals("Pending Withdrawal")) {
            return false;
        }
        
        if (isApproved) {
            app.setStatus("Withdrawn");
        }
        if (student.getAcceptedInternshipId() != internshipId) {
            if (app.isAccepted()) {
                app.setStatus("Successful");
            }
            else {
                app.setStatus("Pending");
            }
        }
        
        return true;
    }

    public void generateInternshipReport(List<Internship> allInternships, String filterStatus, Majors filterMajor, InternshipLevel filterLevel) {
        List<Internship> filteredList = new ArrayList<>();

        for (Internship internship : allInternships) {
            boolean statusMatch = filterStatus == null || filterStatus.isEmpty() || internship.getStatus().equalsIgnoreCase(filterStatus);
            boolean majorMatch = filterMajor == null || internship.getPreferredMajor() == filterMajor;
            boolean levelMatch = filterLevel == null || internship.getLevel() == filterLevel;

            if (statusMatch && majorMatch && levelMatch) {
                filteredList.add(internship);
            }
        }

        if (filteredList.isEmpty()) {
            return;
        }

        System.out.println("Found " + filteredList.size() + " matching internships.");
        System.out.println("Filters: Status=" + (filterStatus == null ? "Any" : filterStatus) + 
                           ", Major=" + (filterMajor == null ? "Any" : filterMajor.name()) + 
                           ", Level=" + (filterLevel == null ? "Any" : filterLevel.name()));
        
        for (Internship internship : filteredList) {
            System.out.println(internship.toString());
        }
    }
}
