<<<<<<< HEAD
package user;

public interface IDcfUser {

	/**
	 * Get the user name of the user
	 * @return
	 */
	public String getUsername();
	
	/**
	 * Get the password of the user
	 * @return
	 */
	public String getPassword();
}
=======
package user;

public interface IDcfUser {

	/**
	 * Get the user name of the user
	 * @return
	 */
	public String getUsername();
	
	/**
	 * Get the password of the user
	 * @return
	 */
	public String getPassword();
	
	/**
	 * Get the open API token identifying 
	 * an user without username and password
	 * @return
	 */
	public String getToken();
}
>>>>>>> 5db4cda62aab4fbc1c8dbec8998c82e8bc1d4475
