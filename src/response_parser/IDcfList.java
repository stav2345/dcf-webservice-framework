package response_parser;

public interface IDcfList<T> {

	/**
	 * Add a new element to the list
	 * @param dataset
	 */
	public boolean addElem(T elem);
	
	/**
	 * Create a new instance of an element
	 */
	public T create();
}
