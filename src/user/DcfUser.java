package user;

import config.Environment;
import soap.DetailedSOAPException;
import soap.Ping;

public class DcfUser implements IDcfUser {

	private String username;
	private String password;
	
	/**
	 * Login the user
	 * @param username
	 * @param password
	 */
	public void login(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	/**
	 * Login the user and check if credentials are correct
	 * @param username
	 * @param password
	 * @return
	 * @throws DetailedSOAPException 
	 */
	public boolean verifiedLogin(Environment env, String username, String password) throws DetailedSOAPException {
		
		this.login(username, password);

		boolean logged;

		// try a ping request to check credentials
		try {
			Ping request = new Ping();
			logged = request.ping(env, this);
		} catch (DetailedSOAPException e) {
			e.printStackTrace();
			
			if (e.isUnauthorized()) {
				logged = false;
			}
			else {
				throw e;
			}
		}
		

		// if wrong credential => remove them 
		if ( !logged ) {
			logout();
		}

		return logged;
	}
	
	public void logout() {
		this.username = null;
		this.password = null;
	}
	
	/**
	 * Get the saved dcf username
	 * @return
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Get the saved dcf password
	 * @return
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Check if the user is logged in
	 * @return
	 */
	public boolean isLoggedIn() {
		return username != null && password != null;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (!(obj instanceof IDcfUser))
			return super.equals(obj);
		
		return username.equals(((IDcfUser)obj).getUsername());
	}
	
	@Override
	public String toString() {
		return this.username;
	}
}
