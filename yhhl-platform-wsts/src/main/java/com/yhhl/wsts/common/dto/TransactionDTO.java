package com.yhhl.wsts.common.dto;

import java.io.Serializable;

/**
 * Web Service事务传输对象的接口
 *
 * @author hujh
 * @serialData 2015
 */
public interface TransactionDTO extends Serializable {

	public static final String ERROR_RETURN_PREFIX = "error_but_rollbacked";

	public static final String TIMEOUT_ROLLBACK_PREFIX = "rollback_";

	public static final String TIMEOUT_COMMIT_PREFIX = "commit_";

	/**
	 * abstracted <br>
	 *
	 * @return
	 * @see TransactionObjectType
	 */
	public BusinessObjectType gotBusinessObjectType();

	/**
	 * Ϊ�˱�����?��������������� <br>
	 *
	 * @return could be null, which mean void.
	 */
	public Object gotBusinessObject();

	/**
	 * example: return TIMEOUT_ROLLBACK_PREFIX+"2"; <br>
	 *
	 * @return could be null, which mean use default.
	 */
	public String gotServiceTimeout();

	/**
	 * example: return this.transactionInfo; <br>
	 *
	 * @return
	 */
	public String getTransactionInfo();

	/**
	 * example: this.transactionInfo = transactionInfo; <br>
	 *
	 * @param transactionInfo
	 */
	public void setTransactionInfo(String transactionInfo);
}