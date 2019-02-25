<<<<<<< HEAD
package user;

import config.Environment;
import soap.DetailedSOAPException;
import soap.Ping;

public class DcfUser implements IDcfUser {

	private String username;
	private String password;
	
	/**
	 * Login the user
	 * @param username1
	 * @param password1
	 */
	public void login(String username1, String password1) {
		this.username = username1;
		this.password = password1;
	}
	
	/**
	 * Login the user and check if credentials are correct
	 * @param username1
	 * @param password1
	 * @return
	 * @throws DetailedSOAPException 
	 */
	public boolean verifiedLogin(Environment env, String username1, String password1) throws DetailedSOAPException {
		
		this.login(username1, password1);

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
	@Override
	public String getUsername() {
		return this.username;
	}
	
	/**
	 * Get the saved dcf password
	 * @return
	 */
	@Override
	public String getPassword() {
		return this.password;
	}
	
	/**
	 * Check if the user is logged in
	 * @return
	 */
	public boolean isLoggedIn() {
		return this.username != null && this.password != null;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (!(obj instanceof IDcfUser))
			return super.equals(obj);
		
		return this.username.equals(((IDcfUser)obj).getUsername());
	}
	
	@Override
	public String toString() {
		return this.username;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
}
=======
package user;

import config.Environment;
import soap.DetailedSOAPException;
import soap.Ping;

public class DcfUser implements IDcfUser {

	private String username;
	private String password;
	private String token;
	
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
	 * Login the user with the open api token
	 * @param token
	 */
	public void login(String token) {
		this.token = token;
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
	
	@Override
	public String getToken() {
		return token;
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
>>>>>>> 5db4cda62aab4fbc1c8dbec8998c82e8bc1d4475
