package h;

import java.util.Date;
import java.util.Scanner;

public class MainApplication {
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Internship Placement Management System");
        System.out.println("========================================\n");
        
        testSystem();
    }
    
    public static void testSystem() {
        Student student = new Student("U2345123F", "password", 1, 3, Majors.CSC);
        
        System.out.println("--- Testing Student Creation ---");
        System.out.println("Student: " + student.getUsername());
        System.out.println("Major: " + student.getMajor());
        System.out.println("Year: " + student.getYearOfStudy());
        System.out.println();
        
        System.out.println("--- Testing Login ---");
        boolean loginSuccess = student.login("U2345123F", "password");
        System.out.println("Login Success: " + loginSuccess);
        System.out.println();
        
        System.out.println("--- Creating Company Representative ---");
        CompanyRep rep = new CompanyRep("rep1@company.com", "password", 2, 
                                        "rep1", "John Doe", "Tech Company", 
                                        "Engineering", "Manager");
        rep.setApproved(true);
        System.out.println("Company Rep: " + rep.getName());
        System.out.println("Company: " + rep.getCompanyName());
        System.out.println();
        
        System.out.println("--- Creating Internship ---");
        Date openingDate = new Date();
        Date closingDate = new Date(System.currentTimeMillis() + 30 * 24 * 60 * 60 * 1000);
        
        Internship internship = rep.createInternship(
            "Software Engineer Intern",
            "Develop and test software applications",
            InternshipLevel.INTERMEDIATE,
            Majors.CSC,
            openingDate,
            closingDate,
            5
        );
        System.out.println("Internship Created: " + internship.getTitle());
        System.out.println("Status: " + internship.getStatus());
        System.out.println();
        
        System.out.println("--- Approving Internship ---");
        internship.setStatus("Approved");
        System.out.println("Internship Status: " + internship.getStatus());
        System.out.println("Is Visible: " + internship.isVisible());
        System.out.println();
        
        System.out.println("--- Student Applying for Internship ---");
        boolean applicationResult = student.applyForInternship(internship);
        System.out.println("Application Result: " + applicationResult);
        System.out.println("Total Applications: " + student.getApplications().size());
        System.out.println();
        
        System.out.println("--- Checking Application Status ---");
        StudentApplication app = student.getApplicationDetails(internship.getId());
        if (app != null) {
            System.out.println("Application Status: " + app.getStatus());
            System.out.println("Student ID: " + app.getStudentId());
            System.out.println("Internship ID: " + app.getInternshipId());
        }
        System.out.println();
        
        System.out.println("--- Company Rep Approving Application ---");
        rep.processApplication(internship.getId(), student.getId(), true);
        System.out.println("Application Status After Approval: " + app.getStatus());
        System.out.println();
        
        System.out.println("--- Student Accepting Placement ---");
        boolean acceptResult = student.acceptInternshipPlacement(internship.getId());
        System.out.println("Placement Accepted: " + acceptResult);
        System.out.println("Accepted Internship ID: " + student.getAcceptedInternshipId());
        System.out.println();
        
        System.out.println("--- Testing Logout ---");
        student.logout();
        System.out.println("Logged In: " + student.isLoggedIn());
        System.out.println();
        
        System.out.println("========================================");
        System.out.println("Test Complete");
        System.out.println("========================================");
    }
}