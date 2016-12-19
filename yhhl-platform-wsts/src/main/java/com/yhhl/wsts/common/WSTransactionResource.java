package com.yhhl.wsts.common;

import org.springframework.transaction.TransactionStatus;

import com.yhhl.wsts.server.WstsResourcesHolder;

/**
 * @author hujh
 * @version 1.0.0
 * @see
 * @since 1.0.0
 */
public class WSTransactionResource {

	public WSTransactionResource(TransactionStatus transactionStatus,
			WstsResourcesHolder webServiceResourcesHolder) {
		this.transactionStatus = transactionStatus;
		this.webServiceResourcesHolder = webServiceResourcesHolder;
	}

	private TransactionStatus transactionStatus;

	private WstsResourcesHolder webServiceResourcesHolder;

	public TransactionStatus getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(TransactionStatus transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public WstsResourcesHolder getWebServiceResourcesHolder() {
		return webServiceResourcesHolder;
	}

	public void setWebServiceResourcesHolder(
			WstsResourcesHolder webServiceResourcesHolder) {
		this.webServiceResourcesHolder = webServiceResourcesHolder;
	}
}