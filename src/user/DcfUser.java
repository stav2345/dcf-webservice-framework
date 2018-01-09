package user;

import javax.xml.soap.SOAPException;

import soap.MySOAPException;
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
	 * @throws MySOAPException 
	 */
	public boolean verifiedLogin(String username, String password) throws MySOAPException {
		
		this.login(username, password);

		boolean logged;

		// try a ping request to check credentials
		try {
			Ping request = new Ping(this);
			logged = request.ping();
		} catch (SOAPException e) {
			e.printStackTrace();
			
			MySOAPException exception = new MySOAPException(e);
			
			if (exception.isUnauthorized()) {
				logged = false;
			}
			else {
				throw exception;
			}
		}
		

		// if wrong credential => remove them 
		if ( !logged ) {
			this.username = null;
			this.password = null;
		}

		return logged;
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
}
