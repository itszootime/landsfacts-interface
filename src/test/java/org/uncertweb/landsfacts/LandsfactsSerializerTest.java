package org.uncertweb.landsfacts;

import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.uncertweb.landsfacts.data.Project;
import org.uncertweb.landsfacts.test.TestData;
import org.uncertweb.landsfacts.test.TestHelper;

public class LandsfactsSerializerTest {
	
	private static File baseDir;
	private static File inputDir;
	private static File outputDir;
	private static File sampleBaseDir = new File("src/test/resources/samplerun");
	private static File sampleInputDir = new File(sampleBaseDir, "inputs");
	
	@BeforeClass
	public static void setUp() {
		// id for test
		String testId = String.valueOf(System.currentTimeMillis());
		
		// create directories for landsfacts
		baseDir = new File(System.getProperty("java.io.tmpdir") + "landsfacts_test_" + testId);
		baseDir.mkdir();
		inputDir = new File(baseDir, "inputs");
		inputDir.mkdir();
		outputDir = new File(baseDir, "outputs");
		outputDir.mkdir();
	}
	
	@Test
	public void serialize() throws LandsfactsException {
		// create project
		Project project = new Project(TestData.getInitialTransitionMatrices(), TestData.getFieldDescriptions());
		
		// serialize
		LandsfactsSerializer serializer = new LandsfactsSerializer(project, inputDir);
		serializer.serialize();
	}
	
	@Test
	public void batch() {
		compareInputFile("Batch.txt");
	}
	
	@Test
	public void cidName() {
		compareInputFile("CIDName.txt");
	}
	
	@Test
	public void cidPr() {
		compareInputFile("CIDPr.txt");
	}
	
	@Test
	public void conditions() {
		compareInputFile("Conditions.txt");
	}
	
	@Test
	public void connectivity() {
		compareInputFile("Connectivity.txt");
	}
	
	@Test
	public void fa() {
		compareInputFile("FA.txt");
	}
	
	@Test
	public void fcgd() {
		compareInputFile("FCGD.txt");
	}
	
	@Test
	public void fieldsC() {
		compareInputFile("FieldsC.txt");
	}
	
	@Test
	public void gpCID() {
		compareInputFile("GpCID.txt");
	}
	
	@Test
	public void gpPlgID() {
		compareInputFile("GpPlgID.txt");
	}
	
	@Test
	public void icrf() {
		compareInputFile("ICRF.txt");
	}
	
	@Test
	public void itm() {
		compareInputFile("ITM.txt");
	}
	
	@Test
	public void itmc() {
		compareInputFile("ITMC.txt");
	}
	
	@Test
	public void itpara() {
		compareInputFile("Itpara.txt");
	}
	
	@Test
	public void lc() {
		compareInputFile("LC.txt");
	}
	
	@Test
	public void lcyf() {
		compareInputFile("LCYF.txt");
	}
	
	@Test
	public void linkedFC() {
		compareInputFile("LinkedFC.txt");
	}
	
	@Test
	public void ltptmcid() {
		// not implemented properly
		//compareInputFile("LTPTMCID.txt");
	}
	
	@Test
	public void neigh() {
		compareInputFile("Neigh.txt");
	}
	
	@Test
	public void outputFiles() {
		compareInputFile("OutputFiles.txt");
	}
	
	@Test
	public void pastAlloc() {
		compareInputFile("PastAlloc.txt");
	}
	
	@Test
	public void sepDist() {
		compareInputFile("SepDist.txt");
	}
	
	@Test
	public void simpara() {
		compareInputFile("Simpara.txt");
	}
	
	@Test
	public void tcr() {
		compareInputFile("TCR.txt");
	}
	
	@Test
	public void tcs() {
		compareInputFile("TCS.txt");
	}
	
	@Test
	public void universalCID() {
		compareInputFile("UniversalCID.txt");
	}
	
	@Test
	public void ycp() {
		compareInputFile("YCP.txt");
	}
	
	@AfterClass
	public static void tearDown() {
		// delete files
		for (File file : inputDir.listFiles()) {
			file.delete();
		}
		inputDir.delete();
		for (File file : outputDir.listFiles()) {
			file.delete();
		}
		outputDir.delete();
		baseDir.delete();
	}
	
	private void compareInputFile(String filename) {
		TestHelper.assertFileEquals(new File(sampleInputDir, filename), new File(inputDir, filename));
	}
	
}
