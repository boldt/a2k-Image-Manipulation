package de.dennis_boldt;

public class ExifException extends Exception {

	private static final long serialVersionUID = 1L;

	public ExifException() {
		super();
	}

	public ExifException(String message) {
		super(message);
	}

	public ExifException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExifException(Throwable cause) {
		super(cause);
	}

}