package jp.co.infonic.common.db;

public class DataAccessException extends RuntimeException {

	public DataAccessException(Throwable e) {
		super(e);
	}

	public DataAccessException(String message) {
		super(message);
	}

	public DataAccessException(String message, Throwable e) {
		super(message,e);
	}
}
