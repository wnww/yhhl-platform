package com.yhhl.activity.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yhhl.activity.dao.BusinessActivityMapper;
import com.yhhl.activity.model.BusinessActivity;
import com.yhhl.common.SearchPageUtil;
import com.yhhl.core.Page;

/**
 * 
 * <br>
 * <b>功能：</b>BusinessActivityServiceImpl<br>
 * <b>作者：</b>www.yhhlkj.tk<br>
 * <b>日期：</b> June 2, 2013 <br>
 * <b>版权所有：<b>版权所有(C) 2015 国版中心<br>
 */
@Service("businessActivityService")
public class BusinessActivityServiceImpl implements BusinessActivityServiceI {

	private BusinessActivityMapper businessActivityMapper;

	public BusinessActivityMapper getBusinessActivityMapper() {
		return businessActivityMapper;
	}

	@Autowired
	public void setBusinessActivityMapper(BusinessActivityMapper businessActivityMapper) {
		this.businessActivityMapper = businessActivityMapper;
	}

	/**
	 * 保存
	 */
	@Override
	public void saveBusinessActivity(BusinessActivity businessActivity){
		businessActivity.setTxId(UUID.randomUUID().toString().replace("-", ""));
		if(businessActivity.getGmtCreate()==null){
			businessActivity.setGmtCreate(new Date());
		}
		if(businessActivity.getGmtModified()==null){
			businessActivity.setGmtModified(new Date());
		}
		businessActivityMapper.insert(businessActivity);
	}

	/**
	 * 分页查询
	 */
	@Override
	public Page<BusinessActivity> getPage(Map<String, Object> filterMap, Page<BusinessActivity> page, int pageNo, int pageSize) {
		int count = businessActivityMapper.getCount(filterMap);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setTotalCount(count);
		SearchPageUtil searchPageUtil = new SearchPageUtil();
		String order[] = { "", "" };//排序字段，可以是多个 类似：{ "name  desc", "id asc" };
		searchPageUtil.setOrderBys(order);
		searchPageUtil.setPage(page);
		searchPageUtil.setObject(filterMap);
		List<BusinessActivity> list = businessActivityMapper.getPage(searchPageUtil);
		page.setResult(list);
		return page;
	}

	/**
	*
	* 分页查询的count
	*/
	@Override
	public int getCount(Map<String, Object> filterMap) {
		return businessActivityMapper.getCount(filterMap);
	}
	
	/**
	 * 更新
	 */
	@Override
	public void updateBusinessActivity(BusinessActivity businessActivity) {
		businessActivityMapper.updateByPrimaryKey(businessActivity);
	}

	/**
	 * 根据ID获取实体对象
	 */
	@Override
	public BusinessActivity getById(String id) {
		return businessActivityMapper.selectByPrimaryKey(id);
	}

	/**
	 * 删除信息
	 */
	@Override
	public void deleteById(String id) {
		businessActivityMapper.deleteByPrimaryKey(id);
	}

}
