package com.yhhl.wsts.common.dto;

/**
 * Web Service事务传输对象的抽象实现
 *
 * @author hujh
 * @serialData 2015
 */
public abstract class AbstractTransactionDTO implements TransactionDTO {

	final public BusinessObjectType gotBusinessObjectType() {
		return BusinessObjectType.getType(gotBusinessObject());
	}

	/**
	 * 根据需要可以被重载
	 *
	 * @see TransactionDTO.spring.transaction.domain.ITransactionDTO#gotServiceTimeout()
	 * @return
	 */
	public String gotServiceTimeout() {
		return null;
	}
}