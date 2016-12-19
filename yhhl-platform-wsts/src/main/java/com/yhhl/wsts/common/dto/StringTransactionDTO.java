package com.yhhl.wsts.common.dto;

/**
 * String类型的事务传输对象
 *
 * @author hujh
 * @serialData 2015
 */
public class StringTransactionDTO extends AbstractTransactionDTO {
	/**
	 *
	 */
	private static final long serialVersionUID = 8357267270968642441L;

	private String transactionInfo;

	private String string;

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public String getTransactionInfo() {
		return this.transactionInfo;
	}

	public Object gotBusinessObject() {
		return this.string;
	}

	public void setTransactionInfo(String transactionInfo) {
		this.transactionInfo = transactionInfo;
	}
}