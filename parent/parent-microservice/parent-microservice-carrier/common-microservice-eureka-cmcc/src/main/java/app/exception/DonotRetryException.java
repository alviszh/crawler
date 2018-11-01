package app.exception;

public class DonotRetryException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2010150584161519740L;

	private int errorCode;

	public DonotRetryException() {

	}

	public DonotRetryException(String message) {
		super(message);
	}

	public DonotRetryException(int errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}
