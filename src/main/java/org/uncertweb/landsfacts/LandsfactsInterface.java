package org.uncertweb.landsfacts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncertweb.landsfacts.data.CropAllocation;
import org.uncertweb.landsfacts.data.Project;

public class LandsfactsInterface {

	private final Logger logger = LoggerFactory.getLogger(LandsfactsInterface.class);
	private String executablePath;

	public LandsfactsInterface(String executablePath) {
		this.executablePath = executablePath;
	}

	public List<CropAllocation> run(Project project) throws LandsfactsException {
		// need an id for a run
		String runId = String.valueOf(System.currentTimeMillis());

		// create directories for landsfacts
		File baseDir = new File(System.getProperty("java.io.tmpdir") + "landsfacts_run_" + runId);
		baseDir.mkdir();
		logger.debug("Temporary folder path is " + baseDir.getAbsolutePath() + ".");
		File inputDir = new File(baseDir, "inputs");
		inputDir.mkdir();
		File outputDir = new File(baseDir, "outputs");
		outputDir.mkdir();

		try {
			// write input files
			LandsfactsSerializer serializer = new LandsfactsSerializer(project, inputDir);
			serializer.serialize();

			// run
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec(executablePath + " " + baseDir.getPath());

			// TODO: lots of reliability checks we can do here
			BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while (inputReader.readLine() != null) {
				// ignore
			}			

			// deserialize outputs
			LandsfactsDeserializer deserializer = new LandsfactsDeserializer(project, outputDir, serializer.getCropMapReverse());
			List<CropAllocation> cropAllocationList = deserializer.deserialize();
			return cropAllocationList;
		}
		catch (FileNotFoundException e) {
			throw new LandsfactsException("Error writing input files, or reading output files.");
		}
		catch (IOException e) {
			throw new LandsfactsException("Error executing external program.");
		}
		finally {
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
			if (baseDir.exists()) {
				logger.warn("Some input/output files were not removed, directory '" + baseDir.getName() + "' still exists.");
			}
			else {
				logger.info("Input/output files removed.");
			}
		}
	}

}
