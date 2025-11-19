package h;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * Please save me the test cases are not in order 
 * AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
 * But the test cases have been reordered so they flow better
 */
public class MainApplication {
    // TODO: Test case 16
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    public static void main(String[] args) {
        System.out.println(ANSI_YELLOW + "--- Internship Placement Management System Test ---" + ANSI_RESET);

        Student student = new Student("U2345123F", "studentpwd", 1, 3, Majors.CSC); 
        Student anotherStudent = new Student("U7654321A", "anotherpwd", 2, 3, Majors.CSC);

        // TEST CASE 5: Company Representative Creation & Login
        CompanyRep rep = new CompanyRep("rep@tech.com", "password", 2, 
                                         "rep1", "Test Rep", "Tech Company", 
                                         "Engineering", "Manager");
        

        CareerCenterStaff staff = new CareerCenterStaff("StaffAdmin", "staffpwd", 100);

        // Initialize Company and link users
        anotherStudent.login("U7654321A", "anotherpwd");
        Company techCompany = new Company("Tech Company");
        techCompany.addRepresentative(rep);
        techCompany.addCareerCenterStaff(staff);
        
        
        // TEST CASE 3: Failed login
        System.out.println(ANSI_CYAN + "\n--- Test Case: Failed Student Login ---" + ANSI_RESET);

        student.login("Failed Username", "studentpwd");
        student.login(student.getUsername(), "WrongPassword");
        System.out.println();

        // TEST CASE 1: Student Creation & Login
        System.out.println(ANSI_CYAN + "\n--- Test Case: Student Creation & Login ---" + ANSI_RESET);

        student.login(student.getUsername(), "studentpwd");
        System.out.println();


        // TEST CASE 4: Student Change Password
        System.out.println(ANSI_CYAN + "\n--- Test Case: Student Change Password ---" + ANSI_RESET);

        student.changePwd("studentpwd", "newPassword");
        student.login(student.getUsername(), "newPassword");
        System.out.println();
        

        // TEST CASE 2: Invalid ID
        System.out.println(ANSI_CYAN + "\n--- Test Case: Invalid Student ID ---" + ANSI_RESET);

        try {
            Student failStudent = new Student("U234523F", "studentpwd", 1, 3, Majors.CSC); 
        }
        catch (IllegalArgumentException e) {
            System.out.println(ANSI_RED + "Caught Exception for Invalid ID: " + e.getMessage() + ANSI_RESET);
        }
        System.out.println();

        // TEST CASE: Representitive Creates Application before Acceptance
        System.out.println(ANSI_CYAN + "\n--- Test Case: Representitive Creates Application before Acceptance ---" + ANSI_RESET);

        rep.createInternship("Software Engineer Intern",
            "Develop modern applications.",
            InternshipLevel.INTERMEDIATE,
            Majors.CSC,
            new Date(),
            new Date(),
            5);
            System.out.println();

        // TEST CASE: Staff Authorizes Company Rep
        System.out.println(ANSI_CYAN + "\n--- Test Case: Staff Authorizes Company Rep ---" + ANSI_RESET);

        System.out.println("Rep Approved Status Before: " + (rep.isApproved() ? ANSI_GREEN + "TRUE" : ANSI_RED + "FALSE") + ANSI_RESET);
        staff.authorizeCompanyRep(rep, true);
        System.out.println("Rep Approved Status After: " + (rep.isApproved() ? ANSI_GREEN + "TRUE" : ANSI_RED + "FALSE") + ANSI_RESET);
        System.out.println();

        
        // TEST CASE 13: Internship Creation
        System.out.println(ANSI_CYAN + "\n--- Test Case: Internship Creation ---" + ANSI_RESET);

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

        
        // TEST CASE 21: Staff Approves Internship
        System.out.println(ANSI_CYAN + "\n--- Test Case: Staff Approves Internship ---" + ANSI_RESET);

        // Application denial
        Student testStudent = new Student("U9999999Z", "testpwd", 99, 3, Majors.CSC);
        testStudent.login("U9999999Z", "testpwd");
        System.out.println(ANSI_PURPLE + "--- Before approval ---" + ANSI_RESET);
        testStudent.applyForInternship(internship); // Should fail as status is Pending
        CareerCenterStaff.authorizeInternship(internship, true);
        System.out.println(ANSI_PURPLE + "--- After approval ---" + ANSI_RESET);
        System.out.println("Internship Status: " + internship.getStatus());
        System.out.println("Internship is Visible: " + internship.isVisible());
        testStudent.applyForInternship(internship);
        System.out.println();
        
        
        // TEST CASE 6: Invisible Internship
        System.out.println(ANSI_CYAN + "\n--- Test Case: Invisible Internship ---" + ANSI_RESET);

        Internship invisibleInternship = rep.createInternship(
            "Accountant Intern",
            "MONEY.",
            InternshipLevel.BASIC,
            Majors.ACC,
            openingDate,
            closingDate,
            5
        );
        CareerCenterStaff.authorizeInternship(invisibleInternship, true);
        techCompany.toggleInternshipVisibility(2);
        testStudent.applyForInternship(invisibleInternship);
        System.out.println();

        // TEST CASE 18: View Internship Details
        System.out.println(ANSI_CYAN + "\n--- Test Case: View Internship Details ---" + ANSI_RESET);
        rep.getInternship().forEach(System.out::println);
        System.out.println();

        // TEST CASE 20: Delete Internship
        System.out.println(ANSI_CYAN + "\n--- Test Case: Delete Internship ---" + ANSI_RESET);
        boolean deleteResult = rep.deleteInternship(invisibleInternship.getId());
        System.out.print("Internship Deleted: ");
        System.out.println(deleteResult ? ANSI_GREEN + "TRUE" + ANSI_RESET : ANSI_RED + "FALSE" + ANSI_RESET);
        System.out.println();

        // TEST CASE 7: Ineligible Student Application
        System.out.println(ANSI_CYAN + "\n--- Test Case: Ineligible Student ---" + ANSI_RESET);

        techCompany.toggleInternshipVisibility(2);
        testStudent.applyForInternship(invisibleInternship);
        System.out.println();


        // TEST CASE 14, 15, 19: Full Application and Acceptance Workflow
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
        

        // TEST CASE 8: Toggle Visiblility after Acceptance
        System.out.println(ANSI_CYAN + "\n--- Test Case: Toggle Visiblility after Acceptance ---" + ANSI_RESET);
        boolean toggleResult = techCompany.toggleInternshipVisibility(internship.getId());
        System.out.print("Visibility Toggled: ");
        System.out.println(toggleResult ? ANSI_GREEN + "TRUE" + ANSI_RESET : ANSI_RED + "FALSE" + ANSI_RESET);
        System.out.println("Internship is Visible: " + internship.isVisible());
        student.viewInternship(techCompany);
        anotherStudent.applyForInternship(internship);
        System.out.println();

        // TEST CASE 10: Accepting multiple Internships
        System.out.println(ANSI_CYAN + "\n--- Test Case: Accepting multiple Internships ---" + ANSI_RESET);

        Internship anotherInternship = rep.createInternship(
            "Yet Another Data Analyst Intern",
            "Analyze data trends.",
            InternshipLevel.BASIC,
            Majors.CSC,
            openingDate,
            closingDate,
            3
        );
        CareerCenterStaff.authorizeInternship(anotherInternship, true);
        student.printApplications();
        student.applyForInternship(anotherInternship);
        techCompany.processApplication(anotherInternship.getId(), student.getId(), true);
        student.acceptInternshipPlacement(anotherInternship.getId());
        System.out.println();

        
        // TEST CASE: Company Toggles Internship Visibility (Facade)
        System.out.println(ANSI_CYAN + "\n--- Test Case: Company Toggles Internship Visibility (Facade) ---" + ANSI_RESET);
        
        boolean isVisibleBefore = techCompany.getInternshipById(internship.getId()).isVisible();
        System.out.println("Visibility Before Toggle: " + isVisibleBefore);
        
        techCompany.toggleInternshipVisibility(internship.getId()); 
        
        boolean isVisibleAfter = techCompany.getInternshipById(internship.getId()).isVisible();
        System.out.println("Visibility After Toggle: " + isVisibleAfter);
        System.out.println();
        
        
        // TEST CASE 23: Withdrawal Request and Staff Approval
        System.out.println(ANSI_CYAN + "\n--- Test Case: Withdrawal Request and Staff Approval ---" + ANSI_RESET);
        
        student.requestWithdrawal(internship.getId());
        
        app = student.getApplicationDetails(internship.getId()); // Get updated application details
        System.out.println("Application Status after Request: " + app.getStatus());
        
        staff.processWithdrawalRequest(student, internship.getId(), true);
        
        System.out.println("Final Application Status: " + app.getStatus());
        System.out.println("Student Accepted Internship ID after withdrawal: " + student.getAcceptedInternshipId());
        System.out.println();
        
        
        // TEST CASE 24: Staff Generates Comprehensive Report
        System.out.println(ANSI_CYAN + "\n--- Test Case: Staff Generates Comprehensive Report ---" + ANSI_RESET);
        
        List<Internship> allInternships = techCompany.getInternships();
        
        // Report 1: All internships
        staff.generateInternshipReport(allInternships, null, null, null);
        
        // Report 2: Filtered by Approved, CSC, Intermediate
        staff.generateInternshipReport(allInternships, "Approved", Majors.CSC, InternshipLevel.INTERMEDIATE);
        
        System.out.println();


        // TEST CASE: Testing Logouts
        System.out.println(ANSI_CYAN + "\n--- Test Case: Testing Logouts ---" + ANSI_RESET);
        student.logout();
        rep.logout(); 
        System.out.println();
    }
}