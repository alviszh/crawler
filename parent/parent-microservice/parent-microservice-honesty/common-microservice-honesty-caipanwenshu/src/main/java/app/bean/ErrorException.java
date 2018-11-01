package app.bean;

public class ErrorException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ErrorException() {
		super();
	}

	public ErrorException(String msg) {
		super(msg);
	}
}
