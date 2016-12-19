package com.yhhl.wsts.common.dto;

/**
 * 不返回的事务传输对象
 *
 * @author hujh
 * @serialData 2015
 */
public class VoidTransactionDTO extends AbstractTransactionDTO {

	/**
	 *
	 */
	private static final long serialVersionUID = -7196500576320595081L;

	private String transactionInfo;

	public String getTransactionInfo() {
		return this.transactionInfo;
	}

	public Object gotBusinessObject() {
		return null;
	}

	public void setTransactionInfo(String transactionInfo) {
		this.transactionInfo = transactionInfo;
	}
}