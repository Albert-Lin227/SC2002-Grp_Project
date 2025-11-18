package h;

import java.util.Date;

public class CompanyRep extends User {
    
    private String name;
    private String companyName;
    private String email;
    private boolean isApproved;
    private Company company; 

    public CompanyRep(String username, String password, int id, String userId, 
                     String name, String companyName, String department, String position) {
        super(username, password, id);
        this.name = name;
        this.companyName = companyName;
        this.isApproved = false;
        this.email = username + "@" + companyName.replaceAll(" ", "").toLowerCase() + ".com";
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public boolean login(String username, String password) {
        if (!isApproved) {
            System.out.println("Error: Your account has not been approved by Career Center Staff.");
            return false;
        }
        return super.login(username, password);
    }

    public String getName() {
        return name;
    }
    
    public String getCompanyName() {
        return companyName;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean isApproved) {
        this.isApproved = isApproved;
    }

    public Internship createInternship(String title, String description, InternshipLevel level,
                                     Majors preferredMajor, Date openingDate, Date closingDate,
                                     int slots) {
        if (!isApproved) {
            System.out.println("Error: Account not approved. Cannot create internship.");
            return null;
        }
        
        if (company == null) {
            System.out.println("Error: Representative is not linked to a company.");
            return null;
        }
        
        return company.createInternship(title, description, level, preferredMajor,
                                        openingDate, closingDate, this.username, slots);
    }
    
    public boolean processApplication(int internshipId, int studentId, boolean isApproved) {
        if (company == null) return false;
        return company.processApplication(internshipId, studentId, isApproved);
    }

    public boolean toggleInternshipVisibility(int internshipId) {
        if (company == null) return false;
        return company.toggleInternshipVisibility(internshipId);
    }
}