package h;

import java.util.Date;
import java.util.Calendar;
import java.util.List; // NEW
import java.util.ArrayList; // NEW

public class MainApplication {
    
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_RED = "\u001B[31m";

    public static void main(String[] args) {
        System.out.println(ANSI_YELLOW + "Internship Placement Management System\n" + ANSI_RESET);
        
        runTestSimulation();

        
    }
    
    public static void runTestSimulation() {
        
        Student student = initializeStudent();
        CompanyRep rep = initializeCompanyRep();
        CareerCenterStaff staff = initializeStaff();
    
        testStudentBasics(student);
        testRepAuthorization(staff, rep);
        testCompanyRepBasics(rep);
        
        Internship internship = testInternshipCreation(rep);
        testInternshipApprovalByStaff(staff, internship);
        testApplicationWorkflow(student, internship, rep);
        testWithdrawalWorkflow(student, internship, staff);
        testStaffReporting(staff, internship);
        testLogout(student);
        
    }

    private static Student initializeStudent() {
        Student student = new Student("U2345123F", "password", 1, 3, Majors.CSC);
        return student;
    }

    private static CompanyRep initializeCompanyRep() {
        CompanyRep rep = new CompanyRep("test@test.com", "password", 2, 
                                         "rep1", "Test Rep", "Tech Company", 
                                         "Engineering", "Manager");
        rep.setApproved(false); 
        return rep;
    }
    
    // NEW: Initialization method for Staff
    private static CareerCenterStaff initializeStaff() {
        CareerCenterStaff staff = new CareerCenterStaff("StaffAdmin", "staffpwd", 100);
        return staff;
    }

    private static void testStudentBasics(Student student) {
        System.out.println(ANSI_CYAN + "Test Case: Student Creation & Login" + ANSI_RESET);
        
        System.out.println("Student: " + student.getUsername());
        System.out.println("Major: " + student.getMajor());
        System.out.println("Year: " + student.getYearOfStudy());
        
        boolean loginSuccess = student.login("U2345123F", "password");
        System.out.print("Login Status: ");
        if (loginSuccess) {
            System.out.println(ANSI_GREEN + "Success" + ANSI_RESET);
        } else {
            System.out.println(ANSI_RED + "Failed" + ANSI_RESET);
        }
        System.out.println();
    }

    private static void testCompanyRepBasics(CompanyRep rep) {
        System.out.println(ANSI_CYAN + "Test Case: Company Representative Creation & Login" + ANSI_RESET);
        
        System.out.println("Company Rep: " + rep.getName());
        System.out.println("Company: " + rep.getCompanyName());

        boolean loginSuccess = rep.login("test@test.com", "password");
        System.out.print("Login Status: ");
        if (loginSuccess) {
            System.out.println(ANSI_GREEN + "Success" + ANSI_RESET);
        } else {
            System.out.println(ANSI_RED + "Failed" + ANSI_RESET);
        }
        rep.logout();
        System.out.println();
    }
    
    private static void testRepAuthorization(CareerCenterStaff staff, CompanyRep rep) {
        System.out.println(ANSI_CYAN + "Test Case: Staff Authorizes Company Rep" + ANSI_RESET);
        
        System.out.println("Rep Approved Status Before Staff Action: " + (rep.isApproved() ? ANSI_GREEN + "TRUE" : ANSI_RED + "FALSE") + ANSI_RESET);
        
        staff.authorizeCompanyRep(rep, true);
        
        System.out.print("Rep Approved Status After Staff Action: ");
        if (rep.isApproved()) {
            System.out.println(ANSI_GREEN + "TRUE" + ANSI_RESET);
        } else {
            System.out.println(ANSI_RED + "FALSE" + ANSI_RESET);
        }
        System.out.println();
    }

    private static Internship testInternshipCreation(CompanyRep rep) {
        System.out.println(ANSI_CYAN + "Test Case: Internship Creation" + ANSI_RESET);
        
        Date openingDate = new Date(System.currentTimeMillis()); // Today
        Date closingDate = new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000); // Expires in 30 days
        
        int numSlots = 5;
        Internship internship = rep.createInternship(
            "Software Engineer Intern",
            "Develop and test software applications",
            InternshipLevel.INTERMEDIATE,
            Majors.CSC,
            openingDate,
            closingDate,
            numSlots
        );
        
        System.out.println("Internship Created: " + internship.getTitle());
        System.out.println("Initial Status: " + internship.getStatus());
        System.out.println();
        
        return internship;
    }

    private static void testInternshipApprovalByStaff(CareerCenterStaff staff, Internship internship) {
        System.out.println(ANSI_CYAN + "Test Case: Staff Approves Internship" + ANSI_RESET);
        
        staff.approveInternship(internship);
        
        System.out.println("Internship Status: " + internship.getStatus());
        System.out.print("Is Visible: ");
        if (internship.isVisible()) {
            System.out.println(ANSI_GREEN + "True." + ANSI_RESET);
        } else {
            System.out.println(ANSI_RED + "False." + ANSI_RESET);
        }
        System.out.println();
    }
    
    private static void testApplicationWorkflow(Student student, Internship internship, CompanyRep rep) {
        System.out.println(ANSI_CYAN + "Test Case: Full Application and Acceptance Workflow" + ANSI_RESET);
        
        boolean applicationResult = student.applyForInternship(internship);
        System.out.print("Student Application Result: ");
        if (applicationResult) {
            System.out.println(ANSI_GREEN + "Success" + ANSI_RESET);
        } else {
            System.out.println(ANSI_RED + "Failed" + ANSI_RESET);
        }
        
        StudentApplication app = student.getApplicationDetails(internship.getId());
        if (app != null) {
            System.out.println("Application Initial Status: " + app.getStatus());
        }

        rep.processApplication(internship.getId(), student.getId(), true);
        System.out.print("Status after Rep Approval: ");
        if (app != null) {
            System.out.println(ANSI_GREEN + app.getStatus() + ANSI_RESET);
        } else {
            System.out.println(ANSI_RED + "Error" + ANSI_RESET);
        }
        
        boolean acceptResult = student.acceptInternshipPlacement(internship.getId());
        System.out.print("Placement Accepted: ");
        if (acceptResult) {
            System.out.println(ANSI_GREEN + "TRUE" + ANSI_RESET);
        } else {
            System.out.println(ANSI_RED + "FALSE" + ANSI_RESET);
        }
        System.out.println("Accepted Internship ID: " + student.getAcceptedInternshipId());
        System.out.println();
    }

    private static void testWithdrawalWorkflow(Student student, Internship internship, CareerCenterStaff staff) {
        System.out.println(ANSI_CYAN + "Test Case: Withdrawal Request and Staff Approval" + ANSI_RESET);
        
        boolean requestResult = student.requestWithdrawal(internship.getId());
        
        StudentApplication app = student.getApplicationDetails(internship.getId());
        
        System.out.print("Withdrawal Request Status: ");
        if (requestResult) {
            System.out.println(ANSI_GREEN + "Submitted (" + app.getStatus() + ")" + ANSI_RESET);
        } else {
            System.out.println(ANSI_RED + "Failed" + ANSI_RESET);
        }
        
        staff.processWithdrawalRequest(student, internship.getId(), true);
        
        System.out.println("Final Application Status: " + app.getStatus());
        System.out.println("Student Accepted Internship ID after withdrawal: " + student.getAcceptedInternshipId());
        System.out.println();
    }
    
    private static void testStaffReporting(CareerCenterStaff staff, Internship internship) {
        System.out.println(ANSI_CYAN + "Test Case: Staff Generates Report (Conceptual)" + ANSI_RESET);
        
        List<Internship> allInternships = new ArrayList<>();
        allInternships.add(internship);

        staff.generateInternshipReport(allInternships, null, Majors.CSC, InternshipLevel.INTERMEDIATE);
        
        System.out.println();
    }

    private static void testLogout(Student student) {
        System.out.println(ANSI_CYAN + "Test Case: Testing Logout" + ANSI_RESET);
        
        student.logout();
        
        System.out.print("Logged In Status: ");
        if (student.isLoggedIn()) {
            System.out.println(ANSI_RED + "TRUE" + ANSI_RESET);
        } else {
            System.out.println(ANSI_GREEN + "FALSE" + ANSI_RESET);
        }
        System.out.println();
    }
}