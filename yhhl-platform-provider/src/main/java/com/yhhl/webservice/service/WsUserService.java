package com.yhhl.webservice.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import com.yhhl.user.model.User;
import com.yhhl.user.service.UserServiceI;
import com.yhhl.wsts.common.dto.MapTransactionDTO;
import com.yhhl.wsts.common.dto.VoidTransactionDTO;
import com.yhhl.wsts.server.service.TransactionServiceSupport;
import com.yhhl.wsts.server.utils.SpringContextUtils;

public class WsUserService {

//	private UserServiceI userService;
	
//	public WsUserService(){
//		if(userService==null){
//			userService = getUserService();
//		}
//	}
//
//	public UserServiceI getUserService() {
//		this.userService = (UserServiceI) SpringContextUtils.getBean("userService");
//		return this.userService;
//	}
//
//	public MapTransactionDTO addUser(final User user) {
//		MapTransactionDTO to = (MapTransactionDTO) doInWebServiceTransaction(null,new TransactionCallback() {
//			@Override
//			public Object doInTransaction(TransactionStatus status) {
//				getUserService().saveUser(user);
//				Map<String,Object> map = new HashMap<String,Object>();
//				map.put("a", "11111");
//				map.put("b", "222");
//				return map;
//			}
//		}, MapTransactionDTO.class);
//		return to;
//	}
//
//	public VoidTransactionDTO editUser(final User user) {
//		System.out.println("username ====== "+user.getName());
//		VoidTransactionDTO to = (VoidTransactionDTO) doInWebServiceTransaction(null,new TransactionCallback() {
//			@Override
//			public Object doInTransaction(TransactionStatus status) {
//				getUserService().updateUser(user);
//				return null;
//			}
//		}, VoidTransactionDTO.class);
//		return to;
//	}

//	public User getUserById(final String id){
//		return userService.getById(id);
//	}
}
