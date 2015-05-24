package com.shadowgame.rpg.core;

/**
 * @author wcj10051891@gmail.com
 */
public class NoticeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String msg;
	
	/**
	 * 
	 */
	public NoticeException(String msg) {
		this.msg = msg;
	}
	
}
