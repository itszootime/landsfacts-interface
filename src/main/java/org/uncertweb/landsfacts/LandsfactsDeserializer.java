package org.uncertweb.landsfacts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.uncertweb.landsfacts.data.CropAllocation;
import org.uncertweb.landsfacts.data.Project;

public class LandsfactsDeserializer {
	
	private Project project;
	private File outputDir;
	private Map<Integer, String> cropMapReverse;
	
	public LandsfactsDeserializer(Project project, File outputDir, Map<Integer, String> cropMapReverse) {
		this.project = project;
		this.outputDir = outputDir;
		this.cropMapReverse = cropMapReverse;
	}

	public List<CropAllocation> deserialize() throws LandsfactsException {
		// landsfacts only likes windows line endings
		LandsfactsUtilities.setWindowsLineSeparator();
		
		// build crop allocations
		List<CropAllocation> cropAllocationList = new ArrayList<CropAllocation>();
		try {
			for (int i = 0; i < project.getNumSimulations(); i++) {
				File allocationFile = new File(outputDir, "log_FinalCropAllocID_" + i + ".txt");
				BufferedReader allocationReader = new BufferedReader(new FileReader(allocationFile));
				
				// discard header
				allocationReader.readLine(); 
			
				// parse allocation
				CropAllocation cropAllocation = new CropAllocation();
				String allocLine;
				while ((allocLine = allocationReader.readLine()) != null) {
					if (!allocLine.isEmpty()) {
						String[] tokens = allocLine.split("\t");
						String[] crops = {
								cropMapReverse.get(Integer.parseInt(tokens[1])),
								cropMapReverse.get(Integer.parseInt(tokens[2])),
								cropMapReverse.get(Integer.parseInt(tokens[3])),
								cropMapReverse.get(Integer.parseInt(tokens[4])),
								cropMapReverse.get(Integer.parseInt(tokens[5]))
						};
						cropAllocation.addAllocation(Integer.parseInt(tokens[0]), crops);
					}
				}
				allocationReader.close();
				
				// add to list
				cropAllocationList.add(cropAllocation);
			}
			
			return cropAllocationList;
		}
		catch (IOException e) {
			String errorMessage;
			try {
				errorMessage = readError();
			}
			catch (IOException e2) {
				errorMessage = "Couldn't read output files.";
			}
			throw new LandsfactsException(errorMessage);
		}
		finally {
			// reset to original line separators
			LandsfactsUtilities.resetLineSeparator();
		}
	}
	
	private String readError() throws IOException {
		File errorFile = new File(outputDir, "log_ErrorLog.txt");
		BufferedReader errorReader = new BufferedReader(new FileReader(errorFile));
		StringBuilder errorBuilder = new StringBuilder();
		String errorLine;
		while ((errorLine = errorReader.readLine()) != null) {
			// FIXME: this will still be windows \r\n as we are overriding it
			errorBuilder.append(errorLine + System.getProperty("line.separator"));
		}
		errorReader.close();
		return errorBuilder.toString();
	}
	
}
