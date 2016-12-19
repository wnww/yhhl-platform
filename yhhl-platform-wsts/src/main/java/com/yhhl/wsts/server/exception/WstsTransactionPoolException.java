package com.yhhl.wsts.server.exception;


/**
 * @author hujh
 * @version 1.0.0
 * @see
 * @since 1.0.0
 */
public class WstsTransactionPoolException extends WstsBaseCheckedException {

	/**
	 *
	 */
	private static final long serialVersionUID = -1582425273347713433L;

	public WstsTransactionPoolException(String msg) {
		super(msg);
	}

	public WstsTransactionPoolException(String msg, Throwable ex) {
		super(msg, ex);
	}
}