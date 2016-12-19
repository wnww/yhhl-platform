package com.yhhl.wsts.server;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

/**
 * Web服务事务管理器接口
 *
 * @author hujh
 * @version 1.0.0
 * @see
 * @since 1.0.0
 */
public interface WstsTransactionManager extends
		PlatformTransactionManager {

	/**
	 * 获取事务同步池 <br>
	 * 2015-11-23
	 *
	 * @return
	 */
	public WstsTransactionSynchronizationPool getTransactionSynchronizationPool();

	/**
	 *  为Web服务恢复本地线程中的事务 <br>
	 * 2015-11-23
	 *
	 * @param transactionUID
	 * @return
	 * @throws TransactionException
	 */
	public TransactionStatus webServiceTransactionResume(String transactionUID)
			throws TransactionException;

	/**
	 * 为Web服务挂起本地线程中的事务 <br>
	 * 2015-11-23
	 *
	 * @param status
	 * @return
	 * @throws TransactionException
	 */
	/* transactionUID */
	public String webServiceTransactionSuspend(
			TransactionStatus status) throws TransactionException;
}