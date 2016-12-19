package com.yhhl.wsts.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yhhl.wsts.common.IdWorker;
import com.yhhl.wsts.common.TransactionContextMain;
import com.yhhl.wsts.utils.WebUtil;

@Component
public class BusinessActivityControlService {

	@Autowired
	private IdWorker idWorker;
	
	public TransactionContextMain start(){
		TransactionContextMain contextMain = new TransactionContextMain();
		contextMain.setTxMainId(String.valueOf(idWorker.nextId()));
		// 将 业务事务管理器主事务添加到wsts服务端
		Map<String, Object> parasInput = new HashMap<String, Object>();
		parasInput.put("txId", contextMain.getTxMainId());
		parasInput.put("state", "N"); //  C:提交；R:回滚；N:新建
		String url = "http://192.168.8.9:8080/wstsserver/businessActivity/saveBusinessActivity.do";
		Map<String,Object> result = WebUtil.httpCommit(url, parasInput, null);
		if(String.valueOf(result.get("status")).equals("200")){
			result = WebUtil.parseJSON2Map(String.valueOf(result.get("result")));
			if(String.valueOf(result.get("flag")).equals("F")){
				System.out.println("=========================================---------------------------");
				throw new RuntimeException(String.valueOf(result.get("msg")));
			}
		}
		return contextMain;
	}
}
