package fis.com.vn.exception;

@SuppressWarnings("serial")
public class NotCheckException extends Exception{
	public NotCheckException(String errorMessage) {
		super(errorMessage);
	}
}
