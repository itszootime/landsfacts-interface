package org.uncertweb.landsfacts;


public class LandsfactsUtilities {
	
	private static String originalLineSeparator;
	
	public static void setWindowsLineSeparator() {
		originalLineSeparator = System.getProperty("line.separator");
		System.setProperty("line.separator", "\r\n");
	}
	
	public static void resetLineSeparator() {
		if (originalLineSeparator != null) {
			System.setProperty("line.separator", originalLineSeparator);
		}
	}

}
