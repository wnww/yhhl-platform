package com.yhhl.books.model;

import java.math.BigDecimal;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import com.yhhl.common.CustomDateTimeSerializer;
/**
 * 
 * <br>
 * <b>功能：</b>BooksEntity<br>
 * <b>作者：</b>www.cbice.com<br>
 * <b>日期：</b> June 15, 2015 <br>
 * <b>版权所有：<b>版权所有(C) 2013，国版中心<br>
 */
public class Books implements java.io.Serializable{
	
	private static final long serialVersionUID = -8452769338295329994L;
	private java.lang.String id;//   	private java.lang.String bookName;//   	private java.util.Date createTime;//   	public java.lang.String getId() {	    return this.id;	}	public void setId(java.lang.String id) {	    this.id=id;	}	public java.lang.String getBookName() {	    return this.bookName;	}	public void setBookName(java.lang.String bookName) {	    this.bookName=bookName;	}	@JsonSerialize(using = CustomDateTimeSerializer.class)	public java.util.Date getCreateTime() {	    return this.createTime;	}	public void setCreateTime(java.util.Date createTime) {	    this.createTime=createTime;	}
}

