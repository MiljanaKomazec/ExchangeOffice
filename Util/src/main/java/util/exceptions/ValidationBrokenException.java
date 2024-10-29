package util.exceptions;


public class ValidationBrokenException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ValidationBrokenException() {
		
	}
	
	public ValidationBrokenException(String message) {
		super(findTuneMessage(message));
		
	}
	
	private static String findTuneMessage(String message) {
		String[] decomposeMessage = message.split("\"");

	    if (decomposeMessage.length > 1) {
	        return decomposeMessage[1];
	    } else {
	        return message;
	    }
	}
}