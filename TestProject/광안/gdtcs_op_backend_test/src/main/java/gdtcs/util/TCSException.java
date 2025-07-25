package gdtcs.util;

public class TCSException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6146781388836631043L;
	private ResponseCode responseCode;
	
	public TCSException(ResponseCode responseCode) {
		super(responseCode.getMessage());
		this.responseCode = responseCode;
	}

	public TCSException(ResponseCode responseCode, String detailMessage) {
		super(responseCode.getMessage());
		this.responseCode = responseCode;
		this.responseCode.setDetailMessage(detailMessage);
	}
	
	public ResponseCode getResponseCode() {
		return responseCode;
	}
}
