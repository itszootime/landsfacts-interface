package org.uncertweb.landsfacts;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;
import org.uncertweb.landsfacts.data.CropAllocation;
import org.uncertweb.landsfacts.data.Project;
import org.uncertweb.landsfacts.test.TestData;

public class LandsfactsRunnerTest {
	
	@Test
	public void run() throws LandsfactsException {
		// load properties
		Properties properties = new Properties();
		try {
			FileInputStream in = new FileInputStream("src/test/resources/landsfacts.properties");
			properties.load(in);
		}
		catch (FileNotFoundException e) {
			System.err.println("Couldn't find landsfacts.properties");
			Assert.fail();
		}
		catch (IOException e) {
			System.err.println("Couldn't load landsfacts.properties");
			Assert.fail();
		}
		
		// create runner
		String path = properties.getProperty("executable.path");
		if (path != null) {
			LandsfactsRunner runner = new LandsfactsRunner(path);
			
			// create project
			Project project = new Project(TestData.getInitialTransitionMatrices(), TestData.getFieldDescriptions(), 3);
			List<CropAllocation> cropAllocations = runner.run(project);
			Assert.assertEquals(3, cropAllocations.size());
		}
		else {
			System.err.println("Missing executable.path property");
			Assert.fail();
		}
	}
	
}
