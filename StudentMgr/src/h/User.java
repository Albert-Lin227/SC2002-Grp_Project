package h;

public abstract class User {
    protected String username;
    protected String password;
    protected boolean isLoggedIn = false;
    protected final int id;

    public User(String username, String password, int id) {
        this.username = username;
        this.password = password;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

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

    public void logout() {
        isLoggedIn = false;
        System.out.println("Logout successful.");
    }

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