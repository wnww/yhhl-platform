package com.yhhl.wsts.server;

import java.util.List;

import org.springframework.transaction.support.ResourceHolderSupport;
import org.springframework.transaction.support.TransactionSynchronization;

/**
 * 挂起事务，利用挂起和恢复
 *
 * @author hujh
 * @see #suspend
 * @see #resume
 */
public interface WstsResourcesHolder {

	/**
	 * 获取WebService的事务同步
	 * @return
	 * @see TransactionSynchronization
	 */
	public List getWebServiceSynchronizations();

	/**
	 *
	 * @return
	 * @see org.springframework.jdbc.datasource.ConnectionHolder
	 * @see org.springframework.orm.hibernate3.SessionHolder
	 */
	public ResourceHolderSupport getWebServiceResources();

	/**
	 * such as hibernate resources like ... <br>
	 *
	 * @return
	 */
	public Object getOtherResources();
}