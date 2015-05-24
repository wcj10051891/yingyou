package com.shadowgame.rpg.core;

public class AppException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3798224046148326380L;

	public AppException(String message) {
        super(message);
    }
    
    public AppException(String message, Throwable throwable) {
        super(message, throwable);
    }
    
    public AppException(Throwable throwable) {
        super(throwable);
    }
}