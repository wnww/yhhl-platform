package com.yhhl.activity.model;

import java.math.BigDecimal;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import com.yhhl.common.CustomDateTimeSerializer;

/**
 * 
 * <br>
 * <b>功能：</b>BusinessActivityEntity<br>
 * <b>作者：</b>www.yhhlkj.tk<br>
 * <b>日期：</b> June 15, 2015 <br>
 * <b>版权所有：<b>版权所有(C) 2013，国版中心<br>
 */
public class BusinessActivity {

	private java.lang.String txId;// 主事务ID
	private java.lang.String state;//状态 C:提交；R:回滚；N:新建；U:未知状态，需要回查主业务发起方
	private java.lang.String accountTransState;//
	private java.util.Date gmtCreate;// 创建时间
	private java.util.Date gmtModified;// 修改时间
	private java.lang.String propagation;// 传播行为

	public java.lang.String getTxId() {
		return this.txId;
	}

	public void setTxId(java.lang.String txId) {
		this.txId = txId;
	}

	public java.lang.String getState() {
		return this.state;
	}

	public void setState(java.lang.String state) {
		this.state = state;
	}

	public java.lang.String getAccountTransState() {
		return this.accountTransState;
	}

	public void setAccountTransState(java.lang.String accountTransState) {
		this.accountTransState = accountTransState;
	}

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public java.util.Date getGmtCreate() {
		return this.gmtCreate;
	}

	public void setGmtCreate(java.util.Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public java.util.Date getGmtModified() {
		return this.gmtModified;
	}

	public void setGmtModified(java.util.Date gmtModified) {
		this.gmtModified = gmtModified;
	}

	public java.lang.String getPropagation() {
		return this.propagation;
	}

	public void setPropagation(java.lang.String propagation) {
		this.propagation = propagation;
	}
}
