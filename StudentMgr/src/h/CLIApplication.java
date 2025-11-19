package h;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;
import java.util.stream.Collectors;

public class CLIApplication {

    private final Scanner scanner = new Scanner(System.in);
    // Simulating a database with lists
    private final List<Student> students = new ArrayList<>();
    private final List<CompanyRep> representatives = new ArrayList<>();
    private final List<CareerCenterStaff> staffMembers = new ArrayList<>();
    private Company company;

    public static void main(String[] args) {
        CLIApplication cli = new CLIApplication();
        cli.run();
    }

    public CLIApplication() {
        seedData();
    }

    private void seedData() {
        company = new Company("Tech Company");

        // 1. Setup Student Accounts
        // NOTE: These are the credentials you must use to login!
        Student alice = new Student("U1111111A", "alicepwd", 1, 3, Majors.CSC);
        Student bob = new Student("U2222222B", "bobpwd", 2, 2, Majors.BUS);
        students.add(alice);
        students.add(bob);

        // 2. Setup Company Representative Account
        CompanyRep rep = new CompanyRep("rep@tech.com", "repwd", 10,
                                        "rep1", "Lena Rep", companyName(), "Engineering", "Manager");
        representatives.add(rep);
        company.addRepresentative(rep);
        rep.setCompany(company); 

        // 3. Setup Career Center Staff Account
        CareerCenterStaff staff = new CareerCenterStaff("staff", "staffpwd", 100);
        staffMembers.add(staff);
        company.addCareerCenterStaff(staff);

        // 4. Authorize Representative (Staff Action)
        staff.authorizeCompanyRep(rep, true);

        // 5. Create and Approve a Sample Internship
        Date now = new Date();
        Date later = new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000);
        Internship sample = rep.createInternship(
                "Sample Software Intern",
                "Work with modern stacks.",
                InternshipLevel.INTERMEDIATE,
                Majors.CSC,
                now,
                later,
                2 
        );
        CareerCenterStaff.authorizeInternship(sample, true); 

        // 6. Toggle Visibility ON
        company.toggleInternshipVisibility(sample.getId());
        
        System.out.println("=== SYSTEM DATA SEEDED ===");
        System.out.println("Use these credentials to login:");
        System.out.println("1. Student: [Username: U1111111A, Password: alicepwd]");
        System.out.println("2. Rep:     [Username: rep@tech.com, Password: repwd]");
        System.out.println("3. Staff:   [Username: staff,     Password: staffpwd]");
        System.out.println("==========================\n");
    }

    private String companyName() {
        return "Tech Company";
    }

    public void run() {
        System.out.println("=== Internship Placement Management CLI ===");
        boolean running = true;
        while (running) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Student Portal");
            System.out.println("2. Company Representative Portal");
            System.out.println("3. Career Center Staff Portal");
            System.out.println("0. Exit");
            int choice = readInt("Select option: ");

            switch (choice) {
                case 1 -> handleStudentSession();
                case 2 -> handleRepSession();
                case 3 -> handleStaffSession();
                case 0 -> {
                    running = false;
                    System.out.println("Exiting application.");
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // ==========================================
    //       SESSION HANDLERS
    // ==========================================

    private void handleStudentSession() {
        System.out.println("\n[Student Login]");
        Student student = authenticateStudent();
        if (student == null) return; // Returns to main menu if auth fails

        boolean stay = true;
        while (stay && student.isLoggedIn()) {
            System.out.println("\nStudent Menu (Logged in as: " + student.getUsername() + ")");
            System.out.println("1. View available internships");
            System.out.println("2. Apply for internship");
            System.out.println("3. View my applications");
            System.out.println("4. Accept internship placement");
            System.out.println("5. Request withdrawal");
            System.out.println("6. Change Password");
            System.out.println("0. Logout");
            int choice = readInt("Select option: ");

            switch (choice) {
                case 1 -> viewAvailableInternships();
                case 2 -> applyForInternship(student);
                case 3 -> student.printApplications();
                case 4 -> acceptPlacement(student);
                case 5 -> requestWithdrawal(student);
                case 6 -> changePassword(student);
                case 0 -> {
                    student.logout();
                    stay = false;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void handleRepSession() {
        System.out.println("\n[Representative Login]");
        CompanyRep rep = authenticateRep();
        if (rep == null) return;
        
        if (!rep.isApproved()) {
            System.out.println("Your account is not yet approved by the Career Center Staff. Functionality is restricted.");
            rep.logout();
            return;
        }

        boolean stay = true;
        while (stay && rep.isLoggedIn()) {
            System.out.println("\nRepresentative Menu (Logged in as: " + rep.getUsername() + ")");
            System.out.println("1. Create internship posting");
            System.out.println("2. Toggle internship visibility");
            System.out.println("3. Process application (approve/reject)");
            System.out.println("4. List company internships");
            System.out.println("5. Change Password");
            System.out.println("0. Logout");
            int choice = readInt("Select option: ");
            
            switch (choice) {
                case 1 -> createInternship(rep);
                case 2 -> toggleVisibility(rep);
                case 3 -> processApplication(rep);
                case 4 -> listCompanyInternships();
                case 5 -> changePassword(rep);
                case 0 -> {
                    rep.logout();
                    stay = false;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void handleStaffSession() {
        System.out.println("\n[Staff Login]");
        CareerCenterStaff staff = authenticateStaff();
        if (staff == null) return;

        boolean stay = true;
        while (stay && staff.isLoggedIn()) {
            System.out.println("\nStaff Menu (Logged in as: " + staff.getUsername() + ")");
            System.out.println("1. Authorize Company Representative");
            System.out.println("2. Authorize Internship Posting (Approve/Reject)");
            System.out.println("3. Process Withdrawal Request");
            System.out.println("4. Generate Internship Report");
            System.out.println("5. Change Password");
            System.out.println("0. Logout");
            int choice = readInt("Select option: ");

            switch (choice) {
                case 1 -> authorizeRep(staff);
                case 2 -> authorizeInternship(staff);
                case 3 -> processWithdrawal(staff);
                case 4 -> generateReport(staff);
                case 5 -> changePassword(staff);
                case 0 -> {
                    staff.logout();
                    stay = false;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // ==========================================
    //       AUTHENTICATION LOGIC (Fixed)
    // ==========================================

    private Student authenticateStudent() {
        String username = readString("Username: ");
        String password = readString("Password: ");
        
        Student student = findStudent(username);
        
        if (student == null) {
            System.out.println("Error: Student username not found."); // Added explicit error
            return null;
        }
        
        if (student.login(username, password)) {
            return student;
        }
        // NOTE: student.login() already prints "Invalid password" if false
        return null;
    }

    private CompanyRep authenticateRep() {
        String username = readString("Username: ");
        String password = readString("Password: ");
        
        CompanyRep rep = findRep(username);
        
        if (rep == null) {
            System.out.println("Error: Representative username not found.");
            return null;
        }
        
        if (rep.login(username, password)) {
            return rep;
        }
        return null;
    }

    private CareerCenterStaff authenticateStaff() {
        String username = readString("Username: ");
        String password = readString("Password: ");
        
        CareerCenterStaff staff = findStaff(username);
        
        if (staff == null) {
            System.out.println("Error: Staff username not found.");
            return null;
        }
        
        if (staff.login(username, password)) {
            return staff;
        }
        return null;
    }
    
    private void changePassword(User user) {
        System.out.print("Old password: ");
        String oldPwd = scanner.nextLine();
        System.out.print("New password: ");
        String newPwd = scanner.nextLine();
        user.changePwd(oldPwd, newPwd);
    }
    
    // ==========================================
    //       STUDENT METHODS
    // ==========================================

    private void viewAvailableInternships() {
        System.out.println("\n--- Available Internships (Visible, Approved, Not Filled) ---");
        List<Internship> available = company.getInternships().stream()
            .filter(i -> i.getStatus().equals("Approved") && i.isVisible() && i.getFilledSlots() < i.getTotalSlots())
            .collect(Collectors.toList());

        if (available.isEmpty()) {
            System.out.println("No internships are currently available for application.");
        } else {
            available.forEach(i -> System.out.println(i.toString()));
        }
    }

    private void applyForInternship(Student student) {
        viewAvailableInternships();
        int internshipId = readInt("Enter ID of internship to apply for: ");
        Internship internship = company.getInternshipById(internshipId);

        if (internship == null) {
            System.out.println("Error: Internship not found.");
            return;
        }
        student.applyForInternship(internship);
    }

    private void acceptPlacement(Student student) {
        student.printApplications();
        int internshipId = readInt("Enter internship ID to accept: ");
        Internship internship = company.getInternshipById(internshipId);
        
        if (internship == null) {
            System.out.println("Error: Internship not found.");
            return;
        }

        boolean accepted = student.acceptInternshipPlacement(internshipId);
        if (accepted) {
            internship.incrementFilledSlots();
            System.out.println("Placement accepted. Internship ID " + internshipId + " slot count updated.");
        } else {
            System.out.println("Failed to accept placement. Check status.");
        }
    }

    private void requestWithdrawal(Student student) {
        student.printApplications();
        int internshipId = readInt("Enter internship ID to withdraw from: ");
        student.requestWithdrawal(internshipId);
    }

    // ==========================================
    //       REPRESENTATIVE METHODS
    // ==========================================

    private void createInternship(CompanyRep rep) {
        System.out.println("\n--- Create New Internship Posting ---");
        String title = readString("Title: ");
        String description = readString("Description: ");

        System.out.println("Available Majors: " + Arrays.toString(Majors.values()));
        Majors major = parseMajor(readString("Preferred Major (e.g., CSC): "));
        if (major == null) {
            System.out.println("Invalid major. Aborting.");
            return;
        }

        System.out.println("Available Levels: " + Arrays.toString(InternshipLevel.values()));
        InternshipLevel level = parseLevel(readString("Level (e.g., INTERMEDIATE): "));
        if (level == null) {
            System.out.println("Invalid level. Aborting.");
            return;
        }

        int slots = readInt("Number of total slots (1-10): ");
        Date openingDate = new Date();
        Date closingDate = new Date(System.currentTimeMillis() + 60L * 24 * 60 * 60 * 1000); 

        Internship internship = rep.createInternship(title, description, level, major, openingDate, closingDate, slots);
        
        if (internship != null) {
            System.out.println("Success! Internship created. ID: " + internship.getId() + ". Status: " + internship.getStatus());
        }
    }

    private void toggleVisibility(CompanyRep rep) {
        listCompanyInternships();
        if (rep.getInternship().isEmpty()) return;

        int internshipId = readInt("Enter ID of internship to toggle visibility: ");
        company.toggleInternshipVisibility(internshipId);
    }

    private void processApplication(CompanyRep rep) {
        listCompanyInternships();
        if (rep.getInternship().isEmpty()) return;

        int internshipId = readInt("Enter internship ID to view applications for: ");
        Internship internship = company.getInternshipById(internshipId);

        if (internship == null) {
            System.out.println("Error: Internship not found.");
            return;
        }
        
        System.out.println("\n--- Applications for Internship ID " + internshipId + " ---");
        boolean found = false;
        for (Student s : students) {
            StudentApplication app = s.getApplicationDetails(internshipId);
            if (app != null) {
                System.out.println("Student ID: " + s.getId() + " | Username: " + s.getUsername() + " | Status: " + app.getStatus());
                found = true;
            }
        }

        if (!found) {
            System.out.println("No applications found for this internship.");
            return;
        }

        int studentId = readInt("Enter Student ID to approve/reject: ");
        boolean approve = readBoolean("Approve application for Student ID " + studentId + "? (y/n): ");

        rep.processApplication(internshipId, studentId, approve);
    }

    private void listCompanyInternships() {
        System.out.println("\n--- All Company Internships ---");
        Vector<Internship> internships = company.getInternships();
        if (internships.isEmpty()) {
            System.out.println("No internships posted yet.");
            return;
        }
        internships.forEach(System.out::println);
    }

    // ==========================================
    //       STAFF METHODS
    // ==========================================

    private void authorizeRep(CareerCenterStaff staff) {
        System.out.println("\n--- Authorize Company Representative ---");
        System.out.println("Available Representatives (Approval Status):");
        representatives.forEach(rep -> System.out.println(rep.getUsername() + " (Approved: " + rep.isApproved() + ")"));
        
        String username = readString("Enter username of Rep to authorize/reject: ");
        CompanyRep rep = findRep(username);
        
        if (rep == null) {
            System.out.println("Error: Representative not found.");
            return;
        }

        boolean approve = readBoolean("Approve " + username + "? (y/n): ");
        staff.authorizeCompanyRep(rep, approve);
    }

    private void authorizeInternship(CareerCenterStaff staff) {
        System.out.println("\n--- Authorize Internship Posting ---");
        System.out.println("Internships (Status):");
        company.getInternships().forEach(i -> System.out.println(i.toString()));
        
        int internshipId = readInt("Enter ID of internship to authorize/reject: ");
        Internship internship = company.getInternshipById(internshipId);
        
        if (internship == null) {
            System.out.println("Error: Internship not found.");
            return;
        }
        
        boolean approve = readBoolean("Approve Internship ID " + internshipId + "? (y/n): ");
        CareerCenterStaff.authorizeInternship(internship, approve); 
        System.out.println("Internship status updated to: " + internship.getStatus());
    }

    private void processWithdrawal(CareerCenterStaff staff) {
        System.out.println("\n--- Process Student Withdrawal Request ---");
        int studentId = readInt("Enter ID of Student requesting withdrawal: ");
        Student student = students.stream().filter(s -> s.getId() == studentId).findFirst().orElse(null);
        if (student == null) {
            System.out.println("Error: Student ID not found.");
            return;
        }
        
        student.printApplications();
        int internshipId = readInt("Enter ID of internship for withdrawal: ");
        
        boolean approve = readBoolean("Approve withdrawal request for Student ID " + studentId + "? (y/n): ");
        staff.processWithdrawalRequest(student, internshipId, approve);
    }

    private void generateReport(CareerCenterStaff staff) {
        System.out.println("\n--- Generate Internship Report ---");
        
        String statusInput = readString("Filter status (e.g., Approved, Pending) or press Enter for ANY: ").trim();
        String statusFilter = statusInput.isEmpty() ? null : statusInput;
        
        String majorInput = readString("Filter major (e.g., CSC) or press Enter for ANY: ").trim();
        Majors majorFilter = majorInput.isEmpty() ? null : parseMajor(majorInput);
        
        String levelInput = readString("Filter level (e.g., INTERMEDIATE) or press Enter for ANY: ").trim();
        InternshipLevel levelFilter = levelInput.isEmpty() ? null : parseLevel(levelInput);

        List<Internship> allInternships = company.getInternships().stream().collect(Collectors.toList());
        
        staff.generateInternshipReport(allInternships, statusFilter, majorFilter, levelFilter);
    }

    // ==========================================
    //       UTILITY METHODS
    // ==========================================

    private String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private Majors parseMajor(String value) {
        try {
            return Majors.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private InternshipLevel parseLevel(String value) {
        try {
            return InternshipLevel.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private Student findStudent(String username) {
        return students.stream().filter(s -> s.getUsername().equalsIgnoreCase(username)).findFirst().orElse(null);
    }

    private CompanyRep findRep(String username) {
        return representatives.stream().filter(r -> r.getUsername().equalsIgnoreCase(username)).findFirst().orElse(null);
    }

    private CareerCenterStaff findStaff(String username) {
        return staffMembers.stream().filter(s -> s.getUsername().equalsIgnoreCase(username)).findFirst().orElse(null);
    }

    private int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private boolean readBoolean(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes")) return true;
            if (input.equals("n") || input.equals("no")) return false;
            System.out.println("Enter 'y' or 'n'.");
        }
    }
}