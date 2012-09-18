package org.uncertweb.landsfacts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.uncertweb.landsfacts.data.FieldDescription;
import org.uncertweb.landsfacts.data.InitialTransitionMatrix;
import org.uncertweb.landsfacts.data.Project;

public class LandsfactsSerializer {

	private Project project;
	private File outputDir;

	private TreeMap<String, Integer> cropMap;
	private TreeMap<Integer, String> cropMapReverse;
	private TreeMap<String, Integer> soilMap;
	private TreeMap<Integer, String> soilMapReverse;
	
	private DecimalFormat format;

	public LandsfactsSerializer(Project project, File outputDir) {
		this.project = project;
		this.outputDir = outputDir;
		generateCropMaps();
		generateSoilMaps();
		
		// requied to stop exponential
		format = new DecimalFormat("#0.#", new DecimalFormatSymbols());
		format.setMaximumFractionDigits(10);
	}

	public void serialize() throws LandsfactsException {
		// landsfacts only likes windows line endings
		LandsfactsUtilities.setWindowsLineSeparator();

		// write
		try {
			writeBatch();
			writeCIDName();
			writeCIDPr();
			writeFCGD();
			writeGpCID();
			writeLC();
			writeCondition();
			writeConnectivity();
			writeFieldsC();
			writeGpPlgID();
			writeITMC();
			writeItpara();
			writeLCYF();
			writeLinkedFC();
			writeLTPTMCID();
			writeNeigh();
			writeOutputFiles();
			writePastAlloc();
			writeSepDist();
			writeSimpara();
			writeTCR();
			writeTCS();
			writeUniversalCID();
			writeYCP();
			writeFA();
			writeICRF();
			writeITM();
		}
		catch (FileNotFoundException e) {
			throw new LandsfactsException("Couldn't write input files.");
		}

		// reset to original line separators
		LandsfactsUtilities.resetLineSeparator();
	}

	private void writeBatch() throws FileNotFoundException {
		// output Batch.txt
		File batchFile = new File(outputDir, "Batch.txt");
		PrintWriter batchWriter = new PrintWriter(batchFile);
		batchWriter.println("NbSimulation\tUniqueSeed\tTimeBaseSeed\tSpecificSeed\tUsedSeed");
		batchWriter.println(project.getNumSimulations() + "\t0\t1\t0\t0");
		batchWriter.close();
	}

	private void writeCIDName() throws FileNotFoundException {
		// CIDName.txt
		File cidnameFile = new File(outputDir, "CIDName.txt");
		PrintWriter cidnameWriter = new PrintWriter(cidnameFile);
		cidnameWriter.println("CID\tName");
		for (String category : cropMap.keySet()) {
			int cid = cropMap.get(category);
			cidnameWriter.println(cid + "\t" + category);
		}
		cidnameWriter.close();
	}

	private void writeCIDPr() throws FileNotFoundException {
		// CIDPr.txt (priorities for crops)
		File cidprFile = new File(outputDir, "CIDPr.txt");
		PrintWriter cidprWriter = new PrintWriter(cidprFile);
		cidprWriter.println("CID\tCropPriorities");
		for (String category : cropMap.keySet()) {
			int cid = cropMap.get(category);
			cidprWriter.println(cid + "\t-1");
		}
		cidprWriter.close();
	}

	private void writeFCGD() throws FileNotFoundException {
		// FCGD.txt (crop grouping)
		File fcgdFile = new File(outputDir, "FCGD.txt");
		PrintWriter fcgdWriter = new PrintWriter(fcgdFile);
		fcgdWriter.println("FunctGp\tCID");
		for (String category : cropMap.keySet()) {
			int cid = cropMap.get(category);
			fcgdWriter.println(cid + "\t" + cid);
		}
		fcgdWriter.close();
	}

	private void writeGpCID() throws FileNotFoundException {
		// GpCID.txt
		File gpcidFile = new File(outputDir, "GpCID.txt");
		PrintWriter gpcidWriter = new PrintWriter(gpcidFile);
		gpcidWriter.println("GPCID\tCID");
		for (String category : cropMap.keySet()) {
			int cid = cropMap.get(category);
			gpcidWriter.println(cid + "\t" + cid);
		}
		gpcidWriter.close();
	}

	private void writeLC() throws FileNotFoundException {
		// LC.txt
		File lcFile = new File(outputDir, "LC.txt");
		PrintWriter lcWriter = new PrintWriter(lcFile);
		lcWriter.println("LandCapaID\tCID");
		for (String category : cropMap.keySet()) {
			int cid = cropMap.get(category);
			lcWriter.println("-1\t" + cid);
		}
		lcWriter.close();
	}

	private void writeCondition() throws FileNotFoundException {
		// output Conditions.txt
		File conditionsFile = new File(outputDir, "Conditions.txt");
		PrintWriter conditionsWriter = new PrintWriter(conditionsFile);
		conditionsWriter.println("ConditionID\tConditionType\tToSimulate\tFailed");
		conditionsWriter.close();
	}

	private void writeConnectivity() throws FileNotFoundException {
		// output Connectivity.txt
		File connectivityFile = new File(outputDir, "Connectivity.txt");
		PrintWriter connectivityWriter = new PrintWriter(connectivityFile);
		connectivityWriter.println("ConditionID\tDistance\tGpCID\tPercMin\tPercMax\tNbGpmin\tNbGpmax\tCompactMin\tCompactMax");
		connectivityWriter.close();
	}

	private void writeFieldsC() throws FileNotFoundException {
		// output FieldsC.txt
		File fieldscFile = new File(outputDir, "FieldsC.txt");
		PrintWriter fieldscWriter = new PrintWriter(fieldscFile);
		fieldscWriter.println("PlgID\tConditionID");
		fieldscWriter.close();
	}

	private void writeGpPlgID() throws FileNotFoundException {
		// output GpPlgID.txt
		File gpplgFile = new File(outputDir, "GpPlgID.txt");
		PrintWriter gpplgWriter = new PrintWriter(gpplgFile);
		gpplgWriter.println("GpPlgID\tPlgID");
		gpplgWriter.close();
	}

	private void writeITMC() throws FileNotFoundException {
		// output ITMC.txt
		// our TMCIDs are just C(rop)IDs - 1
		File itmcFile = new File(outputDir, "ITMC.txt");
		PrintWriter itmcWriter = new PrintWriter(itmcFile);
		itmcWriter.println("CRID\tTMCID\tCID");
		for (int soilId : soilMapReverse.keySet()) {
			for (String crop : cropMap.keySet()) {
				int cropId = cropMap.get(crop);
				itmcWriter.println(soilId + "\t" + (cropId - 1) + "\t" + cropId);
			}
		}
		itmcWriter.close();
	}

	private void writeItpara() throws FileNotFoundException {
		// output Itpara.txt
		File itparaFile = new File(outputDir, "Itpara.txt");
		PrintWriter itparaWriter = new PrintWriter(itparaFile);
		itparaWriter.println("AllRdmx\tAllRdpenal\tRdmx\tRdpenal\tRdinTMmx\tRdinTMpenal\tCidGpmx\tCidGppenal\tUnivCidmx\tUnivCidpenal\tSimAnnealing");
		itparaWriter.println("100\t0\t1000\t0\t0\t0\t0\t0\t0\t0\t20");
		itparaWriter.close();
	}

	private void writeLCYF() throws FileNotFoundException {
		// output LCYF.txt
		File lcyfFile = new File(outputDir, "LCYF.txt");
		PrintWriter lcyfWriter = new PrintWriter(lcyfFile);
		lcyfWriter.println("Years\tPlgID\tLandCapaID");
		lcyfWriter.close();
	}

	private void writeLinkedFC() throws FileNotFoundException {
		// output LinkedFC.txt
		File linkedfcFile = new File(outputDir, "LinkedFC.txt");
		PrintWriter linkedfcWriter = new PrintWriter(linkedfcFile);
		linkedfcWriter.println("ConditionID\tGpPlgID\tGpCID\tYears");
		linkedfcWriter.close();
	}

	private void writeLTPTMCID() throws FileNotFoundException {
		// output LTPTMCID.txt
		// FIXME: do this properly
		File ltptmcidFile = new File(outputDir, "LTPTMCID.txt");
		PrintWriter ltptmcidWriter = new PrintWriter(ltptmcidFile);
		ltptmcidWriter.println("CRID\tTMCID\tProporti");
		for (int soilId : soilMapReverse.keySet()) {
			InitialTransitionMatrix itm = project.getInitialTransitionMatrices().get(soilId - 1);
			int numCrops = itm.getCrops().size();
			//double prob = Math.round((1.0 / numCrops) * Math.pow(10, 5)) / Math.pow(10, 5);
			double prob = 1.0 / numCrops;
			//double[][] ltptMatrix = matrix.getMatrix();
			//for (int n = 1; n < 10; n++) {
			//	ltptMatrix = multiplyMatrix(ltptMatrix, matrix.getMatrix());
			//}
			for (String category : itm.getCrops()) {
				int cropId = cropMap.get(category);
				ltptmcidWriter.println(soilId + "\t" + (cropId - 1) + "\t" + prob);
			}

		}
		ltptmcidWriter.close();
	}

	private void writeNeigh() throws FileNotFoundException {
		// output Neigh.txt
		File neighFile = new File(outputDir, "Neigh.txt");
		PrintWriter neighWriter = new PrintWriter(neighFile);
		neighWriter.println("PlgIDa\tPlgIDb\tDistance");
		neighWriter.close();
	}

	private void writeOutputFiles() throws FileNotFoundException {
		// output OutputFiles.txt
		File outputfilesFile = new File(outputDir, "OutputFiles.txt");
		PrintWriter outputfilesWriter = new PrintWriter(outputfilesFile);
		outputfilesWriter.println("OutputFilesID\tYN");
		outputfilesWriter.println("2\t0");
		outputfilesWriter.println("3\t0");
		outputfilesWriter.println("4\t0");
		outputfilesWriter.println("5\t0");
		outputfilesWriter.println("6\t0");
		outputfilesWriter.println("7\t0");
		outputfilesWriter.println("8\t0");
		outputfilesWriter.println("9\t0");
		outputfilesWriter.println("10\t0");
		outputfilesWriter.println("11\t0");
		outputfilesWriter.println("12\t0");
		outputfilesWriter.println("13\t0");
		outputfilesWriter.println("14\t0");
		outputfilesWriter.println("15\t0");
		outputfilesWriter.println("16\t0");
		outputfilesWriter.println("17\t0");
		outputfilesWriter.println("18\t0");
		outputfilesWriter.println("19\t0");
		outputfilesWriter.println("20\t0");
		outputfilesWriter.println("21\t0");
		outputfilesWriter.println("22\t0");
		outputfilesWriter.println("23\t0");
		outputfilesWriter.println("24\t0");
		outputfilesWriter.println("25\t0");
		outputfilesWriter.println("26\t0");
		outputfilesWriter.println("29\t0");
		outputfilesWriter.println("30\t0");
		outputfilesWriter.println("31\t0");
		outputfilesWriter.println("32\t0");
		outputfilesWriter.println("33\t0");
		outputfilesWriter.println("34\t0");
		outputfilesWriter.println("1005\t0");
		outputfilesWriter.println("1000\t0");
		outputfilesWriter.println("1001\t0");
		outputfilesWriter.println("1003\t0");
		outputfilesWriter.println("1004\t2");
		outputfilesWriter.println("1002\t1");
		outputfilesWriter.println("1006\t2");
		outputfilesWriter.close();
	}

	private void writePastAlloc() throws FileNotFoundException {
		// output PastAlloc.txt
		File pastallocFile = new File(outputDir, "PastAlloc.txt");
		PrintWriter pastallocWriter = new PrintWriter(pastallocFile);
		pastallocWriter.println("SimYear\tPlgID\tTMCID\tCID");
		pastallocWriter.close();
	}

	private void writeSepDist() throws FileNotFoundException {
		// output SepDist.txt
		File sepdistFile = new File(outputDir, "SepDist.txt");
		PrintWriter sepdistWriter = new PrintWriter(sepdistFile);
		sepdistWriter.println("ConditionID\tCIDA\tCIDb\tDistance");
		sepdistWriter.close();
	}

	private void writeSimpara() throws FileNotFoundException {
		// output Simpara.txt
		File simparaFile = new File(outputDir, "Simpara.txt");
		PrintWriter simparaWriter = new PrintWriter(simparaFile);
		simparaWriter.println("SimYearMx\tInitRd\tTimeBasedSeed\tSpecificSeed\tOptimisation\tRecPenalty");
		simparaWriter.println("5\t1\t1\t0\t1\t0");
		simparaWriter.close();
	}

	private void writeTCR() throws FileNotFoundException {
		// output TCR.txt
		File tcrFile = new File(outputDir, "TCR.txt");
		PrintWriter tcrWriter = new PrintWriter(tcrFile);
		tcrWriter.println("ConditionID\tReturnP\tInaRow\tInaRowMax\tGpCID");
		tcrWriter.close();
	}

	private void writeTCS() throws FileNotFoundException {
		// output TCS.txt
		File tcsFile = new File(outputDir, "TCS.txt");
		PrintWriter tcsWriter = new PrintWriter(tcsFile);
		tcsWriter.println("ConditionID\tYear\tCID");
		tcsWriter.close();
	}

	private void writeUniversalCID() throws FileNotFoundException {
		// output UniversalCID.txt
		File universalcidFile = new File(outputDir, "UniversalCID.txt");
		PrintWriter universalcidWriter = new PrintWriter(universalcidFile);
		universalcidWriter.println("CID");
		universalcidWriter.close();
	}

	private void writeYCP() throws FileNotFoundException {
		// output YCP.txt
		File ycpFile = new File(outputDir, "YCP.txt");
		PrintWriter ycpWriter = new PrintWriter(ycpFile);
		ycpWriter.println("ConditionID\tSimYear\tGpPlgID\tGpCID\tProporti\tDev\tTimeFail");
		ycpWriter.close();
	}

	private void writeFA() throws FileNotFoundException {
		// output FA.txt and ICRF.txt			
		File faOutFile = new File(outputDir, "FA.txt");
		PrintWriter faWriter = new PrintWriter(faOutFile);
		faWriter.println("PlgID\tArea");
		for (FieldDescription fieldDescription : project.getFieldDescriptions()) {
			faWriter.println(fieldDescription.getId() + "\t" + fieldDescription.getArea());
		}
		faWriter.close();
	}

	private void writeICRF() throws FileNotFoundException {
		// output ICRF.txt			
		File icrfOutFile = new File(outputDir, "ICRF.txt");
		PrintWriter icrfWriter = new PrintWriter(icrfOutFile);
		icrfWriter.println("PlgID\tGpPlgID\tInitTMCID");
		for (FieldDescription fieldDescription : project.getFieldDescriptions()) {
			icrfWriter.println(fieldDescription.getId() + "\t" + soilMap.get(fieldDescription.getSoilType()) + "\t");
		}
		icrfWriter.close();
	}

	private void writeITM() throws FileNotFoundException {
		// output ITM.txt
		File itmFile = new File(outputDir, "ITM.txt");
		PrintWriter itmWriter = new PrintWriter(itmFile);
		itmWriter.println("CRID\tTMCIDa\tTMCIDb\tProba");
		for (int soilId : soilMapReverse.keySet()) {
			InitialTransitionMatrix itm = project.getInitialTransitionMatrices().get(soilId - 1);
			double[][] probMatrix = itm.getMatrix();
			List<String> categories = itm.getCrops();
			for (int i = 0; i < probMatrix.length; i++) {
				String fromCategory = categories.get(i);
				for (int j = 0; j < probMatrix[i].length; j++) {
					String toCategory = categories.get(j);
					itmWriter.println(soilId + "\t" + (cropMap.get(fromCategory) - 1) + "\t" + (cropMap.get(toCategory) - 1) + "\t" + format.format(probMatrix[i][j]));
				}
			}
		}
		itmWriter.close();
	}

	private void generateCropMaps() {
		// CID
		// initialise maps
		cropMap = new TreeMap<String, Integer>();
		cropMapReverse = new TreeMap<Integer, String>();

		// generate ids for each crop
		int nextCid = 1;
		List<String> crops = project.getInitialTransitionMatrices().get(0).getCrops();
		for (String crop : crops) {
			cropMap.put(crop, nextCid);
			cropMapReverse.put(nextCid, crop);
			nextCid++;
		}
	}

	public Map<Integer, String> getCropMapReverse() {
		return cropMapReverse;
	}

	private void generateSoilMaps() {
		// CRID
		// initialise maps
		soilMap = new TreeMap<String, Integer>();
		soilMapReverse = new TreeMap<Integer, String>();

		// generate ids for each soil
		int nextCrid = 1;
		for (InitialTransitionMatrix itm : project.getInitialTransitionMatrices()) {
			String soil = itm.getSoilType();
			soilMap.put(soil, nextCrid);
			soilMapReverse.put(nextCrid, soil);
			nextCrid++;
		}
	}

}
