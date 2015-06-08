package com.shadowgame.rpg.core;

/**
 * @author wcj10051891@gmail.com
 */
public class AlertException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String msg;
	
	/**
	 * 
	 */
	public AlertException(String msg) {
		this.msg = msg;
	}
	
}
