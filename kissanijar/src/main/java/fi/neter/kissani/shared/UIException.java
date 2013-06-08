package fi.neter.kissani.shared;

public class UIException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public UIException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
