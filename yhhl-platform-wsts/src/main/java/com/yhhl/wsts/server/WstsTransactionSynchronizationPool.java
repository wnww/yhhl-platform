package com.yhhl.wsts.server;

import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.yhhl.wsts.common.WSTransactionResource;
import com.yhhl.wsts.server.exception.WstsTransactionPoolException;

/**
 * @author hujh
 * @version 1.0.0
 * @see
 * @since 1.0.0
 * @see TransactionSynchronizationManager
 */
public interface WstsTransactionSynchronizationPool {
	
	/**
	 * 入池
	 * @param wsTransactionResource
	 * @return
	 * @throws WstsTransactionPoolException
	 */
	/* transactionUID */
	public void pushWSTransactionResource(String txMainId,
			WSTransactionResource wsTransactionResource)
			throws WstsTransactionPoolException;

	/**
	 * 出池
	 * @param transactionUID
	 * @return
	 * @throws WstsTransactionPoolException
	 */
	public WSTransactionResource releaseWSTransactionResource(
			String transactionUID) throws WstsTransactionPoolException;

	/**
	 * 获取事务资源
	 * @param transactionUID
	 * @return
	 * @throws WstsTransactionPoolException
	 * @deprecated Please use #releaseWSTransactionResource
	 */
	public WSTransactionResource getWSTransactionResource(String transactionUID)
			throws WstsTransactionPoolException;

	/**
	 * 事务是否存在
	 * @param transactionUID
	 * @return
	 */
	public boolean isActive(String transactionUID);

}