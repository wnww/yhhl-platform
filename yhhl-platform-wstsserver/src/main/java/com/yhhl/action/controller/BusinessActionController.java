package com.yhhl.action.controller;

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
import com.yhhl.action.model.BusinessAction;
import com.yhhl.action.service.BusinessActionServiceI;
 
/**
 * 
 * <br>
 * <b>功能：</b>BusinessActionController<br>
 * <b>作者：</b>www.yhhlkj.tk<br>
 * <b>日期：</b> June 12, 2015 <br>
 * <b>版权所有：<b>版权所有(C) 2015 国版中心<br>
 */ 
@Controller
@RequestMapping("/businessAction") 
public class BusinessActionController {
	
	private final static Logger log= Logger.getLogger(BusinessActionController.class);
	
	// Servrice start
	@Autowired //自动注入，不需要生成set方法了，required=false表示没有实现类，也不会报错。
	private BusinessActionServiceI businessActionService; 
	
	
	/**
	 * 进入列表页面
	 * 
	 * @return
	 */
	@RequestMapping("/index")
	public ModelAndView index() {
		return new ModelAndView("action/businessAction-page");
	}
	
	/**
	 * 查询用户 列表
	 * @param request
	 * @return
	 */
	@RequestMapping("/getBusinessActionDatas")
	@ResponseBody
	public Map<String, Object> getBusinessActionDatas(HttpServletRequest request, @RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows) {
		Map<String, Object> filterMap = WebUtils.getParametersStartingWith(request, "filter_");
		Page<BusinessAction> dataPage = new Page<BusinessAction>();
		dataPage = businessActionService.getPage(filterMap, dataPage, page, rows);
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
	@RequestMapping("/initAddBusinessAction")
	@Token(save = true)
	public ModelAndView initAddBusinessAction(HttpServletRequest request) {
		String id = request.getParameter("id");
		if(StringUtil.isNotEmpty(id)){
			BusinessAction businessAction = businessActionService.getById(id);
			request.setAttribute("businessAction", businessAction);
		}
		return new ModelAndView("action/addBusinessAction");
	}
	
	/**
	 * 新增、修改
	 * @param user
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveBusinessAction")
	@ResponseBody
	public Map<String, Object> saveBusinessAction(BusinessAction businessAction, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		businessActionService.saveBusinessAction(businessAction);
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
	@RequestMapping("/delBusinessAction")
	@ResponseBody
	public Map<String, Object> delBusinessAction(HttpServletRequest request,String id){
		Map<String, Object> map = new HashMap<String, Object>();
		businessActionService.deleteById(id);
		map.put("flag", "T");
		map.put("msg", "删除成功");
		return map;
	}

}
