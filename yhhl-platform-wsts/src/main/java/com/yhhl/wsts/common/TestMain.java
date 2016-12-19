package com.yhhl.wsts.common;

public class TestMain {

	public static void main(String[] args){
		TransactionContextBranch b1 = new TransactionContextBranch();
		b1.setTxBranchId("111111");
		b1.setTransactionInfo("aaaaaa");
		
		
		TransactionContextBranch b2 = new TransactionContextBranch();
		b2.setTxBranchId("111111");
		b2.setTransactionInfo("bbbbbb");
		
		System.out.println("==================");
		System.out.println("b1 与  b2 是否相等 = > "+b1.equals(b2));
		
		System.out.println("==================");
		System.out.println("b1 hashCode = > "+b1.hashCode());
		System.out.println("b2 hashCode = > "+b2.hashCode());
	}
}
