package com.yhhl.wsts.interceptor;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.yhhl.wsts.client.WstsTransactionServiceClient;
import com.yhhl.wsts.common.WstsTrans;

public class TransactionInterceptor implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {

		Method method = invocation.getMethod();
		WstsTrans annotation = method.getAnnotation(WstsTrans.class);

		if (annotation != null) {
			WstsTransactionServiceClient tsc = new WstsTransactionServiceClient();
			try {
				method.invoke(invocation.getThis(), invocation.getArguments());
				// 调用提交方法
				tsc.doCommitByHttp("","");
			} catch (Exception e) {
				// 调用回滚方法
				tsc.doRollbackByHttp("","", "");
				e.printStackTrace();
			}
		} else {
			try {
				method.invoke(invocation.getThis(), invocation.getArguments());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
