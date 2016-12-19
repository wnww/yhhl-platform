package com.yhhl.wsts.common;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TransactionContextMain implements Serializable {

	private static final long serialVersionUID = -8864721995188826341L;

	// 主事务 ID
	private String txMainId;

	// 主事务信息
	private String transactionInfo;

	private String txMainUrl;

	// 子事务集
	private final Set<TransactionContextBranch> branchs = new HashSet<TransactionContextBranch>();

	private Object object;

	private Map<String, Object> map;

	public boolean isTransactionContextBranchActivie() {
		return branchs!=null;
	}

	public Set<TransactionContextBranch> getBranchs() {
		return branchs;
	}

	public String getTxMainId() {
		return txMainId;
	}

	public void setTxMainId(String txMainId) {
		this.txMainId = txMainId;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public String getTransactionInfo() {
		return transactionInfo;
	}

	public void setTransactionInfo(String transactionInfo) {
		this.transactionInfo = transactionInfo;
	}

	public String getTxMainUrl() {
		return txMainUrl;
	}

	public void setTxMainUrl(String txMainUrl) {
		this.txMainUrl = txMainUrl;
	}

}
