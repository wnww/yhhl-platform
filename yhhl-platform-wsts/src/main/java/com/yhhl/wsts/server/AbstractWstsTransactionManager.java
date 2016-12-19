package com.yhhl.wsts.server;

/**
 * @author hujh
 * @version 1.0.0
 * @see
 * @since 1.0.0
 * @deprecated 目前还没有抽象的必要
 */
public abstract class AbstractWstsTransactionManager implements
		WstsTransactionManager {

	private WstsTransactionSynchronizationPool transactionSynchronizationPool;

	public WstsTransactionSynchronizationPool getTransactionSynchronizationPool() {
		return transactionSynchronizationPool;
	}

	public void setTransactionSynchronizationPool(
			WstsTransactionSynchronizationPool transactionSynchronizationPool) {
		this.transactionSynchronizationPool = transactionSynchronizationPool;
	}

}