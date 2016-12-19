package com.yhhl.wsts.server.exception;

import org.springframework.transaction.TransactionException;

/**
 *
 * @author hujh
 * @version 1.0.0
 * @see
 * @since 1.0.0
 */
public class WstsTransactionException extends TransactionException {

	/**
	 *
	 */
	private static final long serialVersionUID = 6195711273139535667L;

	public WstsTransactionException(String msg) {
		super(msg);
	}

	public WstsTransactionException(String msg, Throwable ex) {
		super(msg, ex);
	}
}