package com.yhhl.wsts.server.manager;

import java.util.Iterator;
import java.util.List;

import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.ResourceHolderSupport;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.yhhl.wsts.common.IdWorker;
import com.yhhl.wsts.common.WSTransactionResource;
import com.yhhl.wsts.common.WstsTransactionSynchronization;
import com.yhhl.wsts.server.WstsTransactionSynchronizationPool;
import com.yhhl.wsts.server.WstsResourcesHolder;
import com.yhhl.wsts.server.WstsTransactionManager;
import com.yhhl.wsts.server.exception.WstsTransactionPoolException;
import com.yhhl.wsts.server.exception.WstsTransactionException;

/**
 * <DataSourceTransactionManager>
 * <p>
 * PlatformTransactionManager implementation for a single JDBC DataSource. Binds
 * a JDBC connection from the specified DataSource to the thread, potentially
 * allowing for one thread connection per data source.
 * 
 * <p>
 * Application code is required to retrieve the JDBC Connection via
 * <code>DataSourceUtils.getConnection(DataSource)</code> instead of J2EE's
 * standard <code>DataSource.getConnection()</code>. This is recommended anyway,
 * as it throws unchecked org.springframework.dao exceptions instead of checked
 * SQLException. All framework classes like JdbcTemplate use this strategy
 * implicitly. If not used with this transaction manager, the lookup strategy
 * behaves exactly like the common one - it can thus be used in any case.
 * 
 * <p>
 * Alternatively, you can also allow application code to work with the standard
 * J2EE lookup pattern <code>DataSource.getConnection()</code>, for example for
 * legacy code that is not aware of Spring at all. In that case, define a
 * TransactionAwareDataSourceProxy for your target DataSource, and pass that
 * proxy DataSource to your DAOs, which will automatically participate in
 * Spring-managed transactions through it. Note that
 * DataSourceTransactionManager still needs to be wired with the target
 * DataSource, driving transactions for it.
 * 
 * <p>
 * Supports custom isolation levels, and timeouts that get applied as
 * appropriate JDBC statement query timeouts. To support the latter, application
 * code must either use JdbcTemplate or call
 * <code>DataSourceUtils.applyTransactionTimeout</code> for each created
 * statement.
 * 
 * <p>
 * On JDBC 3.0, this transaction manager supports nested transactions via JDBC
 * 3.0 Savepoints. The "nestedTransactionAllowed" flag defaults to true, as
 * nested transactions work without restrictions on JDBC drivers that support
 * Savepoints (like Oracle).
 * 
 * <p>
 * This implementation can be used instead of JtaTransactionManager in the
 * single resource case, as it does not require the container to support JTA:
 * typically, in combination with a locally defined JDBC DataSource like a
 * Jakarta Commons DBCP connection pool. Switching between this local strategy
 * and a JTA environment is just a matter of configuration, if you stick to the
 * required connection lookup pattern. Note that JTA does not support custom
 * isolation levels!
 * 
 * @author Juergen Hoeller
 * @since 02.05.2003
 * @see #setNestedTransactionAllowed
 * @see java.sql.Savepoint
 * @see DataSourceUtils#getConnection
 * @see DataSourceUtils#applyTransactionTimeout
 * @see DataSourceUtils#closeConnectionIfNecessary
 * @see TransactionAwareDataSourceProxy
 * @see org.springframework.jdbc.core.JdbcTemplate
 * 
 * @author hujh
 */
public class WstsDataSourceTransactionManager extends DataSourceTransactionManager implements WstsTransactionManager {

	/**
	 *
	 */
	private static final long serialVersionUID = 6313205627636723815L;

	private WstsTransactionSynchronizationPool transactionSynchronizationPool;
	
	private IdWorker idWorker;

	/**
	 * 
	 * @see WstsTransactionManager.spring.transaction.IWebServiceTransactionManager#getTransactionSynchronizationPool()
	 * @return
	 */
	public WstsTransactionSynchronizationPool getTransactionSynchronizationPool() {
		return transactionSynchronizationPool;
	}

	/**
	 * 
	 * @see WstsTransactionManager.spring.transaction.IWebServiceTransactionManager#setTransactionSynchronizationPool(alex.spring.transaction.pool.WstsTransactionSynchronizationPool)
	 * @param transactionSynchronizationPool
	 */
	public void setTransactionSynchronizationPool(WstsTransactionSynchronizationPool transactionSynchronizationPool) {
		this.transactionSynchronizationPool = transactionSynchronizationPool;
	}
	
	public IdWorker getIdWorker() {
		return idWorker;
	}

	public void setIdWorker(IdWorker idWorker) {
		this.idWorker = idWorker;
	}

	/**
	 * 挂起事务
	 * 
	 * @param transaction
	 *            the current transaction object
	 * @return an object that holds suspended resources
	 * @see #doSuspend
	 * @see #resume
	 */
	public String webServiceTransactionSuspend(TransactionStatus status) throws TransactionException {
		List<TransactionSynchronization> suspendedSynchronizations = null;
		// 挂起事务
		ConnectionHolder holder = (ConnectionHolder) doSuspend(super.doGetTransaction());
		// 通知所有注册了TransactionSynchronization的资源
		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			suspendedSynchronizations = TransactionSynchronizationManager.getSynchronizations();
			for (Iterator<TransactionSynchronization> it = suspendedSynchronizations.iterator(); it.hasNext();) {
				((TransactionSynchronization) it.next()).suspend();
			}
			TransactionSynchronizationManager.clearSynchronization();
		}
		logger.debug("Web服务事务挂起 ##isNewTransaction：##" + status.isNewTransaction());
		// 入池
		String txMainId = String.valueOf(idWorker.nextId());
		try {
			transactionSynchronizationPool.pushWSTransactionResource(txMainId,new WSTransactionResource(status,
					new DataSourceResourcesHolder(suspendedSynchronizations, holder)));
		} catch (WstsTransactionPoolException e) {
			throw new WstsTransactionException(e.getMessage(), e);
		}
		logger.debug("向事务池中添加一个事务，事务ID是: " + txMainId);
		return txMainId;
	}

	/**
	 * Resume the given transaction. Delegates to the doResume template method
	 * first, then resuming transaction synchronization.
	 * 
	 * @param transaction
	 *            the current transaction object
	 * @param suspendedResources
	 *            the object that holds suspended resources, as returned by
	 *            suspend
	 * @see #doResume
	 * @see #suspend
	 */
	public TransactionStatus webServiceTransactionResume(String transactionUID) throws TransactionException {
		// 出池
		WSTransactionResource wsTransactionResource = null;
		try {
			wsTransactionResource = transactionSynchronizationPool.releaseWSTransactionResource(transactionUID);
		} catch (WstsTransactionPoolException e) {
			// must be already commit or rollback.
			throw new WstsTransactionException(e.getMessage());
		}
		DataSourceResourcesHolder resourcesHolder = (DataSourceResourcesHolder) wsTransactionResource
				.getWebServiceResourcesHolder();
		TransactionStatus status = wsTransactionResource.getTransactionStatus();
		logger.debug("从事务池中取出事务，事务ID是：" + transactionUID);
		// 恢复
		if (resourcesHolder.getWebServiceSynchronizations() != null) {
			TransactionSynchronizationManager.initSynchronization();
			for (Iterator it = resourcesHolder.getWebServiceSynchronizations().iterator(); it.hasNext();) {
				TransactionSynchronization synchronization = (TransactionSynchronization) it.next();
				synchronization.resume();
				TransactionSynchronizationManager.registerSynchronization(synchronization);
			}
		}
		doResume(status, resourcesHolder.getWebServiceResources());
		logger.debug("Web Service 恢复事务, UID: " + transactionUID);
		return status;
	}

	// 注册自己的事务同步
	// @Override
	// protected void prepareSynchronization(DefaultTransactionStatus status,
	// TransactionDefinition definition) {
	// // TODO Auto-generated method stub
	// super.prepareSynchronization(status, definition);
	// TransactionSynchronizationManager.registerSynchronization(new
	// WstsTransactionSynchronization());
	// }

	/**
	 * 
	 * @author hujh
	 * @version 1.0.0
	 * @see
	 * @since 1.0.0
	 */
	private static class DataSourceResourcesHolder implements WstsResourcesHolder {

		private final List webServiceSynchronizations;

		private final ConnectionHolder webServiceResources;

		private DataSourceResourcesHolder(List webServiceSynchronizations, ConnectionHolder webServiceResources) {
			this.webServiceSynchronizations = webServiceSynchronizations;
			this.webServiceResources = webServiceResources;
		}

		/**
		 * 
		 * @see WstsResourcesHolder.spring.transaction.pool.holder.IWebServiceResourcesHolder#getWebServiceResources()
		 * @return
		 */
		public ResourceHolderSupport getWebServiceResources() {
			return this.webServiceResources;
		}

		/**
		 * 
		 * @see WstsResourcesHolder.spring.transaction.pool.holder.IWebServiceResourcesHolder#getWebServiceSynchronizations()
		 * @return
		 */
		public List getWebServiceSynchronizations() {
			return this.webServiceSynchronizations;
		}

		/**
		 * 
		 * @see WstsResourcesHolder.spring.transaction.pool.holder.IWebServiceResourcesHolder#getOtherResources()
		 * @return
		 */
		public Object getOtherResources() {
			throw new WstsTransactionException("The method should not use here.");
		}
	}

}