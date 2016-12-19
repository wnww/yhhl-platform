package com.yhhl.wsts.client;

import java.util.HashMap;
import java.util.Map;

import com.yhhl.wsts.common.GlobalContext;
import com.yhhl.wsts.common.IdWorker;
import com.yhhl.wsts.common.TransactionContextBranch;
import com.yhhl.wsts.common.TransactionContextMain;
import com.yhhl.wsts.utils.ConfigUtils;
import com.yhhl.wsts.utils.WebUtil;

/**
 * Must be config in spring-webservice.xml for init Service Address.
 * 
 * @author hujh
 * @version 1.0.0
 * @see
 * @since 1.0.0
 * @see spring-webservice.xml
 */
public class WstsTransactionServiceClient {

	private IdWorker idWorker;

	public IdWorker getIdWorker() {
		return idWorker;
	}

	public void setIdWorker(IdWorker idWorker) {
		this.idWorker = idWorker;
	}

	public Map<String, Object> doCommitByHttp(String remoteUrl, String uid) {
		Map<String, Object> parasInput = new HashMap<String, Object>();
		parasInput.put("transactionUID", uid);
		String url = remoteUrl + "/scb/publicWsOperate/commit.do";
		return WebUtil.httpCommit(url, parasInput, null);
	}

	public Map<String, Object> doRollbackByHttp(String remoteUrl, String uid, String message) {
		Map<String, Object> parasInput = new HashMap<String, Object>();
		parasInput.put("transactionUID", uid);
		parasInput.put("message", message);
		String url = remoteUrl + "/scb/publicWsOperate/rollback.do";
		return WebUtil.httpCommit(url, parasInput, null);
	}

	/**
	 * 调用远程方法，包括commit和rollback
	 * 
	 * @param roi
	 * @return
	 */
	public Map<String, Object> doIntransaction(WstsRemoteOperateCallBack roi) throws RuntimeException {
		// 第一步：事务管理器记录主事务（目前利用全局变量map存储，将来可用数据库存储或redis存储）
		TransactionContextMain tcm = new TransactionContextMain();
		String txMainId = String.valueOf(idWorker.nextId());
		tcm.setTxMainId(txMainId);
		GlobalContext.global.put(txMainId, tcm);

		// 第二步：调用业务逻辑
		Map<String, Object> map = roi.exceute(tcm);
		int branchCount = tcm.getBranchs().size();
		StringBuffer flag = new StringBuffer("1");
		for(int i=0; i<branchCount; i++){
			flag.append("0");
		}
		// 第三步：根据业务逻辑结果调用事务提交或回滚。
		Map<String, Object> result = null;
		for (TransactionContextBranch tcb : tcm.getBranchs()) {
			if (String.valueOf(map.get("txStatus")).equals("T")) {
				result = this.doCommitByHttp(tcb.getTxBranchUrl(),tcb.getTxBranchId());
			} else {
				result = this.doRollbackByHttp(tcb.getTxBranchUrl(),tcb.getTxBranchId(), tcb.getTransactionInfo());
			}
			if (String.valueOf(result.get("status")).equals("200")) {
				result = WebUtil.parseJSON2Map(String.valueOf(result.get("result")));
				if (String.valueOf(result.get("result")).equals("false")) {
					System.out.println("=========================================---------------------------");
					throw new RuntimeException(String.valueOf(result.get("message")));
				}
			} else {
				// {message=参数或服务器异常!, result=, status=500}
				throw new RuntimeException("服务端异常！");
			}
		}
		return result;
	}
}