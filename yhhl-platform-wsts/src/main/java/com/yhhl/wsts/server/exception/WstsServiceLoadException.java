package com.yhhl.wsts.server.exception;


/**
 * 
 *
 * @author hujh
 * @serialData 2015
 */
public class WstsServiceLoadException extends WstsBaseCheckedException {
	/**
	 *
	 */
	private static final long serialVersionUID = 4481314803008551754L;

	public WstsServiceLoadException(String msg) {
		super(msg);
	}

	public WstsServiceLoadException(String msg, Throwable ex) {
		super(msg, ex);
	}
}
