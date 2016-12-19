package com.yhhl.books.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;
import com.yhhl.common.SearchPageUtil;
import com.yhhl.core.Page;
import com.yhhl.books.dao.BooksMapper;
import com.yhhl.books.model.Books;

/**
 * 
 * <br>
 * <b>功能：</b>BooksServiceImpl<br>
 * <b>作者：</b>www.cbice.com<br>
 * <b>日期：</b> June 2, 2013 <br>
 * <b>版权所有：<b>版权所有(C) 2015 国版中心<br>
 */
@Service("booksService")
public class BooksServiceImpl implements BooksServiceI {

	private BooksMapper booksMapper;

	public BooksMapper getBooksMapper() {
		return booksMapper;
	}

	@Autowired
	public void setBooksMapper(BooksMapper booksMapper) {
		this.booksMapper = booksMapper;
	}

	/**
	 * 保存
	 */
	@Override
	public void saveBooks(Books books) {
		books.setId(UUID.randomUUID().toString().replace("-", ""));
		booksMapper.insert(books);
	}

	/**
	 * 分页查询
	 */
	@Override
	public Page<Books> getPage(Map<String, Object> filterMap, Page<Books> page, int pageNo, int pageSize) {
		int count = booksMapper.getCount(filterMap);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setTotalCount(count);
		SearchPageUtil searchPageUtil = new SearchPageUtil();
		String order[] = { "", "" };// 排序字段，可以是多个 类似：{ "name  desc", "id asc" };
		searchPageUtil.setOrderBys(order);
		searchPageUtil.setPage(page);
		searchPageUtil.setObject(filterMap);
		List<Books> list = booksMapper.getPage(searchPageUtil);
		page.setResult(list);
		return page;
	}

	/**
	 * 
	 * 分页查询的count
	 */
	@Override
	public int getCount(Map<String, Object> filterMap) {
		return booksMapper.getCount(filterMap);
	}

	/**
	 * 更新
	 */
	@Override
	public void updateBooks(Books books) {
		booksMapper.updateByPrimaryKey(books);
	}

	/**
	 * 根据ID获取实体对象
	 */
	@Override
	public Books getById(String id) {
		return booksMapper.selectByPrimaryKey(id);
	}

	/**
	 * 删除信息
	 */
	@Override
	public void deleteById(String id) {
		booksMapper.deleteByPrimaryKey(id);
	}

}
