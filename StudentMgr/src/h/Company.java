package h;

import java.util.Date;
import java.util.Vector;

public class Company {
    
    private String companyName;
    private String email;
    private String phone;
    private String address;
    private Vector<Internship> internships;
    private Vector<CompanyRep> representatives;
    private static final int MAX_INTERNSHIPS = 5;

    public Company(String companyName, String email, String phone, String address) {
        this.companyName = companyName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.internships = new Vector<>();
        this.representatives = new Vector<>();
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public boolean addRepresentative(CompanyRep representative) {
        if (representative != null && !representatives.contains(representative)) {
            representatives.add(representative);
            return true;
        }
        return false;
    }

    public Vector<CompanyRep> getRepresentatives() {
        return representatives;
    }

    public Internship createInternship(String title, 
                                        String description, 
                                        InternshipLevel level,
                                        Majors preferredMajor, 
                                        Date openingDate, 
                                        Date closingDate,
                                        String representativeUsername, 
                                        int slots) {
        if (internships.size() >= MAX_INTERNSHIPS) {
            System.out.println("Error: Maximum number of internships (5) reached.");
            return null;
        }

        if (slots > 10 || slots < 1) {
            System.out.println("Error: Number of slots must be between 1 and 10.");
            return null;
        }

        Internship internship = new Internship(
            title, description, level, preferredMajor,
            openingDate, closingDate, companyName, representativeUsername, slots
        );
        internships.add(internship);
        return internship;
    }

    public Vector<Internship> getInternships() {
        return internships;
    }

    public Internship getInternshipById(int internshipId) {
        for (Internship internship : internships) {
            if (internship.getId() == internshipId) {
                return internship;
            }
        }
        return null;
    }

    public boolean toggleInternshipVisibility(int internshipId) {
        Internship internship = getInternshipById(internshipId);
        if (internship != null) {
            internship.toggleVisibility();
            return true;
        }
        return false;
    }

    public boolean processApplication(int internshipId, int studentId, boolean isApproved) {
        Internship internship = getInternshipById(internshipId);
        if (internship != null) {
            internship.processApplication(studentId, isApproved);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Company{" +
                "companyName='" + companyName + '\'' +
                ", email='" + email + '\'' +
                ", internships=" + internships.size() +
                ", representatives=" + representatives.size() +
                '}';
    }
}