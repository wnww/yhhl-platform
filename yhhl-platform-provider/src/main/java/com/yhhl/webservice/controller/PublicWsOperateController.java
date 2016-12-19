package com.yhhl.webservice.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yhhl.wsts.server.service.TransactionServiceImpl;

@Controller
@RequestMapping("/publicWsOperate")
public class PublicWsOperateController {
	private static final Logger logger = Logger.getLogger(TransactionServiceImpl.class);
	
	private TransactionServiceImpl transactionService;
	
	// 注入
	@Autowired
	public void setTransactionService(TransactionServiceImpl transactionService) {
		this.transactionService = transactionService;
	}

	/**
	 * 公共提交方法
	 * 
	 * @param transactionUID
	 */
	@RequestMapping("/commit.do")
	@ResponseBody
	public synchronized Map<String, String> commitTransaction(String transactionUID, HttpServletRequest request, HttpServletResponse response) {
		Map<String,String> map = new HashMap<String, String>();
		try {
			transactionService.commitTransaction(transactionUID);
			logger.debug("CommitSuccess" + transactionUID);
			map.put("result", "true");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 公共回滚方法
	 * 
	 * @param transactionUID
	 * @param message
	 */
	@RequestMapping("/rollback.do")
	@ResponseBody
	public synchronized Map<String, String> rollbackTransaction(String transactionUID, String message, HttpServletRequest request, HttpServletResponse response) {
		Map<String,String> map = new HashMap<String, String>();
		try {
			transactionService.rollbackTransaction(transactionUID, message);
			logger.debug("RollbackSuccess" + transactionUID);
			map.put("result", "true");
		} catch (Exception e) {
			map.put("result", "false");
			map.put("message", e.getMessage());
			e.printStackTrace();
		}
		return map;
	}
}
