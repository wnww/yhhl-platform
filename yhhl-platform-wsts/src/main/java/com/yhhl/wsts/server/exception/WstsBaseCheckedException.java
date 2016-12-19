package com.yhhl.wsts.server.exception;

import org.springframework.core.NestedCheckedException;

public class WstsBaseCheckedException extends NestedCheckedException {

	private static final long serialVersionUID = 8681827066298598152L;

	public WstsBaseCheckedException(String msg) {
		super(msg);
	}

	public WstsBaseCheckedException(String msg, Throwable ex) {
		super(msg, ex);
	}

	
}
