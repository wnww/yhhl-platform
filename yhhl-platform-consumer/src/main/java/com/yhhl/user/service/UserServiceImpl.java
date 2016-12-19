package com.yhhl.user.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yhhl.common.ConfigUtils;
import com.yhhl.common.SearchPageUtil;
import com.yhhl.core.Page;
import com.yhhl.user.dao.UserMapper;
import com.yhhl.user.model.User;
import com.yhhl.wsts.client.WstsRemoteOperateCallBack;
import com.yhhl.wsts.client.WstsTransactionServiceClient;
import com.yhhl.wsts.common.TransactionContextBranch;
import com.yhhl.wsts.common.TransactionContextMain;
import com.yhhl.wsts.utils.WebUtil;

@Service("userService")
public class UserServiceImpl implements UserServiceI {

	private UserMapper userMapper;

	public UserMapper getUserMapper() {
		return userMapper;
	}

	@Autowired
	public void setUserMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	/**
	 * 保存
	 */
	public void saveUser(User user) {
		// 准备远程调用参数
		final User innerUser = user;
		final Map<String, Object> map = WebUtil.bean2Map(user);
		WstsTransactionServiceClient tsc = new WstsTransactionServiceClient();
		Map<String, Object> lastResult = tsc.doIntransaction(new WstsRemoteOperateCallBack() {
			@Override
			public Map<String, Object> exceute(TransactionContextMain tcm) throws RuntimeException {
				// 远程调用
				Map<String, Object> result = WebUtil.httpCommit(ConfigUtils.getString("remoteUrl"), map, null);
				// 正常返回执行后续操作
				if (String.valueOf(result.get("status")) == "200") {
					result = WebUtil.parseJSON2Map(String.valueOf(result.get("result")));
					if (String.valueOf(result.get("uid")).length() != 13) {
						result.put("txStatus", "F");
						result.put("message", "");
						TransactionContextBranch tcb = new TransactionContextBranch();
						tcb.setTxBranchId(String.valueOf(result.get("uid")));
						tcb.setTxBranchUrl(ConfigUtils.getString("remoteUrl"));
						tcb.setTransactionInfo(String.valueOf(result.get("uid")));
						tcm.getBranchs().add(tcb);
						throw new RuntimeException("服务端异常");
					}
					try {
						// 本地添加
						userMapper.insert(innerUser);
						result.put("txStatus", "T");
						TransactionContextBranch tcb = new TransactionContextBranch();
						tcb.setTxBranchId(String.valueOf(result.get("uid")));
						tcb.setTxBranchUrl(ConfigUtils.getString("remoteUrl"));
						tcb.setTransactionInfo(String.valueOf(result.get("uid")));
						tcm.getBranchs().add(tcb);
					} catch (Exception e) {
						result.put("txStatus", "F");
						result.put("message", e.getMessage());
						e.printStackTrace();
					}
				}
				return result;
			}
		});
	}

	@Override
	public Page<User> getAll(Map<String, Object> filterMap, Page<User> page, int pageNo, int pageSize) {
		int count = userMapper.getCount(filterMap);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setTotalCount(count);
		SearchPageUtil searchPageUtil = new SearchPageUtil();
		String order[] = { "name  desc", "id asc" };
		searchPageUtil.setOrderBys(order);
		searchPageUtil.setPage(page);
		searchPageUtil.setObject(filterMap);
		List<User> list = userMapper.getAll(searchPageUtil);
		page.setResult(list);
		return page;
	}

	@Override
	public int getCount(Map<String, Object> filterMap) {
		return userMapper.getCount(filterMap);
	}

	/**
	 * 更新用户信息
	 */
	@Override
	public void updateUser(User user) {
		userMapper.updateByPrimaryKey(user);
	}

	/**
	 * 分页列表
	 */
	@Override
	public Page<User> getByPage(Page<User> pageUser, Map<String, Object> filterMap, int pageNo, int pageSize) {
		Page<User> dataPage = new Page<User>(pageSize);
		dataPage.setPageNo(pageNo);

		if (pageUser != null) {
			dataPage = pageUser;
		}
		// 分页所需条件
		filterMap.put("offset", (dataPage.getPageNo() - 1) * dataPage.getPageSize() + 1);
		filterMap.put("limit", dataPage.getPageNo() * dataPage.getPageSize());
		if (!(dataPage.getTotalCount() > 0)) {
			dataPage.setTotalCount(userMapper.getCount(filterMap));
		}
		dataPage.setResult(userMapper.findPageByParams(filterMap));
		return dataPage;
	}

	/**
	 * 根据用户ID获取用户对象
	 */
	@Override
	public User getById(String id) {
		return userMapper.selectByPrimaryKey(id);
	}

	/**
	 * 删除用户信息
	 */
	@Override
	public void deleteById(String id) {
		userMapper.deleteByPrimaryKey(id);
	}

}
