package com.yhhl.wsts.common.dto;

import java.util.HashMap;
import java.util.Map;

public class MapTransactionDTO extends AbstractTransactionDTO {

	private static final long serialVersionUID = -2169553248749281231L;

	private HashMap<String, Object> hashMap = new HashMap<String, Object>();

	private String transactionInfo;

	@Override
	public Object gotBusinessObject() {
		return hashMap;
	}

	@Override
	public String getTransactionInfo() {
		return this.transactionInfo;
	}

	@Override
	public void setTransactionInfo(String transactionInfo) {
		this.transactionInfo = transactionInfo;
	}

	public HashMap<String, Object> getHashMap() {
		return hashMap;
	}

	public void setHashMap(HashMap<String, Object> hashMap) {
		this.hashMap = hashMap;
	}
}
