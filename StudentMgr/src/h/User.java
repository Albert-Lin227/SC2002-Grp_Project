package h;

public abstract class User {
	protected String username, password;
	protected static bool isLoggedIn = false;
	protected final int id;

	private String getUsername() {
		return username;
	}
	private String getPassword() {
		return password;
	}

	private int getId() {
		return id;
	}
	
	public void User(String username, String password, int id) {
		this.username = username;
		this.password = password;
		this.id = id;
	}

	public bool login(String username, String password) {
		if (isLoggedIn) {
			return false;
		}
		
		if (this.username.equals(username) && this.password.equals(password)) {
			isLoggedIn = true;
			return true;
		} else {
			return false;
		}
	}

	public void logout() {
		isLoggedIn = false;
	}
	public bool changePassword(String oldPassword, String newPassword) {
		if (this.password.equals(oldPassword)) {
			this.password = newPassword;
			return true;
		} else {
			return false;
		}
	}

}
