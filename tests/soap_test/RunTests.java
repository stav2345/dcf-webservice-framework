package soap_test;

import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import soap_test.WebserviceTest.DownloadLogTests;

// run only a subset of tests
@RunWith(Categories.class)
@IncludeCategory(DownloadLogTests.class)
@SuiteClasses( { WebserviceTest.class })
public class RunTests {

}
