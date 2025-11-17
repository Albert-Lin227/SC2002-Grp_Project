package h;

import java.util.Date;
import java.util.Calendar;

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
        
        testStudentBasics(student);
        testCompanyRepBasics(rep);
        
        Internship internship = testInternshipCreation(rep);
        testInternshipApproval(internship);
        
        testApplicationWorkflow(student, internship, rep);
        
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
        rep.setApproved(true);
        return rep;
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
        System.out.println(ANSI_CYAN + "Test Case: Company Representative Creation" + ANSI_RESET);
        
        System.out.println("Company Rep: " + rep.getName());
        System.out.println("Company: " + rep.getCompanyName());
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

    private static void testInternshipApproval(Internship internship) {
        System.out.println(ANSI_CYAN + "Test Case: Internship Approval" + ANSI_RESET);
        
        internship.setStatus("Approved");
        
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