package com.yhhl.wsts.server.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionCallback;

import com.yhhl.wsts.common.TransactionContextMain;
import com.yhhl.wsts.common.dto.TransactionDTO;
import com.yhhl.wsts.server.WstsTransactionTemplate;
import com.yhhl.wsts.server.utils.IConstants;
import com.yhhl.wsts.server.utils.SpringContextUtils;
import com.yhhl.wsts.server.utils.StringUtils;
import com.yhhl.wsts.utils.ConfigUtils;
import com.yhhl.wsts.utils.WebUtil;

/**
 * @author hujh
 * @version 1.0.0
 * @see
 * @since 1.0.0
 */
public class TransactionServiceSupport implements IConstants {
//	private WstsTransactionTemplate transactionTemplate = (WstsTransactionTemplate) SpringContextUtils
//			.getBean(BEAN_WSTRANSACTION_TEMPLATE);
	
	private WstsTransactionTemplate wstsTransactionTemplate;

	// http://localhost:8080/action/add.do
	private String serviceAddress;

	// 端口
	private String schemaNamespace;

	// wsts服务的工程名称
	private String wstsContextPath;

	/**
	 * @param transactionCallback
	 * @param callbackReturnType
	 * @return ITransactionDTO
	 */
	@SuppressWarnings("rawtypes")
	public TransactionDTO doInWebServiceTransaction(TransactionContextMain contextMain,
			TransactionCallback transactionCallback, Class<?> transactionDTOClz) {
		TransactionDTO transactionDTO = wstsTransactionTemplate.executeNoCommit(transactionCallback, transactionDTOClz);
		if (transactionDTO == null) {
			return transactionDTO;
		}
		if (transactionDTO.getTransactionInfo() == null || "".equals(transactionDTO.getTransactionInfo())) {
			return transactionDTO;
		}
		if (!StringUtils.isNumeric(transactionDTO.getTransactionInfo())) {
			return transactionDTO;
		}

		// 获取Wsts服务地址
		hostOperate();
		Map<String, Object> parasInput = new HashMap<String, Object>();
		parasInput.put("txId", contextMain.getTxMainId());
		parasInput.put("actionId", transactionDTO.getTransactionInfo());
		parasInput.put("state", "N"); //  C:提交；R:回滚；N:新建
		String url = serviceAddress + schemaNamespace + "/" + wstsContextPath + "/businessAction/saveBusinessAction.do";
		Map<String,Object> result = WebUtil.httpCommit(url, parasInput, null);
		if(String.valueOf(result.get("status")).equals("200")){
			result = WebUtil.parseJSON2Map(String.valueOf(result.get("result")));
			if(String.valueOf(result.get("flag")).equals("F")){
				System.out.println("=========================================---------------------------");
				throw new RuntimeException(String.valueOf(result.get("msg")));
			}
		}
		return transactionDTO;
	}

	public void hostOperate() {
		serviceAddress = ConfigUtils.getString("wsts_url");
		schemaNamespace = ConfigUtils.getString("wsts_port");
		wstsContextPath = ConfigUtils.getString("wsts_contextPath");
		if (serviceAddress.endsWith("/")) {
			serviceAddress = serviceAddress.substring(0, serviceAddress.length() - 1);
		}
		if (schemaNamespace != null && !"".equals(schemaNamespace)) {
			this.schemaNamespace = ":" + schemaNamespace;
		} else {
			this.schemaNamespace = "";
		}
	}

	// 注入
	public void setWstsTransactionTemplate(WstsTransactionTemplate wstsTransactionTemplate) {
		this.wstsTransactionTemplate = wstsTransactionTemplate;
	}

	

}