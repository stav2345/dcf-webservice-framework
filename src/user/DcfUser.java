package user;

import javax.xml.soap.SOAPException;

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
			Ping request = new Ping(this, env);
			logged = request.ping();
		} catch (SOAPException e) {
			e.printStackTrace();
			
			DetailedSOAPException exception = new DetailedSOAPException(e);
			
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
