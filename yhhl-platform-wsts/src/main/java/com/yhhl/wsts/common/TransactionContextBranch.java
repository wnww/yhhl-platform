package com.yhhl.wsts.common;

import java.io.Serializable;

public class TransactionContextBranch implements Serializable {

	private static final long serialVersionUID = 4024034287325326081L;

	private String txBranchId;

	private String transactionInfo;

	private String txBranchUrl;

	public String getTxBranchId() {
		return txBranchId;
	}

	public void setTxBranchId(String txBranchId) {
		this.txBranchId = txBranchId;
	}

	public String getTransactionInfo() {
		return transactionInfo;
	}

	public void setTransactionInfo(String transactionInfo) {
		this.transactionInfo = transactionInfo;
	}

	public String getTxBranchUrl() {
		return txBranchUrl;
	}

	public void setTxBranchUrl(String txBranchUrl) {
		this.txBranchUrl = txBranchUrl;
	}

	public int objHashCode(Object obj) {
		if (obj == null) {
			return 0;
		}
		return obj.hashCode();
	}

	@Override
	public int hashCode() {
		// 重写hashCode
		int result = 17;
		result = 37 * result + this.objHashCode(this.txBranchId);
		result = 37 * result + this.objHashCode(this.txBranchUrl);
		System.out.println("TransactionContextBranch HashCOde = > " + result);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		// 重写equals方法，只要txBranchId相同就认为是同一个事务分支
		if (!(obj instanceof TransactionContextBranch)) {
			return false;
		}
		TransactionContextBranch branch = (TransactionContextBranch) obj;
		return branch.txBranchId.equals(this.txBranchId)
				&& branch.getTxBranchUrl().equals(this.getTxBranchUrl());
	}
}
