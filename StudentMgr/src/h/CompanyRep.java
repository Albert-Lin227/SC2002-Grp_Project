package h;
public class CompanyRep extends User{
	
	private String userId;
    private String name;
    private List<Internship> internships;
    private String companyName;
    private boolean isApproved;
    
    
    public CompanyRep(String username, Stirng password, int id, String userId, String name, String companyName) {
    	super(username, password, id);
    	this.userId = userId;
        this.name = name;
        this.companyName = companyName;
        this.internships = new ArrayList<>();
        this.isApproved = false;
    }
    
    //the code below is for the methods
    
    @Override
    public boolean login(String username, String password) {
        return super.login(username, password);
    }

    @Override
    public void logout() {
        super.logout();
    }

    public boolean changePwd(String oldPassword, String newPassword) {
        return super.changePassword(oldPassword, newPassword);
    }

    public void recoverPwd(String newPassword) {
        this.password = newPassword; // simplistic recovery
    }

    public void addInternship(Internship internship) {
        internships.add(internship);
    }

    public void approveInternship(Internship internship) {
        internship.setStatus("APPROVED");
    }

    public void rejectInternship(Internship internship) {
        internship.setStatus("REJECTED");
    }

    public List<Internship> getPostedInternships() {
        return internships;
    }

    public List<Application> getApplicationsByInternship(Internship internship) {
        return internship.getApplications();
    }

    // the code below is for the getters
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getCompanyName() { return companyName; }
    public boolean isApproved() { return isApproved; }
    public void setApproved(boolean approved) { this.isApproved = approved; }
	
	
	
	
	public User()
}
