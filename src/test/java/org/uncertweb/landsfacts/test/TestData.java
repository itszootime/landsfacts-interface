package org.uncertweb.landsfacts.test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Ignore;
import org.uncertweb.landsfacts.data.FieldDescription;
import org.uncertweb.landsfacts.data.InitialTransitionMatrix;

import au.com.bytecode.opencsv.CSVReader;

@Ignore
public class TestData {

	private static List<FieldDescription> fieldDescriptions;
	private static List<InitialTransitionMatrix> initialTransitionMatrices;

	private static File inputsDir = new File("src/test/resources/samplerun/inputs");

	public static List<FieldDescription> getFieldDescriptions() {
		// only read if we haven't already
		if (fieldDescriptions == null) {
			// setup
			fieldDescriptions = new ArrayList<FieldDescription>();

			try {
				// field areas				
				Map<Integer, Double> areaMap = TestData.readFA();

				// soil
				Map<Integer, String> soilMap = TestData.readICRF();

				// create field
				for (int id : areaMap.keySet()) {
					fieldDescriptions.add(new FieldDescription(id, soilMap.get(id), areaMap.get(id)));
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}

		return fieldDescriptions;
	}

	private static Map<Integer, Double> readFA() throws IOException {
		Map<Integer, Double> areaMap = new TreeMap<Integer, Double>();		
		CSVReader r = new CSVReader(new FileReader(new File(inputsDir, "FA.txt")), '\t');
		r.readNext();
		String[] t;
		while ((t = r.readNext()) != null) {
			int id = Integer.parseInt(t[0]);
			double area = Double.parseDouble(t[1]);
			areaMap.put(id, area);
		}
		r.close();
		return areaMap;
	}

	private static Map<Integer, String> readICRF() throws IOException {
		Map<Integer, String> soilMap = readSoilMap();
		Map<Integer, String> fieldSoilMap = new TreeMap<Integer, String>();		
		CSVReader r = new CSVReader(new FileReader(new File(inputsDir, "ICRF.txt")), '\t');
		r.readNext();
		String[] t;
		while ((t = r.readNext()) != null) {
			int id = Integer.parseInt(t[0]);
			int soilId = Integer.parseInt(t[1]);
			fieldSoilMap.put(id, soilMap.get(soilId));
		}
		r.close();
		return fieldSoilMap;
	}

	public static List<InitialTransitionMatrix> getInitialTransitionMatrices() {
		if (initialTransitionMatrices == null) {
			// init
			initialTransitionMatrices = new ArrayList<InitialTransitionMatrix>();

			// we need some crop descriptions
			Map<Integer, String> soilMap = readSoilMap();
			Map<Integer, String> cropMap = readCropMap();	

			// read
			try {
				CSVReader r = new CSVReader(new FileReader(new File(inputsDir, "ITM.txt")), '\t');
				r.readNext();
				String[] t;
				List<Double> probabilities = new ArrayList<Double>();
				while ((t = r.readNext()) != null) {
					// get values
					int soilId = Integer.parseInt(t[0]);
					int crop1Id = Integer.parseInt(t[1]);
					int crop2Id = Integer.parseInt(t[2]);
					double probability = Double.parseDouble(t[3]);

					// check if we already have a matrix for this soil
					if ((soilId - 1) >= initialTransitionMatrices.size()) {
						// new matrix
						InitialTransitionMatrix itm = new InitialTransitionMatrix(soilMap.get(soilId));
						initialTransitionMatrices.add(itm);
					}

					// store probability
					probabilities.add(probability);
					
					// add if last crop					
					if ((crop2Id + 1) == cropMap.size()) {
						initialTransitionMatrices.get(soilId - 1).addRow(cropMap.get(crop1Id + 1), probabilities.toArray(new Double[0]));
						probabilities.clear();
					}
				}
				r.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return initialTransitionMatrices;
	}

	private static Map<Integer, String> readSoilMap() {
		Map<Integer, String> soilMap = new TreeMap<Integer, String>();
		// TODO: read this from ??
		soilMap.put(1, "Clayey");
		soilMap.put(2, "Loamy");
		soilMap.put(3, "Peaty");
		soilMap.put(4, "Sandy");
		return soilMap;
	}

	private static Map<Integer, String> readCropMap() {
		Map<Integer, String> cropMap = new TreeMap<Integer, String>();
		// TODO: read this from CIDName	
		cropMap.put(1, "BA1");
		cropMap.put(2, "BP");
		cropMap.put(3, "FH");
		cropMap.put(4, "FSA");
		cropMap.put(5, "FXLI");
		cropMap.put(6, "GR");
		cropMap.put(7, "OA1");
		cropMap.put(8, "OAR");
		cropMap.put(9, "OLA");
		cropMap.put(10, "OSR");
		cropMap.put(11, "PO1");
		cropMap.put(12, "SU1");
		cropMap.put(13, "VAS");
		cropMap.put(14, "WH");
		cropMap.put(15, "WOOD");
		return cropMap;
	}

}
