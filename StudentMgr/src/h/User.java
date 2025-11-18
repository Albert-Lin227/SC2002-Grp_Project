package h;

/**
 * Abstract base class for all users in the system (Student, CompanyRep, CareerCenterStaff).
 * Handles core authentication and credential management.
 */
public abstract class User {
    protected String username;
    protected String password;
    protected boolean isLoggedIn = false;
    protected final int id;

    /**
     * Constructs a User object.
     * @param username The unique username for the user.
     * @param password The user's password.
     * @param id The unique system ID for the user.
     */
    public User(String username, String password, int id) {
        this.username = username;
        this.password = password;
        this.id = id;
    }

    /**
     * Gets the username of the user.
     * @return The username string.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the unique ID of the user.
     * @return The user's ID integer.
     */
    public int getId() {
        return id;
    }

    /**
     * Checks if the user is currently logged in.
     * @return true if logged in, false otherwise.
     */
    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    /**
     * Authenticates the user with the provided credentials.
     * If the user is already logged in, it returns false.
     * @param username The username to check against.
     * @param password The password to check against.
     * @return true if login is successful, false otherwise.
     */
    public boolean login(String username, String password) {
        if (isLoggedIn) {
            System.out.println("Error: User is already logged in.");
            return false;
        }

        if (this.username.equals(username) && this.password.equals(password)) {
            isLoggedIn = true;
            System.out.println("Login successful.");
            return true;
        } else {
            System.out.println("Error: Invalid username or password.");
            return false;
        }
    }

    /**
     * Logs the user out by setting the logged-in status to false.
     */
    public void logout() {
        isLoggedIn = false;
        System.out.println("Logout successful.");
    }

    /**
     * Allows the user to change their password.
     * If successful, the user is automatically logged out.
     * @param oldPassword The current password for verification.
     * @param newPassword The new password to set.
     * @return true if the password was changed successfully, false otherwise.
     */
    public boolean changePwd(String oldPassword, String newPassword) {
        if (!this.password.equals(oldPassword)) {
            System.out.println("Error: Incorrect old password.");
            return false;
        }

        if (newPassword == null || newPassword.isEmpty()) {
            System.out.println("Error: New password cannot be empty.");
            return false;
        }

        this.password = newPassword;
        System.out.println("Password changed successfully. Please log in again.");
        isLoggedIn = false;
        return true;
    }
}