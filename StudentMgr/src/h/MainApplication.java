package h;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class MainApplication {
    
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";

    public static void main(String[] args) {
        System.out.println(ANSI_YELLOW + "--- Internship Placement Management System Test ---" + ANSI_RESET);

        Student student = new Student("U2345123F", "studentpwd", 1, 3, Majors.CSC); 
        CompanyRep rep = new CompanyRep("rep@tech.com", "password", 2, 
                                         "rep1", "Test Rep", "Tech Company", 
                                         "Engineering", "Manager");
        CareerCenterStaff staff = new CareerCenterStaff("StaffAdmin", "staffpwd", 100);

        // Initialize Company and link users
        Company techCompany = new Company("Tech Company");
        techCompany.addRepresentative(rep);
        techCompany.addCareerCenterStaff(staff);
        
    
        // TEST CASE: Student Creation & Login
        System.out.println(ANSI_CYAN + "\n--- Test Case: Student Creation & Login ---" + ANSI_RESET);

        student.login(student.getUsername(), "studentpwd");
        System.out.println();
        
    

        // TEST CASE: Staff Authorizes Company Rep
        System.out.println(ANSI_CYAN + "\n--- Test Case: Staff Authorizes Company Rep ---" + ANSI_RESET);

        System.out.println("Rep Approved Status Before: " + (rep.isApproved() ? ANSI_GREEN + "TRUE" : ANSI_RED + "FALSE") + ANSI_RESET);
        staff.authorizeCompanyRep(rep, true);
        System.out.println("Rep Approved Status After: " + (rep.isApproved() ? ANSI_GREEN + "TRUE" : ANSI_RED + "FALSE") + ANSI_RESET);
        System.out.println();

        
        // TEST CASE: Internship Creation (Rep Delegates to Company)
        System.out.println(ANSI_CYAN + "\n--- Test Case: Internship Creation (Rep Delegates to Company) ---" + ANSI_RESET);

        Date openingDate = new Date(System.currentTimeMillis()); 
        Date closingDate = new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000); 
        
        Internship internship = rep.createInternship(
            "Software Engineer Intern",
            "Develop modern applications.",
            InternshipLevel.INTERMEDIATE,
            Majors.CSC,
            openingDate,
            closingDate,
            5
        );
        
        if (internship != null) {
            System.out.println("Created: ID " + internship.getId() + ", Status: " + internship.getStatus());
        }
        System.out.println();

        
        // TEST CASE: Staff Approves Internship
        System.out.println(ANSI_CYAN + "\n--- Test Case: Staff Approves Internship ---" + ANSI_RESET);

        // Application denial
        Student testStudent = new Student("U9999999Z", "testpwd", 99, 4, Majors.CSC);
        testStudent.login("U9999999Z", "testpwd");
        System.out.println("--- Before approval ---");
        testStudent.applyForInternship(internship); // Should fail as status is Pending
        CareerCenterStaff.authorizeInternship(internship, true);
        System.out.println("--- After approval ---");
        System.out.println("Internship Status: " + internship.getStatus());
        System.out.println("Internship is Visible: " + internship.isVisible());
        testStudent.applyForInternship(internship);
        System.out.println();
        
        
        // TEST CASE: Full Application and Acceptance Workflow
        System.out.println(ANSI_CYAN + "\n--- Test Case: Full Application and Acceptance Workflow ---" + ANSI_RESET);

        student.applyForInternship(internship);
        StudentApplication app = student.getApplicationDetails(internship.getId());
        techCompany.processApplication(internship.getId(), student.getId(), true);
        System.out.println("Application Status: " + (app != null ? ANSI_GREEN + app.getStatus() + ANSI_RESET : ANSI_RED + "ERROR" + ANSI_RESET));

        boolean acceptResult = student.acceptInternshipPlacement(internship.getId());
        if (acceptResult) {
            internship.incrementFilledSlots();
        }
        System.out.print("Placement Accepted: ");
        System.out.println(acceptResult ? ANSI_GREEN + "TRUE" + ANSI_RESET : ANSI_RED + "FALSE" + ANSI_RESET);
        System.out.println("Internship Slots Filled: " + internship.getFilledSlots() + "/" + internship.getTotalSlots());
        System.out.println("Internship Final Status: " + internship.getStatus());
        System.out.println();
        
        
        // =========================================================================
        // TEST CASE: Company Toggles Internship Visibility (Facade)
        // (Original: testVisibilityToggle)
        // =========================================================================
        System.out.println(ANSI_CYAN + "\n--- Test Case: Company Toggles Internship Visibility (Facade) ---" + ANSI_RESET);
        
        boolean isVisibleBefore = techCompany.getInternshipById(internship.getId()).isVisible();
        System.out.println("Visibility Before Toggle: " + isVisibleBefore);
        
        techCompany.toggleInternshipVisibility(internship.getId()); 
        
        boolean isVisibleAfter = techCompany.getInternshipById(internship.getId()).isVisible();
        System.out.println("Visibility After Toggle: " + isVisibleAfter);
        System.out.println();
        
        
        // =========================================================================
        // TEST CASE: Withdrawal Request and Staff Approval
        // (Original: testWithdrawalWorkflow)
        // =========================================================================
        System.out.println(ANSI_CYAN + "\n--- Test Case: Withdrawal Request and Staff Approval ---" + ANSI_RESET);
        
        student.requestWithdrawal(internship.getId());
        
        app = student.getApplicationDetails(internship.getId()); // Get updated application details
        System.out.println("Application Status after Request: " + app.getStatus());
        
        staff.processWithdrawalRequest(student, internship.getId(), true);
        
        System.out.println("Final Application Status: " + app.getStatus());
        System.out.println("Student Accepted Internship ID after withdrawal: " + student.getAcceptedInternshipId());
        System.out.println();
        
        
        // =========================================================================
        // TEST CASE: Staff Generates Comprehensive Report
        // (Original: testStaffReporting)
        // =========================================================================
        System.out.println(ANSI_CYAN + "\n--- Test Case: Staff Generates Comprehensive Report ---" + ANSI_RESET);
        
        List<Internship> allInternships = techCompany.getInternships();
        
        // Report 1: All internships
        staff.generateInternshipReport(allInternships, null, null, null);
        
        // Report 2: Filtered by Approved, CSC, Intermediate
        staff.generateInternshipReport(allInternships, "Approved", Majors.CSC, InternshipLevel.INTERMEDIATE);
        
        System.out.println();


        // =========================================================================
        // TEST CASE: Testing Logouts
        // (Original: testLogout)
        // =========================================================================
        System.out.println(ANSI_CYAN + "\n--- Test Case: Testing Logouts ---" + ANSI_RESET);
        student.logout();
        rep.logout(); 
        System.out.println();
    }
}