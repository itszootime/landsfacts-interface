package org.uncertweb.landsfacts.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CropAllocation {

	private List<Allocation> allocations;

	public CropAllocation() {
		allocations = new ArrayList<Allocation>();
	}
	
	public List<Allocation> getAllocations() {
		return allocations;
	}
	
	public void addAllocation(int fieldID, String[] crops) {
		allocations.add(new Allocation(fieldID, crops));
	}
	
	public class Allocation {
		private int fieldID;
		private List<String> crops;
		
		public Allocation(int fieldID, String[] crops) {
			this(fieldID);
			this.crops.addAll(Arrays.asList(crops));
		}
		
		public Allocation(int fieldID) {
			this.fieldID = fieldID;
			this.crops = new ArrayList<String>();
		}
		
		public int getFieldID() {
			return fieldID;
		}
		
		public String[] getCrops() {
			return crops.toArray(new String[0]);
		}
	}
	
}
