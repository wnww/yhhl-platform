package com.yhhl.wsts.server.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.yhhl.wsts.server.WstsTransactionTemplate;
import com.yhhl.wsts.server.utils.IConstants;
import com.yhhl.wsts.server.utils.SpringContextUtils;

/**
 * @author hujh
 * @version 1.0.0
 * @see
 * @since 1.0.0
 */
public class TransactionServiceImpl implements IConstants {

//	private WstsTransactionTemplate transactionTemplate = ((WstsTransactionTemplate) SpringContextUtils
//			.getBean(BEAN_WSTRANSACTION_TEMPLATE));
	private WstsTransactionTemplate wstsTransactionTemplate;

	private static final Logger logger = Logger.getLogger(TransactionServiceImpl.class);

	/**
	 * 提交事务 
	 * @param transactionUID
	 */
	public synchronized void commitTransaction(String transactionUID) {
		TransactionStatus status = wstsTransactionTemplate.getWebServiceTransactionManager().webServiceTransactionResume(
						transactionUID);
		wstsTransactionTemplate.getWebServiceTransactionManager().commit(status);
		logger.debug("CommitSuccess" + transactionUID);
	}

	/**
	 * 回滚事务
	 * @param transactionUID
	 * @param message
	 */
	public synchronized void rollbackTransaction(String transactionUID,
			String message) {
		wstsTransactionTemplate.rollbackOnException(transactionUID, message);
	}

	// 注入
	@Autowired
	public void setWstsTransactionTemplate(WstsTransactionTemplate wstsTransactionTemplate) {
		this.wstsTransactionTemplate = wstsTransactionTemplate;
	}

}