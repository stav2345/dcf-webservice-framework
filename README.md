# DCF Webservice Framework

This is a Java library which can be used to call the Data Collection Framework web services and retrieve their responses with standardized Java objects. These objects provide several built-in useful functionalities; they can also be extended to add custom methods for the specific business logic required.

In particular, all the web services inherit from the SOAPRequest object, which handles the SOAP connection.
This class uses the DcfUser class to retrieve the account credentials.

# General use
The procedure to call a web service is always the same:
1. Create an object which implements the IDcfUser interface (or extends the DcfUser class) that contains the credentials of the DCF user
		
		IDcfUser user = new DcfUser();  // default implementation provided by the library
		user.login("myUsername", "myPassword");
		
2. Create the output object (i.e. an object where all the data retrieved from the web service are saved). This object must implements the interface defined in the constructor signature of the web service object called, e.g., for the GetDatasetsList request it is necessary to provide an object which implements the IDcfDatasetsList interface (or extends the DcfDatasetsList class).

		IDcfDatasetsList output = new DcfDatasetsList();  // default implementation provided by the library
		
3. Instantiate the object related to the required web service
		
		GetDatasetsList<IDcfDataset> req = new GetDatasetsList<>(user, outputObject);
		
	Note that in this case, since we are dealing with a **List** object, it is needed to specify the type of the single object of 		the list. For the GetDatasetsList, the type of the object must implement the IDcfDataset interface. This is useful while dealing 	with custom implementations of the IDcfDataset interface, because it allows to use customized objects as input/output of the web 	service call.

4. Send the request to DCF

		req.getList();  // this will fill the output object
		
5. Access the data retrieved using the reference to the output object

		System.out.println(output);
		if (!output.isEmpty()) {
			System.out.println(output.get(0));
		}

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

	public class CustomDataset extends DcfDataset {
		public void myCustomMethod() {
			...
		}
	}

In this class we can define whatever custom method. Most importantly we can use this class for the web service calls, in order to fill it with the web service information.

If you want to provide a complete new implementation, then you can simply implement the IDcfDataset interface in your custom dataset object:

	public class DcfDataset implements IDcfDataset {
		public void myCustomMethod() {
			...
		}
	}
	
However, note that this approach will not provide you any already created method which is present in the DcfDataset class.

### Lists
Lists are more complicated. In particular, it is necessary to implement a method which instantiate the objects of the lists and add them to the list (add and create methods).
The custom list should either implement the **IDcfList** interface or extend the base class provided by the library, as for the objects. In your custom list you will need to implement the add and create methods, in order to specify which are your custom elements of the list.

For example, image we need to create a custom list for storing a IDcfDataset list (IDcfDataset is the interface for the datasets objects):

	public class CustomDatasetList extends ArrayList<IDcfDataset> implements IDcfDatasetList

As you can see we extend the ArrayList class to make our class indeed a list and then we implement the IDcfDatasetList interface in order to be able to use the list as input/output of the Dcf web services methods.

The interface will require to implement the following methods:

	* public IDcfDataset create()
	* public boolean add(IDcfDataset elem)

Since we want a list of custom objects, we need to specify their type (this was done to decouple the web service class from the implementation of the input/output objects). Therefore, in the create method we will return our custom implementation of the IDcfDataset interface:

	
	@Override
	public IDcfDataset create() {
		return new CustomDcfDataset();
	}

And since we extended the ArrayList class, the **add method is already implemented inside it.**

In this way it is possible to use the list in the web services call. For example, for the GetDatasetList call it will be:

	CustomDatasetList output = new CustomDatasetList();
	
	// the user is not specified here, but it is an object which implements IDcfuser
	GetDataCollectionsList<IDcfDataCollection> request = new GetDataCollectionsList<>(user, output);
	request.getList();  // fill the output list
	
	// here the list contains all the data retrieved from the web service
	// and we can access them and also call our custom methods defined in
	// our customized class
	output.get(0).myCustomMethod();

# Dependencies
The project needs the following projects to work properly:
* https://github.com/openefsa/zip-manager
* https://github.com/openefsa/http-manager

