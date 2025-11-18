package h;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class MainApplication {
    
    // ANSI codes for console output formatting
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";

    public static void main(String[] args) {
        System.out.println(ANSI_YELLOW + "--- Internship Placement Management System Test ---" + ANSI_RESET);
        runTestSimulation();
    }
    
    public static void runTestSimulation() {
        
        Student student = initializeStudent();
        CompanyRep rep = initializeCompanyRep();
        CareerCenterStaff staff = initializeStaff();
        
        Company techCompany = new Company("Tech Company");
        techCompany.addRepresentative(rep);
        techCompany.addCareerCenterStaff(staff);
        
        testRepAuthorization(staff, rep);
        
        Internship internship = testInternshipCreation(rep);
        testInternshipApprovalByStaff(internship);
        
        testStudentBasics(student);
        testApplicationWorkflow(student, internship, techCompany);
        
        testVisibilityToggle(techCompany, internship.getId());

        testWithdrawalWorkflow(student, internship, staff);
        
        testStaffReporting(staff, techCompany.getInternships());
        
        testLogout(student, rep);
    }
    

    private static Student initializeStudent() {
        return new Student("U2345123F", "studentpwd", 1, 3, Majors.CSC); 
    }

    private static CompanyRep initializeCompanyRep() {
        CompanyRep rep = new CompanyRep("rep@tech.com", "password", 2, 
                                         "rep1", "Test Rep", "Tech Company", 
                                         "Engineering", "Manager");
        rep.setApproved(false); // Starts as unapproved
        return rep;
    }
    
    private static CareerCenterStaff initializeStaff() {
        return new CareerCenterStaff("StaffAdmin", "staffpwd", 100);
    }

    private static void testStudentBasics(Student student) {
        printTestCaseHeader("Student Creation & Login");
        student.login(student.getUsername(), "studentpwd");
        System.out.println();
    }

    private static void testRepAuthorization(CareerCenterStaff staff, CompanyRep rep) {
        printTestCaseHeader("Staff Authorizes Company Rep");
        
        System.out.println("Rep Approved Status Before: " + (rep.isApproved() ? ANSI_GREEN + "TRUE" : ANSI_RED + "FALSE") + ANSI_RESET);
        
        staff.authorizeCompanyRep(rep, true);
        
        System.out.println("Rep Approved Status After: " + (rep.isApproved() ? ANSI_GREEN + "TRUE" : ANSI_RED + "FALSE") + ANSI_RESET);
        System.out.println();
    }

    private static Internship testInternshipCreation(CompanyRep rep) {
        printTestCaseHeader("Internship Creation (Rep Delegates to Company)");
        
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
        return internship;
    }

    private static void testInternshipApprovalByStaff(Internship internship) {
        printTestCaseHeader("Staff Approves Internship");
        
        CareerCenterStaff.authorizeInternship(internship, true);
        
        System.out.println("Internship Status: " + internship.getStatus());
        System.out.println("Internship is Visible: " + internship.isVisible());
        System.out.println();
    }
    
    private static void testApplicationWorkflow(Student student, Internship internship, Company company) {
        printTestCaseHeader("Full Application and Acceptance Workflow");
        
        student.applyForInternship(internship);
        StudentApplication app = student.getApplicationDetails(internship.getId());
        
        company.processApplication(internship.getId(), student.getId(), true);
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
    }

    private static void testVisibilityToggle(Company company, int internshipId) {
        printTestCaseHeader("Company Toggles Internship Visibility (Facade)");
        
        boolean isVisibleBefore = company.getInternshipById(internshipId).isVisible();
        System.out.println("Visibility Before Toggle: " + isVisibleBefore);
        
        company.toggleInternshipVisibility(internshipId); 
        
        boolean isVisibleAfter = company.getInternshipById(internshipId).isVisible();
        System.out.println("Visibility After Toggle: " + isVisibleAfter);
        System.out.println();
    }
    
    private static void testWithdrawalWorkflow(Student student, Internship internship, CareerCenterStaff staff) {
        printTestCaseHeader("Withdrawal Request and Staff Approval");
        
        student.requestWithdrawal(internship.getId());
        
        StudentApplication app = student.getApplicationDetails(internship.getId());
        System.out.println("Application Status after Request: " + app.getStatus());
        
        staff.processWithdrawalRequest(student, internship.getId(), true);
        
        System.out.println("Final Application Status: " + app.getStatus());
        System.out.println("Student Accepted Internship ID after withdrawal: " + student.getAcceptedInternshipId());
        System.out.println();
    }
    
    private static void testStaffReporting(CareerCenterStaff staff, List<Internship> allInternships) {
        printTestCaseHeader("Staff Generates Comprehensive Report");
        
        staff.generateInternshipReport(allInternships, null, null, null);
        
        staff.generateInternshipReport(allInternships, "Approved", Majors.CSC, InternshipLevel.INTERMEDIATE);
        
        System.out.println();
    }

    private static void testLogout(Student student, CompanyRep rep) {
        printTestCaseHeader("Testing Logouts");
        student.logout();
        rep.logout(); 
        System.out.println();
    }
    
    private static void printTestCaseHeader(String title) {
        System.out.println(ANSI_CYAN + "\n--- Test Case: " + title + " ---" + ANSI_RESET);
    }
}