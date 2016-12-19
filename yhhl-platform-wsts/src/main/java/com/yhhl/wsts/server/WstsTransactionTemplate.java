package com.yhhl.wsts.server;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.yhhl.wsts.common.dto.TransactionDTO;
import com.yhhl.wsts.server.exception.WstsTransactionException;

/**
 * 属性配置指南：
 * <p>
 * <p>
 * <property name="webServiceTransactionManager"> <br>
 * <!-- 在这里注入事务管理器 --> <br>
 * </property>
 * <p>
 * <property name="serviceTimeout"> <br>
 * <!-- 在这里注入超时策略和时间，单位：秒。也可以单独注入超时时间，例如：3 --> <br>
 * <value>rollback_3 </value> <br>
 * </property>
 * <p>
 * <p>
 * 
 * @see TransactionTemplate
 * @author hujh
 * 
 */
public class WstsTransactionTemplate extends DefaultTransactionDefinition implements InitializingBean {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * after IOC, could not be modify
	 */
	private long timeout = 1500;

	/**
	 * after IOC, could not be modify
	 */
	private int timeoutStrategy = TIMEOUT_ROLLBACK;

	/**
	 * after IOC, could not be modify
	 */
	private WstsTransactionManager webServiceTransactionManager;

	private static final Logger logger = Logger.getLogger(WstsTransactionTemplate.class);

	private static final String DEBUG_NOWAY = "UnableTreatException";

	private static final String TIMEOUT_ROLLBACK_PREFIX = TransactionDTO.TIMEOUT_ROLLBACK_PREFIX;

	private static final int TIMEOUT_ROLLBACK = 0;

	private static final String TIMEOUT_COMMIT_PREFIX = TransactionDTO.TIMEOUT_COMMIT_PREFIX;

	private static final int TIMEOUT_COMMIT = 1;

	/**
	 * @see TransactionDTO
	 */
	private static final String METHOD_SET_TRANSACTION_INFO = "setTransactionInfo";

	/**
	 * 2015-11-24
	 * 
	 * @deprecated IOC method
	 * @param prop
	 */
	public void setServiceTimeout(String prop) {
		if (prop.startsWith(TIMEOUT_ROLLBACK_PREFIX)) {
			timeoutStrategy = TIMEOUT_ROLLBACK;
			timeout = 1000 * Integer.parseInt(prop.substring(TIMEOUT_ROLLBACK_PREFIX.length()));
		} else if (prop.startsWith(TIMEOUT_COMMIT_PREFIX)) {
			timeoutStrategy = TIMEOUT_COMMIT;
			timeout = 1000 * Integer.parseInt(prop.substring(TIMEOUT_COMMIT_PREFIX.length()));
		} else {
			timeout = 1000 * Integer.parseInt(prop);
		}
	}

	public void afterPropertiesSet() {
		if (webServiceTransactionManager == null)
			throw new IllegalArgumentException("NoWSTransactionManager");
		else
			return;
	}

	@SuppressWarnings("rawtypes")
	public TransactionDTO executeNoCommit(TransactionCallback action, Class<?> transactionDTOClz) {
		TransactionStatus status = webServiceTransactionManager.getTransaction(this);
		String uid = null;
		TransactionDTO dto = null;
		try {
			dto = (TransactionDTO) transactionDTOClz.newInstance();
			Object bzReturn = action.doInTransaction(status);

			// 挂起事务
			uid = webServiceTransactionManager.webServiceTransactionSuspend(status);

			// if bzReturn == null means void
			if (bzReturn != null) {
				String fullClzName = bzReturn.getClass().getName();
				String setMethodName = null;
				if (bzReturn instanceof Object[]) {
					setMethodName = "set"
							+ fullClzName.substring(fullClzName.lastIndexOf(".") + 1, fullClzName.length() - 1);
				} else {
					setMethodName = "set" + fullClzName.substring(fullClzName.lastIndexOf(".") + 1);
				}
				Method setMethod = transactionDTOClz.getMethod(setMethodName, new Class[] { bzReturn.getClass() });
				setMethod.invoke(dto, new Object[] { bzReturn });
			}

			dto.setTransactionInfo(uid);
			// build dto ok
		} catch (Throwable th) {
			rollbackOnException(uid, th);
			try {
				Method method = transactionDTOClz.getMethod(METHOD_SET_TRANSACTION_INFO, new Class[] { String.class });
				Object errDto = transactionDTOClz.newInstance();
				method.invoke(errDto, new Object[] { TransactionDTO.ERROR_RETURN_PREFIX + th.getMessage() });
				return (TransactionDTO) errDto;
			} catch (Exception e) {
				logger.debug(DEBUG_NOWAY);
				logger.error(e.getMessage(), e);
				throw new WstsTransactionException(e.getMessage(), e);
			}
		}

		// do timeout strategy
		String resultTimeout = dto.gotServiceTimeout();
		final long realTimeout;
		final int realTimeoutStrategy;
		if (resultTimeout != null) {
			// 应用DTO设定的个性化超时策略
			if (resultTimeout.startsWith(TIMEOUT_ROLLBACK_PREFIX)) {
				realTimeoutStrategy = TIMEOUT_ROLLBACK;
				realTimeout = 1000 * Integer.parseInt(resultTimeout.substring(TIMEOUT_ROLLBACK_PREFIX.length()));
			} else if (resultTimeout.startsWith(TIMEOUT_COMMIT_PREFIX)) {
				realTimeoutStrategy = TIMEOUT_COMMIT;
				realTimeout = 1000 * Integer.parseInt(resultTimeout.substring(TIMEOUT_COMMIT_PREFIX.length()));
			} else {
				realTimeoutStrategy = this.timeoutStrategy;
				realTimeout = 1000 * Integer.parseInt(resultTimeout);
			}
		} else {
			realTimeoutStrategy = this.timeoutStrategy;
			realTimeout = this.timeout;
		}

		if (realTimeout > 0) {
			final String finalUid = uid;
			new Thread() {
				public void run() {
					String msg = null;
					try {
						logger.debug("WaitTransactionTimeout " + realTimeout + " ms");
						Thread.sleep(realTimeout);
					} catch (InterruptedException e) {
						msg = e.getMessage();
					} finally {
						if (webServiceTransactionManager.getTransactionSynchronizationPool().isActive(finalUid)) {
							doTimeoutStrategy(finalUid, msg, realTimeoutStrategy);
						}
					}
				}
			}.start();
		}
		return dto;
	}

	private void doTimeoutStrategy(String uid, String errorMSG, int realTimeoutStrategy) {
		if (realTimeoutStrategy == TIMEOUT_COMMIT) {
			try {
				logger.info("WSTransactionTimeout");
				TransactionStatus status = webServiceTransactionManager.webServiceTransactionResume(uid);
				webServiceTransactionManager.commit(status);
				logger.debug(TIMEOUT_COMMIT_PREFIX + "UID: " + uid);
				return;
			} catch (TransactionException e) {
				errorMSG = e.getMessage();
			}
		}
		try {
			logger.info("WSTransactionTimeout");
			rollbackOnException(uid, new Throwable(errorMSG == null ? "WSTransactionTimeout" : errorMSG));
		} catch (TransactionException e) {
			logger.debug(DEBUG_NOWAY);
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * @param uid
	 * @param msg
	 */
	public void rollbackOnException(String uid, String msg) {
		rollbackOnException(uid, new Throwable(msg));
	}

	private void rollbackOnException(String uid, Throwable ex) {
		logger.debug("RollbackForException", ex);
		try {
			TransactionStatus status = webServiceTransactionManager.webServiceTransactionResume(uid);
			webServiceTransactionManager.rollback(status);
		} catch (RuntimeException e) {
			logger.error("RollbackException", e);
			throw e;
		} catch (Error err) {
			logger.error("RollbackError", err);
			throw err;
		}
		logger.debug(TIMEOUT_ROLLBACK_PREFIX + "UID: " + uid);
	}

	public WstsTransactionManager getWebServiceTransactionManager() {
		return webServiceTransactionManager;
	}

	public void setWebServiceTransactionManager(WstsTransactionManager webServiceTransactionManager) {
		this.webServiceTransactionManager = webServiceTransactionManager;
	}
}