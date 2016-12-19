package com.yhhl.wsts.client;

import java.util.Map;

import com.yhhl.wsts.common.TransactionContextMain;


public interface WstsRemoteOperateCallBack {

	public Map<String, Object> exceute(TransactionContextMain tcm) throws RuntimeException;
}
