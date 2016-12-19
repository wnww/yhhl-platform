package com.yhhl.books.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.yhhl.common.StringUtil;
import com.yhhl.core.Page;
import com.yhhl.interceptor.Token;
import com.yhhl.books.model.Books;
import com.yhhl.books.service.BooksServiceI;
 
/**
 * 
 * <br>
 * <b>功能：</b>BooksController<br>
 * <b>作者：</b>www.cbice.com<br>
 * <b>日期：</b> June 12, 2015 <br>
 * <b>版权所有：<b>版权所有(C) 2015 国版中心<br>
 */ 
@Controller
@RequestMapping("/books") 
public class BooksController {
	
	private final static Logger log= Logger.getLogger(BooksController.class);
	
	// Servrice start
	@Autowired //自动注入，不需要生成set方法了，required=false表示没有实现类，也不会报错。
	private BooksServiceI booksService; 
	
	
	/**
	 * 进入列表页面
	 * 
	 * @return
	 */
	@RequestMapping("/index")
	public ModelAndView index() {
		return new ModelAndView("books/books-page");
	}
	
	/**
	 * 查询用户 列表
	 * @param request
	 * @return
	 */
	@RequestMapping("/getBooksDatas")
	@ResponseBody
	public Map<String, Object> getBooksDatas(HttpServletRequest request, @RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows) {
		Map<String, Object> filterMap = WebUtils.getParametersStartingWith(request, "filter_");
		Page<Books> dataPage = new Page<Books>();
		dataPage = booksService.getPage(filterMap, dataPage, page, rows);
		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("total", dataPage.getTotalCount());
		mapData.put("rows", dataPage.getResult());
		return mapData;
	}
	
	/**
	 * 进入到初始化新增、修改页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/initAddBooks")
	@Token(save = true)
	public ModelAndView initAddBooks(HttpServletRequest request) {
		String id = request.getParameter("id");
		if(StringUtil.isNotEmpty(id)){
			Books books = booksService.getById(id);
			request.setAttribute("books", books);
		}
		return new ModelAndView("books/addBooks");
	}
	
	/**
	 * 新增、修改
	 * @param user
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveBooks")
	@Token(remove = true)
	@ResponseBody
	public Map<String, Object> saveBooks(Books books, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtil.isNotEmpty(books.getId())){
			Books booksTemp = booksService.getById(books.getId());
			// 将页面修改的信息在这里替换
			booksService.updateBooks(booksTemp);
			map.put("flag", "T");
			map.put("msg", "修改成功");
			return map;
		}
		booksService.saveBooks(books);
		map.put("flag", "T");
		map.put("msg", "保存成功");
		return map;
	}
	
	/**
	* 删除
	*
	* @param request
	* @param id
	*/
	@RequestMapping("/delBooks")
	@ResponseBody
	public Map<String, Object> delBooks(HttpServletRequest request,String id){
		Map<String, Object> map = new HashMap<String, Object>();
		booksService.deleteById(id);
		map.put("flag", "T");
		map.put("msg", "删除成功");
		return map;
	}

}
