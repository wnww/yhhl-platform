package com.yhhl.wsts.common;

import org.springframework.transaction.support.TransactionSynchronizationAdapter;


public class WstsTransactionSynchronization extends TransactionSynchronizationAdapter {

	@Override
	public void suspend() {
		System.out.println("=============> suspend");
	}

	@Override
	public void resume() {
		System.out.println("=============> resume");
	}

	@Override
	public void beforeCommit(boolean readOnly) {
		System.out.println("=============> beforeCommit");
	}

	@Override
	public void beforeCompletion() {
		System.out.println("=============> beforeCompletion");
	}

	@Override
	public void afterCommit() {
		System.out.println("=============> afterCommit");
	}

	@Override
	public void afterCompletion(int status) {
		System.out.println("=============> afterCompletion");
	}

}
