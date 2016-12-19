package com.yhhl.wsts.server;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.yhhl.wsts.common.IdWorker;
import com.yhhl.wsts.common.WSTransactionResource;
import com.yhhl.wsts.common.WstsConstants;
import com.yhhl.wsts.server.exception.WstsTransactionPoolException;
import com.yhhl.wsts.server.utils.SpringContextUtils;

/**
 * 
 * @author hujh
 * @serialData 2015
 */
public class WstsTransactionSynchronizationPoolImpl implements WstsTransactionSynchronizationPool, WstsConstants {

	/**
	 * 事务池
	 */
	private final Map<String, WSTransactionResource> pool = Collections
			.synchronizedMap(new HashMap<String, WSTransactionResource>());

	/**
	 * 事务标识
	 */
	private long uid = 0;

	/**
	 * 根据事务ID获取事务
	 * 
	 * @deprecated
	 * @param transactionUID
	 * @return
	 * @throws WstsTransactionPoolException
	 */
	public WSTransactionResource getWSTransactionResource(String transactionUID) throws WstsTransactionPoolException {
		if (isActive(transactionUID)) {
			return (WSTransactionResource) pool.get(transactionUID);
		} else {
			throw new WstsTransactionPoolException("事务不存在 " + transactionUID);
		}
	}

	/**
	 * 
	 * 事务入池
	 * 
	 * @param wsTransactionResource
	 * @return
	 * @throws WstsTransactionPoolException
	 */
	public synchronized void pushWSTransactionResource(String TxMainId, WSTransactionResource wsTransactionResource)
			throws WstsTransactionPoolException {
		pool.put(TxMainId, wsTransactionResource);
	}

	/**
	 * 
	 * 事务出池
	 * 
	 * @param transactionUID
	 * @return
	 * @throws WstsTransactionPoolException
	 */
	public WSTransactionResource releaseWSTransactionResource(String transactionUID)
			throws WstsTransactionPoolException {
		System.out.println("事务ID = " + transactionUID);
		if (isActive(transactionUID)) {
			return (WSTransactionResource) pool.remove(transactionUID);
		} else {
			throw new WstsTransactionPoolException("事务不存在 " + transactionUID);
		}
	}

	/**
	 * 
	 * 判断事务是否存在
	 * @param transactionUID
	 * @return
	 */
	public boolean isActive(String transactionUID) {
		return pool.containsKey(transactionUID);
	}
}