package xgame.core.net;

public class NetException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NetException(String message) {
        super(message);
    }
    
    public NetException(String message, Throwable throwable) {
        super(message, throwable);
    }
    
    public NetException(Throwable throwable) {
        super(throwable);
    }
}