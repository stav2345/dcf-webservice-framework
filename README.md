# DCF Webservice Framework

This is a Java library which can be used to call the Data Collection Framework web services and retrieve their responses with standardized Java objects. These objects provide several built-in useful functionalities; they can also be extended to add custom methods for the specific business logic required.

In particular, all the web services inherit from the SOAPRequest object, which handles the SOAP connection.
This class uses the DcfUser class to retrieve the account credentials.

## How to extends the DcfUser class
If your application needs an extended concept of 'user', you can extend the DcfUser class to still be able to call the dcf web services maintaining your implementation. In particular, make sure that your class either inherits from the DcfUser class, or implements the IDcfUser interface.

	public class CustomUser extends DcfUser

### How to use the DcfUser class

	DcfUser user = new DcfUser();
		
	user.login(username, password);

	// call the web services here passing the user reference


If you want you can extend the DcfUser class in order to make it a singleton. This has the advantage that the user credentials can be accessed everywhere in your application. Use this approach only if only one user at a time is active in the local application.

## How to extend a DCF class (a class which contains the result of a web service call)

### Simple objects
It is sufficient to extend the base class provided by the library. While calling a web service, you will be asked to provide the output object which will be filled with the web service information. This object must implement the related interface or extend the base class.

For example, image we want to extend the base class for Dcf datasets:

	public class CustomDataset extends DcfDataset

In this class we can define whatever custom method. More importantly we can use this class for the web service calls.

If you want to provide a complete new implementation, then you can simply implements the IDcfDataset interface in your custom dataset object:

	public class DcfDataset implements IDcfDataset

### Lists
Lists are more complicated. In particular, it is necessary to implement a method which instantiate the objects of the lists and add them to the list (addElem and create methods).
The custom list should either implement the IDcfList interface or extend the base class provided by the library. In your custom list you will need to implement the addElem and create methods, in order to specify which are your custom elements of the list.

For example, image we need to create a custom list for storing a IDcfDataset list (IDcfDataset is the interface for the datasets objects):

	public class CustomDatasetList extends ArrayList<IDcfDataset> implements IDcfDatasetList

As you can see we extend the ArrayList class to make our class a list and then we implement the IDcfDatasetList interface in order to be able to use the list in the Dcf web services methods.

The interface will require to implement the following methods:

	* public IDcfDataset create()
	* public boolean addElem(IDcfDataset elem)

Since we want a list of custom objects, we need to specify their type. Therefore, in the create method we will return our custom implementation of the IDcfDataset interface:

	
	@Override
	public IDcfDataset create() {
		return new CustomDcfDataset();
	}

And since we are working with a simple list, we can simply implement the addElem function with the super.add function of the array list.

	@Override
	public boolean addElem(IDcfDataset elem) {
		return super.add(elem);
	}

In this way it is possible to use the list in the web services call. For example, for the GetDatasetList call it will be:


	CustomDatasetList output = new CustomDatasetList();
	
	// the user is not specified here, but it is an object which implements IDcfuser
	GetDataCollectionsList request = new GetDataCollectionsList(user, output);
	request.getList();  // fill the output list
	
	// here the list contains all the data retrieved from the web service
	output.get(0).myCustomMethod();



