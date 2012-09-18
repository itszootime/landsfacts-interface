package org.uncertweb.landsfacts.data;

import java.util.List;

public class Project {

	private List<InitialTransitionMatrix> initialTransitionMatrices;
	private List<FieldDescription> fieldDescriptions;
	private int numSimulations = 3;
	
	public Project(List<InitialTransitionMatrix> initialTransitionMatrices, List<FieldDescription> fieldDescriptions) {
		this.initialTransitionMatrices = initialTransitionMatrices;
		this.fieldDescriptions = fieldDescriptions;
	}
	
	public Project(List<InitialTransitionMatrix> initialTransitionMatrices, List<FieldDescription> fieldDescriptions, int numSimulations) {
		this(initialTransitionMatrices, fieldDescriptions);
		this.numSimulations = numSimulations;
	}

	public List<InitialTransitionMatrix> getInitialTransitionMatrices() {
		return initialTransitionMatrices;
	}

	public List<FieldDescription> getFieldDescriptions() {
		return fieldDescriptions;
	}

	public int getNumSimulations() {
		return numSimulations;
	}
	
}
