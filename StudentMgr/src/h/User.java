package h;

public abstract class User {
	protected String username, password;
	protected boolean isLoggedIn = false; //fixed boolean statement + removed static keyword for instance-level login behavior
	protected final int id;

	public String getUsername() { //getter methods should be public/protected if access needed~
		return username;
	}
	public String getPassword() {
		return password;
	}

	public int getId() {
		return id;
	}
	
	public User(String username, String password, int id) { //fixed constructor
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
