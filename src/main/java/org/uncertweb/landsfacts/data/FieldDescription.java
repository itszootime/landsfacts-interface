package org.uncertweb.landsfacts.data;


public class FieldDescription {
	
	private int id;
	private double easting;
	private double northing;
	private String soilType;
	private double area;

	public FieldDescription(int id, String soilType, double area) {
		this.id = id;
		this.easting = Double.NaN;
		this.northing = Double.NaN;
		this.soilType = soilType;
		this.area = area;
	}

	public FieldDescription(int id, double easting, double northing, String landClass, double area) {
		this(id, landClass, area);
		this.easting = easting;
		this.northing = northing;
	}

	public double getEasting() {
		return easting;
	}

	public double getNorthing() {
		return northing;
	}

	public int getId() {
		return id;
	}

	public String getSoilType() {
		return soilType;
	}

	public double getArea() {
		return area;
	}
	
}