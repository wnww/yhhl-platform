package com.yhhl.action.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yhhl.action.dao.BusinessActionMapper;
import com.yhhl.action.model.BusinessAction;
import com.yhhl.common.SearchPageUtil;
import com.yhhl.core.Page;

/**
 * 
 * <br>
 * <b>功能：</b>BusinessActionServiceImpl<br>
 * <b>作者：</b>www.yhhlkj.tk<br>
 * <b>日期：</b> June 2, 2013 <br>
 * <b>版权所有：<b>版权所有(C) 2015 国版中心<br>
 */
@Service("businessActionService")
public class BusinessActionServiceImpl implements BusinessActionServiceI {

	private BusinessActionMapper businessActionMapper;

	public BusinessActionMapper getBusinessActionMapper() {
		return businessActionMapper;
	}

	@Autowired
	public void setBusinessActionMapper(BusinessActionMapper businessActionMapper) {
		this.businessActionMapper = businessActionMapper;
	}

	/**
	 * 保存
	 */
	@Override
	public void saveBusinessAction(BusinessAction businessAction) {
		if(businessAction.getGmtCreate()==null){
			businessAction.setGmtCreate(new Date());
		}
		if(businessAction.getGmtModified()==null){
			businessAction.setGmtModified(new Date());
		}
		businessActionMapper.insert(businessAction);
	}

	/**
	 * 分页查询
	 */
	@Override
	public Page<BusinessAction> getPage(Map<String, Object> filterMap, Page<BusinessAction> page, int pageNo,
			int pageSize) {
		int count = businessActionMapper.getCount(filterMap);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setTotalCount(count);
		SearchPageUtil searchPageUtil = new SearchPageUtil();
		String order[] = { "", "" };// 排序字段，可以是多个 类似：{ "name  desc", "id asc" };
		searchPageUtil.setOrderBys(order);
		searchPageUtil.setPage(page);
		searchPageUtil.setObject(filterMap);
		List<BusinessAction> list = businessActionMapper.getPage(searchPageUtil);
		page.setResult(list);
		return page;
	}

	/**
	 * 
	 * 分页查询的count
	 */
	@Override
	public int getCount(Map<String, Object> filterMap) {
		return businessActionMapper.getCount(filterMap);
	}

	/**
	 * 更新
	 */
	@Override
	public void updateBusinessAction(BusinessAction businessAction) {
		businessActionMapper.updateByPrimaryKey(businessAction);
	}

	/**
	 * 根据ID获取实体对象
	 */
	@Override
	public BusinessAction getById(String id) {
		return businessActionMapper.selectByPrimaryKey(id);
	}

	/**
	 * 删除信息
	 */
	@Override
	public void deleteById(String id) {
		businessActionMapper.deleteByPrimaryKey(id);
	}

}
