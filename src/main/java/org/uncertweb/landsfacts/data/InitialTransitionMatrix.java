package org.uncertweb.landsfacts.data;

import java.util.ArrayList;
import java.util.List;

public class InitialTransitionMatrix {

	private String soilType;
	private List<String> crops;
	private List<Double[]> rowProbabilities;
	
	public InitialTransitionMatrix(String soilType) {
		this.soilType = soilType;
		crops = new ArrayList<String>();
		rowProbabilities = new ArrayList<Double[]>();
	}
	
	public void addRow(String crop, Double[] probabilities) {
		crops.add(crop);
		rowProbabilities.add(probabilities);
	}
	
	public String getSoilType() {
		return soilType;
	}

	public List<String> getCrops() {
		return crops;
	}

	public List<Double[]> getRowProbabilities() {
		return rowProbabilities;
	}
	
	public double[][] getMatrix() {
		double[][] matrix = new double[crops.size()][crops.size()];
		for (int i = 0; i < rowProbabilities.size(); i++) {
			Double[] probabilities = rowProbabilities.get(i);
			for (int j = 0; j < probabilities.length; j++) {
				matrix[i][j] = probabilities[j];
			}
		}
		return matrix;
	}
	
}