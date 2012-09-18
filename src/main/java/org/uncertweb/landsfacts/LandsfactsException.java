package org.uncertweb.landsfacts;

public class LandsfactsException extends Exception {

	private static final long serialVersionUID = 1L;

	public LandsfactsException() {
		super();
	}
	
	public LandsfactsException(String message) {
		super(message);
	}
	
	public LandsfactsException(Throwable cause) {
		super(cause);
	}
	
}
